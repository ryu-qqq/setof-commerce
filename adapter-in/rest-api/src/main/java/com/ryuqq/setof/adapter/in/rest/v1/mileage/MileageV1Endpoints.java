package com.ryuqq.setof.adapter.in.rest.v1.mileage;

/**
 * MileageV1Endpoints - 마일리지 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>레거시 MileageController 경로 호환:
 *
 * <ul>
 *   <li>GET /api/v1/mileage/my-page/mileage-histories
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class MileageV1Endpoints {

    private MileageV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 마일리지 기본 경로 */
    public static final String BASE_MILEAGE = "/api/v1/mileage";

    /** 마일리지 이력 조회 경로 (GET /api/v1/mileage/my-page/mileage-histories) */
    public static final String MILEAGE_HISTORIES = BASE_MILEAGE + "/my-page/mileage-histories";
}
