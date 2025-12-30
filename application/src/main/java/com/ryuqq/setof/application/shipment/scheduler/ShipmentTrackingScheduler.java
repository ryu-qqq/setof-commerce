package com.ryuqq.setof.application.shipment.scheduler;

import com.ryuqq.setof.application.carrier.port.in.query.GetCarrierUseCase;
import com.ryuqq.setof.application.shipment.dto.client.TrackingApiResult;
import com.ryuqq.setof.application.shipment.dto.command.UpdateTrackingCommand;
import com.ryuqq.setof.application.shipment.port.in.command.MarkDeliveredUseCase;
import com.ryuqq.setof.application.shipment.port.in.command.UpdateTrackingUseCase;
import com.ryuqq.setof.application.shipment.port.in.query.GetActiveShipmentsUseCase;
import com.ryuqq.setof.application.shipment.port.out.client.DeliveryTrackingPort;
import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import com.ryuqq.setof.domain.shipment.aggregate.Shipment;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Shipment Tracking Scheduler
 *
 * <p>배송 추적 정보를 주기적으로 업데이트하는 스케줄러입니다.
 *
 * <p>스케줄러 동작:
 *
 * <ul>
 *   <li>1시간 간격으로 실행
 *   <li>활성 상태(PENDING, IN_TRANSIT, OUT_FOR_DELIVERY)의 운송장 조회
 *   <li>스마트택배 API를 통해 추적 정보 조회
 *   <li>추적 정보 업데이트 및 배송 완료 처리
 * </ul>
 *
 * <p>주의사항:
 *
 * <ul>
 *   <li>반품 배송은 별도 처리 (이 스케줄러에서 제외)
 *   <li>API 호출 실패 시 해당 운송장은 스킵 (다음 주기에 재시도)
 *   <li>배송 완료 감지 시 자동으로 상태 변경
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShipmentTrackingScheduler {

    private static final Logger log = LoggerFactory.getLogger(ShipmentTrackingScheduler.class);

    private final GetActiveShipmentsUseCase getActiveShipmentsUseCase;
    private final GetCarrierUseCase getCarrierUseCase;
    private final DeliveryTrackingPort deliveryTrackingPort;
    private final UpdateTrackingUseCase updateTrackingUseCase;
    private final MarkDeliveredUseCase markDeliveredUseCase;

    public ShipmentTrackingScheduler(
            GetActiveShipmentsUseCase getActiveShipmentsUseCase,
            GetCarrierUseCase getCarrierUseCase,
            DeliveryTrackingPort deliveryTrackingPort,
            UpdateTrackingUseCase updateTrackingUseCase,
            MarkDeliveredUseCase markDeliveredUseCase) {
        this.getActiveShipmentsUseCase = getActiveShipmentsUseCase;
        this.getCarrierUseCase = getCarrierUseCase;
        this.deliveryTrackingPort = deliveryTrackingPort;
        this.updateTrackingUseCase = updateTrackingUseCase;
        this.markDeliveredUseCase = markDeliveredUseCase;
    }

    /**
     * 1시간 간격으로 배송 추적 정보 업데이트
     *
     * <p>cron: 매 시간 정각에 실행
     */
    @Scheduled(cron = "0 0 * * * *")
    public void updateTrackingInfo() {
        log.info("[Scheduler] Starting shipment tracking update job");

        List<Shipment> activeShipments = getActiveShipmentsUseCase.execute();
        log.info("[Scheduler] Found {} active shipments to track", activeShipments.size());

        int successCount = 0;
        int failCount = 0;
        int deliveredCount = 0;

        for (Shipment shipment : activeShipments) {
            try {
                boolean updated = processShipment(shipment);
                if (updated) {
                    successCount++;
                    if (shipment.isDelivered()) {
                        deliveredCount++;
                    }
                }
            } catch (Exception e) {
                failCount++;
                log.error(
                        "[Scheduler] Failed to update tracking for shipmentId={}",
                        shipment.getIdValue(),
                        e);
            }
        }

        log.info(
                "[Scheduler] Completed. success={}, failed={}, delivered={}",
                successCount,
                failCount,
                deliveredCount);
    }

    /**
     * 개별 운송장 처리
     *
     * @param shipment 운송장
     * @return 업데이트 여부
     */
    private boolean processShipment(Shipment shipment) {
        Long shipmentId = shipment.getIdValue();
        Long carrierId = shipment.getCarrierId();
        String invoiceNumber = shipment.getInvoiceNumberValue();

        // 택배사 정보 조회
        Carrier carrier = getCarrierUseCase.execute(carrierId);
        String carrierCode = carrier.getCodeValue();

        // 스마트택배 API 호출
        Optional<TrackingApiResult> resultOpt =
                deliveryTrackingPort.fetchTrackingInfo(carrierCode, invoiceNumber);

        if (resultOpt.isEmpty()) {
            log.debug(
                    "[Scheduler] No tracking info for shipmentId={}, carrier={}, invoice={}",
                    shipmentId,
                    carrierCode,
                    maskInvoice(invoiceNumber));
            return false;
        }

        TrackingApiResult result = resultOpt.get();

        // 배송 완료 처리
        if (result.isDelivered() && !shipment.isDelivered()) {
            markDeliveredUseCase.execute(shipmentId);
            log.info(
                    "[Scheduler] Shipment delivered. shipmentId={}, invoice={}",
                    shipmentId,
                    maskInvoice(invoiceNumber));
            return true;
        }

        // 추적 정보 업데이트
        if (result.hasTrackingData()) {
            UpdateTrackingCommand command =
                    new UpdateTrackingCommand(
                            result.lastLocation(),
                            result.lastMessage(),
                            result.lastTrackedAt(),
                            result.status());

            updateTrackingUseCase.execute(shipmentId, command);
            log.debug(
                    "[Scheduler] Updated tracking. shipmentId={}, location={}",
                    shipmentId,
                    result.lastLocation());
            return true;
        }

        return false;
    }

    private String maskInvoice(String invoiceNumber) {
        if (invoiceNumber == null || invoiceNumber.length() < 6) {
            return "****";
        }
        return invoiceNumber.substring(0, 4) + "****";
    }
}
