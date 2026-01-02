package com.connectly.partnerAdmin.module.product.entity.stock;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.product.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "PRODUCT_STOCK")
@Entity
public class ProductStock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private long id;

    @Column(name = "STOCK_QUANTITY")
    private int stockQuantity;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    public ProductStock(long id, int stockQuantity) {
        this.id = id;
        this.stockQuantity = stockQuantity;
    }

    public ProductStock(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setProduct(Product product) {
        if (this.product != null && this.product.equals(product)) {
            return;
        }
        this.product = product;
        if (product != null && !product.getProductStock().equals(this)) {
            product.setProductStock(this);
        }
    }


    public void updateStockQuantity(int stockQuantity) {
        if(stockQuantity <0){
            this.stockQuantity = this.stockQuantity + (stockQuantity * -1);
        }else{
            this.stockQuantity = this.stockQuantity - stockQuantity;
            hasNoQuantity();
        }
        validateStockCount();
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
        hasNoQuantity();
        validateStockCount();
    }

    private void validateStockCount() {
        if (stockQuantity < 0) {
            throw new NotEnoughStockException(this.id);
        }
    }

    private void hasNoQuantity(){
        if(stockQuantity == 0){
            this.product.soldOut();
        }else{
            if(this.product != null) {
                this.product.onStock();
            }
        }
    }

    public void delete(){
        this.setDeleteYn(Yn.Y);
    }
}

