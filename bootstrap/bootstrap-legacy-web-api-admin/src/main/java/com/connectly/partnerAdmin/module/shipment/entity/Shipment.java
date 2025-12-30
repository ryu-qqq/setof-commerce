package com.connectly.partnerAdmin.module.shipment.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.shipment.enums.DeliveryStatus;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentCompanyCode;
import com.connectly.partnerAdmin.module.shipment.enums.ShipmentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "SHIPMENT")
public class Shipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHIPMENT_ID")
    private long id;

    @Column(name = "SHIPMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private ShipmentType shipmentType;

    @Column(name = "DELIVERY_STATUS")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "SENDER_NAME")
    private String senderName;

    @Column(name = "SENDER_EMAIL")
    private String senderEmail;

    @Column(name = "SENDER_PHONE_NUMBER")
    private String senderPhoneNumber;

    @Column(name = "PAYMENT_SNAPSHOT_SHIPPING_ADDRESS_ID")
    private long paymentSnapShotShippingAddressId;
    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "COMPANY_CODE")
    @Enumerated(EnumType.STRING)
    private ShipmentCompanyCode companyCode;

    @Column(name = "DELIVERY_DATE")
    private LocalDateTime deliveryDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    public void deliveryStart(String invoiceNo, ShipmentCompanyCode companyCode, ShipmentType shipmentType){
        this.shipmentType =shipmentType;
        this.invoiceNo = invoiceNo;
        this.companyCode = companyCode;
        this.deliveryStatus = DeliveryStatus.DELIVERY_PROCESSING;
    }


    public void setOrder(Order order) {
        if (this.order != null && this.order.equals(order)) {
            return;
        }
        this.order = order;
        if (order != null && !order.getShipment().equals(this)) {
            order.setShipment(this);
        }
    }




}
