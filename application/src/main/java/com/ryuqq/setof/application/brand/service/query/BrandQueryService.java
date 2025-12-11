package com.ryuqq.setof.application.brand.service.query;

import com.ryuqq.setof.application.brand.assembler.BrandAssembler;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchQuery;
import com.ryuqq.setof.application.brand.dto.response.BrandResponse;
import com.ryuqq.setof.application.brand.dto.response.BrandSummaryResponse;
import com.ryuqq.setof.application.brand.factory.query.BrandQueryFactory;
import com.ryuqq.setof.application.brand.manager.query.BrandReadManager;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandUseCase;
import com.ryuqq.setof.application.brand.port.in.query.GetBrandsUseCase;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.query.criteria.BrandSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 브랜드 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>BrandReadManager로 브랜드 조회
 *   <li>BrandAssembler로 Response DTO 변환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class BrandQueryService implements GetBrandUseCase, GetBrandsUseCase {

    private final BrandReadManager brandReadManager;
    private final BrandQueryFactory brandQueryFactory;
    private final BrandAssembler brandAssembler;

    public BrandQueryService(
            BrandReadManager brandReadManager,
            BrandQueryFactory brandQueryFactory,
            BrandAssembler brandAssembler) {
        this.brandReadManager = brandReadManager;
        this.brandQueryFactory = brandQueryFactory;
        this.brandAssembler = brandAssembler;
    }

    @Override
    public BrandResponse execute(Long brandId) {
        Brand brand = brandReadManager.findById(brandId);
        return brandAssembler.toBrandResponse(brand);
    }

    @Override
    public PageResponse<BrandSummaryResponse> execute(BrandSearchQuery query) {
        BrandSearchCriteria criteria = brandQueryFactory.createCriteria(query);

        List<Brand> brands = brandReadManager.findByCriteria(criteria);
        long totalCount = brandReadManager.countByCriteria(criteria);

        List<BrandSummaryResponse> content = brandAssembler.toBrandSummaryResponses(brands);

        int totalPages = calculateTotalPages(totalCount, query.size());
        boolean isFirst = query.page() == 0;
        boolean isLast = query.page() >= totalPages - 1;

        return PageResponse.of(
                content, query.page(), query.size(), totalCount, totalPages, isFirst, isLast);
    }

    private int calculateTotalPages(long totalElements, int size) {
        return (int) Math.ceil((double) totalElements / size);
    }
}
