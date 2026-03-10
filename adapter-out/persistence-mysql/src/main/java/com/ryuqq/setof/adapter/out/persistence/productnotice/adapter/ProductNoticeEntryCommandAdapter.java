package com.ryuqq.setof.adapter.out.persistence.productnotice.adapter;

import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeEntryJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.mapper.ProductNoticeJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productnotice.repository.ProductNoticeEntryJpaRepository;
import com.ryuqq.setof.application.productnotice.port.out.command.ProductNoticeEntryCommandPort;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNoticeEntry;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductNoticeEntryCommandAdapter - 상품고시 항목 Command 어댑터.
 *
 * <p>ProductNoticeEntryCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductNoticeEntryCommandAdapter implements ProductNoticeEntryCommandPort {

    private final ProductNoticeEntryJpaRepository jpaRepository;
    private final ProductNoticeJpaEntityMapper mapper;

    public ProductNoticeEntryCommandAdapter(
            ProductNoticeEntryJpaRepository jpaRepository, ProductNoticeJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 상품고시 항목 목록 저장.
     *
     * @param entries 상품고시 항목 도메인 객체 목록
     */
    @Override
    public void persistAll(List<ProductNoticeEntry> entries) {
        List<ProductNoticeEntryJpaEntity> entities =
                entries.stream().map(mapper::toEntryEntity).toList();
        jpaRepository.saveAll(entities);
    }

    /**
     * 상품고시 ID로 고시 항목 전체 삭제.
     *
     * @param productNoticeId 상품고시 ID
     */
    @Override
    public void deleteByProductNoticeId(Long productNoticeId) {
        jpaRepository.deleteByProductNoticeId(productNoticeId);
    }
}
