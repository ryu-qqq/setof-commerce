package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ComponentDetailsV1ApiRequest - 컴포넌트 상세 설정 v1 요청 DTO.
 *
 * <p>레거시 ComponentDetails(Embedded) 형식을 그대로 유지합니다.
 *
 * @param componentType 컴포넌트 타입
 * @param listType 리스트 타입
 * @param orderType 정렬 타입
 * @param badgeType 뱃지 타입
 * @param filterYn 필터 사용 여부 ("Y" or "N")
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "컴포넌트 상세 설정")
public record ComponentDetailsV1ApiRequest(
        @Schema(description = "컴포넌트 타입") String componentType,
        @Schema(description = "리스트 타입") String listType,
        @Schema(description = "정렬 타입") String orderType,
        @Schema(description = "뱃지 타입") String badgeType,
        @Schema(
                        description = "필터 사용 여부",
                        allowableValues = {"Y", "N"})
                String filterYn) {}
