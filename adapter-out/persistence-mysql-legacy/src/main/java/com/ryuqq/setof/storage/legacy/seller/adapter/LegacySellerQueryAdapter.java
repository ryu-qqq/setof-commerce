package com.ryuqq.setof.storage.legacy.seller.adapter;

import com.ryuqq.setof.application.seller.port.out.query.SellerQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import com.ryuqq.setof.storage.legacy.seller.entity.LegacySellerEntity;
import com.ryuqq.setof.storage.legacy.seller.mapper.LegacySellerEntityMapper;
import com.ryuqq.setof.storage.legacy.seller.repository.LegacySellerQueryDslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacySellerQueryAdapter - 레거시 셀러 Query 어댑터.
 *
 * <p>SellerQueryPort를 구현하여 레거시 DB와 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>활성화 조건: persistence.legacy.seller.enabled=true
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.seller.enabled", havingValue = "true")
public class LegacySellerQueryAdapter implements SellerQueryPort {

    private final LegacySellerQueryDslRepository queryDslRepository;
    private final LegacySellerEntityMapper mapper;

    public LegacySellerQueryAdapter(
            LegacySellerQueryDslRepository queryDslRepository, LegacySellerEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Seller> findById(SellerId id) {
        return queryDslRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<Seller> findByIds(List<SellerId> ids) {
        List<Long> idValues = ids.stream().map(SellerId::value).toList();
        List<LegacySellerEntity> entities = queryDslRepository.findByIds(idValues);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsById(SellerId id) {
        return queryDslRepository.existsById(id.value());
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
        List<LegacySellerEntity> entities = queryDslRepository.findByCriteria(criteria);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countByCriteria(SellerSearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }
}
