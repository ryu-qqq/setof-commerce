package com.ryuqq.setof.batch.legacy.shipment.client;

import com.ryuqq.setof.batch.legacy.shipment.dto.ShipmentInfo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * 택배 배송 조회 API 클라이언트
 *
 * <p>외부 택배 조회 API를 호출하여 배송 상태를 확인합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShipmentTrackerClient {

    private static final Logger log = LoggerFactory.getLogger(ShipmentTrackerClient.class);
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestClient restClient;
    private final String baseUrl;
    private final String apiKey;

    public ShipmentTrackerClient(
            RestClient.Builder restClientBuilder,
            @Value("${shipment.tracker.url:}") String baseUrl,
            @Value("${shipment.tracker.api-key:}") String apiKey) {
        this.restClient = restClientBuilder.build();
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    /**
     * 배송 상태 조회
     *
     * @param shipmentInfo 배송 정보
     * @return 배송완료 시간 (완료가 아니면 empty)
     */
    public Optional<LocalDateTime> trackShipment(ShipmentInfo shipmentInfo) {
        try {
            String companyCode = normalizeCompanyCode(shipmentInfo.companyCode());
            String invoiceNo = shipmentInfo.invoiceNo().replace("-", "");
            String tCode = getTrackerCode(companyCode);

            // baseUrl이 ?로 끝나면 그대로 사용, 아니면 ? 추가
            String separator = baseUrl.endsWith("?") ? "" : "?";
            String url =
                    String.format(
                            "%s%st_code=%s&t_invoice=%s&t_key=%s",
                            baseUrl, separator, tCode, invoiceNo, apiKey);

            log.debug("Tracking API URL: t_code={}, invoice={}", tCode, invoiceNo);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.get().uri(url).retrieve().body(Map.class);

            if (response == null) {
                return Optional.empty();
            }

            String completeYN = (String) response.get("completeYN");
            if ("Y".equals(completeYN)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> lastDetail = (Map<String, Object>) response.get("lastDetail");
                if (lastDetail != null) {
                    String timeString = (String) lastDetail.get("timeString");
                    if (timeString != null) {
                        return Optional.of(parseDateTime(timeString));
                    }
                }
                return Optional.of(LocalDateTime.now());
            }

            return Optional.empty();

        } catch (Exception e) {
            log.error(
                    "Failed to track shipment: orderId={}, invoiceNo={}, error={}",
                    shipmentInfo.orderId(),
                    shipmentInfo.invoiceNo(),
                    e.getMessage());
            return Optional.empty();
        }
    }

    private String normalizeCompanyCode(String companyCode) {
        if ("SHIP146".equals(companyCode)) {
            return "SHIP08";
        }
        return companyCode;
    }

    private String getTrackerCode(String companyCode) {
        // 택배사 코드 매핑 (SweetTracker API 코드)
        // Python Airflow의 ShipmentCompanyCode enum 기준으로 정확한 매핑
        return switch (companyCode) {
            case "SHIP01" -> "01"; // 우체국택배
            case "SHIP04" -> "04"; // CJ대한통운
            case "SHIP05" -> "05"; // 한진택배
            case "SHIP06" -> "06"; // 로젠택배
            case "SHIP08" -> "08"; // 롯데택배
            case "SHIP11" -> "11"; // 일양로지스
            case "SHIP16" -> "16"; // 한의사랑택배
            case "SHIP17" -> "17"; // 천일택배
            case "SHIP18" -> "18"; // 건영택배
            case "SHIP20" -> "20"; // 한덱스
            case "SHIP22" -> "22"; // 대신택배
            case "SHIP23" -> "23"; // 경동택배
            case "SHIP24" -> "24"; // GS Postbox 택배
            case "SHIP32" -> "32"; // 합동택배
            case "SHIP40" -> "40"; // 굿투럭
            case "SHIP43" -> "43"; // 애니트랙
            case "SHIP44" -> "44"; // SLX택배
            case "SHIP45" -> "45"; // 우리택배(구호남택배)
            case "SHIP46" -> "46"; // CU 편의점택배
            case "SHIP47" -> "47"; // 우리한방택배
            case "SHIP53" -> "53"; // 농협택배
            case "SHIP54" -> "54"; // 홈픽택배
            case "SHIP71" -> "71"; // IK물류
            case "SHIP72" -> "72"; // 성훈물류
            case "SHIP74" -> "74"; // 용마로지스
            case "SHIP75" -> "75"; // 원더스퀵
            case "SHIP79" -> "79"; // 로지스밸리택배
            case "SHIP82" -> "82"; // 컬리로지스
            case "SHIP85" -> "85"; // 풀앳홈
            case "SHIP86" -> "86"; // 삼성전자물류
            case "SHIP88" -> "88"; // 큐런택배
            case "SHIP89" -> "89"; // 두발히어로
            case "SHIP90" -> "90"; // 위니아딤채
            case "SHIP92" -> "92"; // 지니고 당일배송
            case "SHIP94" -> "94"; // 오늘의픽업
            case "SHIP96" -> "96"; // 로지스밸리
            case "SHIP101" -> "101"; // 한샘서비스원 택배
            case "SHIP103" -> "103"; // NDEX KOREA
            case "SHIP104" -> "104"; // 도도플렉스(dodoflex)
            case "SHIP107" -> "107"; // LG전자(판토스)
            case "SHIP110" -> "110"; // 부릉
            case "SHIP112" -> "112"; // 1004홈
            case "SHIP113" -> "113"; // 썬더히어로
            case "SHIP116" -> "116"; // (주)팀프레시
            case "SHIP118" -> "118"; // 롯데칠성
            case "SHIP119" -> "119"; // 핑퐁
            case "SHIP120" -> "120"; // 발렉스 특수물류
            case "SHIP123" -> "123"; // 엔티엘피스
            case "SHIP125" -> "125"; // GTS로지스
            case "SHIP127" -> "127"; // 로지스팟
            case "SHIP129" -> "129"; // 홈픽 오늘도착
            case "SHIP130" -> "130"; // UFO로지스
            case "SHIP131" -> "131"; // 딜리래빗
            case "SHIP132" -> "132"; // 지오피
            case "SHIP134" -> "134"; // 에이치케이홀딩스
            case "SHIP135" -> "135"; // HTNS
            case "SHIP136" -> "136"; // 케이제이티
            case "SHIP137" -> "137"; // 더바오
            case "SHIP138" -> "138"; // 라스트마일
            case "SHIP139" -> "139"; // 오늘회 러쉬
            case "SHIP142" -> "142"; // 탱고앤고
            case "SHIP143" -> "143"; // 투데이
            case "SHIP144" -> "144"; // 발렉스 특수물류
            case "SHIP146" -> "146"; // 롯데택배(구 현대택배)
            default -> {
                log.warn("Unknown company code: {}, returning as-is", companyCode);
                yield companyCode;
            }
        };
    }

    private LocalDateTime parseDateTime(String timeString) {
        try {
            return LocalDateTime.parse(timeString, DATE_FORMATTER);
        } catch (Exception e) {
            log.warn("Failed to parse datetime: {}", timeString);
            return LocalDateTime.now();
        }
    }
}
