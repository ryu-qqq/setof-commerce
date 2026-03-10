package com.ryuqq.setof.adapter.out.persistence.productgroupimage.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.ProductGroupImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.mapper.ProductGroupImageJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroupimage.repository.ProductGroupImageJpaRepository;
import com.ryuqq.setof.application.productgroupimage.port.out.command.ProductGroupImageCommandPort;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupImageCommandAdapter - 상품그룹 이미지 Command 어댑터.
 *
 * <p>ProductGroupImageCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupImageCommandAdapter implements ProductGroupImageCommandPort {

    private final ProductGroupImageJpaRepository repository;
    private final ProductGroupImageJpaEntityMapper mapper;

    public ProductGroupImageCommandAdapter(
            ProductGroupImageJpaRepository repository, ProductGroupImageJpaEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 상품그룹 이미지 단건 저장.
     *
     * @param image ProductGroupImage 도메인 객체
     * @param productGroupId 상품그룹 ID
     */
    @Override
    public void persist(ProductGroupImage image, Long productGroupId) {
        ProductGroupImageJpaEntity entity = mapper.toEntity(image, productGroupId);
        repository.save(entity);
    }

    /**
     * 상품그룹 이미지 일괄 저장.
     *
     * @param images ProductGroupImage 도메인 객체 목록
     * @param productGroupId 상품그룹 ID
     */
    @Override
    public void persistAll(List<ProductGroupImage> images, Long productGroupId) {
        List<ProductGroupImageJpaEntity> entities =
                images.stream().map(img -> mapper.toEntity(img, productGroupId)).toList();
        repository.saveAll(entities);
    }
}
