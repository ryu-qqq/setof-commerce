package com.ryuqq.setof.adapter.out.persistence.productnotice.adapter;

import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.mapper.ProductNoticeJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productnotice.repository.ProductNoticeJpaRepository;
import com.ryuqq.setof.application.productnotice.port.out.command.ProductNoticeCommandPort;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import org.springframework.stereotype.Component;

/**
 * ProductNoticeCommandAdapter - 상품고시 Command 어댑터.
 *
 * <p>ProductNoticeCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductNoticeCommandAdapter implements ProductNoticeCommandPort {

    private final ProductNoticeJpaRepository jpaRepository;
    private final ProductNoticeJpaEntityMapper mapper;

    public ProductNoticeCommandAdapter(
            ProductNoticeJpaRepository jpaRepository, ProductNoticeJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 상품고시 저장.
     *
     * @param notice 상품고시 도메인 객체
     * @return 저장된 상품고시 ID
     */
    @Override
    public Long persist(ProductNotice notice) {
        ProductNoticeJpaEntity entity = mapper.toEntity(notice);
        ProductNoticeJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }
}
