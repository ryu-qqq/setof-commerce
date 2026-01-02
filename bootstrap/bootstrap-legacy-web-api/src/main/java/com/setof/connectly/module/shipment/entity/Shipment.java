package com.setof.connectly.module.shipment.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.seller.dto.SenderDto;
import com.setof.connectly.module.shipment.enums.DeliveryStatus;
import com.setof.connectly.module.shipment.enums.ShipmentCompanyCode;
import com.setof.connectly.module.shipment.enums.ShipmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "shipment")
public class Shipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long id;

    @Column(name = "order_id")
    private long orderId;

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

    public Shipment(long orderId, long paymentSnapShotShippingAddressId, SenderDto senderDto) {
        this.orderId = orderId;
        this.deliveryStatus = DeliveryStatus.DELIVERY_PENDING;
        this.senderName = senderDto.getSellerName();
        this.senderEmail = senderDto.getSellerEmail();
        this.senderPhoneNumber = senderDto.getSellerPhoneNumber();
        this.paymentSnapShotShippingAddressId = paymentSnapShotShippingAddressId;
        this.companyCode = ShipmentCompanyCode.REFER_DETAIL;
        this.invoiceNo = "";
    }
}
