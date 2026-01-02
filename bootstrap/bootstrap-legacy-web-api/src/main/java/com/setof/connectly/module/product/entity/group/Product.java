package com.setof.connectly.module.product.entity.group;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.product.entity.group.embedded.ProductStatus;
import com.setof.connectly.module.product.entity.stock.ProductStock;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "product")
@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Embedded private ProductStatus productStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_GROUP_ID", referencedColumnName = "PRODUCT_GROUP_ID")
    private ProductGroup productGroup;

    @OneToOne(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private ProductStock productStock;

    public int getStockQuantity() {
        return productStock.getStockQuantity();
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public void setProductStock(ProductStock productStock) {
        if (this.productStock != null && this.productStock.equals(productStock)) {
            return;
        }

        if (this.productStock != null) {
            this.productStock.setProduct(null);
        }

        this.productStock = productStock;
        if (productStock != null) {
            productStock.setProduct(this);
        }
    }

    public void updateStock(int qty) {
        productStock.setStockQuantity(qty);

        if (productStock.getStockQuantity() == 0) {
            soldOut();
        } else {
            onStock();
        }
    }

    private void onStock() {
        productStatus.onStock();
        productGroup.onStock();
    }

    private void soldOut() {
        productStatus.soldOut();
    }

    public boolean isSoldOut() {
        return this.productStatus.isSoldOut();
    }
}
