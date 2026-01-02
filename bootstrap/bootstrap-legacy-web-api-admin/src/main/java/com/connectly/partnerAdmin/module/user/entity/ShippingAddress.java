package com.connectly.partnerAdmin.module.user.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.user.entity.embedded.ShippingDetails;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "shipping_address")
@Entity
public class ShippingAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_address_id")
    private long id;

    @Embedded
    private ShippingDetails shippingDetails;

    @Enumerated(EnumType.STRING)
    private Yn defaultYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public void setUsers(Users users) {
        if (this.users != null) {
            this.users.getShippingAddresses().remove(this);
        }
        this.users = users;

        if (users != null && !users.getShippingAddresses().contains(this)) {
            users.getShippingAddresses().add(this);
        }

    }


}
