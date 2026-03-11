package com.ryuqq.setof.adapter.out.persistence.composite.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.composite.seller.mapper.SellerCompositeMapper;
import com.ryuqq.setof.adapter.out.persistence.composite.seller.repository.SellerCompositeQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.composite.seller.repository.SellerPolicyCompositeQueryDslRepository;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerPolicyCompositeResult;
import com.ryuqq.setof.application.seller.port.out.query.SellerCompositionQueryPort;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * SellerCompositionQueryAdapter - 셀러 Composition 조회 Adapter.
 *
 * <p>크로스 도메인 조인을 통한 성능 최적화된 조회 구현.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Port 인터페이스 구현.
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.seller.enabled", havingValue = "false")
public class SellerCompositionQueryAdapter implements SellerCompositionQueryPort {

    private final SellerCompositeQueryDslRepository compositeRepository;
    private final SellerPolicyCompositeQueryDslRepository policyCompositeRepository;
    private final SellerCompositeMapper compositeMapper;

    public SellerCompositionQueryAdapter(
            SellerCompositeQueryDslRepository compositeRepository,
            SellerPolicyCompositeQueryDslRepository policyCompositeRepository,
            SellerCompositeMapper compositeMapper) {
        this.compositeRepository = compositeRepository;
        this.policyCompositeRepository = policyCompositeRepository;
        this.compositeMapper = compositeMapper;
    }

    @Override
    public Optional<SellerCompositeResult> findSellerCompositeById(Long sellerId) {
        return compositeRepository.findBySellerId(sellerId).map(compositeMapper::toResult);
    }

    @Override
    public Optional<SellerPolicyCompositeResult> findPolicyCompositeById(Long sellerId) {
        return policyCompositeRepository
                .findBySellerId(sellerId)
                .map(compositeMapper::toPolicyResult);
    }

    @Override
    public boolean existsByRegistrationNumber(String registrationNumber) {
        return compositeRepository.existsByRegistrationNumber(registrationNumber);
    }
}
