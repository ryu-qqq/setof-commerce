package com.ryuqq.setof.adapter.in.rest.v1.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MyPageV1ApiResponse - 마이페이지 정보 응답 DTO.
 *
 * <p>레거시 MyPageResponse 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>Response DTO는 Domain 객체 의존 금지 -> Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param name 사용자 이름
 * @param phoneNumber 전화번호
 * @param email 이메일
 * @param socialLoginType 소셜 로그인 타입
 * @param registrationDate 가입일시
 * @param userGrade 사용자 등급
 * @param currentMileage 현재 마일리지
 * @param orderCounts 주문 상태별 건수 목록
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.user.dto.mypage.MyPageResponse
 */
@Schema(description = "마이페이지 정보 응답")
public record MyPageV1ApiResponse(
        @Schema(description = "사용자 이름", example = "홍길동") String name,
        @Schema(description = "전화번호", example = "01012345678") String phoneNumber,
        @Schema(description = "이메일", example = "hong@example.com", nullable = true) String email,
        @Schema(
                        description = "소셜 로그인 타입",
                        example = "KAKAO",
                        allowableValues = {"NONE", "KAKAO", "NAVER", "GOOGLE", "APPLE"})
                String socialLoginType,
        @Schema(description = "가입일시", example = "2024-01-15T10:30:00")
                LocalDateTime registrationDate,
        @Schema(
                        description = "사용자 등급",
                        example = "VIP",
                        allowableValues = {"NORMAL_GRADE", "GOLD", "VIP", "VVIP"})
                String userGrade,
        @Schema(description = "현재 마일리지", example = "50000.0") double currentMileage,
        @Schema(description = "주문 상태별 건수 목록") List<OrderCountResponse> orderCounts) {

    /**
     * OrderCountResponse - 주문 상태별 건수 응답.
     *
     * @param orderStatus 주문 상태
     * @param count 해당 상태의 건수
     */
    @Schema(description = "주문 상태별 건수")
    public record OrderCountResponse(
            @Schema(
                            description = "주문 상태",
                            example = "ORDER_PROCESSING",
                            allowableValues = {
                                "ORDER_FAILED",
                                "ORDER_PROCESSING",
                                "ORDER_COMPLETED",
                                "DELIVERY_PENDING",
                                "DELIVERY_PROCESSING",
                                "DELIVERY_COMPLETED",
                                "CANCEL_REQUEST",
                                "CANCEL_REQUEST_RECANT",
                                "CANCEL_REQUEST_REJECTED",
                                "CANCEL_REQUEST_CONFIRMED",
                                "SALE_CANCELLED",
                                "RETURN_REQUEST",
                                "RETURN_DELIVERY_PROCESSING",
                                "RETURN_REQUEST_CONFIRMED",
                                "RETURN_REQUEST_RECANT",
                                "RETURN_REQUEST_REJECTED",
                                "CANCEL_REQUEST_COMPLETED",
                                "SALE_CANCELLED_COMPLETED",
                                "RETURN_REQUEST_COMPLETED",
                                "SETTLEMENT_PROCESSING",
                                "SETTLEMENT_COMPLETED"
                            })
                    String orderStatus,
            @Schema(description = "해당 상태의 건수", example = "5") long count) {}
}
