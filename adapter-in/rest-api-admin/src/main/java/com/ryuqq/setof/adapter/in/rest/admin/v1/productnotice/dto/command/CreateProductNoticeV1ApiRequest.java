package com.ryuqq.setof.adapter.in.rest.admin.v1.productnotice.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

/**
 * V1 상품 고지 생성 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 고지 생성 요청")
public record CreateProductNoticeV1ApiRequest(
        @Schema(description = "소재", example = "면 100%")
                @Length(max = 200, message = "소재는 200자를 넘을 수 없습니다.")
                String material,
        @Schema(description = "색상", example = "블랙")
                @Length(max = 100, message = "색상은 100자를 넘을 수 없습니다.")
                String color,
        @Schema(description = "사이즈", example = "M")
                @Length(max = 500, message = "사이즈는 500자를 넘을 수 없습니다.")
                String size,
        @Schema(description = "제조사", example = "제조사명")
                @Length(max = 50, message = "제조사는 50자를 넘을 수 없습니다.")
                String maker,
        @Schema(description = "원산지", example = "한국")
                @Length(max = 50, message = "원산지는 50자를 넘을 수 없습니다.")
                String origin,
        @Schema(description = "세탁 방법", example = "손세탁")
                @Length(max = 100, message = "세탁 방법은 100자를 넘을 수 없습니다.")
                String washingMethod,
        @Schema(description = "제조년월", example = "2024-01")
                @Length(max = 50, message = "제조년월은 50자를 넘을 수 없습니다.")
                String yearMonth,
        @Schema(description = "품질보증기준", example = "소비자분쟁해결기준")
                @Length(max = 50, message = "품질보증기준은 50자를 넘을 수 없습니다.")
                String assuranceStandard,
        @Schema(description = "A/S 전화번호", example = "1588-0000")
                @Length(max = 50, message = "A/S 전화번호는 50자를 넘을 수 없습니다.")
                String asPhone) {}
