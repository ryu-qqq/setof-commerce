package com.ryuqq.setof.domain.payment.vo;

/**
 * PgProvider - PG사 (Payment Gateway Provider)
 *
 * <p>지원하는 PG사를 정의합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java enum 사용
 * </ul>
 */
public enum PgProvider {

    /** 포트원 (구 아임포트) */
    PORTONE("포트원"),

    /** 토스페이먼츠 */
    TOSS("토스페이먼츠"),

    /** 나이스페이먼츠 */
    NICE("나이스페이먼츠"),

    /** KG이니시스 */
    INICIS("KG이니시스"),

    /** NHN KCP */
    KCP("KCP"),

    /** 모의 결제 (테스트용) */
    MOCK("모의 결제");

    private final String description;

    PgProvider(String description) {
        this.description = description;
    }

    /**
     * PG사 설명 반환
     *
     * @return PG사 설명
     */
    public String description() {
        return description;
    }

    /**
     * 테스트용 PG 여부
     *
     * @return 테스트용이면 true
     */
    public boolean isTest() {
        return this == MOCK;
    }
}
