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
@Table(name = "SHIPMENT")
public class Shipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHIPMENT_ID")
    private long id;

    @Column(name = "ORDER_ID")
    private long orderId;

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
