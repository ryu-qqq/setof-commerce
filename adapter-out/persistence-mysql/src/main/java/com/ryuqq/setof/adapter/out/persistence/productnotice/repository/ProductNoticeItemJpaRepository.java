package com.ryuqq.setof.adapter.out.persistence.productnotice.repository;

import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeItemJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductNoticeItemJpaRepository - ProductNoticeItem JPA Repository
 *
 * <p>Spring Data JPA 기본 CRUD 인터페이스
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ProductNoticeItemJpaRepository
        extends JpaRepository<ProductNoticeItemJpaEntity, Long> {

    /**
     * 상품고시 ID로 항목 목록 조회
     *
     * @param productNoticeId 상품고시 ID
     * @return 항목 Entity 목록
     */
    List<ProductNoticeItemJpaEntity> findByProductNoticeIdOrderByDisplayOrderAsc(
            Long productNoticeId);

    /**
     * 상품고시 ID로 항목 삭제
     *
     * @param productNoticeId 상품고시 ID
     */
    void deleteByProductNoticeId(Long productNoticeId);
}
