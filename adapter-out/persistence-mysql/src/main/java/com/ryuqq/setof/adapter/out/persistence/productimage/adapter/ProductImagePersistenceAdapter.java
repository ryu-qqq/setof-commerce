package com.ryuqq.setof.adapter.out.persistence.productimage.adapter;

import com.ryuqq.setof.adapter.out.persistence.productimage.entity.ProductImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productimage.mapper.ProductImageJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productimage.repository.ProductImageJpaRepository;
import com.ryuqq.setof.application.productimage.port.out.command.ProductImagePersistencePort;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import org.springframework.stereotype.Component;

/**
 * ProductImagePersistenceAdapter - ProductImage Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, ProductImage 저장 요청을 JpaRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductImagePersistenceAdapter implements ProductImagePersistencePort {

    private final ProductImageJpaRepository jpaRepository;
    private final ProductImageJpaEntityMapper mapper;

    public ProductImagePersistenceAdapter(
            ProductImageJpaRepository jpaRepository, ProductImageJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * ProductImage 저장 (신규 생성)
     *
     * @param productImage 저장할 이미지
     * @return 저장된 이미지 ID
     */
    @Override
    public Long save(ProductImage productImage) {
        ProductImageJpaEntity entity = mapper.toEntity(productImage);
        ProductImageJpaEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.getId();
    }

    /**
     * ProductImage 업데이트
     *
     * @param productImage 업데이트할 이미지
     */
    @Override
    public void update(ProductImage productImage) {
        ProductImageJpaEntity entity = mapper.toEntity(productImage);
        jpaRepository.save(entity);
    }

    /**
     * ProductImage 삭제
     *
     * @param productImageId 삭제할 이미지 ID
     */
    @Override
    public void delete(Long productImageId) {
        jpaRepository.deleteById(productImageId);
    }

    /**
     * 상품그룹 ID로 이미지 일괄 삭제
     *
     * @param productGroupId 상품그룹 ID
     */
    @Override
    public void deleteByProductGroupId(Long productGroupId) {
        jpaRepository.deleteByProductGroupId(productGroupId);
    }
}
