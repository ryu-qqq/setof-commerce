package com.connectly.partnerAdmin.module.discount.entity.history;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "discount_use_history")
@Entity
public class DiscountUseHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_use_history_id")
    private long id;
    private long discountPolicyId;
    private long userId;
    private long orderId;
    private long paymentId;
    private long productGroupId;
    private LocalDateTime useDate;




}
