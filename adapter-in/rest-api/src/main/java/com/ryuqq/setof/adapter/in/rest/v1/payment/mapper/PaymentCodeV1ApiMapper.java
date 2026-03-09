package com.ryuqq.setof.adapter.in.rest.v1.payment.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.BankV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.payment.dto.response.PayMethodV1ApiResponse;
import com.ryuqq.setof.application.commoncode.dto.response.CommonCodeResult;
import com.ryuqq.setof.application.payment.dto.response.PaymentMethodResult;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * PaymentCodeV1ApiMapper - 결제 코드 V1 API Response 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-003: Application Result → API Response 변환.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class PaymentCodeV1ApiMapper {

    /**
     * PaymentMethodResult → PayMethodV1ApiResponse 변환.
     *
     * @param result 결제 수단 결과
     * @return PayMethodV1ApiResponse
     */
    public PayMethodV1ApiResponse toPayMethodResponse(PaymentMethodResult result) {
        return new PayMethodV1ApiResponse(
                result.displayName(), result.code(), result.merchantKey());
    }

    /**
     * PaymentMethodResult 목록 → PayMethodV1ApiResponse 목록 변환.
     *
     * @param results 결제 수단 결과 목록
     * @return PayMethodV1ApiResponse 목록
     */
    public List<PayMethodV1ApiResponse> toPayMethodListResponse(List<PaymentMethodResult> results) {
        return results.stream().map(this::toPayMethodResponse).toList();
    }

    /**
     * CommonCodeResult → BankV1ApiResponse 변환.
     *
     * <p>code → bankCode, displayName → bankName으로 매핑.
     *
     * @param result 공통 코드 결과
     * @return BankV1ApiResponse
     */
    public BankV1ApiResponse toBankResponse(CommonCodeResult result) {
        return new BankV1ApiResponse(result.code(), result.displayName());
    }

    /**
     * CommonCodeResult 목록 → BankV1ApiResponse 목록 변환.
     *
     * @param results 공통 코드 결과 목록
     * @return BankV1ApiResponse 목록
     */
    public List<BankV1ApiResponse> toBankListResponse(List<CommonCodeResult> results) {
        return results.stream().map(this::toBankResponse).toList();
    }
}
