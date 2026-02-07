package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * SearchBannersV1ApiRequest - 배너 그룹 목록 검색 요청 DTO.
 *
 * <p>레거시 BannerFilter 기반 변환.
 *
 * <p>GET /api/v1/content/banners - 배너 그룹 목록 조회
 *
 * <p>변환 내역:
 *
 * <ul>
 *   <li>class → record 타입
 *   <li>BannerType enum → String 타입
 *   <li>Yn enum → String 타입
 *   <li>SearchKeyword enum → String 타입
 *   <li>Pageable → record 내부 page, size 필드
 *   <li>@Parameter 어노테이션 추가
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 * @see com.connectly.partnerAdmin.module.display.dto.banner.filter.BannerFilter
 */
@Schema(description = "배너 그룹 목록 검색 요청")
public record SearchBannersV1ApiRequest(
        @Parameter(
                        description = "배너 타입",
                        example = "CATEGORY",
                        schema =
                                @Schema(
                                        allowableValues = {
                                            "CATEGORY",
                                            "MY_PAGE",
                                            "CART",
                                            "PRODUCT_DETAIL_DESCRIPTION",
                                            "RECOMMEND",
                                            "LOGIN"
                                        }))
                String bannerType,
        @Parameter(
                        description = "전시 여부",
                        example = "Y",
                        schema = @Schema(allowableValues = {"Y", "N"}))
                String displayYn,
        @Parameter(description = "No-Offset 페이징용 마지막 배너 ID", example = "12345") Long lastDomainId,
        @Parameter(description = "조회 시작일 (yyyy-MM-dd HH:mm:ss)", example = "2024-01-01 00:00:00")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime startDate,
        @Parameter(description = "조회 종료일 (yyyy-MM-dd HH:mm:ss)", example = "2024-12-31 23:59:59")
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime endDate,
        @Parameter(
                        description = "검색 키워드 타입",
                        example = "BANNER_NAME",
                        schema =
                                @Schema(
                                        allowableValues = {
                                            "BANNER_NAME",
                                            "INSERT_OPERATOR",
                                            "UPDATE_OPERATOR"
                                        }))
                String searchKeyword,
        @Parameter(description = "검색어", example = "메인 배너") String searchWord,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {

    /**
     * No-Offset 페이징 사용 여부를 반환합니다.
     *
     * @return lastDomainId가 null이 아니면 true
     */
    public boolean isNoOffsetFetch() {
        return lastDomainId != null;
    }
}
