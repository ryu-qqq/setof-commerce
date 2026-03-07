package com.ryuqq.setof.domain.refundaccount.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.refundaccount.exception.AccountVerificationFailedException;
import com.ryuqq.setof.domain.refundaccount.id.RefundAccountId;
import com.ryuqq.setof.domain.refundaccount.vo.RefundBankInfo;
import java.time.Instant;

/**
 * 환불 계좌 Aggregate.
 *
 * <p>회원의 환불 계좌 정보를 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class RefundAccount {

    private final RefundAccountId id;
    private final MemberId memberId;
    private RefundBankInfo bankInfo;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private RefundAccount(
            RefundAccountId id,
            MemberId memberId,
            RefundBankInfo bankInfo,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.bankInfo = bankInfo;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 환불 계좌 생성.
     *
     * @param memberId 회원 ID
     * @param bankInfo 은행 계좌 정보
     * @param occurredAt 생성 시각
     * @return 새 RefundAccount 인스턴스
     */
    public static RefundAccount forNew(
            MemberId memberId, RefundBankInfo bankInfo, Instant occurredAt) {
        return new RefundAccount(
                RefundAccountId.forNew(),
                memberId,
                bankInfo,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /** 영속성 레이어에서 복원. */
    public static RefundAccount reconstitute(
            RefundAccountId id,
            MemberId memberId,
            RefundBankInfo bankInfo,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new RefundAccount(id, memberId, bankInfo, deletionStatus, createdAt, updatedAt);
    }

    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 계좌 정보 수정.
     *
     * @param updateData 수정 데이터
     * @param occurredAt 변경 시각
     */
    public void update(RefundAccountUpdateData updateData, Instant occurredAt) {
        this.bankInfo = updateData.bankInfo();
        this.updatedAt = occurredAt;
    }

    /**
     * 조회된 예금주명과 등록된 예금주명을 비교하여 검증합니다.
     *
     * @param fetchedHolderName 외부 API에서 조회된 예금주명
     * @throws AccountVerificationFailedException 예금주명 불일치 시
     */
    public void validateAccountHolder(String fetchedHolderName) {
        if (fetchedHolderName == null || fetchedHolderName.isBlank()) {
            throw new AccountVerificationFailedException(
                    bankInfo.bankName(), bankInfo.accountNumber());
        }
        String normalized = fetchedHolderName.replaceAll("\\s+", "");
        String expected = bankInfo.accountHolderName().replaceAll("\\s+", "");
        if (!normalized.equals(expected)) {
            throw new AccountVerificationFailedException(
                    bankInfo.bankName(), bankInfo.accountNumber());
        }
    }

    /** 소프트 삭제. */
    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
        this.updatedAt = occurredAt;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    // VO Getters
    public RefundAccountId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public MemberId memberId() {
        return memberId;
    }

    public String memberIdValue() {
        return memberId.value();
    }

    public RefundBankInfo bankInfo() {
        return bankInfo;
    }

    public String bankName() {
        return bankInfo.bankName();
    }

    public String accountNumber() {
        return bankInfo.accountNumber();
    }

    public String accountHolderName() {
        return bankInfo.accountHolderName();
    }

    public String maskedAccountNumber() {
        return bankInfo.maskedAccountNumber();
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
