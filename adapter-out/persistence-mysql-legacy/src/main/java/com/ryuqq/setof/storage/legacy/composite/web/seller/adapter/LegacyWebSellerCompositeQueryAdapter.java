package com.ryuqq.setof.storage.legacy.composite.web.seller.adapter;

import com.ryuqq.setof.application.seller.dto.composite.SellerAdminCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.application.seller.dto.composite.SellerPolicyCompositeResult;
import com.ryuqq.setof.application.seller.port.out.query.SellerCompositionQueryPort;
import com.ryuqq.setof.storage.legacy.composite.web.seller.mapper.LegacyWebSellerMapper;
import com.ryuqq.setof.storage.legacy.composite.web.seller.repository.LegacyWebSellerCompositeQueryDslRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * LegacyWebSellerCompositeQueryAdapter - 레거시 Web 판매자 Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>SellerCompositionQueryPort를 구현하여 Application Layer에 조회 기능을 제공합니다.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebSellerCompositeQueryAdapter implements SellerCompositionQueryPort {

    private final LegacyWebSellerCompositeQueryDslRepository repository;
    private final LegacyWebSellerMapper mapper;

    public LegacyWebSellerCompositeQueryAdapter(
            LegacyWebSellerCompositeQueryDslRepository repository, LegacyWebSellerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<SellerCompositeResult> findSellerCompositeById(Long sellerId) {
        return mapper.toCompositeResult(repository.fetchSeller(sellerId));
    }

    @Override
    public Optional<SellerAdminCompositeResult> findAdminCompositeById(Long sellerId) {
        return mapper.toAdminCompositeResult(repository.fetchSeller(sellerId));
    }

    /** 레거시 DB에는 배송/환불 정책 테이블이 없으므로 빈 Optional을 반환합니다. */
    @Override
    public Optional<SellerPolicyCompositeResult> findPolicyCompositeById(Long sellerId) {
        return Optional.empty();
    }

    @Override
    public boolean existsByRegistrationNumber(String registrationNumber) {
        return repository.existsByRegistrationNumber(registrationNumber);
    }
}
