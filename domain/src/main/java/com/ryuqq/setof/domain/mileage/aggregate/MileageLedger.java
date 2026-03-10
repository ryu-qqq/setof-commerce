package com.ryuqq.setof.domain.mileage.aggregate;

import com.ryuqq.setof.domain.common.vo.LegacyUserId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.mileage.exception.MileageErrorCode;
import com.ryuqq.setof.domain.mileage.exception.MileageException;
import com.ryuqq.setof.domain.mileage.id.MileageLedgerId;
import com.ryuqq.setof.domain.mileage.vo.MileageBalance;
import com.ryuqq.setof.domain.mileage.vo.MileageEntryStatus;
import com.ryuqq.setof.domain.mileage.vo.MileageIssueType;
import com.ryuqq.setof.domain.mileage.vo.MileageReason;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 마일리지 원장 Aggregate Root.
 *
 * <p>사용자별 1개의 원장을 가지며, 모든 마일리지 적립/사용/환불/만료 오퍼레이션의 진입점입니다.
 *
 * <h3>비즈니스 규칙</h3>
 *
 * <ul>
 *   <li>적립: 구매확정 시 결제금액의 1%, 회원가입 시 5,000원 (만료 30일)
 *   <li>사용: 최소 1,000원 단위, 결제금액의 최대 10%
 *   <li>차감 순서: 만료 임박순 (Earliest Expiration First)
 *   <li>전체 취소: 사용한 마일리지 자동 반환
 *   <li>부분 환불: 마일리지 반환 없음
 *   <li>만료된 마일리지: 반환 없음
 * </ul>
 */
public class MileageLedger {

    /** 최소 사용 금액 (원). */
    public static final long MIN_USAGE_AMOUNT = 1_000;

    /** 기본 만료 기간 (일). */
    public static final int DEFAULT_EXPIRATION_DAYS = 30;

    private final MileageLedgerId id;
    private final MemberId memberId;
    private final LegacyUserId legacyUserId;
    private final List<MileageEntry> entries;
    private final List<MileageTransaction> pendingTransactions;
    private final Instant createdAt;
    private Instant updatedAt;

