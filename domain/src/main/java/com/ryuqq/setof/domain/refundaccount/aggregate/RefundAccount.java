package com.ryuqq.setof.domain.refundaccount.aggregate;

import com.ryuqq.setof.domain.refundaccount.exception.RefundAccountNotOwnerException;
import com.ryuqq.setof.domain.refundaccount.vo.AccountHolderName;
import com.ryuqq.setof.domain.refundaccount.vo.AccountNumber;
import com.ryuqq.setof.domain.refundaccount.vo.RefundAccountId;
import com.ryuqq.setof.domain.refundaccount.vo.VerificationInfo;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

/**
 * RefundAccount Aggregate Root
 *
 * <p>회원의 환불계좌 정보를 관리하는 Aggregate Root입니다.
 *
 * <p>비즈니스 규칙:
 *
 * <ul>
 *   <li>회원당 최대 1개 등록 가능 (memberId UNIQUE)
 *   <li>은행 정보는 Bank 테이블 참조 (Long FK)
 *   <li>등록/수정 시 외부 계좌 검증 API 통해 검증 필수
 *   <li>검증 실패 시 저장 불가
 *   <li>Soft Delete 적용
 * </ul>
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드 (상태 변경은 도메인 메서드로만)
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 *   <li>Tell, Don't Ask - 상태 변경은 도메인 메서드로만
 *   <li>Long FK 전략 - Bank 참조는 bankId(Long)로만
 * </ul>
 */
public class RefundAccount {

