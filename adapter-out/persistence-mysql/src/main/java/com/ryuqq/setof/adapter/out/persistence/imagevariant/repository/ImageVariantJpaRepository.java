package com.ryuqq.setof.adapter.out.persistence.imagevariant.repository;

import com.ryuqq.setof.adapter.out.persistence.imagevariant.entity.ImageVariantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * ImageVariantJpaRepository - 이미지 Variant JPA Repository.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ImageVariantJpaRepository extends JpaRepository<ImageVariantJpaEntity, Long> {

    @Modifying
    @Query(
            value =
                    "UPDATE image_variants SET deleted_at = NOW()"
                            + " WHERE source_image_id = :sourceImageId AND deleted_at IS NULL",
            nativeQuery = true)
    void softDeleteBySourceImageId(@Param("sourceImageId") Long sourceImageId);
}
