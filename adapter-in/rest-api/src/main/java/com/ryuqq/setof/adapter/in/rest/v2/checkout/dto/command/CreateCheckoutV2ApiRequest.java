package com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 체크아웃 생성 API 요청 DTO
 *
 * @param idempotencyKey 멱등성 키 (프론트엔드에서 생성)
 * @param items 체크아웃 아이템 목록
 * @param pgProvider PG사 (TOSS, KAKAO, NICE)
 * @param paymentMethod 결제 수단 (CARD, KAKAO_PAY, TOSS_PAY)
 * @param receiverName 수령인 이름
 * @param receiverPhone 수령인 연락처
 * @param zipCode 우편번호
 * @param address 주소
 * @param addressDetail 상세주소
 * @param deliveryRequest 배송 요청사항
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "체크아웃 생성 요청")
public record CreateCheckoutV2ApiRequest(
        @Schema(description = "멱등성 키 (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
                @NotBlank(message = "멱등성 키는 필수입니다")
                String idempotencyKey,
        @Schema(description = "체크아웃 아이템 목록") @NotEmpty(message = "아이템은 최소 1개 이상이어야 합니다") @Valid
                List<CreateCheckoutItemV2ApiRequest> items,
        @Schema(description = "PG사", example = "TOSS") @NotBlank(message = "PG사는 필수입니다")
                String pgProvider,
        @Schema(description = "결제 수단", example = "CARD") @NotBlank(message = "결제 수단은 필수입니다")
                String paymentMethod,
        @Schema(description = "수령인 이름", example = "홍길동")
                @NotBlank(message = "수령인 이름은 필수입니다")
                @Size(max = 50, message = "수령인 이름은 50자 이하입니다")
                String receiverName,
        @Schema(description = "수령인 연락처", example = "010-1234-5678")
                @NotBlank(message = "수령인 연락처는 필수입니다")
                String receiverPhone,
        @Schema(description = "우편번호", example = "06234") @NotBlank(message = "우편번호는 필수입니다")
                String zipCode,
        @Schema(description = "주소", example = "서울시 강남구 테헤란로") @NotBlank(message = "주소는 필수입니다")
                String address,
        @Schema(description = "상세주소", example = "123동 456호") String addressDetail,
        @Schema(description = "배송 요청사항", example = "부재 시 경비실에 맡겨주세요") String deliveryRequest) {}