    private final RefundAccountId id;
    private final UUID memberId;
    private Long bankId;
    private AccountNumber accountNumber;
    private AccountHolderName accountHolderName;
    private VerificationInfo verificationInfo;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private RefundAccount(
            RefundAccountId id,
            UUID memberId,
            Long bankId,
            AccountNumber accountNumber,
            AccountHolderName accountHolderName,
            VerificationInfo verificationInfo,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.memberId = memberId;
        this.bankId = bankId;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.verificationInfo = verificationInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * 신규 환불계좌 생성용 Static Factory Method (검증 전)
     *
     * <p>ID 없이 생성 (Persistence Layer에서 Auto-increment) 초기 상태는 미검증 상태로 생성됩니다. Application Layer에서 검증
     * 후 verify() 호출이 필요합니다.
     *
     * @param memberId 회원 ID
     * @param bankId 은행 ID (FK)
     * @param accountNumber 계좌번호
     * @param accountHolderName 예금주명
     * @param clock 시간 제공자
     * @return RefundAccount 인스턴스 (미검증 상태)
     */
    public static RefundAccount forNew(
            UUID memberId,
            Long bankId,
            AccountNumber accountNumber,
            AccountHolderName accountHolderName,
            Clock clock) {
        Instant now = clock.instant();
        return new RefundAccount(
                null,
                memberId,
                bankId,
                accountNumber,
                accountHolderName,
                VerificationInfo.unverified(),
                now,
                now,
                null);
    }

    /**
     * 신규 환불계좌 생성용 Static Factory Method (검증 완료)
     *
     * <p>외부 검증 완료 후 호출하는 팩토리 메서드입니다.
     *
     * @param memberId 회원 ID
     * @param bankId 은행 ID (FK)
     * @param accountNumber 계좌번호
     * @param accountHolderName 예금주명
     * @param clock 시간 제공자
     * @return RefundAccount 인스턴스 (검증 완료 상태)
     */
    public static RefundAccount forNewVerified(
            UUID memberId,
            Long bankId,
            AccountNumber accountNumber,
            AccountHolderName accountHolderName,
            Clock clock) {
        Instant now = clock.instant();
        return new RefundAccount(
                null,
                memberId,
                bankId,
                accountNumber,
                accountHolderName,
                VerificationInfo.verified(clock),
                now,
                now,
                null);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @return RefundAccount 인스턴스
     */
    public static RefundAccount reconstitute(
            RefundAccountId id,
            UUID memberId,
            Long bankId,
            AccountNumber accountNumber,
            AccountHolderName accountHolderName,
            VerificationInfo verificationInfo,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new RefundAccount(
                id,
                memberId,
                bankId,
                accountNumber,
                accountHolderName,
                verificationInfo,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 환불계좌 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 환불계좌 ID Long 값 또는 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 계좌번호 값 반환 (Law of Demeter 준수)
     *
     * @return 계좌번호 문자열
     */
    public String getAccountNumberValue() {
        return accountNumber.value();
    }

    /**
     * 정규화된 계좌번호 반환 (숫자만)
     *
     * @return 정규화된 계좌번호
     */
    public String getNormalizedAccountNumber() {
        return accountNumber.normalized();
    }

    /**
     * 마스킹된 계좌번호 반환 (Law of Demeter 준수)
     *
     * @return 마스킹된 계좌번호
     */
    public String getMaskedAccountNumber() {
        return accountNumber.masked();
    }

    /**
     * 예금주명 값 반환 (Law of Demeter 준수)
     *
     * @return 예금주명 문자열
     */
    public String getAccountHolderNameValue() {
        return accountHolderName.value();
    }

    /**
     * 검증 완료 일시 반환 (Law of Demeter 준수)
     *
     * @return 검증 완료 일시 또는 null
     */
    public Instant getVerifiedAt() {
        return verificationInfo.verifiedAt();
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 계좌 정보 수정 (재검증 필요)
     *
     * <p>계좌 정보가 변경되면 미검증 상태로 변경됩니다. Application Layer에서 재검증 후 verify() 호출이 필요합니다.
     *
     * @param bankId 새 은행 ID
     * @param accountNumber 새 계좌번호
     * @param accountHolderName 새 예금주명
     * @param clock 시간 제공자
     */
    public void update(
            Long bankId,
            AccountNumber accountNumber,
            AccountHolderName accountHolderName,
            Clock clock) {
        this.bankId = bankId;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.verificationInfo = VerificationInfo.unverified();
        this.updatedAt = clock.instant();
    }

    /**
     * 계좌 정보 수정 (검증 완료)
     *
     * <p>외부 검증 완료 후 호출하는 메서드입니다.
     *
     * @param bankId 새 은행 ID
     * @param accountNumber 새 계좌번호
     * @param accountHolderName 새 예금주명
     * @param clock 시간 제공자
     */
    public void updateVerified(
            Long bankId,
            AccountNumber accountNumber,
            AccountHolderName accountHolderName,
            Clock clock) {
        this.bankId = bankId;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.verificationInfo = VerificationInfo.verified(clock);
        this.updatedAt = clock.instant();
    }

    /**
     * 계좌 검증 완료 처리
     *
     * @param clock 시간 제공자
     */
    public void verify(Clock clock) {
        this.verificationInfo = VerificationInfo.verified(clock);
        this.updatedAt = clock.instant();
    }

    /**
     * 환불계좌 삭제 (Soft Delete)
     *
     * @param clock 시간 제공자
     */
    public void delete(Clock clock) {
        Instant now = clock.instant();
        this.deletedAt = now;
        this.updatedAt = now;
    }

    /**
     * 소유자 확인 - 요청 회원이 소유자가 아니면 예외
     *
     * @param requestMemberId 요청 회원 ID
     * @throws RefundAccountNotOwnerException 소유자가 아닌 경우
     */
    public void validateOwnership(UUID requestMemberId) {
        if (!this.memberId.equals(requestMemberId)) {
            throw new RefundAccountNotOwnerException(getIdValue(), requestMemberId);
        }
    }

    /**
     * 검증 완료 여부 확인 (Tell, Don't Ask)
     *
     * @return 검증 완료 상태이면 true
     */
    public boolean isVerified() {
        return this.verificationInfo.isVerified();
    }

    /**
     * 미검증 상태 여부 확인 (Tell, Don't Ask)
     *
     * @return 미검증 상태이면 true
     */
    public boolean isUnverified() {
        return this.verificationInfo.isUnverified();
    }

    /**
     * 삭제된 환불계좌인지 확인 (Tell, Don't Ask)
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    /**
     * 활성 환불계좌인지 확인 (Tell, Don't Ask)
     *
     * @return 삭제되지 않았으면 true
     */
    public boolean isActive() {
        return this.deletedAt == null;
    }

    /**
     * 해당 회원 소유인지 확인 (Tell, Don't Ask)
     *
     * @param memberId 확인할 회원 ID
     * @return 소유자이면 true
     */
    public boolean isOwnedBy(UUID memberId) {
        return this.memberId.equals(memberId);
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public RefundAccountId getId() {
        return id;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public Long getBankId() {
        return bankId;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public AccountHolderName getAccountHolderName() {
        return accountHolderName;
    }

    public VerificationInfo getVerificationInfo() {
        return verificationInfo;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
