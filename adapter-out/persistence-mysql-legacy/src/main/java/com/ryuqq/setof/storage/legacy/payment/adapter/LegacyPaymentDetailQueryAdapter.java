package com.ryuqq.setof.storage.legacy.payment.adapter;

import com.ryuqq.setof.application.payment.port.out.query.PaymentDetailQueryPort;
import com.ryuqq.setof.domain.payment.vo.PaymentFullDetail;
import com.ryuqq.setof.storage.legacy.payment.dto.LegacyPaymentDetailFlatDto;
import com.ryuqq.setof.storage.legacy.payment.mapper.LegacyPaymentDetailMapper;
import com.ryuqq.setof.storage.legacy.payment.repository.LegacyPaymentDetailQueryDslRepository;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * LegacyPaymentDetailQueryAdapter - 결제 단건 상세 조회 Adapter.
 *
 * <p>2번의 쿼리로 결제 상세를 조회합니다: (1) 결제 상세 1행, (2) 주문 ID 목록.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyPaymentDetailQueryAdapter implements PaymentDetailQueryPort {

    private final LegacyPaymentDetailQueryDslRepository repository;
    private final LegacyPaymentDetailMapper mapper;

    public LegacyPaymentDetailQueryAdapter(
            LegacyPaymentDetailQueryDslRepository repository, LegacyPaymentDetailMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 결제 ID와 사용자 ID로 결제 전체 상세 조회.
     *
     * <p>Step 1: 결제 상세 정보 1행 조회 (payment + bill + method + vbank + user + address +
     * refund_account). Step 2: 해당 결제에 포함된 주문 ID 목록 별도 조회.
     *
     * @param paymentId 결제 ID
     * @param userId 사용자 ID (본인 확인용)
     * @return 결제 전체 상세 (도메인 VO)
     */
    @Override
    public PaymentFullDetail fetchPaymentDetail(long paymentId, long userId) {
        LegacyPaymentDetailFlatDto dto = repository.fetchPaymentDetail(paymentId, userId);
        if (dto == null) {
            return null;
        }

        List<Long> orderIdList = repository.fetchOrderIdsByPaymentId(paymentId);
        Set<Long> orderIds = new LinkedHashSet<>(orderIdList);

        return mapper.toFullDetail(dto, orderIds);
    }
}
