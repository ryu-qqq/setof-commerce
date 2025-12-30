package com.ryuqq.setof.adapter.out.client.smartdelivery.config;

/**
 * SmartDeliveryPaths - 스마트택배 API 경로 상수
 *
 * <p>스마트택배(SweetTracker) API 엔드포인트 경로를 관리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public final class SmartDeliveryPaths {

    private SmartDeliveryPaths() {
        // 인스턴스화 방지
    }

    /**
     * 택배사 목록 조회 API
     *
     * <p>GET /api/v1/companylist?t_key={api_key}
     */
    public static final String COMPANY_LIST = "/api/v1/companylist";

    /**
     * 운송장 추적 API
     *
     * <p>GET /api/v1/trackingInfo?t_key={api_key}&t_code={carrier_code}&t_invoice={invoice}
     */
    public static final String TRACKING_INFO = "/api/v1/trackingInfo";

    /**
     * 추천 택배사 조회 API
     *
     * <p>GET /api/v1/recommend?t_key={api_key}&t_invoice={invoice}
     */
    public static final String RECOMMEND_COMPANY = "/api/v1/recommend";
}
