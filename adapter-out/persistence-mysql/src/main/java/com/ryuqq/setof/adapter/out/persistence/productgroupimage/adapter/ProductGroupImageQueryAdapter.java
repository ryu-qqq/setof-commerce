package com.ryuqq.setof.adapter.out.persistence.productgroupimage.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroupimage.mapper.ProductGroupImageJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.repository.ProductGroupImageQueryDslRepository;
import com.ryuqq.setof.application.productgroupimage.port.out.query.ProductGroupImageQueryPort;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * ProductGroupImageQueryAdapter - 상품그룹 이미지 Query 어댑터.
 *
 * <p>ProductGroupImageQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupImageQueryAdapter implements ProductGroupImageQueryPort {

    private final ProductGroupImageQueryDslRepository queryDslRepository;
    private final ProductGroupImageJpaEntityMapper mapper;

    public ProductGroupImageQueryAdapter(
            ProductGroupImageQueryDslRepository queryDslRepository,
            ProductGroupImageJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * 단일 상품그룹 ID로 이미지 목록 조회.
     *
     * @param productGroupId 상품그룹 ID VO
     * @return 활성 상품그룹 이미지 목록
     */
    @Override
    public List<ProductGroupImage> findByProductGroupId(ProductGroupId productGroupId) {
        return queryDslRepository.findByProductGroupId(productGroupId.value()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    /**
     * 복수 상품그룹 ID로 이미지 목록 조회.
     *
     * @param productGroupIds 상품그룹 ID VO 목록
     * @return 활성 상품그룹 이미지 목록
     */
    @Override
    public List<ProductGroupImage> findByProductGroupIds(List<ProductGroupId> productGroupIds) {
        List<Long> rawIds = productGroupIds.stream().map(ProductGroupId::value).toList();
        return queryDslRepository.findByProductGroupIds(rawIds).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Map<Long, Long> findThumbnailImageIdsByProductGroupIds(
            List<ProductGroupId> productGroupIds) {
        List<Long> rawIds = productGroupIds.stream().map(ProductGroupId::value).toList();
        return queryDslRepository.findThumbnailImageIdsByProductGroupIds(rawIds);
    }
}
