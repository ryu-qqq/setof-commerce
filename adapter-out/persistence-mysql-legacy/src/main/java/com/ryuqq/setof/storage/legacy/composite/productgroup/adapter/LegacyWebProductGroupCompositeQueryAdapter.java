package com.ryuqq.setof.storage.legacy.composite.productgroup.adapter;

import com.ryuqq.setof.application.productgroup.dto.composite.LegacyProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupCompositionQueryPort;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import com.ryuqq.setof.domain.legacy.search.dto.query.LegacySearchCondition;
import com.ryuqq.setof.storage.legacy.composite.productgroup.dto.LegacyWebProductGroupBasicQueryDto;
import com.ryuqq.setof.storage.legacy.composite.productgroup.dto.LegacyWebProductGroupThumbnailQueryDto;
import com.ryuqq.setof.storage.legacy.composite.productgroup.dto.LegacyWebProductImageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.productgroup.dto.LegacyWebProductQueryDto;
import com.ryuqq.setof.storage.legacy.composite.productgroup.mapper.LegacyWebProductGroupMapper;
import com.ryuqq.setof.storage.legacy.composite.productgroup.repository.LegacyWebProductGroupCompositeQueryDslRepository;
import com.ryuqq.setof.storage.legacy.composite.search.mapper.LegacyWebSearchMapper;
import com.ryuqq.setof.storage.legacy.composite.search.repository.LegacyWebSearchCompositeQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * LegacyWebProductGroupCompositeQueryAdapter - 레거시 Web 상품그룹 Composite 조회 Adapter.
 *
 * <p>ProductGroupCompositionQueryPort를 구현하여 Application Layer에 상품그룹 조회 기능을 제공합니다.
 *
 * <p>product/group 목록 조회와 키워드 검색(MySQL ngram FULLTEXT) 모두 이 Adapter에서 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebProductGroupCompositeQueryAdapter
        implements ProductGroupCompositionQueryPort {

    private final LegacyWebProductGroupCompositeQueryDslRepository productGroupRepository;
    private final LegacyWebSearchCompositeQueryDslRepository searchRepository;
    private final LegacyWebProductGroupMapper productGroupMapper;
    private final LegacyWebSearchMapper searchMapper;

    public LegacyWebProductGroupCompositeQueryAdapter(
            LegacyWebProductGroupCompositeQueryDslRepository productGroupRepository,
            LegacyWebSearchCompositeQueryDslRepository searchRepository,
            LegacyWebProductGroupMapper productGroupMapper,
            LegacyWebSearchMapper searchMapper) {
        this.productGroupRepository = productGroupRepository;
        this.searchRepository = searchRepository;
        this.productGroupMapper = productGroupMapper;
        this.searchMapper = searchMapper;
    }

    /**
     * 상품그룹 단건 상세 조회 (fetchProductGroup).
     *
     * <p>기본 정보(쿼리 1) + 개별 상품 목록(쿼리 2) + 이미지 목록(쿼리 3)을 조합합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @return LegacyProductGroupDetailCompositeResult Optional
     */
    @Override
    public Optional<LegacyProductGroupDetailCompositeResult> fetchProductGroupDetail(
            Long productGroupId) {
        Optional<LegacyWebProductGroupBasicQueryDto> basicOpt =
                productGroupRepository.fetchBasicInfo(productGroupId);
        if (basicOpt.isEmpty()) {
            return Optional.empty();
        }
        LegacyWebProductGroupBasicQueryDto basic = basicOpt.get();
        List<LegacyWebProductQueryDto> products =
                productGroupRepository.fetchProducts(productGroupId);
        List<LegacyWebProductImageQueryDto> images =
                productGroupRepository.fetchImages(productGroupId);
        return Optional.of(productGroupMapper.toDetailCompositeResult(basic, products, images));
    }

    /**
     * 상품그룹 썸네일 목록 조회 (fetchProductGroups - 커서 페이징).
     *
     * <p>pageSize + 1개 조회하여 hasNext 판별을 위임합니다.
     *
     * @param condition 검색 조건
     * @return 썸네일 목록
     */
    @Override
    public List<ProductGroupThumbnailCompositeResult> fetchProductGroupThumbnails(
            LegacyProductGroupSearchCondition condition) {
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                productGroupRepository.fetchProductGroupThumbnails(condition);
        return productGroupMapper.toThumbnailCompositeResults(dtos);
    }

    /**
     * 상품그룹 총 개수 조회 (카운트 쿼리).
     *
     * @param condition 검색 조건
     * @return 총 개수
     */
    @Override
    public long fetchProductGroupCount(LegacyProductGroupSearchCondition condition) {
        return productGroupRepository.fetchProductGroupCount(condition);
    }

    /**
     * ID 목록 기반 상품그룹 썸네일 조회 (찜 목록 등).
     *
     * <p>요청 ID 순서로 재정렬하여 반환합니다.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return 요청 ID 순서로 재정렬된 썸네일 목록
     */
    @Override
    public List<ProductGroupThumbnailCompositeResult> fetchProductGroupsByIds(
            List<Long> productGroupIds) {
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                productGroupRepository.fetchProductGroupsByIds(productGroupIds);
        List<ProductGroupThumbnailCompositeResult> results =
                productGroupMapper.toThumbnailCompositeResults(dtos);
        return productGroupMapper.reOrder(productGroupIds, results);
    }

    /**
     * 브랜드별 상품그룹 썸네일 조회 (fetchProductGroupWithBrand).
     *
     * <p>Redis 캐시 없이 직접 DB 조회합니다. (score DESC 정렬)
     *
     * @param brandId 브랜드 ID
     * @param pageSize 페이지 크기
     * @return 썸네일 목록
     */
    @Override
    public List<ProductGroupThumbnailCompositeResult> fetchProductGroupsByBrand(
            Long brandId, int pageSize) {
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                productGroupRepository.fetchProductGroupsByBrand(brandId, pageSize);
        return productGroupMapper.toThumbnailCompositeResults(dtos);
    }

    /**
     * 셀러별 상품그룹 썸네일 조회 (fetchProductGroupWithSeller).
     *
     * <p>Redis 캐시 없이 직접 DB 조회합니다. (score DESC 정렬)
     *
     * @param sellerId 셀러 ID
     * @param pageSize 페이지 크기
     * @return 썸네일 목록
     */
    @Override
    public List<ProductGroupThumbnailCompositeResult> fetchProductGroupsBySeller(
            Long sellerId, int pageSize) {
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                productGroupRepository.fetchProductGroupsBySeller(sellerId, pageSize);
        return productGroupMapper.toThumbnailCompositeResults(dtos);
    }

    /**
     * 키워드 검색 결과 목록 조회 (MySQL ngram FULLTEXT + 커서 페이징).
     *
     * <p>pageSize + 1개 조회하여 hasNext 판별을 위임합니다.
     *
     * @param condition 검색 조건 (searchWord 포함)
     * @return 검색 결과 목록
     */
    @Override
    public List<ProductGroupThumbnailCompositeResult> fetchSearchResults(
            LegacySearchCondition condition) {
        return searchMapper.toThumbnailCompositeResults(
                searchRepository.fetchSearchResults(condition));
    }

    /**
     * 키워드 검색 전체 건수 조회.
     *
     * @param condition 검색 조건
     * @return 전체 검색 건수
     */
    @Override
    public long fetchSearchCount(LegacySearchCondition condition) {
        return searchRepository.fetchSearchCount(condition);
    }
}
