package com.ryuqq.setof.application.brand.assembler;

import com.ryuqq.setof.application.brand.dto.response.BrandResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Brand Assembler
 *
 * <p>Brand 도메인 객체를 Response DTO로 변환하는 Assembler
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BrandAssembler {

    /**
     * Brand 도메인을 BrandResponse로 변환
     *
     * @param brand Brand 도메인 객체
     * @return BrandResponse
     */
    public BrandResponse toBrandResponse(Brand brand) {
        return BrandResponse.of(
                brand.getIdValue(),
                brand.getCodeValue(),
                brand.getNameKoValue(),
                brand.getNameEnValue(),
                brand.getLogoUrlValue(),
                brand.getStatusValue());
    }

    /**
     * Brand 도메인을 BrandSummaryResponse로 변환
     *
     * @param brand Brand 도메인 객체
     * @return BrandSummaryResponse
     */
    public BrandSummaryResponse toBrandSummaryResponse(Brand brand) {
        return BrandSummaryResponse.of(
                brand.getIdValue(),
                brand.getCodeValue(),
                brand.getNameKoValue(),
                brand.getLogoUrlValue());
    }

    /**
     * Brand 도메인 목록을 BrandSummaryResponse 목록으로 변환
     *
     * @param brands Brand 도메인 목록
     * @return BrandSummaryResponse 목록
     */
    public List<BrandSummaryResponse> toBrandSummaryResponses(List<Brand> brands) {
        return brands.stream().map(this::toBrandSummaryResponse).toList();
    }
}
