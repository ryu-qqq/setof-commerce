package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerQueryDslRepository;
import com.ryuqq.setof.application.seller.port.out.query.SellerQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * SellerQueryAdapter - 셀러 Query 어댑터.
 *
 * <p>SellerQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerQueryAdapter implements SellerQueryPort {

    private final SellerQueryDslRepository queryDslRepository;
    private final SellerJpaEntityMapper mapper;

    public SellerQueryAdapter(
            SellerQueryDslRepository queryDslRepository, SellerJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Seller> findById(Long id) {
        return queryDslRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Seller> findByIds(List<Long> ids) {
        List<SellerJpaEntity> entities = queryDslRepository.findByIds(ids);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return queryDslRepository.existsById(id);
    }

    @Override
    public boolean existsBySellerName(String sellerName) {
        return queryDslRepository.existsBySellerName(sellerName);
    }

    @Override
    public boolean existsBySellerNameExcluding(String sellerName, SellerId excludeId) {
        return queryDslRepository.existsBySellerNameExcluding(sellerName, excludeId.value());
    }

    @Override
    public List<Seller> findByCriteria(SellerSearchCriteria criteria) {
        List<SellerJpaEntity> entities = queryDslRepository.findByCriteria(criteria);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countByCriteria(SellerSearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }
}
