package com.ryuqq.setof.application.payment.service.query;

import com.ryuqq.setof.application.commoncode.assembler.CommonCodeAssembler;
import com.ryuqq.setof.application.commoncode.dto.response.CommonCodeResult;
import com.ryuqq.setof.application.payment.port.in.query.GetRefundBankCodesUseCase;
import com.ryuqq.setof.application.payment.port.out.query.PaymentCodeQueryPort;
import com.ryuqq.setof.domain.commoncode.aggregate.CommonCode;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetRefundBankCodesService - 환불 계좌용 은행 코드 목록 조회 서비스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetRefundBankCodesService implements GetRefundBankCodesUseCase {

    private final PaymentCodeQueryPort paymentCodeQueryPort;
    private final CommonCodeAssembler commonCodeAssembler;

    public GetRefundBankCodesService(
            PaymentCodeQueryPort paymentCodeQueryPort, CommonCodeAssembler commonCodeAssembler) {
        this.paymentCodeQueryPort = paymentCodeQueryPort;
        this.commonCodeAssembler = commonCodeAssembler;
    }

    @Override
    public List<CommonCodeResult> execute() {
        List<CommonCode> codes = paymentCodeQueryPort.findRefundBankCodes();
        return commonCodeAssembler.toResults(codes);
    }
}