    private MileageLedger(
            MileageLedgerId id,
            MemberId memberId,
            LegacyUserId legacyUserId,
            List<MileageEntry> entries,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.legacyUserId = legacyUserId;
        this.entries = new ArrayList<>(entries);
        this.pendingTransactions = new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** 새 원장 생성. */
    public static MileageLedger create(MemberId memberId, LegacyUserId legacyUserId, Instant now) {
        return new MileageLedger(
                MileageLedgerId.forNew(), memberId, legacyUserId, new ArrayList<>(), now, now);
    }

    /** DB 복원용 팩토리 메서드. */
    public static MileageLedger reconstitute(
            MileageLedgerId id,
            MemberId memberId,
            LegacyUserId legacyUserId,
            List<MileageEntry> entries,
            Instant createdAt,
            Instant updatedAt) {
        return new MileageLedger(id, memberId, legacyUserId, entries, createdAt, updatedAt);
    }

    /**
     * 마일리지 적립.
     *
     * @param amount 적립 금액
     * @param issueType 적립 유형 (JOIN, ORDER, REVIEW 등)
     * @param title 적립 제목
     * @param targetId 관련 대상 ID (주문 ID 등, nullable)
     * @param issuedAt 적립 시각
     * @param expirationDate 만료일
     * @param now 현재 시각
     * @return 생성된 MileageEntry
     */
    public MileageEntry earn(
            long amount,
            MileageIssueType issueType,
            String title,
            Long targetId,
            LocalDateTime issuedAt,
            LocalDateTime expirationDate,
            Instant now) {
        MileageEntry entry =
                MileageEntry.create(amount, issueType, title, targetId, issuedAt, expirationDate);
        entries.add(entry);
        pendingTransactions.add(
                MileageTransaction.create(
                        entry.id(), amount, MileageReason.EARN, null, null, title, now));
        this.updatedAt = now;
        return entry;
    }

    /**
     * 마일리지 사용. 만료 임박순으로 차감합니다.
     *
     * @param amount 사용할 금액
     * @param paymentAmount 총 결제 금액 (최대 사용 비율 검증용)
     * @param orderId 주문 ID
     * @param paymentId 결제 ID
     * @param now 현재 시각
     * @return 실제 사용된 총 금액
     */
    public long use(long amount, long paymentAmount, Long orderId, Long paymentId, Instant now) {
        validateUsage(amount, paymentAmount);

        long remaining = amount;
        List<MileageEntry> usableEntries = getUsableEntriesSortedByExpiration();

        for (MileageEntry entry : usableEntries) {
            if (remaining <= 0) {
                break;
            }
            long deducted = entry.deduct(remaining);
            if (deducted > 0) {
                remaining -= deducted;
                pendingTransactions.add(
                        MileageTransaction.create(
                                entry.id(),
                                -deducted,
                                MileageReason.USE,
                                orderId,
                                paymentId,
                                "주문 결제 시 사용",
                                now));
            }
        }

        long totalUsed = amount - remaining;
        if (totalUsed < amount) {
            throw new MileageException(
                    MileageErrorCode.INSUFFICIENT_BALANCE,
                    String.format("잔액 부족: 요청=%d, 사용가능=%d", amount, totalUsed));
        }

        this.updatedAt = now;
        return totalUsed;
    }

    /**
     * 전체 취소 시 마일리지 환불. 사용한 마일리지를 새 Entry로 재적립합니다.
     *
     * @param amount 환불할 금액
     * @param orderId 관련 주문 ID
     * @param now 현재 시각
     * @return 생성된 환불 MileageEntry
     */
    public MileageEntry refund(long amount, Long orderId, Instant now) {
        if (amount <= 0) {
            throw new MileageException(MileageErrorCode.INVALID_MILEAGE_AMOUNT, "환불 금액은 양수여야 합니다");
        }

        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expirationDate = issuedAt.plusDays(DEFAULT_EXPIRATION_DAYS);

        MileageEntry refundEntry =
                MileageEntry.create(
                        amount,
                        MileageIssueType.ADMIN,
                        "주문 취소 마일리지 반환",
                        orderId,
                        issuedAt,
                        expirationDate);
        entries.add(refundEntry);

        pendingTransactions.add(
                MileageTransaction.create(
                        refundEntry.id(),
                        amount,
                        MileageReason.REFUND,
                        orderId,
                        null,
                        "주문 취소 마일리지 반환",
                        now));

        this.updatedAt = now;
        return refundEntry;
    }

    /**
     * 만료 처리. 지정 시각 기준으로 만료된 Entry를 처리합니다.
     *
     * @param now 기준 시각
     * @param currentInstant 이벤트 시각
     * @return 만료된 총 금액
     */
    public long expireEntries(LocalDateTime now, Instant currentInstant) {
        long totalExpired = 0;
        for (MileageEntry entry : entries) {
            if (entry.isUsable() && entry.isExpiredAt(now)) {
                long remaining = entry.remainingAmount();
                entry.expire();
                totalExpired += remaining;
                pendingTransactions.add(
                        MileageTransaction.create(
                                entry.id(),
                                -remaining,
                                MileageReason.EXPIRE,
                                null,
                                null,
                                "적립금 유효기간 만료",
                                currentInstant));
            }
        }
        if (totalExpired > 0) {
            this.updatedAt = currentInstant;
        }
        return totalExpired;
    }

    /**
     * 적립분 회수 (취소된 주문의 적립 마일리지).
     *
     * @param targetId 회수 대상 주문 ID
     * @param now 현재 시각
     * @return 회수된 총 금액
     */
    public long revokeByTarget(Long targetId, Instant now) {
        long totalRevoked = 0;
        for (MileageEntry entry : entries) {
            if (entry.issueType() == MileageIssueType.ORDER
                    && targetId.equals(entry.targetId())
                    && (entry.status() == MileageEntryStatus.ACTIVE
                            || entry.status() == MileageEntryStatus.EXHAUSTED)) {
                long remaining = entry.remainingAmount();
                entry.revoke();
                totalRevoked += remaining;
                pendingTransactions.add(
                        MileageTransaction.create(
                                entry.id(),
                                -remaining,
                                MileageReason.REVOKE,
                                targetId,
                                null,
                                "주문 취소로 인한 적립분 회수",
                                now));
            }
        }
        if (totalRevoked > 0) {
            this.updatedAt = now;
        }
        return totalRevoked;
    }

    /** 현재 사용 가능 잔액. */
    public long availableBalance() {
        return entries.stream()
                .filter(MileageEntry::isUsable)
                .mapToLong(MileageEntry::remainingAmount)
                .sum();
    }

    /** 마일리지 잔액 요약. */
    public MileageBalance balance() {
        long earned = 0;
        long used = 0;
        long expired = 0;
        long revoked = 0;
        for (MileageEntry entry : entries) {
            earned += entry.earnedAmount();
            used += entry.usedAmount();
            if (entry.status() == MileageEntryStatus.EXPIRED) {
                expired += entry.remainingAmount();
            }
            if (entry.status() == MileageEntryStatus.REVOKED) {
                revoked += entry.remainingAmount();
            }
        }
        return new MileageBalance(earned, used, expired, revoked);
    }

    /** 만료 임박순으로 정렬된 사용 가능 Entry 목록. */
    private List<MileageEntry> getUsableEntriesSortedByExpiration() {
        return entries.stream()
                .filter(MileageEntry::isUsable)
                .sorted(
                        Comparator.comparing(
                                MileageEntry::expirationDate,
                                Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    private void validateUsage(long amount, long paymentAmount) {
        if (amount < MIN_USAGE_AMOUNT) {
            throw new MileageException(
                    MileageErrorCode.BELOW_MIN_USAGE,
                    String.format("최소 사용 금액은 %d원입니다: %d", MIN_USAGE_AMOUNT, amount));
        }

        long maxUsable = paymentAmount / 10;
        if (amount > maxUsable) {
            throw new MileageException(
                    MileageErrorCode.EXCEEDS_MAX_USAGE_RATIO,
                    String.format(
                            "결제 금액(%d원)의 10%%(%d원)를 초과할 수 없습니다: %d",
                            paymentAmount, maxUsable, amount));
        }

        if (availableBalance() < amount) {
            throw new MileageException(
                    MileageErrorCode.INSUFFICIENT_BALANCE,
                    String.format("잔액 부족: 요청=%d, 사용가능=%d", amount, availableBalance()));
        }
    }

    /** 아직 저장되지 않은 트랜잭션 이력. */
    public List<MileageTransaction> pendingTransactions() {
        return List.copyOf(pendingTransactions);
    }

    /** 저장 완료 후 pending 목록 초기화. */
    public void clearPendingTransactions() {
        pendingTransactions.clear();
    }

    public boolean isNew() {
        return id.isNew();
    }

    // Getters
    public MileageLedgerId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public MemberId memberId() {
        return memberId;
    }

    public LegacyUserId legacyUserId() {
        return legacyUserId;
    }

    public long legacyUserIdValue() {
        return legacyUserId.value();
    }

    public List<MileageEntry> entries() {
        return List.copyOf(entries);
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
