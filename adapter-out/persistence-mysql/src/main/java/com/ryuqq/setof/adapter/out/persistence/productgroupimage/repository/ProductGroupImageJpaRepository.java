package com.ryuqq.setof.adapter.out.persistence.productgroupimage.repository;

import com.ryuqq.setof.adapter.out.persistence.productgroupimage.entity.ProductGroupImageJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * ProductGroupImageJpaRepository - 상품그룹 이미지 JPA Repository.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ProductGroupImageJpaRepository
        extends JpaRepository<ProductGroupImageJpaEntity, Long> {

    void deleteByProductGroupId(Long productGroupId);

    List<ProductGroupImageJpaEntity> findByProductGroupIdAndDeletedAtIsNull(Long productGroupId);

    @Modifying
    @Query(
            value =
                    "UPDATE product_group_images SET deleted_at = NOW()"
                            + " WHERE product_group_id = :productGroupId AND deleted_at IS NULL",
            nativeQuery = true)
    void softDeleteByProductGroupId(@Param("productGroupId") Long productGroupId);
}
