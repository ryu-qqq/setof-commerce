package com.ryuqq.setof.storage.legacy.composite.payment.adapter;

import com.ryuqq.setof.application.legacy.payment.dto.response.LegacyPaymentResult;
import com.ryuqq.setof.domain.legacy.payment.dto.query.LegacyPaymentSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.payment.dto.LegacyWebPaymentQueryDto;
import com.ryuqq.setof.storage.legacy.composite.payment.mapper.LegacyWebPaymentMapper;
import com.ryuqq.setof.storage.legacy.composite.payment.repository.LegacyWebPaymentCompositeQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebPaymentCompositeQueryAdapter - 레거시 웹 결제 Composite 조회 Adapter.
 *
 * <p>TODO: Application Layer의 LegacyPaymentCompositeQueryPort implements 추가
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebPaymentCompositeQueryAdapter {

    private final LegacyWebPaymentCompositeQueryDslRepository repository;
    private final LegacyWebPaymentMapper mapper;

    public LegacyWebPaymentCompositeQueryAdapter(
            LegacyWebPaymentCompositeQueryDslRepository repository, LegacyWebPaymentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 결제 목록 조회 (커서 기반 페이징).
     *
     * @param condition 검색 조건
     * @param limit 조회 개수
     * @return 결제 목록
     */
    public List<LegacyPaymentResult> fetchPayments(
            LegacyPaymentSearchCondition condition, int limit) {
        // 1단계: 결제 ID 조회
        List<Long> paymentIds = repository.fetchPaymentIds(condition, limit);
        if (paymentIds.isEmpty()) {
            return List.of();
        }
        // 2단계: 결제 상세 조회
        List<LegacyWebPaymentQueryDto> dtos = repository.fetchPayments(paymentIds);
        return mapper.toResults(dtos);
    }

    /**
     * 결제 단건 조회.
     *
     * @param paymentId 결제 ID
     * @param userId 사용자 ID
     * @return 결제 Optional
     */
    public LegacyPaymentResult fetchPayment(long paymentId, long userId) {
        LegacyWebPaymentQueryDto dto = repository.fetchPaymentById(paymentId, userId);
        return mapper.toResult(dto);
    }

    /**
     * 결제 개수 조회.
     *
     * @param condition 검색 조건
     * @return 결제 개수
     */
    public long countPayments(LegacyPaymentSearchCondition condition) {
        return repository.countPayments(condition);
    }
}
