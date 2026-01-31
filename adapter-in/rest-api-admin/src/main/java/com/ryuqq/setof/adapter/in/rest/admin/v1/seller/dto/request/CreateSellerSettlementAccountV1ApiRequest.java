package com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * CreateSellerSettlementAccountV1ApiRequest - 셀러 정산 계좌 확인 요청 DTO (V1 레거시).
 *
 * @deprecated 이 API는 더 이상 지원되지 않습니다. validateSellerAccount API is deprecated and no longer
 *     supported.
 * @author ryu-qqq
 * @since 1.0.0
 */
@Deprecated
@Schema(description = "셀러 정산 계좌 확인 요청 DTO (V1) - DEPRECATED")
public record CreateSellerSettlementAccountV1ApiRequest(
        @Schema(description = "은행명", example = "국민은행") String bankName,
        @Schema(description = "계좌번호", example = "1234567890123") String accountNumber,
        @Schema(description = "예금주", example = "홍길동") String accountHolderName) {}
