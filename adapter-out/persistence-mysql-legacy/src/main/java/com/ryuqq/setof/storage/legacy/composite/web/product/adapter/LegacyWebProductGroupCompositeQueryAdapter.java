package com.ryuqq.setof.storage.legacy.composite.web.product.adapter;

import com.ryuqq.setof.application.legacy.product.dto.response.LegacyProductGroupDetailResult;
import com.ryuqq.setof.application.legacy.product.dto.response.LegacyProductGroupThumbnailResult;
import com.ryuqq.setof.domain.legacy.product.dto.query.LegacyProductGroupSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.product.dto.LegacyWebProductGroupBasicQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.product.dto.LegacyWebProductGroupThumbnailQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.product.dto.LegacyWebProductImageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.product.dto.LegacyWebProductQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.product.mapper.LegacyWebProductGroupMapper;
import com.ryuqq.setof.storage.legacy.composite.web.product.repository.LegacyWebProductGroupCompositeQueryDslRepository;
import com.ryuqq.setof.storage.legacy.composite.web.product.repository.LegacyWebProductGroupDetailQueryDslRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebProductGroupCompositeQueryAdapter - 레거시 웹 상품그룹 Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacyProductGroupCompositeQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebProductGroupCompositeQueryAdapter {

    private final LegacyWebProductGroupCompositeQueryDslRepository repository;
    private final LegacyWebProductGroupDetailQueryDslRepository detailRepository;
    private final LegacyWebProductGroupMapper mapper;

    public LegacyWebProductGroupCompositeQueryAdapter(
            LegacyWebProductGroupCompositeQueryDslRepository repository,
            LegacyWebProductGroupDetailQueryDslRepository detailRepository,
            LegacyWebProductGroupMapper mapper) {
        this.repository = repository;
        this.detailRepository = detailRepository;
        this.mapper = mapper;
    }

    /**
     * 상품그룹 썸네일 목록 조회 (필터 + 페이징).
     *
     * @param condition 검색 조건
     * @return 상품그룹 썸네일 목록
     */
    public List<LegacyProductGroupThumbnailResult> fetchProductGroupThumbnails(
            LegacyProductGroupSearchCondition condition) {
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                repository.fetchProductGroupThumbnails(condition);
        return mapper.toResults(dtos);
    }

    /**
     * 브랜드별 상품그룹 목록 조회.
     *
     * @param brandId 브랜드 ID
     * @param pageSize 페이지 크기
     * @return 상품그룹 썸네일 목록
     */
    public List<LegacyProductGroupThumbnailResult> fetchProductGroupsByBrand(
            Long brandId, int pageSize) {
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                repository.fetchProductGroupsByBrand(brandId, pageSize);
        return mapper.toResults(dtos);
    }

    /**
     * 셀러별 상품그룹 목록 조회.
     *
     * @param sellerId 셀러 ID
     * @param pageSize 페이지 크기
     * @return 상품그룹 썸네일 목록
     */
    public List<LegacyProductGroupThumbnailResult> fetchProductGroupsBySeller(
            Long sellerId, int pageSize) {
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                repository.fetchProductGroupsBySeller(sellerId, pageSize);
        return mapper.toResults(dtos);
    }

    /**
     * 최근 본 상품 조회 (ID 목록, 순서 유지).
     *
     * <p>요청된 ID 순서대로 정렬하여 반환합니다.
     *
     * @param productGroupIds 상품그룹 ID 목록
     * @return 상품그룹 썸네일 목록 (요청 순서 유지)
     */
    public List<LegacyProductGroupThumbnailResult> fetchProductGroupsByIds(
            List<Long> productGroupIds) {
        List<LegacyWebProductGroupThumbnailQueryDto> dtos =
                repository.fetchProductGroupsByIds(productGroupIds);
        List<LegacyProductGroupThumbnailResult> results = mapper.toResults(dtos);

        return reorderByRequestedIds(results, productGroupIds);
    }

    /**
     * 상품그룹 개수 조회.
     *
     * @param condition 검색 조건
     * @return 상품그룹 개수
     */
    public long countProductGroups(LegacyProductGroupSearchCondition condition) {
        return repository.countProductGroups(condition);
    }

    /**
     * 상품그룹 상세 조회.
     *
     * <p>3개의 분리된 쿼리로 최적화:
     *
     * <ul>
     *   <li>Query 1: 기본 정보 (8개 테이블 조인)
     *   <li>Query 2: 상품+옵션 (5개 테이블)
     *   <li>Query 3: 이미지 (1개 테이블)
     * </ul>
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품그룹 상세 정보 Optional
     */
    public Optional<LegacyProductGroupDetailResult> fetchProductGroupDetail(Long productGroupId) {
        // Query 1: 기본 정보 조회
        Optional<LegacyWebProductGroupBasicQueryDto> basicInfoOpt =
                detailRepository.fetchBasicInfo(productGroupId);
        if (basicInfoOpt.isEmpty()) {
            return Optional.empty();
        }

        LegacyWebProductGroupBasicQueryDto basicInfo = basicInfoOpt.get();

        // Query 2: 상품+옵션 조회
        List<LegacyWebProductQueryDto> products = detailRepository.fetchProducts(productGroupId);

        // Query 3: 이미지 조회
        List<LegacyWebProductImageQueryDto> images = detailRepository.fetchImages(productGroupId);

        // 3개 쿼리 결과를 하나의 Result로 조합
        LegacyProductGroupDetailResult result = mapper.toDetailResult(basicInfo, products, images);

        return Optional.of(result);
    }

    /** 요청 ID 순서대로 결과 재정렬. */
    private List<LegacyProductGroupThumbnailResult> reorderByRequestedIds(
            List<LegacyProductGroupThumbnailResult> results, List<Long> requestedIds) {
        Map<Long, LegacyProductGroupThumbnailResult> resultMap =
                results.stream()
                        .collect(
                                Collectors.toMap(
                                        LegacyProductGroupThumbnailResult::productGroupId,
                                        Function.identity()));

        return requestedIds.stream().filter(resultMap::containsKey).map(resultMap::get).toList();
    }
}
