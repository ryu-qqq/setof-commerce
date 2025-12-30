package com.ryuqq.setof.adapter.in.rest.admin.v2.carrier.dto.response;

import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

/**
 * 택배사 API 응답 DTO
 *
 * @param carrierId 택배사 ID
 * @param code 택배사 코드 (스마트택배 API 기준)
 * @param name 택배사명
 * @param status 상태 (ACTIVE/INACTIVE)
 * @param trackingUrlTemplate 배송 조회 URL 템플릿
 * @param displayOrder 표시 순서
 * @param createdAt 생성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "택배사 응답")
public record CarrierV2ApiResponse(
        @Schema(description = "택배사 ID", example = "1") Long carrierId,
        @Schema(description = "택배사 코드", example = "04") String code,
        @Schema(description = "택배사명", example = "CJ대한통운") String name,
        @Schema(description = "상태", example = "ACTIVE") String status,
        @Schema(
                        description = "배송 조회 URL 템플릿",
                        example = "https://trace.cjlogistics.com/next/{invoiceNumber}")
                String trackingUrlTemplate,
        @Schema(description = "표시 순서", example = "1") Integer displayOrder,
        @Schema(description = "생성일시") Instant createdAt,
        @Schema(description = "수정일시") Instant updatedAt) {

    /**
     * Carrier 도메인을 API Response로 변환
     *
     * @param carrier Carrier 도메인
     * @return API 응답 DTO
     */
    public static CarrierV2ApiResponse from(Carrier carrier) {
        return new CarrierV2ApiResponse(
                carrier.getIdValue(),
                carrier.getCodeValue(),
                carrier.getNameValue(),
                carrier.getStatusValue(),
                carrier.getTrackingUrlTemplate(),
                carrier.getDisplayOrder(),
                carrier.getCreatedAt(),
                carrier.getUpdatedAt());
    }
}
