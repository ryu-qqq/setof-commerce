package com.ryuqq.setof.domain.bank;

import com.ryuqq.setof.domain.bank.aggregate.Bank;
import com.ryuqq.setof.domain.bank.vo.BankCode;
import com.ryuqq.setof.domain.bank.vo.BankId;
import com.ryuqq.setof.domain.bank.vo.BankName;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

/**
 * Bank TestFixture - Object Mother Pattern
 *
 * <p>테스트에서 Bank 인스턴스 생성을 위한 팩토리 클래스
 */
public final class BankFixture {

    public static final Clock FIXED_CLOCK =
            Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    private BankFixture() {
        // Utility class - 인스턴스 생성 방지
    }

    /**
     * 기본 은행 생성 (KB국민은행)
     *
     * @return Bank 인스턴스
     */
    public static Bank create() {
        return Bank.reconstitute(
                BankId.of(1L),
                BankCode.of("004"),
                BankName.of("KB국민은행"),
                1,
                true,
                FIXED_TIME,
                FIXED_TIME);
    }

    /**
     * ID 지정하여 은행 생성
     *
     * @param id 은행 ID
     * @return Bank 인스턴스
     */
    public static Bank createWithId(Long id) {
        return Bank.reconstitute(
                BankId.of(id),
                BankCode.of("004"),
                BankName.of("KB국민은행"),
                1,
                true,
                FIXED_TIME,
                FIXED_TIME);
    }

    /**
     * 비활성 은행 생성
     *
     * @return Bank 인스턴스 (비활성)
     */
    public static Bank createInactive() {
        return Bank.reconstitute(
                BankId.of(99L),
                BankCode.of("999"),
                BankName.of("테스트은행"),
                99,
                false,
                FIXED_TIME,
                FIXED_TIME);
    }

    /**
     * 여러 은행 목록 생성
     *
     * @return Bank 목록
     */
    public static List<Bank> createList() {
        return List.of(
                Bank.reconstitute(
                        BankId.of(1L),
                        BankCode.of("004"),
                        BankName.of("KB국민은행"),
                        1,
                        true,
                        FIXED_TIME,
                        FIXED_TIME),
                Bank.reconstitute(
                        BankId.of(2L),
                        BankCode.of("088"),
                        BankName.of("신한은행"),
                        2,
                        true,
                        FIXED_TIME,
                        FIXED_TIME),
                Bank.reconstitute(
                        BankId.of(3L),
                        BankCode.of("020"),
                        BankName.of("우리은행"),
                        3,
                        true,
                        FIXED_TIME,
                        FIXED_TIME));
    }

    /**
     * 커스텀 은행 생성
     *
     * @param id 은행 ID
     * @param bankCode 은행 코드
     * @param bankName 은행 이름
     * @param displayOrder 표시 순서
     * @param active 활성 상태
     * @return Bank 인스턴스
     */
    public static Bank createCustom(
            Long id, String bankCode, String bankName, int displayOrder, boolean active) {
        return Bank.reconstitute(
                BankId.of(id),
                BankCode.of(bankCode),
                BankName.of(bankName),
                displayOrder,
                active,
                FIXED_TIME,
                FIXED_TIME);
    }
}
