package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.Length;

/**
 * CreateContentV1ApiRequest - 콘텐츠 등록/수정 v1 요청 DTO.
 *
 * <p>레거시 CreateContent 형식을 그대로 유지합니다.
 *
 * <p>contentId가 null/0이면 등록, 양수이면 수정으로 처리합니다.
 *
 * @param contentId 콘텐츠 ID (null이면 등록, 양수이면 수정)
 * @param displayPeriod 전시 기간
 * @param title 콘텐츠 제목 (최대 50자)
 * @param memo 메모 (최대 200자)
 * @param imageUrl 대표 이미지 URL
 * @param displayYn 전시 여부 ("Y" or "N")
 * @param components 하위 컴포넌트 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "콘텐츠 등록/수정 요청")
public record CreateContentV1ApiRequest(
        @Schema(description = "콘텐츠 ID (null이면 등록, 양수이면 수정)")
                @JsonInclude(JsonInclude.Include.NON_NULL)
                Long contentId,
        @Schema(description = "전시 기간") @Valid DisplayPeriodV1ApiRequest displayPeriod,
        @Schema(description = "콘텐츠 제목", maxLength = 50)
                @Length(max = 50, message = "컨텐트 타이틀은 최대 50자 이내 입니다.")
                String title,
        @Schema(description = "메모", maxLength = 200)
                @Length(max = 200, message = "컨텐트 메모는 최대 200자 이내 입니다.")
                String memo,
        @Schema(description = "대표 이미지 URL") String imageUrl,
        @Schema(
                        description = "전시 여부",
                        allowableValues = {"Y", "N"})
                @NotNull(message = "displayYn는 필수입니다")
                String displayYn,
        @Schema(description = "하위 컴포넌트 목록")
                @Size(min = 1, message = "하위 컴포넌트 사이즈는 최소 1보다 커야합니다.")
                @Valid
                List<SubComponentV1ApiRequest> components) {}
