package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerCsJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerCsQueryDslRepository;
import com.ryuqq.setof.application.seller.port.out.query.SellerCsQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * SellerCsQueryAdapter - 셀러 CS 정보 Query 어댑터.
 *
 * <p>SellerCsQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerCsQueryAdapter implements SellerCsQueryPort {

    private final SellerCsQueryDslRepository queryDslRepository;
    private final SellerCsJpaEntityMapper mapper;

    public SellerCsQueryAdapter(
            SellerCsQueryDslRepository queryDslRepository, SellerCsJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<SellerCs> findById(Long id) {
        return queryDslRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<SellerCs> findBySellerId(SellerId sellerId) {
        return queryDslRepository.findBySellerId(sellerId.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsBySellerId(SellerId sellerId) {
        return queryDslRepository.existsBySellerId(sellerId.value());
    }
}
