package com.ryuqq.setof.domain.refundaccount;

import com.ryuqq.setof.domain.refundaccount.aggregate.RefundAccount;
import com.ryuqq.setof.domain.refundaccount.vo.AccountHolderName;
import com.ryuqq.setof.domain.refundaccount.vo.AccountNumber;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import com.ryuqq.setof.domain.refundaccount.vo.VerificationInfo;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

/**
 * RefundAccount TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 RefundAccount 인스턴스 생성을 위한 팩토리 클래스
 */
public final class RefundAccountFixture {

    public static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    public static final UUID DEFAULT_MEMBER_ID =
            UUID.fromString("01936ddc-8d37-7c6e-8ad6-18c76adc9dfa");

    public static final Long DEFAULT_BANK_ID = 1L;

    private RefundAccountFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 신규 환불계좌 생성 (미검증)
     *
     * @return RefundAccount 인스턴스 (미검증)
     */
    public static RefundAccount createNew() {
        return RefundAccount.forNew(
                DEFAULT_MEMBER_ID,
                DEFAULT_BANK_ID,
                AccountNumber.of("1234567890123"),
                AccountHolderName.of("홍길동"),
                FIXED_CLOCK);
    }

    /**
     * 신규 환불계좌 생성 (검증 완료)
     *
     * @return RefundAccount 인스턴스 (검증 완료)
     */
    public static RefundAccount createNewVerified() {
        return RefundAccount.forNewVerified(
                DEFAULT_MEMBER_ID,
                DEFAULT_BANK_ID,
                AccountNumber.of("1234567890123"),
                AccountHolderName.of("홍길동"),
                FIXED_CLOCK);
    }

    /**
     * ID가 포함된 환불계좌 생성 (검증 완료)
     *
     * @param id 환불계좌 ID
     * @return RefundAccount 인스턴스
     */
    public static RefundAccount createWithId(Long id) {
        return RefundAccount.reconstitute(
                RefundAccountId.of(id),
                DEFAULT_MEMBER_ID,
                DEFAULT_BANK_ID,
                AccountNumber.of("1234567890123"),
                AccountHolderName.of("홍길동"),
                VerificationInfo.verified(FIXED_CLOCK),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    /**
     * ID가 포함된 미검증 환불계좌 생성
     *
     * @param id 환불계좌 ID
     * @return RefundAccount 인스턴스 (미검증)
     */
    public static RefundAccount createUnverifiedWithId(Long id) {
        return RefundAccount.reconstitute(
                RefundAccountId.of(id),
                DEFAULT_MEMBER_ID,
                DEFAULT_BANK_ID,
                AccountNumber.of("1234567890123"),
                AccountHolderName.of("홍길동"),
                VerificationInfo.unverified(),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    /**
     * 삭제된 환불계좌 생성
     *
     * @param id 환불계좌 ID
     * @return RefundAccount 인스턴스 (삭제됨)
     */
    public static RefundAccount createDeleted(Long id) {
        return RefundAccount.reconstitute(
                RefundAccountId.of(id),
                DEFAULT_MEMBER_ID,
                DEFAULT_BANK_ID,
                AccountNumber.of("1234567890123"),
                AccountHolderName.of("홍길동"),
                VerificationInfo.verified(FIXED_CLOCK),
                FIXED_TIME,
                FIXED_TIME,
                FIXED_TIME);
    }

    /**
     * 특정 회원의 환불계좌 생성
     *
     * @param id 환불계좌 ID
     * @param memberId 회원 ID
     * @return RefundAccount 인스턴스
     */
    public static RefundAccount createForMember(Long id, UUID memberId) {
        return RefundAccount.reconstitute(
                RefundAccountId.of(id),
                memberId,
                DEFAULT_BANK_ID,
                AccountNumber.of("1234567890123"),
                AccountHolderName.of("홍길동"),
                VerificationInfo.verified(FIXED_CLOCK),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }

    /**
     * 커스텀 환불계좌 생성
     *
     * @param id 환불계좌 ID
     * @param memberId 회원 ID
     * @param bankId 은행 ID
     * @param accountNumber 계좌번호
     * @param accountHolderName 예금주명
     * @param verified 검증 완료 여부
     * @return RefundAccount 인스턴스
     */
    public static RefundAccount createCustom(
            Long id,
            UUID memberId,
            Long bankId,
            String accountNumber,
            String accountHolderName,
            boolean verified) {
        return RefundAccount.reconstitute(
                RefundAccountId.of(id),
                memberId,
                bankId,
                AccountNumber.of(accountNumber),
                AccountHolderName.of(accountHolderName),
                verified ? VerificationInfo.verified(FIXED_CLOCK) : VerificationInfo.unverified(),
                FIXED_TIME,
                FIXED_TIME,
                null);
    }
}
