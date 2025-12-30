package com.connectly.partnerAdmin.module.order.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.entity.snapshot.delivery.OrderSnapShotProductDelivery;
import com.connectly.partnerAdmin.module.order.entity.snapshot.group.OrderSnapShotProductGroup;
import com.connectly.partnerAdmin.module.order.entity.snapshot.image.OrderSnapShotProductGroupDetailDescription;
import com.connectly.partnerAdmin.module.order.entity.snapshot.image.OrderSnapShotProductGroupImage;
import com.connectly.partnerAdmin.module.order.entity.snapshot.mileage.OrderSnapShotMileage;
import com.connectly.partnerAdmin.module.order.entity.snapshot.notice.OrderSnapShotProductNotice;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.OrderSnapShotOptionDetail;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.OrderSnapShotOptionGroup;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.OrderSnapShotProductOption;
import com.connectly.partnerAdmin.module.order.entity.snapshot.stock.OrderSnapShotProduct;
import com.connectly.partnerAdmin.module.order.entity.snapshot.stock.OrderSnapShotProductStock;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.payment.entity.Payment;
import com.connectly.partnerAdmin.module.shipment.entity.Shipment;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "ORDERS")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private long id;

    
    @Column(name = "ORDER_AMOUNT")
    private BigDecimal orderAmount;

    @Column(name = "ORDER_STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Setter
    @Column(name = "PURCHASE_CONFIRMED_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseConfirmedDate;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "REVIEW_YN")
    @Enumerated(EnumType.STRING)
    private Yn reviewYn;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "SELLER_ID")
    private long sellerId;

    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "SETTLEMENT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime settlementDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_ID", nullable = false)
    private Payment payment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderHistory> histories = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Shipment shipment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Settlement settlement;


    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private OrderSnapShotProductGroup orderSnapShotProductGroup;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private OrderSnapShotProduct orderSnapShotProduct;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private OrderSnapShotProductStock orderSnapShotProductStock;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private OrderSnapShotProductGroupDetailDescription productGroupDetailDescription;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrderSnapShotProductGroupImage> orderSnapShotProductGroupImages = new LinkedHashSet<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private OrderSnapShotProductDelivery orderSnapShotProductDelivery;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private OrderSnapShotProductNotice orderSnapShotProductNotice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrderSnapShotProductOption> orderSnapShotProductOptions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrderSnapShotOptionGroup> orderSnapShotOptionGroups = new LinkedHashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrderSnapShotOptionDetail> orderSnapShotOptionDetails = new LinkedHashSet<>();

    @OneToOne(mappedBy = "order", orphanRemoval = true, fetch = FetchType.LAZY)
    private OrderSnapShotMileage orderSnapShotMileage;


    public Order(Money orderAmount, OrderStatus orderStatus, int quantity, Yn reviewYn, long productId, long userId, long sellerId) {
        this.orderAmount = orderAmount.getAmount();
        this.orderStatus = orderStatus;
        this.quantity = quantity;
        this.reviewYn = reviewYn;
        this.productId = productId;
        this.userId = userId;
        this.sellerId = sellerId;
        this.histories.add(new OrderHistory(this));
    }

    private Order(long id) {
        this.id = id;
    }

    public static Order of(long id) {
        return new Order(id);
    }

    public void setOrderStatus(OrderStatus orderStatus, String changeReason, String changeDetailReason) {
        if(this.orderStatus != orderStatus) {
            addOrderHistory(changeReason, changeDetailReason, orderStatus);
            this.orderStatus = orderStatus;
        }
    }

    private void addOrderHistory(String changeReason, String changeDetailReason, OrderStatus orderStatus) {
        OrderHistory history = new OrderHistory(this, changeReason, changeDetailReason, orderStatus);
        this.histories.add(history);
    }


    public void setPayment(Payment payment) {
        if (this.payment != null) {
            this.payment.getOrders().remove(this);
        }

        this.payment = payment;
    }


    public void setShipment(Shipment shipment) {
        if (this.shipment != null) {
            this.shipment.setOrder(null);
        }

        this.shipment = shipment;

        if (shipment != null) {
            shipment.setOrder(this);
        }
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
        if (settlement != null) {
            settlement.setOrder(this);
        }
    }

    public void setOrderSnapShotProductGroup(OrderSnapShotProductGroup orderSnapShotProductGroup) {
        this.orderSnapShotProductGroup = orderSnapShotProductGroup;
        if (orderSnapShotProductGroup != null) {
            orderSnapShotProductGroup.setOrder(this);
        }
    }

    public void setOrderSnapShotProduct(OrderSnapShotProduct orderSnapShotProduct) {
        this.orderSnapShotProduct = orderSnapShotProduct;
        if (orderSnapShotProduct != null) {
            orderSnapShotProduct.setOrder(this);
        }
    }

    public void setOrderSnapShotProductStock(OrderSnapShotProductStock orderSnapShotProductStock) {
        this.orderSnapShotProductStock = orderSnapShotProductStock;
        if (orderSnapShotProductStock != null) {
            orderSnapShotProductStock.setOrder(this);
        }
    }

    public void setProductGroupDetailDescription(OrderSnapShotProductGroupDetailDescription productGroupDetailDescription) {
        this.productGroupDetailDescription = productGroupDetailDescription;
        if (productGroupDetailDescription != null) {
            productGroupDetailDescription.setOrder(this);
        }
    }

    public void setOrderSnapShotProductDelivery(OrderSnapShotProductDelivery orderSnapShotProductDelivery) {
        this.orderSnapShotProductDelivery = orderSnapShotProductDelivery;
        if (orderSnapShotProductDelivery != null) {
            orderSnapShotProductDelivery.setOrder(this);
        }
    }


    public void setOrderSnapShotProductNotice(OrderSnapShotProductNotice orderSnapShotProductNotice) {
        this.orderSnapShotProductNotice = orderSnapShotProductNotice;
        if (orderSnapShotProductNotice != null) {
            orderSnapShotProductNotice.setOrder(this);
        }
    }

    public void addOrderSnapShotProductGroupImage(OrderSnapShotProductGroupImage orderSnapShotProductGroupImage) {
        orderSnapShotProductGroupImages.add(orderSnapShotProductGroupImage);
        orderSnapShotProductGroupImage.setOrder(this);
    }

    public void addOrderSnapShotProductOption(OrderSnapShotProductOption orderSnapShotProductOption) {
        orderSnapShotProductOptions.add(orderSnapShotProductOption);
        orderSnapShotProductOption.setOrder(this);
    }


    public void addOrderSnapShotOptionGroup(OrderSnapShotOptionGroup orderSnapShotOptionGroup) {
        orderSnapShotOptionGroups.add(orderSnapShotOptionGroup);
        orderSnapShotOptionGroup.setOrder(this);
    }


    public void addOrderSnapShotOptionDetail(OrderSnapShotOptionDetail orderSnapShotOptionDetail) {
        orderSnapShotOptionDetails.add(orderSnapShotOptionDetail);
        orderSnapShotOptionDetail.setOrder(this);
    }


    public void setOrderSnapShotMileage(OrderSnapShotMileage orderSnapShotMileage) {
        this.orderSnapShotMileage = orderSnapShotMileage;
        if (orderSnapShotMileage != null) {
            orderSnapShotMileage.setOrder(this);
        }
    }
}
