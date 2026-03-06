package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerBusinessInfoJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerBusinessInfoQueryDslRepository;
import com.ryuqq.setof.application.seller.port.out.query.SellerBusinessInfoQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * SellerBusinessInfoQueryAdapter - 셀러 사업자 정보 Query 어댑터.
 *
 * <p>SellerBusinessInfoQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerBusinessInfoQueryAdapter implements SellerBusinessInfoQueryPort {

    private final SellerBusinessInfoQueryDslRepository queryDslRepository;
    private final SellerBusinessInfoJpaEntityMapper mapper;

    public SellerBusinessInfoQueryAdapter(
            SellerBusinessInfoQueryDslRepository queryDslRepository,
            SellerBusinessInfoJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<SellerBusinessInfo> findById(Long id) {
        return queryDslRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<SellerBusinessInfo> findBySellerId(SellerId sellerId) {
        return queryDslRepository.findBySellerId(sellerId.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsBySellerId(SellerId sellerId) {
        return queryDslRepository.existsBySellerId(sellerId.value());
    }

    @Override
    public boolean existsByRegistrationNumber(String registrationNumber) {
        return queryDslRepository.existsByRegistrationNumber(registrationNumber);
    }

    @Override
    public boolean existsByRegistrationNumberExcluding(
            String registrationNumber, SellerId excludeId) {
        return queryDslRepository.existsByRegistrationNumberExcluding(
                registrationNumber, excludeId.value());
    }
}
