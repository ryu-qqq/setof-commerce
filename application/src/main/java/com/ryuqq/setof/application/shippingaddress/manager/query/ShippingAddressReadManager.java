package com.ryuqq.setof.application.shippingaddress.manager.query;

import com.ryuqq.setof.application.shippingaddress.port.out.query.ShippingAddressQueryPort;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressNotFoundException;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ShippingAddress Read Manager
 *
 * <p>단일 Query Port 조회를 담당하는 Read Manager
 *
 * <p>읽기 전용 트랜잭션 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingAddressReadManager {

    private final ShippingAddressQueryPort shippingAddressQueryPort;

    public ShippingAddressReadManager(ShippingAddressQueryPort shippingAddressQueryPort) {
        this.shippingAddressQueryPort = shippingAddressQueryPort;
    }

    /**
     * 배송지 ID로 조회 (필수)
     *
     * @param shippingAddressId 배송지 ID
     * @return 조회된 ShippingAddress
     * @throws ShippingAddressNotFoundException 배송지를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public ShippingAddress findById(Long shippingAddressId) {
        ShippingAddressId id = ShippingAddressId.of(shippingAddressId);
        return shippingAddressQueryPort
                .findById(id)
                .orElseThrow(() -> new ShippingAddressNotFoundException(shippingAddressId));
    }

    /**
     * 배송지 ID로 조회 (선택)
     *
     * @param shippingAddressId 배송지 ID
     * @return 조회된 ShippingAddress (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<ShippingAddress> findByIdOptional(Long shippingAddressId) {
        ShippingAddressId id = ShippingAddressId.of(shippingAddressId);
        return shippingAddressQueryPort.findById(id);
    }

    /**
     * 회원 ID로 배송지 목록 조회 (최근 등록순)
     *
     * @param memberId 회원 ID
     * @return 배송지 목록
     */
    @Transactional(readOnly = true)
    public List<ShippingAddress> findByMemberId(UUID memberId) {
        return shippingAddressQueryPort.findAllByMemberId(memberId);
    }

    /**
     * 회원의 기본 배송지 조회 (선택)
     *
     * @param memberId 회원 ID
     * @return 기본 배송지 (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<ShippingAddress> findDefaultByMemberId(UUID memberId) {
        return shippingAddressQueryPort.findDefaultByMemberId(memberId);
    }

    /**
     * 회원의 배송지 개수 조회
     *
     * @param memberId 회원 ID
     * @return 배송지 개수
     */
    @Transactional(readOnly = true)
    public long countByMemberId(UUID memberId) {
        return shippingAddressQueryPort.countByMemberId(memberId);
    }

    /**
     * 회원의 가장 최근 등록 배송지 조회 (기본 배송지 자동 설정용)
     *
     * @param memberId 회원 ID
     * @param excludeId 제외할 배송지 ID (삭제 중인 배송지)
     * @return 가장 최근 등록 배송지 (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<ShippingAddress> findLatestExcluding(UUID memberId, Long excludeId) {
        ShippingAddressId id = ShippingAddressId.of(excludeId);
        return shippingAddressQueryPort.findLatestByMemberIdExcluding(memberId, id);
    }
}
