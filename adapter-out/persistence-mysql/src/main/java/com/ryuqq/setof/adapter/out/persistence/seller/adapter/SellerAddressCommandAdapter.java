package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerAddressJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerAddressJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerAddressJpaRepository;
import com.ryuqq.setof.application.seller.port.out.command.SellerAddressCommandPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * SellerAddressCommandAdapter - 셀러 주소 명령 어댑터.
 *
 * <p>SellerAddressCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 */
@Component
public class SellerAddressCommandAdapter implements SellerAddressCommandPort {

    private final SellerAddressJpaRepository repository;
    private final SellerAddressJpaEntityMapper mapper;

    public SellerAddressCommandAdapter(
            SellerAddressJpaRepository repository, SellerAddressJpaEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * SellerAddress 영속화 (생성/수정).
     *
     * @param address 영속화할 SellerAddress
     * @return 영속화된 SellerAddress ID
     */
    @Override
    public Long persist(SellerAddress address) {
        SellerAddressJpaEntity entity = mapper.toEntity(address);
        SellerAddressJpaEntity saved = repository.save(entity);
        return saved.getId();
    }

    /**
     * SellerAddress 목록 일괄 영속화.
     *
     * @param addresses 영속화할 SellerAddress 목록
     */
    @Override
    public void persistAll(List<SellerAddress> addresses) {
        List<SellerAddressJpaEntity> entities = addresses.stream().map(mapper::toEntity).toList();
        repository.saveAll(entities);
    }
}
