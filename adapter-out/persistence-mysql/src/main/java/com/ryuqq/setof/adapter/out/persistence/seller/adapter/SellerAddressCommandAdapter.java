package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerAddressJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerAddressJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerAddressJpaRepository;
import com.ryuqq.setof.application.selleraddress.port.out.command.SellerAddressCommandPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * SellerAddressCommandAdapter - 셀러 주소 Command 어댑터.
 *
 * <p>SellerAddressCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerAddressCommandAdapter implements SellerAddressCommandPort {

    private final SellerAddressJpaRepository jpaRepository;
    private final SellerAddressJpaEntityMapper mapper;

    public SellerAddressCommandAdapter(
            SellerAddressJpaRepository jpaRepository, SellerAddressJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 셀러 주소 저장.
     *
     * @param sellerAddress 주소 도메인 객체
     * @return 저장된 주소 ID
     */
    @Override
    public Long persist(SellerAddress sellerAddress) {
        SellerAddressJpaEntity entity = mapper.toEntity(sellerAddress);
        SellerAddressJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }

    /**
     * 셀러 주소 목록 저장.
     *
     * @param sellerAddresses 주소 도메인 객체 목록
     */
    @Override
    public void persistAll(List<SellerAddress> sellerAddresses) {
        List<SellerAddressJpaEntity> entities =
                sellerAddresses.stream().map(mapper::toEntity).toList();
        jpaRepository.saveAll(entities);
    }
}
