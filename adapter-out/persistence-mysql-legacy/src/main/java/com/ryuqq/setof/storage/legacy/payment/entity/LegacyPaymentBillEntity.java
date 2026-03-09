package com.ryuqq.setof.storage.legacy.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * LegacyPaymentBillEntity - 레거시 결제 청구서 엔티티.
 *
 * <p>레거시 DB의 payment_bill 테이블 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 Entity 접미사 사용.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션(@ManyToOne 등) 사용 금지.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "payment_bill")
public class LegacyPaymentBillEntity {

    @Id
    @Column(name = "payment_bill_id")
    private Long id;

    @Column(name = "PAYMENT_ID")
    private long paymentId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "PAYMENT_METHOD_ID")
    private long paymentMethodId;

    @Column(name = "PAYMENT_AMOUNT")
    private long paymentAmount;

    @Column(name = "USED_MILEAGE_AMOUNT")
    private long usedMileageAmount;

    @Column(name = "BUYER_NAME")
    private String buyerName;

    @Column(name = "BUYER_EMAIL")
    private String buyerEmail;

    @Column(name = "BUYER_PHONE_NUMBER")
    private String buyerPhoneNumber;

    @Column(name = "PAYMENT_AGENCY_ID")
    private String paymentAgencyId;

    @Column(name = "PAYMENT_UNIQUE_ID")
    private String paymentUniqueId;

    @Column(name = "RECEIPT_URL")
    private String receiptUrl;

    @Column(name = "PAYMENT_CHANNEL")
    private String paymentChannel;

    @Column(name = "CARD_NAME")
    private String cardName;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "delete_yn")
    private String deleteYn;

    @Column(name = "INSERT_OPERATOR")
    private String insertOperator;

    @Column(name = "UPDATE_OPERATOR")
    private String updateOperator;

    @Column(name = "INSERT_DATE")
    private LocalDateTime insertDate;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

    protected LegacyPaymentBillEntity() {}

    public Long getId() {
        return id;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public long getUserId() {
        return userId;
    }

    public long getPaymentMethodId() {
        return paymentMethodId;
    }

    public long getPaymentAmount() {
        return paymentAmount;
    }

    public long getUsedMileageAmount() {
        return usedMileageAmount;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public String getBuyerPhoneNumber() {
        return buyerPhoneNumber;
    }

    public String getPaymentAgencyId() {
        return paymentAgencyId;
    }

    public String getPaymentUniqueId() {
        return paymentUniqueId;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getDeleteYn() {
        return deleteYn;
    }

    public String getInsertOperator() {
        return insertOperator;
    }

    public String getUpdateOperator() {
        return updateOperator;
    }

    public LocalDateTime getInsertDate() {
        return insertDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
