package com.ryuqq.setof.application.brand.assembler;

import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Brand Assembler.
 *
 * <p>Domain → Result 변환 및 PageResult 생성을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class BrandAssembler {

    /**
     * Domain → BrandResult 변환.
     *
     * @param brand Brand 도메인 객체
     * @return BrandResult
     */
    public BrandResult toResult(Brand brand) {
        return BrandResult.of(
                brand.idValue(),
                brand.brandNameValue(),
                brand.displayNameValue(),
                brand.brandIconImageUrlValue(),
                brand.isDisplayed());
    }

    /**
     * Domain List → BrandResult List 변환.
     *
     * @param brands Brand 도메인 객체 목록
     * @return BrandResult 목록
     */
    public List<BrandResult> toResults(List<Brand> brands) {
        return brands.stream().map(this::toResult).toList();
    }

    /**
     * 페이지 결과 생성.
     *
     * @param brands Brand 도메인 객체 목록
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param totalCount 전체 개수
     * @return BrandPageResult
     */
    public BrandPageResult toPageResult(List<Brand> brands, int page, int size, long totalCount) {
        List<BrandResult> results = toResults(brands);
        return BrandPageResult.of(results, totalCount, page, size);
    }

    /**
     * Domain → BrandDisplayResult 변환.
     *
     * @param brand Brand 도메인 객체
     * @return BrandDisplayResult
     */
    public BrandDisplayResult toDisplayResult(Brand brand) {
        return BrandDisplayResult.of(
                brand.idValue(),
                brand.brandNameValue(),
                brand.displayNameValue(),
                brand.brandIconImageUrlValue());
    }

    /**
     * Domain List → BrandDisplayResult List 변환.
     *
     * @param brands Brand 도메인 객체 목록
     * @return BrandDisplayResult 목록
     */
    public List<BrandDisplayResult> toDisplayResults(List<Brand> brands) {
        return brands.stream().map(this::toDisplayResult).toList();
    }
}
