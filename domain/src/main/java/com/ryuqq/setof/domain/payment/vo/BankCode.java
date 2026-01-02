package com.ryuqq.setof.domain.payment.vo;

import java.util.Arrays;
import java.util.List;

/**
 * BankCode - 은행 코드
 *
 * <p>가상계좌 및 환불 계좌에서 사용되는 은행 코드를 정의합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java enum 사용
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public enum BankCode {

    /** KB국민은행 */
    KB("004", "KB국민은행", true, true),

    /** SC제일은행 */
    SC("023", "SC제일은행", true, true),

    /** 경남은행 */
    KYONGNAM("039", "경남은행", true, true),

    /** 광주은행 */
    KWANGJU("034", "광주은행", true, true),

    /** 기업은행 */
    IBK("003", "기업은행", true, true),

    /** 농협 */
    NH("011", "농협", true, true),

    /** 대구은행 */
    DGB("031", "대구은행", true, true),

    /** 부산은행 */
    BUSAN("032", "부산은행", true, true),

    /** 산업은행 */
    KDB("002", "산업은행", true, true),

    /** 신한은행 */
    SHINHAN("088", "신한은행", true, true),

    /** 신협 */
    SHINHYUP("048", "신협", true, true),

    /** 우리은행 */
    WOORI("020", "우리은행", true, true),

    /** 우체국 */
    EPOST("071", "우체국", true, true),

    /** 전북은행 */
    JEONBUK("037", "전북은행", true, true),

    /** 제주은행 */
    JEJU("035", "제주은행", true, true),

    /** 축협 */
    CHUK("012", "축협", true, true),

    /** 하나은행 */
    HANA("081", "하나은행", true, true),

    /** 한국씨티은행 */
    CITI("027", "한국씨티은행", true, true),

    /** 수협 */
    SUHYUP("007", "수협", true, true),

    /** K뱅크 */
    KBANK("089", "K뱅크", true, true),

    /** 카카오뱅크 */
    KAKAO("090", "카카오뱅크", true, true);

    private final String code;
    private final String displayName;
    private final boolean supportVirtualAccount;
    private final boolean supportRefund;

    BankCode(
            String code, String displayName, boolean supportVirtualAccount, boolean supportRefund) {
        this.code = code;
        this.displayName = displayName;
        this.supportVirtualAccount = supportVirtualAccount;
        this.supportRefund = supportRefund;
    }

    /**
     * 은행 코드 반환
     *
     * @return 은행 코드
     */
    public String code() {
        return code;
    }

    /**
     * 은행명 반환
     *
     * @return 은행명
     */
    public String displayName() {
        return displayName;
    }

    /**
     * 가상계좌 지원 여부
     *
     * @return 가상계좌 지원 시 true
     */
    public boolean supportVirtualAccount() {
        return supportVirtualAccount;
    }

    /**
     * 환불 지원 여부
     *
     * @return 환불 지원 시 true
     */
    public boolean supportRefund() {
        return supportRefund;
    }

    /**
     * 은행 코드로 BankCode 조회
     *
     * @param code 은행 코드
     * @return 해당 BankCode
     * @throws IllegalArgumentException 해당하는 은행이 없을 경우
     */
    public static BankCode fromCode(String code) {
        return Arrays.stream(values())
                .filter(bank -> bank.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 은행 코드가 존재하지 않습니다: " + code));
    }

    /**
     * 가상계좌 지원 은행 목록 조회
     *
     * @return 가상계좌 지원 은행 목록
     */
    public static List<BankCode> getVirtualAccountBanks() {
        return Arrays.stream(values()).filter(BankCode::supportVirtualAccount).toList();
    }

    /**
     * 환불 지원 은행 목록 조회
     *
     * @return 환불 지원 은행 목록
     */
    public static List<BankCode> getRefundBanks() {
        return Arrays.stream(values()).filter(BankCode::supportRefund).toList();
    }
}
