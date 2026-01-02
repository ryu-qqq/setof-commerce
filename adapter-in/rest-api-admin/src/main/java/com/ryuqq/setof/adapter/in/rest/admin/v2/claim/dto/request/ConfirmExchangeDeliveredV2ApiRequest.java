package com.ryuqq.setof.adapter.in.rest.admin.v2.claim.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ConfirmExchangeDeliveredV2ApiRequest - 교환품 수령 확인 API Request DTO
 *
 * <p>교환품 수령 확인은 추가 파라미터 없이 claimId만으로 처리합니다. 빈 Request Body를 사용하여 API 일관성을 유지합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "교환품 수령 확인 요청")
public record ConfirmExchangeDeliveredV2ApiRequest() {}
