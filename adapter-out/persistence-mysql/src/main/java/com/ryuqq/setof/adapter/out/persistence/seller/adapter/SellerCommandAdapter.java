package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerJpaRepository;
import com.ryuqq.setof.application.seller.port.out.command.SellerCommandPort;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * SellerCommandAdapter - 셀러 Command 어댑터.
 *
 * <p>SellerCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerCommandAdapter implements SellerCommandPort {

    private final SellerJpaRepository jpaRepository;
    private final SellerJpaEntityMapper mapper;

    public SellerCommandAdapter(SellerJpaRepository jpaRepository, SellerJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 셀러 저장.
     *
     * @param seller 셀러 도메인 객체
     * @return 저장된 셀러 도메인 객체
     */
    @Override
    public Seller persist(Seller seller) {
        SellerJpaEntity entity = mapper.toEntity(seller);
        SellerJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    /**
     * 셀러 목록 저장.
     *
     * @param sellers 셀러 도메인 객체 목록
     * @return 저장된 셀러 도메인 객체 목록
     */
    @Override
    public List<Seller> persistAll(List<Seller> sellers) {
        List<SellerJpaEntity> entities = sellers.stream().map(mapper::toEntity).toList();
        List<SellerJpaEntity> saved = jpaRepository.saveAll(entities);
        return saved.stream().map(mapper::toDomain).toList();
    }
}
