package com.connectly.partnerAdmin.module.product.entity.stock;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.product.core.Sku;
import com.connectly.partnerAdmin.module.product.dto.option.OptionDto;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductStatus;
import com.connectly.partnerAdmin.module.product.entity.option.OptionDetail;
import com.connectly.partnerAdmin.module.product.entity.option.ProductOption;
import jakarta.persistence.*;
import lombok.*;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "product")
@Entity
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    @Column(name ="PRODUCT_ID")
    private long id;

    @Embedded
    private ProductStatus productStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_GROUP_ID", referencedColumnName = "PRODUCT_GROUP_ID")
    private ProductGroup productGroup;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductStock productStock;

    @OneToMany(mappedBy = "product", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductOption> productOptions = new LinkedHashSet<>();


    public Product(long id, int qty) {
        this.id = id;
        this.productStatus = new ProductStatus(qty);
        this.setProductStock(new ProductStock(id, qty));
    }

    public Product(int qty) {
        this.productStatus = new ProductStatus(qty);
        this.setProductStock(new ProductStock(qty));
    }

    public void setProductGroup(ProductGroup productGroup) {
        if (this.productGroup != null) {
            this.productGroup.getProducts().remove(this);
        }

        this.productGroup = productGroup;

        if (productGroup != null && !productGroup.getProducts().contains(this)) {
            productGroup.getProducts().add(this);
        }

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


    public void addProductOption(ProductOption productOption) {
        this.productOptions.add(productOption);
        productOption.setProduct(this);
    }

    public int getStockQuantity(){
        return productStock.getStockQuantity();
    }

    public void updateStock(Sku sku){
        productStock.updateStockQuantity(sku.getQuantity());
    }

    public void setStock(Sku sku){
        productStock.setStockQuantity(sku.getQuantity());
    }

    public void soldOut(){
        productStatus.soldOut();
    }

    public void onStock(){
        productStatus.onStock();
        if(this.productGroup != null){
            this.productGroup.onStock();
        }
    }


    public Set<OptionDto> toOptions() {
        return this.productOptions.stream()
                .map(po -> {
                    OptionDetail optionDetail = po.getOptionDetail();
                    return OptionDto.builder()
                            .optionGroupId(optionDetail.getOptionGroup().getId())
                            .optionDetailId(optionDetail.getId())
                            .optionName(optionDetail.getOptionGroup().getOptionName())
                            .optionValue(optionDetail.getOptionValue())
                            .build();

                })
                .sorted(Comparator.comparingLong(OptionDto::getOptionGroupId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public String getOption(){
        Set<OptionDto> options = toOptions();
        return options.stream().map(OptionDto::getFullOptionName)
                .collect(Collectors.joining(" "));
    }

    public void updateDisplayYn(Yn displayYn){
        productStatus.setDisplayYn(displayYn);
    }

    public void delete(){
        this.setDeleteYn(Yn.Y);
        this.productStock.delete();
        this.productOptions.forEach(ProductOption::delete);
    }

    public boolean isSoldOut(){
        return this.productStatus.isSoldOut();
    }
}
