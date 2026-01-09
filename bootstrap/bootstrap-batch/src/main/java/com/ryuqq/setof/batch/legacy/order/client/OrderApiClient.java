package com.ryuqq.setof.batch.legacy.order.client;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * 주문 처리 API 클라이언트
 *
 * <p>취소 요청, 결제 실패 등 Spring 서버 API 호출이 필요한 작업 수행
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OrderApiClient {

    private static final Logger log = LoggerFactory.getLogger(OrderApiClient.class);

    private final RestClient restClient;
    private final String baseUrl;

    public OrderApiClient(
            RestClient.Builder restClientBuilder, @Value("${order.api.url:}") String baseUrl) {
        this.restClient = restClientBuilder.build();
        this.baseUrl = baseUrl;
    }

    /**
     * 주문 취소 처리 API 호출
     *
     * @param orderIds 처리할 주문 ID 목록
     * @return 성공 여부
     */
    public boolean processCancelOrders(List<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return true;
        }

        try {
            String url = baseUrl + "/api/v1/order/cancel/batch";

            Map<String, Object> request = Map.of("orderIds", orderIds, "source", "BATCH");

            restClient.post().uri(url).body(request).retrieve().toBodilessEntity();

            log.info("Successfully processed {} cancel orders", orderIds.size());
            return true;

        } catch (Exception e) {
            log.error("Failed to process cancel orders: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 결제 실패 주문 처리 API 호출
     *
     * @param orderIds 처리할 주문 ID 목록
     * @return 성공 여부
     */
    public boolean processPaymentFailOrders(List<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return true;
        }

        try {
            String url = baseUrl + "/api/v1/order/payment-fail/batch";

            Map<String, Object> request = Map.of("orderIds", orderIds, "source", "BATCH");

            restClient.post().uri(url).body(request).retrieve().toBodilessEntity();

            log.info("Successfully processed {} payment fail orders", orderIds.size());
            return true;

        } catch (Exception e) {
            log.error("Failed to process payment fail orders: {}", e.getMessage());
            return false;
        }
    }
}
