package com.ryuqq.setof.adapter.out.persistence.imagevariant.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.entity.ImageVariantJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.entity.QImageVariantJpaEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * ImageVariantQueryDslRepository - 이미지 Variant QueryDSL Repository.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class ImageVariantQueryDslRepository {

    private static final QImageVariantJpaEntity imageVariant =
            QImageVariantJpaEntity.imageVariantJpaEntity;

    private final JPAQueryFactory queryFactory;

    public ImageVariantQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 단일 원본 이미지 ID로 Variant 목록 조회.
     *
     * @param sourceImageId 원본 이미지 ID
     * @return 활성 Variant 목록
     */
    public List<ImageVariantJpaEntity> findBySourceImageId(Long sourceImageId) {
        return queryFactory
                .selectFrom(imageVariant)
                .where(
                        imageVariant.sourceImageId.eq(sourceImageId),
                        imageVariant.deletedAt.isNull())
                .fetch();
    }

    /**
     * 복수 원본 이미지 ID로 Variant 목록 조회.
     *
     * @param sourceImageIds 원본 이미지 ID 목록
     * @return 활성 Variant 목록
     */
    public List<ImageVariantJpaEntity> findBySourceImageIds(List<Long> sourceImageIds) {
        return queryFactory
                .selectFrom(imageVariant)
                .where(
                        imageVariant.sourceImageId.in(sourceImageIds),
                        imageVariant.deletedAt.isNull())
                .fetch();
    }
}
