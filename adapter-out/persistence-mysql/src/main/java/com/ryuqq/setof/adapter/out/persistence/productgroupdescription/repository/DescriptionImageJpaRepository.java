package com.ryuqq.setof.adapter.out.persistence.productgroupdescription.repository;

import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.DescriptionImageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DescriptionImageJpaRepository - 상세설명 이미지 JPA 레포지토리.
 *
 * <p>PER-REP-001: JPA 레포지토리는 Command 전용.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface DescriptionImageJpaRepository
        extends JpaRepository<DescriptionImageJpaEntity, Long> {

    /**
     * 상세설명 ID 기준으로 이미지 삭제.
     *
     * @param productGroupDescriptionId 상세설명 ID
     */
    void deleteByProductGroupDescriptionId(Long productGroupDescriptionId);
}
