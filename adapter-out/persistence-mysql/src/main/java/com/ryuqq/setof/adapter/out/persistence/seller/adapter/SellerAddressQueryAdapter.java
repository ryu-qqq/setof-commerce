package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerAddressJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerAddressJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerAddressQueryDslRepository;
import com.ryuqq.setof.application.selleraddress.port.out.query.SellerAddressQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.id.SellerAddressId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.query.SellerAddressSearchCriteria;
import com.ryuqq.setof.domain.seller.vo.AddressType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * SellerAddressQueryAdapter - 셀러 주소 Query 어댑터.
 *
 * <p>SellerAddressQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
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

    @Override
    public Optional<SellerAddress> findById(SellerAddressId id) {
        return queryDslRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<SellerAddress> findAllBySellerId(SellerId sellerId) {
        List<SellerAddressJpaEntity> entities =
                queryDslRepository.findAllBySellerId(sellerId.value());
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<SellerAddress> findDefaultBySellerId(
            SellerId sellerId, AddressType addressType) {
        return queryDslRepository
                .findDefaultBySellerId(sellerId.value(), addressType)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsBySellerId(SellerId sellerId) {
        return queryDslRepository.existsBySellerId(sellerId.value());
    }

    @Override
    public boolean existsBySellerIdAndAddressTypeAndAddressName(
            SellerId sellerId, AddressType addressType, String addressName) {
        return queryDslRepository.existsBySellerIdAndAddressTypeAndAddressName(
                sellerId.value(), addressType, addressName);
    }

    @Override
    public List<SellerAddress> search(SellerAddressSearchCriteria criteria) {
        List<SellerAddressJpaEntity> entities = queryDslRepository.search(criteria);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public long count(SellerAddressSearchCriteria criteria) {
        return queryDslRepository.count(criteria);
    }
}
