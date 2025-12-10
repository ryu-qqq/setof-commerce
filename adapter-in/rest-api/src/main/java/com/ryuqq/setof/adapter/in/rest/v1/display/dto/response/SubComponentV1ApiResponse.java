package com.ryuqq.setof.adapter.in.rest.v1.display.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 서브 컴포넌트 Response 인터페이스
 *
 * <p>모든 서브 컴포넌트 타입의 공통 인터페이스입니다. Jackson의 다형성 처리를 위해 @JsonTypeInfo와 @JsonSubTypes를 사용합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BrandComponentDetailV1ApiResponse.class, name = "brandComponent"),
    @JsonSubTypes.Type(
            value = CategoryComponentDetailV1ApiResponse.class,
            name = "categoryComponent"),
    @JsonSubTypes.Type(
            value = ProductComponentDetailV1ApiResponse.class,
            name = "productComponent"),
    @JsonSubTypes.Type(value = TabComponentDetailV1ApiResponse.class, name = "tabComponent"),
    @JsonSubTypes.Type(value = ImageComponentDetailV1ApiResponse.class, name = "imageComponent"),
    @JsonSubTypes.Type(value = TextComponentDetailV1ApiResponse.class, name = "textComponent"),
    @JsonSubTypes.Type(value = TitleComponentDetailV1ApiResponse.class, name = "titleComponent"),
    @JsonSubTypes.Type(value = BlankComponentDetailV1ApiResponse.class, name = "blankComponent"),
})
public sealed interface SubComponentV1ApiResponse
        permits BrandComponentDetailV1ApiResponse,
                CategoryComponentDetailV1ApiResponse,
                ProductComponentDetailV1ApiResponse,
                TabComponentDetailV1ApiResponse,
                ImageComponentDetailV1ApiResponse,
                TextComponentDetailV1ApiResponse,
                TitleComponentDetailV1ApiResponse,
                BlankComponentDetailV1ApiResponse {

    /**
     * 전시 기간 응답
     *
     * @param displayStartDate 전시 시작 기간
     * @param displayEndDate 전시 종료 기간
     */
    @Schema(description = "전시 기간 응답")
    record DisplayPeriodV1ApiResponse(
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    @Schema(description = "전시 시작 기간", example = "2024-12-30 00:00:00")
                    LocalDateTime displayStartDate,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    @Schema(description = "전시 종료 기간", example = "2025-12-30 00:00:00")
                    LocalDateTime displayEndDate) {}
}
