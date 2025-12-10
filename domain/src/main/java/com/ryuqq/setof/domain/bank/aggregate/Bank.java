package com.ryuqq.setof.domain.bank.aggregate;

import com.ryuqq.setof.domain.bank.vo.BankCode;
import com.ryuqq.setof.domain.bank.vo.BankId;
import com.ryuqq.setof.domain.bank.vo.BankName;
import java.time.Instant;

/**
 * Bank Aggregate Root
 *
 * <p>은행 정보를 나타내는 코드 테이블 엔티티입니다.
 * 일반적으로 관리자가 직접 DB에서 관리하며, 애플리케이션에서는 조회만 수행합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 * </ul>
 */
public class Bank {

    private final BankId id;
    private final BankCode bankCode;
    private final BankName bankName;
    private final int displayOrder;
    private final boolean active;
    private final Instant createdAt;
    private final Instant updatedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private Bank(
            BankId id,
            BankCode bankCode,
            BankName bankName,
            int displayOrder,
            boolean active,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.displayOrder = displayOrder;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 은행 ID
     * @param bankCode 은행 코드
     * @param bankName 은행 이름
     * @param displayOrder 표시 순서
     * @param active 활성 상태
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return Bank 인스턴스
     */
    public static Bank reconstitute(
            BankId id,
            BankCode bankCode,
            BankName bankName,
            int displayOrder,
            boolean active,
            Instant createdAt,
            Instant updatedAt) {
        return new Bank(id, bankCode, bankName, displayOrder, active, createdAt, updatedAt);
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 은행 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 은행 ID Long 값
     */
    public Long getIdValue() {
        return id.value();
    }

    /**
     * 은행 코드 값 반환 (Law of Demeter 준수)
     *
     * @return 은행 코드 문자열
     */
    public String getBankCodeValue() {
        return bankCode.value();
    }

    /**
     * 은행 이름 값 반환 (Law of Demeter 준수)
     *
     * @return 은행 이름 문자열
     */
    public String getBankNameValue() {
        return bankName.value();
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 활성 상태 여부 확인 (Tell, Don't Ask)
     *
     * @return 활성 상태이면 true
     */
    public boolean isActive() {
        return this.active;
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public BankId getId() {
        return id;
    }

    public BankCode getBankCode() {
        return bankCode;
    }

    public BankName getBankName() {
        return bankName;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
