package com.ryuqq.setof.application.seller.port.out.query;

import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerPolicyCompositeResult;
import java.util.Optional;

/**
 * SellerCompositionQueryPort - 셀러 Composition 조회 Port.
 *
 * <p>크로스 도메인 조인을 통한 성능 최적화된 조회를 제공합니다.
 */
public interface SellerCompositionQueryPort {

    /**
     * Customer용 셀러 Composite 조회 (Seller + Address + BusinessInfo + CS).
     *
     * @param sellerId 셀러 ID
     * @return 셀러 Composite 결과
     */
    Optional<SellerCompositeResult> findSellerCompositeById(Long sellerId);

    Optional<SellerPolicyCompositeResult> findPolicyCompositeById(Long sellerId);

    /**
     * 사업자등록번호 존재 여부 확인.
     *
     * @param registrationNumber 사업자등록번호
     * @return 존재하면 true
     */
    boolean existsByRegistrationNumber(String registrationNumber);
}
