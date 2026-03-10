package com.ryuqq.setof.adapter.in.scheduler.shipment;

import com.ryuqq.setof.adapter.in.scheduler.annotation.SchedulerJob;
import com.ryuqq.setof.adapter.in.scheduler.config.SchedulerProperties;
import com.ryuqq.setof.application.shipment.port.in.command.TrackShipmentDeliveryUseCase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 배송 추적 스케줄러.
 *
 * <p>주기적으로 배송중 주문의 배송 상태를 추적하여 완료 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "scheduler.jobs.shipment-delivery.track.enabled",
        havingValue = "true")
public class ShipmentDeliveryTrackingScheduler {

    private final TrackShipmentDeliveryUseCase trackShipmentDeliveryUseCase;
    private final SchedulerProperties properties;

    public ShipmentDeliveryTrackingScheduler(
            TrackShipmentDeliveryUseCase trackShipmentDeliveryUseCase,
            SchedulerProperties properties) {
        this.trackShipmentDeliveryUseCase = trackShipmentDeliveryUseCase;
        this.properties = properties;
    }

    @Scheduled(
            cron = "${scheduler.jobs.shipment-delivery.track.cron}",
            zone = "${scheduler.jobs.shipment-delivery.track.timezone}")
    @SchedulerJob("ShipmentDeliveryTracking")
    public int track() {
        int batchSize = properties.jobs().shipmentDelivery().track().batchSize();
        return trackShipmentDeliveryUseCase.execute(batchSize);
    }
}
