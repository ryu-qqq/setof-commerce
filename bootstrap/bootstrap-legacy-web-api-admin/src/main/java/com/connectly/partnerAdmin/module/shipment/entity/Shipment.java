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
@Table(name = "shipment")
public class Shipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private long id;

    @Column(name = "shipment_type")
    @Enumerated(EnumType.STRING)
    private ShipmentType shipmentType;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "sender_email")
    private String senderEmail;

    @Column(name = "sender_phone_number")
    private String senderPhoneNumber;

    @Column(name = "payment_snapshot_shipping_address_id")
    private long paymentSnapShotShippingAddressId;
    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "company_code")
    @Enumerated(EnumType.STRING)
    private ShipmentCompanyCode companyCode;

    @Column(name = "delivery_date")
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
