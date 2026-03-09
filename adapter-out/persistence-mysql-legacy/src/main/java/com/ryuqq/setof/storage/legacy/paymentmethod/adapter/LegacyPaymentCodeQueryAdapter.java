package com.ryuqq.setof.storage.legacy.paymentmethod.adapter;

import com.ryuqq.setof.application.payment.dto.response.PaymentMethodResult;
import com.ryuqq.setof.application.payment.port.out.query.PaymentCodeQueryPort;
import com.ryuqq.setof.domain.commoncode.aggregate.CommonCode;
import com.ryuqq.setof.storage.legacy.commoncode.mapper.LegacyCommonCodeMapper;
import com.ryuqq.setof.storage.legacy.commoncode.repository.LegacyCommonCodeQueryDslRepository;
import com.ryuqq.setof.storage.legacy.paymentmethod.mapper.LegacyPaymentMethodMapper;
import com.ryuqq.setof.storage.legacy.paymentmethod.repository.LegacyPaymentMethodQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyPaymentCodeQueryAdapter - 레거시 결제 코드 조회 Adapter.
 *
 * <p>PaymentCodeQueryPort를 구현하여 레거시 DB에서 결제 관련 코드를 조회합니다.
 *
 * <p>은행 코드: common_code 테이블에서 조회 후 CommonCode 도메인으로 변환.
 *
 * <p>결제 수단: payment_method + common_code JOIN 후 PaymentMethodResult로 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyPaymentCodeQueryAdapter implements PaymentCodeQueryPort {

    private static final long VBANK_CODE_GROUP_ID = 10L;
    private static final long REFUND_BANK_CODE_GROUP_ID = 12L;

    private final LegacyPaymentMethodQueryDslRepository paymentMethodRepository;
    private final LegacyPaymentMethodMapper paymentMethodMapper;
    private final LegacyCommonCodeQueryDslRepository commonCodeRepository;
    private final LegacyCommonCodeMapper commonCodeMapper;

    public LegacyPaymentCodeQueryAdapter(
            LegacyPaymentMethodQueryDslRepository paymentMethodRepository,
            LegacyPaymentMethodMapper paymentMethodMapper,
            LegacyCommonCodeQueryDslRepository commonCodeRepository,
            LegacyCommonCodeMapper commonCodeMapper) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
        this.commonCodeRepository = commonCodeRepository;
        this.commonCodeMapper = commonCodeMapper;
    }

    @Override
    public List<PaymentMethodResult> findActivePaymentMethods() {
        return paymentMethodMapper.toResults(paymentMethodRepository.findActivePaymentMethods());
    }

    @Override
    public List<CommonCode> findVBankCodes() {
        return commonCodeMapper.toDomains(
                commonCodeRepository.findByCodeGroupId(VBANK_CODE_GROUP_ID));
    }

    @Override
    public List<CommonCode> findRefundBankCodes() {
        return commonCodeMapper.toDomains(
                commonCodeRepository.findByCodeGroupId(REFUND_BANK_CODE_GROUP_ID));
    }
}
