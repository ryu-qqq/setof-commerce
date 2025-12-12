package com.ryuqq.setof.application.shippingpolicy.manager.query;

import com.ryuqq.setof.application.shippingpolicy.port.out.query.ShippingPolicyQueryPort;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.exception.ShippingPolicyNotFoundException;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 배송 정책 Read Manager
 *
 * <p>읽기 전용 트랜잭션으로 Port-Out을 통해 조회
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
@Transactional(readOnly = true)
public class ShippingPolicyReadManager {

    private final ShippingPolicyQueryPort shippingPolicyQueryPort;

    public ShippingPolicyReadManager(ShippingPolicyQueryPort shippingPolicyQueryPort) {
        this.shippingPolicyQueryPort = shippingPolicyQueryPort;
    }

    /**
     * ID로 배송 정책 조회
     *
     * @param shippingPolicyId 배송 정책 ID
     * @return 배송 정책 도메인
     * @throws ShippingPolicyNotFoundException 배송 정책이 없는 경우
     */
    public ShippingPolicy findById(Long shippingPolicyId) {
        return shippingPolicyQueryPort
                .findById(ShippingPolicyId.of(shippingPolicyId))
                .orElseThrow(() -> new ShippingPolicyNotFoundException(shippingPolicyId));
    }

    /**
     * 셀러 ID로 배송 정책 목록 조회
     *
     * @param sellerId 셀러 ID
     * @param includeDeleted 삭제된 정책 포함 여부
     * @return 배송 정책 목록
     */
    public List<ShippingPolicy> findBySellerId(Long sellerId, boolean includeDeleted) {
        return shippingPolicyQueryPort.findBySellerId(sellerId, includeDeleted);
    }

    /**
     * 셀러의 기본 배송 정책 조회
     *
     * @param sellerId 셀러 ID
     * @return 기본 배송 정책 (Optional)
     */
    public ShippingPolicy findDefaultBySellerId(Long sellerId) {
        return shippingPolicyQueryPort.findDefaultBySellerId(sellerId).orElse(null);
    }

    /**
     * 셀러의 배송 정책 개수 조회
     *
     * @param sellerId 셀러 ID
     * @param includeDeleted 삭제된 정책 포함 여부
     * @return 배송 정책 개수
     */
    public long countBySellerId(Long sellerId, boolean includeDeleted) {
        return shippingPolicyQueryPort.countBySellerId(sellerId, includeDeleted);
    }
}
