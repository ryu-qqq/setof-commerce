package com.setof.connectly.module.cart.entity.embedded;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Embeddable
public class CartDetails {

    @NotNull(message = "상품 그룹 아이디는 필수 입니다.")
    private long productGroupId;

    @NotNull(message = "상품 아이디는 필수 입니다.")
    private long productId;

    @Min(value = 1, message = "재고의 수량은  0 보다 커야합니다.")
    @Max(value = 999, message = "재고의 수량은 999 보다 작아야 합니다.")
    private int quantity;

    @NotNull(message = "판매자 아이디는 필수 입니다.")
    private long sellerId;

    protected CartDetails() {}

    @Builder
    public CartDetails(long productGroupId, long productId, int quantity, long sellerId) {
        this.productGroupId = productGroupId;
        this.productId = productId;
        this.quantity = quantity;
        this.sellerId = sellerId;
    }

    public long getProductGroupId() {
        return productGroupId;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
