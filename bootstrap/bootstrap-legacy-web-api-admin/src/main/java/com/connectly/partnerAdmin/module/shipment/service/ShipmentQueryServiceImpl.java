package com.connectly.partnerAdmin.module.shipment.service;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.coreServer.CoreServerClient;
import com.connectly.partnerAdmin.module.coreServer.request.order.ShipmentStartRequestDto;
import com.connectly.partnerAdmin.module.external.service.order.ExternalOrderFetchService;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.seller.core.BusinessSellerContext;
import com.connectly.partnerAdmin.module.shipment.dto.ShipmentInfo;
import com.connectly.partnerAdmin.module.shipment.entity.Shipment;
import com.connectly.partnerAdmin.module.shipment.enums.DeliveryStatus;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;
import com.connectly.partnerAdmin.module.shipment.repository.ShipmentRepository;

import static com.connectly.partnerAdmin.module.shipment.enums.ShipmentType.PARCEL_SERVICE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ShipmentQueryServiceImpl implements ShipmentQueryService{

    private final ShipmentFetchService shipmentFetchService;
    private final ShipmentRepository shipmentRepository;

    private final ExternalOrderFetchService externalOrderFetchService;
    private final CoreServerClient coreServerClient;
    private static final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();


    @Override
    public void saveShipment(long paymentSnapShotShippingId, Order order, BusinessSellerContext businessSellerContext) {

        Shipment shipment = Shipment.builder()
                .companyCode(ShipmentCompanyCode.REFER_DETAIL)
                .deliveryStatus(DeliveryStatus.DELIVERY_PENDING)
                .senderName(businessSellerContext.getSellerName())
                .senderEmail(businessSellerContext.getEmail())
                .senderPhoneNumber(businessSellerContext.getCsPhoneNumber())
                .paymentSnapShotShippingAddressId(paymentSnapShotShippingId)
                .order(order)
                .build();

        shipmentRepository.save(shipment);
    }

    @Override
    public ShipmentInfo deliveryStart(long orderId, ShipmentInfo shipmentInfo) {
        Shipment shipment = shipmentFetchService.fetchShipmentEntity(orderId);
        String invoice = shipmentInfo.getInvoiceNo().replaceAll("-", "");
        shipment.deliveryStart(invoice, shipmentInfo.getCompanyCode(), shipmentInfo.getShipmentType());
        sendRequest(orderId, shipmentInfo);
        return shipmentInfo;
    }


    public void sendRequest(long orderId, ShipmentInfo shipmentInfo) {
        String traceId = MDC.get("traceId");

        virtualThreadExecutor.submit(() -> {
            MDC.put("traceId", traceId);
            try {
                tempSendShipment(orderId, shipmentInfo);
            } catch (Exception e) {
                log.error("[배송 연동 실패] orderId={}, siteId={}, invoice={}",
                    orderId,
                    shipmentInfo.getCompanyCode(),
                    shipmentInfo.getInvoiceNo(), e);
            } finally {
                MDC.clear();
            }
        });
    }

    private void tempSendShipment(long orderId, ShipmentInfo shipmentInfo) {
        Optional.ofNullable(externalOrderFetchService.fetchByOrderId(orderId))
            .flatMap(o -> o)
            .ifPresent(e -> {
                if (e.getSiteId() == 9 && shipmentInfo.getShipmentType().equals(PARCEL_SERVICE)) {
                    String invoice = shipmentInfo.getInvoiceNo().replaceAll("-", "");
                    coreServerClient.updateShipmentStatus(
                        MDC.get("traceId"),
                        new ShipmentStartRequestDto(
                            e.getSiteId(),
                            e.getExternalIdx(),
                            Long.parseLong(e.getExternalOrderPkId()),
                            invoice,
                            shipmentInfo.getCompanyCode()
                        )
                    );
                }
            });
    }



}
