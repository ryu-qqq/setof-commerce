package com.ryuqq.setof.adapter.out.persistence.productdescription.adapter;

import com.ryuqq.setof.adapter.out.persistence.productdescription.entity.ProductDescriptionImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productdescription.entity.ProductDescriptionJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productdescription.mapper.ProductDescriptionJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productdescription.repository.ProductDescriptionQueryDslRepository;
import com.ryuqq.setof.application.productdescription.port.out.query.ProductDescriptionQueryPort;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productdescription.exception.ProductDescriptionNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * ProductDescriptionQueryAdapter - ProductDescription Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, ProductDescription 조회 요청을 QueryDslRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductDescriptionQueryAdapter implements ProductDescriptionQueryPort {

    private final ProductDescriptionQueryDslRepository queryDslRepository;
    private final ProductDescriptionJpaEntityMapper mapper;

    public ProductDescriptionQueryAdapter(
            ProductDescriptionQueryDslRepository queryDslRepository,
            ProductDescriptionJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * ID로 상품설명 조회
     *
     * @param productDescriptionId 상품설명 ID
     * @return 상품설명
     * @throws ProductDescriptionNotFoundException 상품설명을 찾을 수 없는 경우
     */
    @Override
    public ProductDescription findById(Long productDescriptionId) {
        ProductDescriptionJpaEntity entity =
                queryDslRepository
                        .findById(productDescriptionId)
                        .orElseThrow(
                                () ->
                                        new ProductDescriptionNotFoundException(
                                                productDescriptionId));
        List<ProductDescriptionImageJpaEntity> imageEntities =
                queryDslRepository.findImagesByProductDescriptionId(productDescriptionId);
        return mapper.toDomain(entity, imageEntities);
    }

    /**
     * 상품그룹 ID로 상품설명 조회
     *
     * @param productGroupId 상품그룹 ID
     * @return 상품설명 (없으면 empty)
     */
    @Override
    public Optional<ProductDescription> findByProductGroupId(Long productGroupId) {
        return queryDslRepository
                .findByProductGroupId(productGroupId)
                .map(
                        entity -> {
                            List<ProductDescriptionImageJpaEntity> imageEntities =
                                    queryDslRepository.findImagesByProductDescriptionId(
                                            entity.getId());
                            return mapper.toDomain(entity, imageEntities);
                        });
    }
}
