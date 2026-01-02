package com.setof.connectly.module.product.entity.group;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.crawl.CrawlProduct;
import com.setof.connectly.module.product.entity.delivery.ProductDelivery;
import com.setof.connectly.module.product.entity.group.embedded.ProductGroupDetails;
import com.setof.connectly.module.product.entity.image.ProductGroupDetailDescription;
import com.setof.connectly.module.product.entity.image.ProductGroupImage;
import com.setof.connectly.module.product.entity.notice.ProductNotice;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "product_group")
@Entity
public class ProductGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_GROUP_ID")
    private long id;

    @Embedded private ProductGroupDetails productGroupDetails;

    @OneToOne(
            mappedBy = "productGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            optional = false)
    private ProductDelivery productDelivery;

    @OneToOne(
            mappedBy = "productGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            optional = false)
    private ProductNotice productNotice;

    @Builder.Default
    @OneToMany(
            mappedBy = "productGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<ProductGroupImage> images = new HashSet<>();

    @OneToOne(
            mappedBy = "productGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            optional = false)
    private ProductGroupDetailDescription detailDescription;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "PRODUCT_GROUP_ID", referencedColumnName = "PRODUCT_GROUP_ID")
    private CrawlProduct crawlProduct;

    @Builder
    public ProductGroup(long productGroupId, ProductGroupDetails productGroupDetails) {
        this.id = productGroupId;
        this.productGroupDetails = productGroupDetails;
    }

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

    public void setDetailDescription(ProductGroupDetailDescription description) {
        if (this.detailDescription != null) {
            this.detailDescription.setProductGroup(null);
        }
        this.detailDescription = description;
        if (description != null) {
            description.setProductGroup(this);
        }
    }

    public void setCrawlProduct(CrawlProduct crawlProduct) {
        this.crawlProduct = crawlProduct;
    }

    public void addImage(ProductGroupImage image) {
        images.add(image);
        image.setProductGroup(this);
    }

    public void onStock() {
        this.productGroupDetails.getProductStatus().onStock();
    }

    public void soldOut() {
        this.productGroupDetails.getProductStatus().soldOut();
    }
}
