package com.ryuqq.setof.storage.legacy.composite.payment.adapter;

import com.ryuqq.setof.application.payment.port.out.query.PaymentCompositeQueryPort;
import com.ryuqq.setof.domain.payment.query.PaymentSearchCriteria;
import com.ryuqq.setof.domain.payment.vo.PaymentOverview;
import com.ryuqq.setof.storage.legacy.composite.payment.dto.LegacyPaymentOverviewFlatDto;
import com.ryuqq.setof.storage.legacy.composite.payment.mapper.LegacyPaymentCompositeMapper;
import com.ryuqq.setof.storage.legacy.composite.payment.repository.LegacyPaymentCompositeQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyPaymentCompositeQueryAdapter - 결제 Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다. 커서 기반 ID 조회와 목록 개요 Composite 조회를 담당합니다.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyPaymentCompositeQueryAdapter implements PaymentCompositeQueryPort {

    private final LegacyPaymentCompositeQueryDslRepository repository;
    private final LegacyPaymentCompositeMapper mapper;

    public LegacyPaymentCompositeQueryAdapter(
            LegacyPaymentCompositeQueryDslRepository repository,
            LegacyPaymentCompositeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 검색 조건으로 결제 ID 목록 조회 (커서 기반 페이징).
     *
     * @param criteria 결제 검색 조건
     * @return 결제 ID 목록 (fetchSize = size + 1)
     */
    @Override
    public List<Long> fetchPaymentIds(PaymentSearchCriteria criteria) {
        return repository.fetchPaymentIds(criteria);
    }

    /**
     * 결제 ID 목록으로 결제 목록 개요 조회 (Composite JOIN).
     *
     * @param paymentIds 결제 ID 목록
     * @return 결제 개요 목록 (도메인 VO)
     */
    @Override
    public List<PaymentOverview> fetchPaymentOverviews(List<Long> paymentIds) {
        if (paymentIds.isEmpty()) {
            return List.of();
        }
        List<LegacyPaymentOverviewFlatDto> dtos = repository.fetchPaymentOverviews(paymentIds);
        return mapper.toOverviews(dtos);
    }
}
