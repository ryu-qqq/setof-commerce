package com.ryuqq.setof.adapter.out.persistence.imagevariant.adapter;

import com.ryuqq.setof.adapter.out.persistence.imagevariant.entity.ImageVariantJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.mapper.ImageVariantJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.repository.ImageVariantJpaRepository;
import com.ryuqq.setof.application.imagevariant.port.out.command.ImageVariantCommandPort;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ImageVariantCommandAdapter - 이미지 Variant Command 어댑터.
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
public class ImageVariantCommandAdapter implements ImageVariantCommandPort {

    private final ImageVariantJpaRepository repository;
    private final ImageVariantJpaEntityMapper mapper;

    public ImageVariantCommandAdapter(
            ImageVariantJpaRepository repository, ImageVariantJpaEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void persistAll(List<ImageVariant> variants) {
        List<ImageVariantJpaEntity> entities = variants.stream().map(mapper::toEntity).toList();
        repository.saveAll(entities);
    }

    @Override
    public void softDeleteBySourceImageId(Long sourceImageId) {
        repository.softDeleteBySourceImageId(sourceImageId);
    }
}
