package com.ryuqq.setof.adapter.in.rest.admin.v1.order.dto.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * V1 주문 수정 Request (Sealed Interface)
 *
 * @author development-team
 * @since 1.0.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = ShipOrderV1ApiRequest.class, name = "shipOrder"),
        @JsonSubTypes.Type(value = ClaimOrderV1ApiRequest.class, name = "claimOrder"),
        @JsonSubTypes.Type(value = ClaimRejectedAndShipmentOrderV1ApiRequest.class,
                name = "claimRejectedAndShipmentOrder"),
        @JsonSubTypes.Type(value = NormalOrderV1ApiRequest.class, name = "normalOrder")})
@Schema(description = "주문 수정 요청")
public sealed interface UpdateOrderV1ApiRequest permits ShipOrderV1ApiRequest,
        ClaimOrderV1ApiRequest, ClaimRejectedAndShipmentOrderV1ApiRequest, NormalOrderV1ApiRequest {

    @Schema(description = "주문 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "주문 ID는 필수입니다.")
    Long getOrderId();

    @Schema(description = "주문 상태", example = "CANCELLED",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "주문 상태는 필수입니다.")
    String getOrderStatus();

    @Schema(description = "변경 사유", example = "고객 요청")
    String getChangeReason();

    @Schema(description = "변경 상세 사유", example = "단순 변심")
    String getChangeDetailReason();

    @Schema(description = "우회 여부", example = "false")
    Boolean isByPass();
}


/**
 * V1 일반 주문 수정 Request
 */
@Schema(description = "일반 주문 수정 요청")
record NormalOrderV1ApiRequest(
        @Schema(description = "주문 ID", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "주문 ID는 필수입니다.") Long orderId,
        @Schema(description = "주문 상태", example = "CANCELLED",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "주문 상태는 필수입니다.") String orderStatus,
        @Schema(description = "우회 여부", example = "false") Boolean byPass)
        implements UpdateOrderV1ApiRequest {

    @Override
    public Long getOrderId() {
        return orderId;
    }

    @Override
    public String getOrderStatus() {
        return orderStatus;
    }

    @Override
    public String getChangeReason() {
        return "";
    }

    @Override
    public String getChangeDetailReason() {
        return "";
    }

    @Override
    public Boolean isByPass() {
        return byPass != null ? byPass : false;
    }
}


/**
 * V1 배송 주문 수정 Request
 */
@Schema(description = "배송 주문 수정 요청")
record ShipOrderV1ApiRequest(
        @Schema(description = "주문 ID", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "주문 ID는 필수입니다.") Long orderId,
        @Schema(description = "주문 상태", example = "SHIPPING",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "주문 상태는 필수입니다.") String orderStatus,
        @Schema(description = "우회 여부", example = "false") Boolean byPass,
        @Schema(description = "배송 정보") @jakarta.validation.Valid ShipmentInfoV1ApiRequest shipmentInfo)
        implements UpdateOrderV1ApiRequest {

    @Override
    public Long getOrderId() {
        return orderId;
    }

    @Override
    public String getOrderStatus() {
        return orderStatus;
    }

    @Override
    public String getChangeReason() {
        return "";
    }

    @Override
    public String getChangeDetailReason() {
        return "";
    }

    @Override
    public Boolean isByPass() {
        return byPass != null ? byPass : false;
    }
}


/**
 * V1 클레임 주문 수정 Request
 */
@Schema(description = "클레임 주문 수정 요청")
record ClaimOrderV1ApiRequest(
        @Schema(description = "주문 ID", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "주문 ID는 필수입니다.") Long orderId,
        @Schema(description = "주문 상태", example = "CANCELLED",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "주문 상태는 필수입니다.") String orderStatus,
        @Schema(description = "우회 여부", example = "false") Boolean byPass,
        @Schema(description = "변경 사유", example = "고객 요청") @Length(max = 200,
                message = "변경 사유는 200자를 넘을 수 없습니다.") String changeReason,
        @Schema(description = "변경 상세 사유", example = "단순 변심") @Length(max = 500,
                message = "변경 상세 사유는 500자를 넘을 수 없습니다.") String changeDetailReason)
        implements UpdateOrderV1ApiRequest {

    @Override
    public Long getOrderId() {
        return orderId;
    }

    @Override
    public String getOrderStatus() {
        return orderStatus;
    }

    @Override
    public String getChangeReason() {
        return changeReason;
    }

    @Override
    public String getChangeDetailReason() {
        return changeDetailReason;
    }

    @Override
    public Boolean isByPass() {
        return byPass != null ? byPass : false;
    }
}


/**
 * V1 클레임 거부 및 배송 주문 수정 Request
 */
@Schema(description = "클레임 거부 및 배송 주문 수정 요청")
record ClaimRejectedAndShipmentOrderV1ApiRequest(
        @Schema(description = "주문 ID", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "주문 ID는 필수입니다.") Long orderId,
        @Schema(description = "주문 상태", example = "SHIPPING",
                requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(
                        message = "주문 상태는 필수입니다.") String orderStatus,
        @Schema(description = "우회 여부", example = "false") Boolean byPass,
        @Schema(description = "변경 사유", example = "고객 요청") @Length(max = 200,
                message = "변경 사유는 200자를 넘을 수 없습니다.") String changeReason,
        @Schema(description = "변경 상세 사유", example = "단순 변심") @Length(max = 500,
                message = "변경 상세 사유는 500자를 넘을 수 없습니다.") String changeDetailReason,
        @Schema(description = "배송 정보") @jakarta.validation.Valid ShipmentInfoV1ApiRequest shipmentInfo)
        implements UpdateOrderV1ApiRequest {

    @Override
    public Long getOrderId() {
        return orderId;
    }

    @Override
    public String getOrderStatus() {
        return orderStatus;
    }

    @Override
    public String getChangeReason() {
        return changeReason;
    }

    @Override
    public String getChangeDetailReason() {
        return changeDetailReason;
    }

    @Override
    public Boolean isByPass() {
        return byPass != null ? byPass : false;
    }
}
