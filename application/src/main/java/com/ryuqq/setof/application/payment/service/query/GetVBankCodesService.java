package com.ryuqq.setof.application.payment.service.query;

import com.ryuqq.setof.application.commoncode.assembler.CommonCodeAssembler;
import com.ryuqq.setof.application.commoncode.dto.response.CommonCodeResult;
import com.ryuqq.setof.application.payment.port.in.query.GetVBankCodesUseCase;
import com.ryuqq.setof.application.payment.port.out.query.PaymentCodeQueryPort;
import com.ryuqq.setof.domain.commoncode.aggregate.CommonCode;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GetVBankCodesService - 가상계좌 환불용 은행 코드 목록 조회 서비스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetVBankCodesService implements GetVBankCodesUseCase {

    private final PaymentCodeQueryPort paymentCodeQueryPort;
    private final CommonCodeAssembler commonCodeAssembler;

    public GetVBankCodesService(
            PaymentCodeQueryPort paymentCodeQueryPort, CommonCodeAssembler commonCodeAssembler) {
        this.paymentCodeQueryPort = paymentCodeQueryPort;
        this.commonCodeAssembler = commonCodeAssembler;
    }

    @Override
    public List<CommonCodeResult> execute() {
        List<CommonCode> codes = paymentCodeQueryPort.findVBankCodes();
        return commonCodeAssembler.toResults(codes);
    }
}
