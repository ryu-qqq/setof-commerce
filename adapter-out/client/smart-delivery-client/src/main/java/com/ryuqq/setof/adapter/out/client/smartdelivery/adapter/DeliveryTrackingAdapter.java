package com.ryuqq.setof.adapter.out.client.smartdelivery.adapter;

import com.ryuqq.setof.adapter.out.client.smartdelivery.client.SmartDeliveryClient;
import com.ryuqq.setof.adapter.out.client.smartdelivery.config.SmartDeliveryProperties;
import com.ryuqq.setof.adapter.out.client.smartdelivery.dto.SmartDeliveryTrackingResponse;
import com.ryuqq.setof.adapter.out.client.smartdelivery.dto.SmartDeliveryTrackingResponse.TrackingDetail;
import com.ryuqq.setof.application.shipment.dto.client.TrackingApiResult;
import com.ryuqq.setof.application.shipment.dto.client.TrackingApiResult.TrackingEvent;
import com.ryuqq.setof.application.shipment.port.out.client.DeliveryTrackingPort;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * DeliveryTrackingAdapter - 배송 추적 Adapter
 *
 * <p>스마트택배 API를 사용하여 배송 추적 정보를 조회합니다.
 *
 * <p><strong>Port-Out 구현체:</strong>
 *
 * <ul>
 *   <li>DeliveryTrackingPort 구현
 *   <li>SmartDeliveryClient를 통해 외부 API 호출
 *   <li>응답을 Application Layer DTO로 변환
 * </ul>
 *
 * <p><strong>변환 규칙:</strong>
 *
 * <ul>
 *   <li>스마트택배 level → 배송 상태 매핑
 *   <li>timeString → Instant 변환 (KST 기준)
 *   <li>실패 시 Optional.empty() 반환
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class DeliveryTrackingAdapter implements DeliveryTrackingPort {

    private static final Logger log = LoggerFactory.getLogger(DeliveryTrackingAdapter.class);

    /** 한국 시간대 */
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    /** 시간 포맷 1: yyyyMMddHHmmss */
    private static final DateTimeFormatter FORMAT_1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /** 시간 포맷 2: yyyy.MM.dd HH:mm:ss */
    private static final DateTimeFormatter FORMAT_2 =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    /** 시간 포맷 3: yyyy-MM-dd HH:mm:ss */
    private static final DateTimeFormatter FORMAT_3 =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final SmartDeliveryClient smartDeliveryClient;
    private final SmartDeliveryProperties properties;

    public DeliveryTrackingAdapter(
            SmartDeliveryClient smartDeliveryClient, SmartDeliveryProperties properties) {
        this.smartDeliveryClient = smartDeliveryClient;
        this.properties = properties;
    }

    @Override
    public Optional<TrackingApiResult> fetchTrackingInfo(String carrierCode, String invoiceNumber) {
        if (!properties.isEnabled()) {
            log.info(
                    "Smart Delivery is disabled. Skipping tracking. carrierCode={}, invoice={}",
                    carrierCode,
                    maskInvoice(invoiceNumber));
            return Optional.empty();
        }

        try {
            SmartDeliveryTrackingResponse response =
                    smartDeliveryClient.fetchTrackingInfo(carrierCode, invoiceNumber);

            if (!response.isSuccess()) {
                log.warn(
                        "Tracking info not found. carrierCode={}, invoice={}, msg={}",
                        carrierCode,
                        maskInvoice(invoiceNumber),
                        response.getMsg());
                return Optional.empty();
            }

            TrackingApiResult result = convertToResult(carrierCode, invoiceNumber, response);
            return Optional.of(result);

        } catch (Exception e) {
            log.error(
                    "Failed to fetch tracking info. carrierCode={}, invoice={}",
                    carrierCode,
                    maskInvoice(invoiceNumber),
                    e);
            return Optional.empty();
        }
    }

    /** 스마트택배 응답 → TrackingApiResult 변환 */
    private TrackingApiResult convertToResult(
            String carrierCode, String invoiceNumber, SmartDeliveryTrackingResponse response) {

        TrackingDetail lastDetail = response.getLastDetail();
        String lastLocation = lastDetail != null ? lastDetail.getWhere() : null;
        String lastMessage = lastDetail != null ? lastDetail.getKind() : null;
        Instant lastTrackedAt =
                lastDetail != null ? parseTimeString(lastDetail.getTimeString()) : null;

        boolean isDelivered = response.isDelivered();
        Instant deliveredAt = isDelivered ? lastTrackedAt : null;

        List<TrackingEvent> events = convertEvents(response.getTrackingDetails());

        String status = mapLevelToStatus(response.getLevel(), isDelivered);

        return new TrackingApiResult(
                carrierCode,
                invoiceNumber,
                status,
                lastLocation,
                lastMessage,
                lastTrackedAt,
                isDelivered,
                deliveredAt,
                events);
    }

    /** 추적 상세 목록 → TrackingEvent 목록 변환 */
    private List<TrackingEvent> convertEvents(List<TrackingDetail> details) {
        if (details == null || details.isEmpty()) {
            return List.of();
        }

        List<TrackingEvent> events = new ArrayList<>();
        for (TrackingDetail detail : details) {
            Instant time = parseTimeString(detail.getTimeString());
            TrackingEvent event =
                    new TrackingEvent(time, detail.getWhere(), detail.getCode(), detail.getKind());
            events.add(event);
        }
        return events;
    }

    /**
     * 스마트택배 level → 배송 상태 매핑
     *
     * <p>스마트택배 level:
     *
     * <ul>
     *   <li>1: 접수
     *   <li>2: 집화 처리
     *   <li>3: 간선 상차
     *   <li>4: 간선 하차
     *   <li>5: 배송 출발
     *   <li>6: 배송 완료
     * </ul>
     */
    private String mapLevelToStatus(Integer level, boolean isDelivered) {
        if (isDelivered) {
            return "DELIVERED";
        }
        if (level == null) {
            return "PENDING";
        }
        return switch (level) {
            case 1 -> "PENDING";
            case 2, 3 -> "IN_TRANSIT";
            case 4, 5 -> "OUT_FOR_DELIVERY";
            case 6 -> "DELIVERED";
            default -> "IN_TRANSIT";
        };
    }

    /**
     * 시간 문자열 → Instant 변환
     *
     * <p>스마트택배는 여러 시간 포맷을 사용합니다.
     */
    private Instant parseTimeString(String timeString) {
        if (timeString == null || timeString.isBlank()) {
            return null;
        }

        // 공백 제거
        String cleaned = timeString.trim();

        // 포맷 1 시도: yyyyMMddHHmmss
        try {
            LocalDateTime ldt = LocalDateTime.parse(cleaned, FORMAT_1);
            return ldt.atZone(KST).toInstant();
        } catch (DateTimeParseException ignored) {
            // 다음 포맷 시도
        }

        // 포맷 2 시도: yyyy.MM.dd HH:mm:ss
        try {
            LocalDateTime ldt = LocalDateTime.parse(cleaned, FORMAT_2);
            return ldt.atZone(KST).toInstant();
        } catch (DateTimeParseException ignored) {
            // 다음 포맷 시도
        }

        // 포맷 3 시도: yyyy-MM-dd HH:mm:ss
        try {
            LocalDateTime ldt = LocalDateTime.parse(cleaned, FORMAT_3);
            return ldt.atZone(KST).toInstant();
        } catch (DateTimeParseException e) {
            log.warn("Failed to parse timeString: {}", timeString);
            return null;
        }
    }

    private String maskInvoice(String invoiceNumber) {
        if (invoiceNumber == null || invoiceNumber.length() < 6) {
            return "****";
        }
        return invoiceNumber.substring(0, 4) + "****";
    }
}
