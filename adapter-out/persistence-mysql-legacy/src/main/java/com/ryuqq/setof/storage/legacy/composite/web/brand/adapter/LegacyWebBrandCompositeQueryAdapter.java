package com.ryuqq.setof.storage.legacy.composite.web.brand.adapter;

import com.ryuqq.setof.application.legacy.brand.dto.response.LegacyBrandResult;
import com.ryuqq.setof.domain.legacy.brand.dto.query.LegacyBrandSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.brand.dto.LegacyWebBrandQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.brand.mapper.LegacyWebBrandMapper;
import com.ryuqq.setof.storage.legacy.composite.web.brand.repository.LegacyWebBrandCompositeQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * LegacyWebBrandCompositeQueryAdapter - 레거시 Web 브랜드 Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacyWebBrandCompositeQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebBrandCompositeQueryAdapter {

    private final LegacyWebBrandCompositeQueryDslRepository repository;
    private final LegacyWebBrandMapper mapper;

    public LegacyWebBrandCompositeQueryAdapter(
            LegacyWebBrandCompositeQueryDslRepository repository, LegacyWebBrandMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // ===== 목록 조회 (fetchBrands) =====

    /**
     * 브랜드 목록 조회.
     *
     * <p>검색어가 있으면 검색 조회, 없으면 전체 조회.
     *
     * @param condition 검색 조건
     * @return 브랜드 목록
     */
    public List<LegacyBrandResult> fetchBrands(LegacyBrandSearchCondition condition) {
        List<LegacyWebBrandQueryDto> dtos;
        if (condition.hasSearchWord()) {
            dtos = repository.fetchBrands(condition);
        } else {
            dtos = repository.fetchBrands();
        }
        return mapper.toResults(dtos);
    }

    /**
     * 전체 브랜드 조회.
     *
     * @return 브랜드 목록
     */
    public List<LegacyBrandResult> fetchBrands() {
        List<LegacyWebBrandQueryDto> dtos = repository.fetchBrands();
        return mapper.toResults(dtos);
    }

    // ===== 단건 조회 (fetchBrand) =====

    /**
     * 브랜드 단건 조회 (ID).
     *
     * @param brandId 브랜드 ID
     * @return 브랜드 Optional
     */
    public Optional<LegacyBrandResult> fetchBrand(Long brandId) {
        return repository.fetchBrand(brandId).map(mapper::toResult);
    }
}
