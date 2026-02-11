package com.ryuqq.setof.application.seller.manager;

import com.ryuqq.setof.application.seller.dto.composite.SellerAdminCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerPolicyCompositeResult;
import com.ryuqq.setof.application.seller.port.out.query.SellerCompositionQueryPort;
import com.ryuqq.setof.domain.seller.exception.SellerNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * SellerCompositionReadManager - 셀러 Composition 조회 매니저.
 *
 * <p>크로스 도메인 조인을 통한 성능 최적화된 조회를 담당합니다.
 */
@Component
public class SellerCompositionReadManager {

    private final SellerCompositionQueryPort compositionQueryPort;

    public SellerCompositionReadManager(SellerCompositionQueryPort compositionQueryPort) {
        this.compositionQueryPort = compositionQueryPort;
    }

    /**
     * Customer용 셀러 Composite 조회 (Seller + Address + BusinessInfo + CS).
     *
     * @param sellerId 셀러 ID
     * @return 셀러 Composite 결과
     */
    @Transactional(readOnly = true)
    public SellerCompositeResult getSellerComposite(Long sellerId) {
        return compositionQueryPort
                .findSellerCompositeById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException(sellerId));
    }

    /**
     * Admin용 셀러 Composite 조회 (Seller + Address + BusinessInfo + CS + Contract + Settlement).
     *
     * @param sellerId 셀러 ID
     * @return Admin용 셀러 Composite 결과
     */
    @Transactional(readOnly = true)
    public SellerAdminCompositeResult getAdminComposite(Long sellerId) {
        return compositionQueryPort
                .findAdminCompositeById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException(sellerId));
    }

    @Transactional(readOnly = true)
    public SellerPolicyCompositeResult getPolicyComposite(Long sellerId) {
        return compositionQueryPort
                .findPolicyCompositeById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException(sellerId));
    }

    /**
     * 사업자등록번호 존재 여부 확인.
     *
     * @param registrationNumber 사업자등록번호
     * @return 존재하면 true
     */
    @Transactional(readOnly = true)
    public boolean existsByRegistrationNumber(String registrationNumber) {
        return compositionQueryPort.existsByRegistrationNumber(registrationNumber);
    }
}
