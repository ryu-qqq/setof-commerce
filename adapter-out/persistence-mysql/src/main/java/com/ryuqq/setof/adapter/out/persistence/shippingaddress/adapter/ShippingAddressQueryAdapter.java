package com.ryuqq.setof.adapter.out.persistence.shippingaddress.adapter;

import com.ryuqq.setof.adapter.out.persistence.shippingaddress.mapper.ShippingAddressJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.repository.ShippingAddressQueryDslRepository;
import com.ryuqq.setof.application.shippingaddress.port.out.query.ShippingAddressQueryPort;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * ShippingAddressQueryAdapter - ShippingAddress Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, ShippingAddress 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해
 * Domain으로 변환하여 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>ID로 단건 조회 - 삭제 포함 (findByIdIncludeDeleted)
 *   <li>회원별 배송지 목록 조회 (findAllByMemberId)
 *   <li>회원의 기본 배송지 조회 (findDefaultByMemberId)
 *   <li>회원별 배송지 개수 조회 (countByMemberId)
 *   <li>특정 ID 제외 최신 배송지 조회 (findLatestByMemberIdExcluding)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingAddressQueryAdapter implements ShippingAddressQueryPort {

    private final ShippingAddressQueryDslRepository queryDslRepository;
    private final ShippingAddressJpaEntityMapper shippingAddressJpaEntityMapper;

    public ShippingAddressQueryAdapter(
            ShippingAddressQueryDslRepository queryDslRepository,
            ShippingAddressJpaEntityMapper shippingAddressJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.shippingAddressJpaEntityMapper = shippingAddressJpaEntityMapper;
    }

    /**
     * ID로 ShippingAddress 단건 조회 (삭제되지 않은 것만)
     *
     * @param id ShippingAddress ID (Value Object)
     * @return ShippingAddress Domain (Optional)
     */
    @Override
    public Optional<ShippingAddress> findById(ShippingAddressId id) {
        return queryDslRepository
                .findById(id.value())
                .map(shippingAddressJpaEntityMapper::toDomain);
    }

    /**
     * ID로 ShippingAddress 단건 조회 (삭제된 것 포함)
     *
     * @param id ShippingAddress ID (Value Object)
     * @return ShippingAddress Domain (Optional)
     */
    @Override
    public Optional<ShippingAddress> findByIdIncludeDeleted(ShippingAddressId id) {
        return queryDslRepository
                .findByIdIncludeDeleted(id.value())
                .map(shippingAddressJpaEntityMapper::toDomain);
    }

    /**
     * 회원별 배송지 목록 조회 (최신순)
     *
     * @param memberId 회원 ID (UUID)
     * @return ShippingAddress 목록
     */
    @Override
    public List<ShippingAddress> findAllByMemberId(UUID memberId) {
        return queryDslRepository.findByMemberId(memberId.toString()).stream()
                .map(shippingAddressJpaEntityMapper::toDomain)
                .toList();
    }

    /**
     * 회원의 기본 배송지 조회
     *
     * @param memberId 회원 ID (UUID)
     * @return ShippingAddress (Optional)
     */
    @Override
    public Optional<ShippingAddress> findDefaultByMemberId(UUID memberId) {
        return queryDslRepository
                .findDefaultByMemberId(memberId.toString())
                .map(shippingAddressJpaEntityMapper::toDomain);
    }

    /**
     * 회원별 배송지 개수 조회
     *
     * @param memberId 회원 ID (UUID)
     * @return 배송지 개수
     */
    @Override
    public long countByMemberId(UUID memberId) {
        return queryDslRepository.countByMemberId(memberId.toString());
    }

    /**
     * 특정 ID 제외 최신 배송지 조회 (기본 배송지 재지정용)
     *
     * @param memberId 회원 ID (UUID)
     * @param excludeId 제외할 배송지 ID
     * @return ShippingAddress (Optional)
     */
    @Override
    public Optional<ShippingAddress> findLatestByMemberIdExcluding(UUID memberId, ShippingAddressId excludeId) {
        return queryDslRepository
                .findLatestExcluding(memberId.toString(), excludeId.value())
                .map(shippingAddressJpaEntityMapper::toDomain);
    }
}
