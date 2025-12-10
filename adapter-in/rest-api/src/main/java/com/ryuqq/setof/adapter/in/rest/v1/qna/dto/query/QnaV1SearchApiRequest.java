package com.ryuqq.setof.adapter.in.rest.v1.review.dto.query;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 브랜드 검색 필터 Request
 *
 * <p>브랜드 목록을 검색할 때 사용하는 필터 조건입니다. 검색어를 통해 브랜드명을 검색할 수 있습니다.
 *
 * @param orderType 정렬 순 (리뷰 카운트)
 * @param productGroupId 상품 그룹 PK 아이디
 * @param lastDomainId 리뷰 커서용 PK 아이디
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "리뷰 검색 필터")
public record QnaV1SearchApiRequest(
    @Schema(description = "검색 시작 날짜", example = "2024-01-01 00:00:00",
    pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
    @Schema(description = "검색 종료 날짜", example = "2024-12-31 23:59:59",
    pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
    @Schema(description = "QNA 커서용 PK 아이디", example = "1") Long lastDomainId,
    @Schema(description = "리뷰 커서용 PK 아이디", example = "PRODUCT, ORDER") Long qnaType,
    @Schema(description = "QNA 커서용 페이지 크기", example = "10") Integer pageSize
) {
}
