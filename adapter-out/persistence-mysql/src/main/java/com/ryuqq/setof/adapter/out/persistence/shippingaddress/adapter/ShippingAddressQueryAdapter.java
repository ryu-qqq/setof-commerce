package com.ryuqq.setof.adapter.out.persistence.shippingaddress.adapter;

import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.ShippingAddressJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.mapper.ShippingAddressJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.repository.ShippingAddressQueryDslRepository;
import com.ryuqq.setof.application.shippingaddress.port.out.ShippingAddressQueryPort;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * ShippingAddressQueryAdapter - 배송지 Query 어댑터.
 *
 * <p>ShippingAddressQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * <p>활성화 조건: persistence.shippingaddress.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.shippingaddress.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class ShippingAddressQueryAdapter implements ShippingAddressQueryPort {

    private final ShippingAddressQueryDslRepository queryDslRepository;
    private final ShippingAddressJpaEntityMapper mapper;

    public ShippingAddressQueryAdapter(
            ShippingAddressQueryDslRepository queryDslRepository,
            ShippingAddressJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * 레거시 회원 ID + 배송지 ID로 배송지 조회.
     *
     * <p>Port의 userId 파라미터는 legacy_member_id에 매핑됩니다.
     *
     * @param userId 레거시 회원 ID
     * @param shippingAddressId 배송지 ID
     * @return 배송지 Optional
     */
    @Override
    public Optional<ShippingAddress> findById(Long userId, Long shippingAddressId) {
        return queryDslRepository.findById(userId, shippingAddressId).map(mapper::toDomain);
    }

    /**
     * 레거시 회원 ID로 배송지 목록 조회.
     *
     * <p>Port의 userId 파라미터는 legacy_member_id에 매핑됩니다.
     *
     * @param userId 레거시 회원 ID
     * @return 배송지 목록
     */
    @Override
    public List<ShippingAddress> findAllByUserId(Long userId) {
        List<ShippingAddressJpaEntity> entities =
                queryDslRepository.findAllByLegacyMemberId(userId);
        return entities.stream().map(mapper::toDomain).toList();
    }

    /**
     * 레거시 회원 ID로 배송지 개수 조회.
     *
     * <p>Port의 userId 파라미터는 legacy_member_id에 매핑됩니다.
     *
     * @param userId 레거시 회원 ID
     * @return 배송지 개수
     */
    @Override
    public int countByUserId(Long userId) {
        return queryDslRepository.countByLegacyMemberId(userId);
    }
}
