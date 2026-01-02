package com.setof.connectly.module.discount.entity.history;

import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "discount_use_history")
@Entity
public class DiscountUseHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_use_history")
    private long id;

    @Column(name = "discount_policy_id")
    private long discountPolicyId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "payment_id")
    private long paymentId;

    @Column(name = "product_group_id")
    private long productGroupId;

    @Column(name = "use_date")
    private LocalDateTime useDate;
}
