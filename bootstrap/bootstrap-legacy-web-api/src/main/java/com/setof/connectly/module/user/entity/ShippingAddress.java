package com.setof.connectly.module.user.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.user.dto.shipping.UserShippingInfo;
import com.setof.connectly.module.user.entity.embedded.ShippingDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "shipping_address")
@Entity
public class ShippingAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_address_id")
    private Long id;

    private long userId;
    @Embedded private ShippingDetails shippingDetails;

    @Enumerated(EnumType.STRING)
    private Yn defaultYn;

    @Builder
    public ShippingAddress(long id, long userId, ShippingDetails shippingDetails, Yn defaultYn) {
        this.id = id;
        this.userId = userId;
        this.shippingDetails = shippingDetails;
        this.defaultYn = defaultYn;
    }

    public void update(UserShippingInfo userShippingInfo) {
        this.shippingDetails = userShippingInfo.getShippingDetails();
        this.defaultYn = userShippingInfo.getDefaultYn();
    }

    public void deactivation() {
        this.defaultYn = Yn.N;
    }
}
