package com.ryuqq.setof.adapter.out.persistence.imagevariant.adapter;

import com.ryuqq.setof.adapter.out.persistence.imagevariant.mapper.ImageVariantJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.repository.ImageVariantQueryDslRepository;
import com.ryuqq.setof.application.imagevariant.port.out.query.ImageVariantQueryPort;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ImageVariantQueryAdapter - 이미지 Variant Query 어댑터.
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
public class ImageVariantQueryAdapter implements ImageVariantQueryPort {

    private final ImageVariantQueryDslRepository queryDslRepository;
    private final ImageVariantJpaEntityMapper mapper;

    public ImageVariantQueryAdapter(
            ImageVariantQueryDslRepository queryDslRepository, ImageVariantJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ImageVariant> findBySourceImageId(Long sourceImageId) {
        return queryDslRepository.findBySourceImageId(sourceImageId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<ImageVariant> findBySourceImageIds(List<Long> sourceImageIds) {
        return queryDslRepository.findBySourceImageIds(sourceImageIds).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
