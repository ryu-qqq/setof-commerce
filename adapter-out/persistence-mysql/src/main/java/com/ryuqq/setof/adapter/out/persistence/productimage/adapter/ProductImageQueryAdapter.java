package com.ryuqq.setof.adapter.out.persistence.productimage.adapter;

import com.ryuqq.setof.adapter.out.persistence.productimage.mapper.ProductImageJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productimage.repository.ProductImageQueryDslRepository;
import com.ryuqq.setof.application.productimage.port.out.query.ProductImageQueryPort;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ProductImageQueryAdapter - ProductImage Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, ProductImage 조회 요청을 QueryDslRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductImageQueryAdapter implements ProductImageQueryPort {

    private final ProductImageQueryDslRepository queryDslRepository;
    private final ProductImageJpaEntityMapper mapper;

    public ProductImageQueryAdapter(
            ProductImageQueryDslRepository queryDslRepository, ProductImageJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * 이미지 ID로 조회
     *
     * @param productImageId 이미지 ID
     * @return 이미지 (없으면 empty)
     */
    @Override
    public Optional<ProductImage> findById(Long productImageId) {
        return queryDslRepository.findById(productImageId).map(mapper::toDomain);
    }

    /**
     * 상품그룹 ID로 이미지 목록 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 이미지 목록
     */
    @Override
    public List<ProductImage> findByProductGroupId(Long productGroupId) {
        return queryDslRepository.findByProductGroupId(productGroupId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
