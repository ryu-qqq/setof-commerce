package com.ryuqq.setof.adapter.out.client.smartdelivery.client;

import com.ryuqq.setof.adapter.out.client.smartdelivery.config.SmartDeliveryPaths;
import com.ryuqq.setof.adapter.out.client.smartdelivery.config.SmartDeliveryProperties;
import com.ryuqq.setof.adapter.out.client.smartdelivery.dto.SmartDeliveryTrackingResponse;
import com.ryuqq.setof.adapter.out.client.smartdelivery.support.SmartDeliveryApiExecutor;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * SmartDeliveryClient - 스마트택배 API 클라이언트
 *
 * <p>스마트택배(SweetTracker) API와 통신하는 클라이언트입니다.
 *
 * <p><strong>주요 기능:</strong>
 *
 * <ul>
 *   <li>운송장 추적 정보 조회
 *   <li>자동 재시도 및 서킷 브레이커 (ApiExecutor)
 *   <li>실패 시 빈 응답 반환 (예외 미발생)
 * </ul>
 *
 * <p><strong>API 문서:</strong>
 *
 * <p>https://info.sweettracker.co.kr/apiguide
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SmartDeliveryClient {

    private static final Logger log = LoggerFactory.getLogger(SmartDeliveryClient.class);

    private final RestClient restClient;
    private final SmartDeliveryProperties properties;
    private final SmartDeliveryApiExecutor apiExecutor;

    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification =
                    "RestClient, SmartDeliveryProperties, and SmartDeliveryApiExecutor"
                            + " are Spring-managed beans, immutable after injection")
    public SmartDeliveryClient(
            RestClient smartDeliveryRestClient,
            SmartDeliveryProperties properties,
            SmartDeliveryApiExecutor apiExecutor) {
        this.restClient = smartDeliveryRestClient;
        this.properties = properties;
        this.apiExecutor = apiExecutor;
    }

    /**
     * 운송장 추적 정보 조회
     *
     * <p>스마트택배 API를 통해 운송장 추적 정보를 조회합니다.
     *
     * <p><strong>특징:</strong>
     *
     * <ul>
     *   <li>실패 시 빈 응답 반환 (예외 미발생)
     *   <li>자동 재시도 및 서킷브레이커 적용
     * </ul>
     *
     * @param carrierCode 택배사 코드 (스마트택배 t_code)
     * @param invoiceNumber 운송장 번호
     * @return 추적 정보, 실패 시 빈 응답
     */
    public SmartDeliveryTrackingResponse fetchTrackingInfo(
            String carrierCode, String invoiceNumber) {
        if (!properties.isEnabled()) {
            log.warn("Smart Delivery is disabled. Returning empty response.");
            return createEmptyResponse();
        }

        return apiExecutor.executeWithDefault(
                "fetchTrackingInfo",
                () -> callTrackingApi(carrierCode, invoiceNumber),
                this::createEmptyResponse);
    }

    private SmartDeliveryTrackingResponse callTrackingApi(
            String carrierCode, String invoiceNumber) {
        log.debug(
                "Fetching tracking info. carrierCode={}, invoiceNumber={}",
                carrierCode,
                maskInvoiceNumber(invoiceNumber));

        SmartDeliveryTrackingResponse response =
                restClient
                        .get()
                        .uri(
                                uriBuilder ->
                                        uriBuilder
                                                .path(SmartDeliveryPaths.TRACKING_INFO)
                                                .queryParam("t_key", properties.getApiKey())
                                                .queryParam("t_code", carrierCode)
                                                .queryParam("t_invoice", invoiceNumber)
                                                .build())
                        .retrieve()
                        .body(SmartDeliveryTrackingResponse.class);

        if (response == null) {
            log.warn(
                    "Empty response from Smart Delivery API. carrierCode={}, invoiceNumber={}",
                    carrierCode,
                    maskInvoiceNumber(invoiceNumber));
            return createEmptyResponse();
        }

        if (!response.isSuccess()) {
            log.warn(
                    "Smart Delivery API returned failure. carrierCode={}, invoiceNumber={}, msg={}",
                    carrierCode,
                    maskInvoiceNumber(invoiceNumber),
                    response.getMsg());
            return response;
        }

        log.debug(
                "Successfully fetched tracking info. carrierCode={}, invoiceNumber={}, complete={}",
                carrierCode,
                maskInvoiceNumber(invoiceNumber),
                response.getComplete());

        return response;
    }

    private SmartDeliveryTrackingResponse createEmptyResponse() {
        SmartDeliveryTrackingResponse response = new SmartDeliveryTrackingResponse();
        response.setResult("N");
        response.setMsg("Disabled or failed");
        return response;
    }

    private String maskInvoiceNumber(String invoiceNumber) {
        if (invoiceNumber == null || invoiceNumber.length() < 6) {
            return "****";
        }
        return invoiceNumber.substring(0, 4) + "****";
    }
}
