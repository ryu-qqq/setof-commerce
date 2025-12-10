package com.ryuqq.setof.adapter.in.rest.v1.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * V1 마이페이지 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "마이페이지 응답")
public record MyPageV1ApiResponse(
        @Schema(description = "이름", example = "홍길동") String name,
        @Schema(description = "핸드폰 번호", example = "01012345678") String phoneNumber,
        @Schema(description = "이메일", example = "user@example.com") String email,
        @Schema(description = "소셜 로그인 타입", example = "KAKAO") String socialLoginType,
        @Schema(description = "가입일시", example = "2024-01-01 00:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime registrationDate,
        @Schema(description = "회원 등급", example = "NORMAL_GRADE") String userGrade,
        @Schema(description = "현재 마일리지", example = "10000.0") Double currentMileage,
        @Schema(description = "주문 상태별 개수") List<OrderCountV1Response> orderCounts) {

    /** 주문 상태별 개수 Response */
    @Schema(description = "주문 상태별 개수")
    public record OrderCountV1Response(
            @Schema(description = "주문 상태", example = "COMPLETED") String orderStatus,
            @Schema(description = "개수", example = "5") Long count) {}
}
