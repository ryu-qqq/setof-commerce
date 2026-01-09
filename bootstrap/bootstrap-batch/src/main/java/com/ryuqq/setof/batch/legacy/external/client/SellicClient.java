package com.ryuqq.setof.batch.legacy.external.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.batch.legacy.external.dto.SellicOrder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/** SELLIC API 클라이언트 */
@Component
public class SellicClient {

    private static final Logger log = LoggerFactory.getLogger(SellicClient.class);
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${sellic.api.url:http://api.sellic.co.kr}")
    private String baseUrl;

    @Value("${sellic.api.customer-id:1012}")
    private String customerId;

    @Value("${sellic.api.api-key:}")
    private String apiKey;

    @Value("${sellic.api.fetch-days:20}")
    private int fetchDays;

    public SellicClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /** SELLIC에서 주문 목록 조회 */
    public List<SellicOrder> fetchOrders() {
        String url = baseUrl + "/openapi/get_order";

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(fetchDays).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = now.withHour(23).withMinute(59).withSecond(59);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("customer_id", customerId);
        requestBody.put("api_key", apiKey);
        requestBody.put("s_date", startDate.format(DATE_FORMATTER));
        requestBody.put("e_date", endDate.format(DATE_FORMATTER));

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            log.debug("SELLIC API request: url={}, body={}", url, jsonBody);

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info(
                        "SELLIC API response: {}",
                        response.getBody().length() > 500
                                ? response.getBody().substring(0, 500) + "..."
                                : response.getBody());
                return parseOrderResponse(response.getBody());
            }

            log.warn("SELLIC API returned non-success status: {}", response.getStatusCode());
            return Collections.emptyList();

        } catch (Exception e) {
            log.error("Failed to fetch orders from SELLIC: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<SellicOrder> parseOrderResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);

            // SELLIC API 응답 구조: {"result":"success", "datas":[...]}
            String result = root.has("result") ? root.get("result").asText() : "";
            if (!"success".equals(result)) {
                String message =
                        root.has("message") ? root.get("message").asText() : "Unknown error";
                log.warn("SELLIC API returned error: {}", message);
                return Collections.emptyList();
            }

            JsonNode dataNode = root.has("datas") ? root.get("datas") : root;

            if (dataNode.isArray()) {
                List<SellicOrder> orders =
                        objectMapper.convertValue(
                                dataNode, new TypeReference<List<SellicOrder>>() {});
                log.info("Parsed {} orders from SELLIC", orders.size());
                return orders;
            }

            log.warn("Unexpected SELLIC response format - datas is not an array");
            return Collections.emptyList();

        } catch (Exception e) {
            log.error("Failed to parse SELLIC response: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
