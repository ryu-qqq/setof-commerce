package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerBusinessInfoJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerBusinessInfoJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerBusinessInfoJpaRepository;
import com.ryuqq.setof.application.seller.port.out.command.SellerBusinessInfoCommandPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * SellerBusinessInfoCommandAdapter - 셀러 사업자 정보 Command 어댑터.
 *
 * <p>SellerBusinessInfoCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerBusinessInfoCommandAdapter implements SellerBusinessInfoCommandPort {

    private final SellerBusinessInfoJpaRepository jpaRepository;
    private final SellerBusinessInfoJpaEntityMapper mapper;

    public SellerBusinessInfoCommandAdapter(
            SellerBusinessInfoJpaRepository jpaRepository,
            SellerBusinessInfoJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 셀러 사업자 정보 저장.
     *
     * @param businessInfo 사업자 정보 도메인 객체
     * @return 저장된 사업자 정보 도메인 객체
     */
    @Override
    public SellerBusinessInfo persist(SellerBusinessInfo businessInfo) {
        SellerBusinessInfoJpaEntity entity = mapper.toEntity(businessInfo);
        SellerBusinessInfoJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    /**
     * 셀러 사업자 정보 목록 저장.
     *
     * @param businessInfos 사업자 정보 도메인 객체 목록
     * @return 저장된 사업자 정보 도메인 객체 목록
     */
    @Override
    public List<SellerBusinessInfo> persistAll(List<SellerBusinessInfo> businessInfos) {
        List<SellerBusinessInfoJpaEntity> entities =
                businessInfos.stream().map(mapper::toEntity).toList();
        List<SellerBusinessInfoJpaEntity> saved = jpaRepository.saveAll(entities);
        return saved.stream().map(mapper::toDomain).toList();
    }
}
