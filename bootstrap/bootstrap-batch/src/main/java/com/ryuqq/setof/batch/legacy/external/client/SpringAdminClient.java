package com.ryuqq.setof.batch.legacy.external.client;

import com.ryuqq.setof.batch.legacy.external.dto.SellicOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Spring Admin API 클라이언트
 *
 * <p>legacy-web-api-admin의 /external/order 엔드포인트를 호출합니다.
 */
@Component
public class SpringAdminClient {

    private static final Logger log = LoggerFactory.getLogger(SpringAdminClient.class);

    private final RestTemplate restTemplate;

    @Value("${admin-server.url:http://localhost:8089}")
    private String adminUrl;

    @Value("${admin-server.api-key:}")
    private String apiKey;

    @Value("${admin-server.dry-run:false}")
    private boolean dryRun;

    public SpringAdminClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /** 주문 데이터를 Spring Admin API로 전송 */
    public void sendOrder(SellicOrder order) {
        String url = adminUrl + "/api/v1/external/order";

        if (dryRun) {
            log.info(
                    "[DRY-RUN] Would send order to Admin API: idx={}, orderId={}",
                    order.getIdx(),
                    order.getOrderId());
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (apiKey != null && !apiKey.isEmpty()) {
                headers.set("API-KEY", apiKey);
            }

            String jsonBody = order.toAdminApiJson();
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.debug("Order sent successfully: idx={}", order.getIdx());
            } else {
                log.warn(
                        "Admin API returned non-success: idx={}, status={}",
                        order.getIdx(),
                        response.getStatusCode());
            }

        } catch (RestClientException e) {
            log.error(
                    "Failed to send order to Admin API: idx={}, error={}",
                    order.getIdx(),
                    e.getMessage());
            throw e;
        }
    }
}
