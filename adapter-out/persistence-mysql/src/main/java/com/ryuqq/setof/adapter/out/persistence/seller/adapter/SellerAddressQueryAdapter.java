package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerAddressJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerAddressQueryDslRepository;
import com.ryuqq.setof.application.seller.port.out.query.SellerAddressQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.id.SellerAddressId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * SellerAddressQueryAdapter - 셀러 주소 조회 어댑터.
 *
 * <p>SellerAddressQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 */
@Component
public class SellerAddressQueryAdapter implements SellerAddressQueryPort {

    private final SellerAddressQueryDslRepository queryDslRepository;
    private final SellerAddressJpaEntityMapper mapper;

    public SellerAddressQueryAdapter(
            SellerAddressQueryDslRepository queryDslRepository,
            SellerAddressJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * ID로 주소 조회.
     *
     * @param id 주소 ID
     * @return 주소 Optional
     */
    @Override
    public Optional<SellerAddress> findById(SellerAddressId id) {
        return queryDslRepository.findById(id.value()).map(mapper::toDomain);
    }

    /**
     * 셀러 ID로 주소 조회.
     *
     * @param sellerId 셀러 ID
     * @return 주소 Optional
     */
    @Override
    public Optional<SellerAddress> findBySellerId(SellerId sellerId) {
        return queryDslRepository.findBySellerId(sellerId.value()).map(mapper::toDomain);
    }

    /**
     * 셀러 ID 존재 여부 확인.
     *
     * @param sellerId 셀러 ID
     * @return 존재하면 true
     */
    @Override
    public boolean existsBySellerId(SellerId sellerId) {
        return queryDslRepository.existsBySellerId(sellerId.value());
    }
}
