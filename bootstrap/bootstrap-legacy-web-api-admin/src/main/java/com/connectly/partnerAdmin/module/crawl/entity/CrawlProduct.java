package com.connectly.partnerAdmin.module.crawl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.external.enums.MappingStatus;


@Getter
@Table(name = "crawl_product")
@Entity
public class CrawlProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crawl_product_id")
    private Long id;

    private Long productGroupId;

    private long crawlProductSku;

    private long siteId;

    private long sellerId;

    private long brandId;

    private long categoryId;

    private String colorCode;

    @Enumerated(EnumType.STRING)
    private MappingStatus updateStatus;

    protected CrawlProduct() {}

    @Builder
    public CrawlProduct(Long id, Long productGroupId, long crawlProductSku, long siteId, long sellerId, long brandId,
                        long categoryId, String colorCode, MappingStatus updateStatus) {
        this.id = id;
        this.productGroupId = productGroupId;
        this.crawlProductSku = crawlProductSku;
        this.siteId = siteId;
        this.sellerId = sellerId;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.colorCode = colorCode;
        this.updateStatus = updateStatus;
    }

    public void sync(long productGroupId){
        this.productGroupId = productGroupId;
        active();
    }

    public void active(){
        this.updateStatus = MappingStatus.ACTIVE;
        this.setDeleteYn(Yn.N);
    }

    public void update(){
        if(this.updateStatus.isPending()){
            return;
        }
        this.updateStatus = MappingStatus.UPDATE;
    }


}
