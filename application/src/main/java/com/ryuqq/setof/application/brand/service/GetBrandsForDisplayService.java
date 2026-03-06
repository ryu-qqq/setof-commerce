package com.ryuqq.setof.application.brand.service;

import com.ryuqq.setof.application.brand.assembler.BrandAssembler;
import com.ryuqq.setof.application.brand.dto.query.BrandDisplaySearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
import com.ryuqq.setof.application.brand.factory.BrandQueryFactory;
import com.ryuqq.setof.application.brand.manager.BrandReadManager;
import com.ryuqq.setof.application.brand.port.in.GetBrandsForDisplayUseCase;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 노출용 브랜드 조회 Service.
 *
 * <p>Public API용 간단한 브랜드 목록 조회입니다.
 *
 * <p>QueryFactory를 통해 Params → Criteria 변환
 *
 * <p>Assembler를 통해 BrandDisplayResult 생성
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Service
public class GetBrandsForDisplayService implements GetBrandsForDisplayUseCase {

    private final BrandReadManager readManager;
    private final BrandQueryFactory queryFactory;
    private final BrandAssembler assembler;

    public GetBrandsForDisplayService(
            BrandReadManager readManager,
            BrandQueryFactory queryFactory,
            BrandAssembler assembler) {
        this.readManager = readManager;
        this.queryFactory = queryFactory;
        this.assembler = assembler;
    }

    @Override
    public List<BrandDisplayResult> execute(BrandDisplaySearchParams params) {
        BrandSearchCriteria criteria = queryFactory.createDisplayCriteria(params);

        List<Brand> brands = readManager.findByCriteria(criteria);

        return assembler.toDisplayResults(brands);
    }
}
