package com.ryuqq.setof.domain.mileage.aggregate;

import com.ryuqq.setof.domain.mileage.exception.MileageErrorCode;
import com.ryuqq.setof.domain.mileage.exception.MileageException;
import com.ryuqq.setof.domain.mileage.id.MileageEntryId;
import com.ryuqq.setof.domain.mileage.vo.MileageEntryStatus;
import com.ryuqq.setof.domain.mileage.vo.MileageIssueType;
import java.time.LocalDateTime;

/**
 * 마일리지 적립 건.
 *
 * <p>개별 적립 건 단위로 잔액을 추적합니다. 만료 임박순 차감 시 Entry 단위로 부분 사용이 가능합니다.
 *
 * <p>레거시 mileage 테이블에 대응합니다.
 */
public class MileageEntry {

    private final MileageEntryId id;
    private final long earnedAmount;
    private long usedAmount;
    private MileageEntryStatus status;
    private final MileageIssueType issueType;
    private final String title;
    private final Long targetId;
    private final LocalDateTime issuedAt;
    private final LocalDateTime expirationDate;

    private MileageEntry(
            MileageEntryId id,
            long earnedAmount,
            long usedAmount,
            MileageEntryStatus status,
            MileageIssueType issueType,
            String title,
            Long targetId,
            LocalDateTime issuedAt,
            LocalDateTime expirationDate) {
        this.id = id;
        this.earnedAmount = earnedAmount;
        this.usedAmount = usedAmount;
        this.status = status;
        this.issueType = issueType;
        this.title = title;
        this.targetId = targetId;
        this.issuedAt = issuedAt;
        this.expirationDate = expirationDate;
    }

    /** 새 적립 건 생성. */
    public static MileageEntry create(
            long amount,
            MileageIssueType issueType,
            String title,
            Long targetId,
            LocalDateTime issuedAt,
            LocalDateTime expirationDate) {
        if (amount <= 0) {
            throw new MileageException(MileageErrorCode.INVALID_MILEAGE_AMOUNT, "적립 금액은 양수여야 합니다");
        }
        return new MileageEntry(
                MileageEntryId.forNew(),
                amount,
                0,
                MileageEntryStatus.ACTIVE,
                issueType,
                title,
                targetId,
                issuedAt,
                expirationDate);
    }

    /** DB 복원용 팩토리 메서드. */
    public static MileageEntry reconstitute(
            MileageEntryId id,
            long earnedAmount,
            long usedAmount,
            MileageEntryStatus status,
            MileageIssueType issueType,
            String title,
            Long targetId,
            LocalDateTime issuedAt,
            LocalDateTime expirationDate) {
        return new MileageEntry(
                id,
                earnedAmount,
                usedAmount,
                status,
                issueType,
                title,
                targetId,
                issuedAt,
                expirationDate);
    }

    /** 현재 잔액. */
    public long remainingAmount() {
        return earnedAmount - usedAmount;
    }

    /** 사용 가능 여부. ACTIVE 상태이고 잔액이 있어야 합니다. */
    public boolean isUsable() {
        return status == MileageEntryStatus.ACTIVE && remainingAmount() > 0;
    }

    /** 만료 여부 확인. */
    public boolean isExpiredAt(LocalDateTime now) {
        return expirationDate != null && !now.isBefore(expirationDate);
    }

    /**
     * 이 Entry에서 지정 금액을 차감합니다.
     *
     * @param amount 차감할 금액
     * @return 실제 차감된 금액 (잔액보다 크면 잔액만큼만)
     */
    public long deduct(long amount) {
        if (!isUsable()) {
            return 0;
        }
        long deductible = Math.min(amount, remainingAmount());
        this.usedAmount += deductible;
        if (remainingAmount() == 0) {
            this.status = MileageEntryStatus.EXHAUSTED;
        }
        return deductible;
    }

    /** 만료 처리. */
    public void expire() {
        if (status != MileageEntryStatus.ACTIVE) {
            return;
        }
        this.status = MileageEntryStatus.EXPIRED;
    }

    /** 회수 처리 (취소된 주문의 적립분). */
    public void revoke() {
        if (status != MileageEntryStatus.ACTIVE && status != MileageEntryStatus.EXHAUSTED) {
            throw new MileageException(
                    MileageErrorCode.INVALID_ENTRY_STATUS, String.format("회수 불가 상태: %s", status));
        }
        this.status = MileageEntryStatus.REVOKED;
    }

    // Getters
    public MileageEntryId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public long earnedAmount() {
        return earnedAmount;
    }

    public long usedAmount() {
        return usedAmount;
    }

    public MileageEntryStatus status() {
        return status;
    }

    public MileageIssueType issueType() {
        return issueType;
    }

    public String title() {
        return title;
    }

    public Long targetId() {
        return targetId;
    }

    public LocalDateTime issuedAt() {
        return issuedAt;
    }

    public LocalDateTime expirationDate() {
        return expirationDate;
    }
}
