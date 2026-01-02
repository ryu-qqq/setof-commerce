package com.setof.connectly.module.product.entity.stock;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.product.entity.group.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "product_stock")
@Entity
public class ProductStock extends BaseEntity {

    @Id
    @Column(name = "product_id")
    private long id;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

    public void setProduct(Product product) {
        if (this.product != null && this.product.equals(product)) {
            return;
        }
        this.product = product;
        if (product != null && !product.getProductStock().equals(this)) {
            product.setProductStock(this);
        }
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            this.stockQuantity = this.stockQuantity + (stockQuantity * -1);
        } else {
            this.stockQuantity = this.stockQuantity - stockQuantity;
        }
    }
}
