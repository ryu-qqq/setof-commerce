package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerCsJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerCsJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerCsJpaRepository;
import com.ryuqq.setof.application.seller.port.out.command.SellerCsCommandPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * SellerCsCommandAdapter - 셀러 CS 정보 Command 어댑터.
 *
 * <p>SellerCsCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerCsCommandAdapter implements SellerCsCommandPort {

    private final SellerCsJpaRepository jpaRepository;
    private final SellerCsJpaEntityMapper mapper;

    public SellerCsCommandAdapter(
            SellerCsJpaRepository jpaRepository, SellerCsJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 셀러 CS 정보 저장.
     *
     * @param sellerCs CS 정보 도메인 객체
     * @return 저장된 CS 정보 도메인 객체
     */
    @Override
    public SellerCs persist(SellerCs sellerCs) {
        SellerCsJpaEntity entity = mapper.toEntity(sellerCs);
        SellerCsJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    /**
     * 셀러 CS 정보 목록 저장.
     *
     * @param sellerCsList CS 정보 도메인 객체 목록
     * @return 저장된 CS 정보 도메인 객체 목록
     */
    @Override
    public List<SellerCs> persistAll(List<SellerCs> sellerCsList) {
        List<SellerCsJpaEntity> entities = sellerCsList.stream().map(mapper::toEntity).toList();
        List<SellerCsJpaEntity> saved = jpaRepository.saveAll(entities);
        return saved.stream().map(mapper::toDomain).toList();
    }
}
