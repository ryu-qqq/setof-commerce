package com.setof.connectly.module.cart.entity;

import com.setof.connectly.module.cart.entity.embedded.CartDetails;
import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "CART")
@Entity
public class Cart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ID")
    private long id;

    private long userId;
    @Embedded private CartDetails cartDetails;

    protected Cart() {}

    public Cart(long userId, CartDetails cartDetails) {
        this.userId = userId;
        this.cartDetails = cartDetails;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public CartDetails getCartDetails() {
        return cartDetails;
    }

    public void updateQuantity(int quantity) {
        this.cartDetails.setQuantity(quantity);
    }

    public void delete() {
        this.setDeleteYn(Yn.Y);
    }

    public void rollBack() {
        this.setDeleteYn(Yn.N);
    }
}
