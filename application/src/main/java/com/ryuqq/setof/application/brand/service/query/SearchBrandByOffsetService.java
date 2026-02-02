package com.ryuqq.setof.application.brand.service.query;

import com.ryuqq.setof.application.brand.assembler.BrandAssembler;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;
import com.ryuqq.setof.application.brand.factory.BrandQueryFactory;
import com.ryuqq.setof.application.brand.manager.BrandReadManager;
import com.ryuqq.setof.application.brand.port.in.query.SearchBrandByOffsetUseCase;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 브랜드 검색 Service (Offset 기반 페이징).
 *
 * <p>APP-UC-001: Offset 페이징은 Search{Domain}ByOffsetService
 *
 * <p>QueryFactory를 통해 Params → Criteria 변환
 *
 * <p>Assembler를 통해 BrandPageResult 생성
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Service
public class SearchBrandByOffsetService implements SearchBrandByOffsetUseCase {

    private final BrandReadManager readManager;
    private final BrandQueryFactory queryFactory;
    private final BrandAssembler assembler;

    public SearchBrandByOffsetService(
            BrandReadManager readManager,
            BrandQueryFactory queryFactory,
            BrandAssembler assembler) {
        this.readManager = readManager;
        this.queryFactory = queryFactory;
        this.assembler = assembler;
    }

    @Override
    public BrandPageResult execute(BrandSearchParams params) {
        BrandSearchCriteria criteria = queryFactory.createCriteria(params);

        List<Brand> brands = readManager.findByCriteria(criteria);
        long totalElements = readManager.countByCriteria(criteria);

        return assembler.toPageResult(brands, params.page(), params.size(), totalElements);
    }
}
