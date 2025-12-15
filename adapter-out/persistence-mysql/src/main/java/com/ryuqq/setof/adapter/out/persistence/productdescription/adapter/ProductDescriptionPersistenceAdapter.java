package com.ryuqq.setof.adapter.out.persistence.productdescription.adapter;

import com.ryuqq.setof.adapter.out.persistence.productdescription.entity.ProductDescriptionImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productdescription.entity.ProductDescriptionJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productdescription.mapper.ProductDescriptionJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productdescription.repository.ProductDescriptionImageJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productdescription.repository.ProductDescriptionJpaRepository;
import com.ryuqq.setof.application.productdescription.port.out.command.ProductDescriptionPersistencePort;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductDescriptionPersistenceAdapter - ProductDescription Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, ProductDescription 저장 요청을 JpaRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductDescriptionPersistenceAdapter implements ProductDescriptionPersistencePort {

    private final ProductDescriptionJpaRepository jpaRepository;
    private final ProductDescriptionImageJpaRepository imageJpaRepository;
    private final ProductDescriptionJpaEntityMapper mapper;

    public ProductDescriptionPersistenceAdapter(
            ProductDescriptionJpaRepository jpaRepository,
            ProductDescriptionImageJpaRepository imageJpaRepository,
            ProductDescriptionJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.imageJpaRepository = imageJpaRepository;
        this.mapper = mapper;
    }

    /**
     * ProductDescription 저장 (신규 생성)
     *
     * @param productDescription 저장할 상품설명
     * @return 저장된 상품설명 ID
     */
    @Override
    public Long persist(ProductDescription productDescription) {
        // 1. 메인 Entity 저장
        ProductDescriptionJpaEntity entity = mapper.toEntity(productDescription);
        ProductDescriptionJpaEntity savedEntity = jpaRepository.save(entity);

        // 2. 이미지 Entity 저장
        saveImages(productDescription, savedEntity.getId());

        return savedEntity.getId();
    }

    /**
     * ProductDescription 수정
     *
     * @param productDescription 수정할 상품설명
     */
    @Override
    public void update(ProductDescription productDescription) {
        // 1. 메인 Entity 저장
        ProductDescriptionJpaEntity entity = mapper.toEntity(productDescription);
        jpaRepository.save(entity);

        // 2. 기존 이미지 삭제 후 새로 저장
        Long productDescriptionId = productDescription.getIdValue();
        imageJpaRepository.deleteByProductDescriptionId(productDescriptionId);
        saveImages(productDescription, productDescriptionId);
    }

    /**
     * 이미지 저장
     *
     * @param productDescription 상품설명
     * @param productDescriptionId 상품설명 ID
     */
    private void saveImages(ProductDescription productDescription, Long productDescriptionId) {
        List<ProductDescriptionImageJpaEntity> imageEntities =
                mapper.toImageEntities(productDescription.getImages(), productDescriptionId);
        if (!imageEntities.isEmpty()) {
            imageJpaRepository.saveAll(imageEntities);
        }
    }
}
