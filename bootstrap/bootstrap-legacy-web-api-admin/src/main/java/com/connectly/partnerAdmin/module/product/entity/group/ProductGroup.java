package com.connectly.partnerAdmin.module.product.entity.group;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.product.entity.delivery.ProductDelivery;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.Price;
import com.connectly.partnerAdmin.module.product.entity.group.embedded.ProductGroupDetails;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupDetailDescription;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupImage;
import com.connectly.partnerAdmin.module.product.entity.notice.ProductNotice;
import com.connectly.partnerAdmin.module.product.entity.score.ProductRatingStats;
import com.connectly.partnerAdmin.module.product.entity.score.ProductScore;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.connectly.partnerAdmin.module.product.enums.option.OptionType;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "PRODUCT_GROUP")
@Entity
public class ProductGroup extends BaseEntity {

    @Id
    @Column(name = "PRODUCT_GROUP_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    private ProductGroupDetails productGroupDetails;

    @OneToOne(mappedBy = "productGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductDelivery productDelivery;

    @OneToOne(mappedBy = "productGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductNotice productNotice;

    @Builder.Default
    @OneToMany(mappedBy = "productGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductGroupImage> images = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "productGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Product> products = new LinkedHashSet<>();

    @OneToOne(mappedBy = "productGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductGroupDetailDescription detailDescription;

    @OneToOne(mappedBy = "productGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductScore productScore;

    @OneToOne(mappedBy = "productGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductRatingStats productRatingStats;


    public void setProductDelivery(ProductDelivery productDelivery) {
        if (this.productDelivery != null && this.productDelivery.equals(productDelivery)) {
            return;
        }

        if (this.productDelivery != null) {
            this.productDelivery.setProductGroup(null);
        }
        this.productDelivery = productDelivery;

        if (productDelivery != null) {
            productDelivery.setProductGroup(this);
        }
    }

    public void setProductNotice(ProductNotice productNotice) {
        if (this.productNotice != null && this.productNotice.equals(productNotice)) {
            return;
        }

        if (this.productNotice != null) {
            this.productNotice.setProductGroup(null);
        }

        this.productNotice = productNotice;
        if (productNotice != null) {
            productNotice.setProductGroup(this);
        }
    }

    public void updateProductGroupDetails(ProductGroupDetails productGroupDetails){
        this.productGroupDetails.setProductGroupDetails(productGroupDetails);
        updatePrice(productGroupDetails.getPrice());
    }


    public void addImage(ProductGroupImage image) {
        images.add(image);
        image.setProductGroup(this);
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setProductGroup(this);
    }

    public void setDetailDescription(ProductGroupDetailDescription description) {
        if (this.detailDescription != null) {
            this.detailDescription.setProductGroup(null);
        }
        this.detailDescription = description;
        if (description != null) {
            description.setProductGroup(this);
        }
    }

    public void setProductScore(ProductScore productScore) {
        if (this.productScore != null) {
            this.productScore.setProductGroup(null);
        }
        this.productScore = productScore;
        if (productScore != null) {
            productScore.setProductGroup(this);
        }
    }

    public void setProductRatingStats(ProductRatingStats productRatingStats) {
        if (this.productRatingStats != null) {
            this.productRatingStats.setProductGroup(null);
        }
        this.productRatingStats = productRatingStats;
        if (productRatingStats != null) {
            productRatingStats.setProductGroup(this);
        }
    }


    public void soldOut(){
        this.productGroupDetails.getProductStatus().soldOut();
    }

    public void onStock(){
        this.productGroupDetails.getProductStatus().onStock();
    }

    public void updateCategory(long categoryId){
        this.productGroupDetails.setCategoryId(categoryId);
    }

    public void updatePrice(Price price){
        if(price.getRegularPrice().intValue() < price.getCurrentPrice().intValue()) throw new IllegalStateException("Regular price cannot be less than current price.");
        log.info("Product Group Id {} update Price  Before: {}, After: {}", this.id, this.productGroupDetails.getPrice(), price);
        Money regularPrice = Money.wons(price.getRegularPrice());
        Money currentPrice = Money.wons(price.getCurrentPrice());
        this.productGroupDetails.setPrice(new Price(regularPrice, currentPrice));
    }

    public void updateDisplayYn(Yn displayYn){
        this.productGroupDetails.getProductStatus().setDisplayYn(displayYn);
    }

    public boolean isOption(){
        return !this.getProductGroupDetails().getOptionType().isSingle();
    }

    public void setOptionType(OptionType optionType) {
        this.productGroupDetails.setOptionType(optionType);
    }

}
