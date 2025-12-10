package com.ryuqq.setof.application.shippingaddress.port.out.query;

import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ShippingAddress Query Port (Query)
 *
 * <p>ShippingAddress Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ShippingAddressQueryPort {

    /**
     * ID로 배송지 단건 조회 (삭제되지 않은 것만)
     *
     * @param id 배송지 ID
     * @return ShippingAddress Domain (Optional)
     */
    Optional<ShippingAddress> findById(ShippingAddressId id);

    /**
     * ID로 배송지 단건 조회 (삭제된 것 포함)
     *
     * @param id 배송지 ID
     * @return ShippingAddress Domain (Optional)
     */
    Optional<ShippingAddress> findByIdIncludeDeleted(ShippingAddressId id);

    /**
     * 회원의 모든 배송지 목록 조회 (삭제되지 않은 것만)
     *
     * <p>생성일시 내림차순 정렬 (최근 등록순)
     *
     * @param memberId 회원 ID
     * @return 배송지 목록
     */
    List<ShippingAddress> findAllByMemberId(UUID memberId);

    /**
     * 회원의 기본 배송지 조회
     *
     * @param memberId 회원 ID
     * @return 기본 배송지 (Optional)
     */
    Optional<ShippingAddress> findDefaultByMemberId(UUID memberId);

    /**
     * 회원의 배송지 개수 조회 (삭제되지 않은 것만)
     *
     * @param memberId 회원 ID
     * @return 배송지 개수
     */
    long countByMemberId(UUID memberId);

    /**
     * 회원의 가장 최근 배송지 조회 (기본 배송지 제외)
     *
     * <p>기본 배송지 삭제 시 새로운 기본 배송지 선정에 사용
     *
     * @param memberId 회원 ID
     * @param excludeId 제외할 배송지 ID (삭제되는 기본 배송지)
     * @return 가장 최근 배송지 (Optional)
     */
    Optional<ShippingAddress> findLatestByMemberIdExcluding(
            UUID memberId, ShippingAddressId excludeId);
}
