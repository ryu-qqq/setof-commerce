package com.connectly.partnerAdmin.module.external.entity;

import java.math.BigDecimal;

import org.springframework.util.StringUtils;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.external.enums.MappingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "EXTERNAL_PRODUCT")
@Entity
public class ExternalProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXTERNAL_PRODUCT_ID")
    private long id;

    @Column(name = "SITE_ID")
    private long siteId;

    @Setter
    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

    @Column(name = "EXTERNAL_IDX")
    private String externalIdx;

    @Column(name = "MAPPING_STATUS")
    @Enumerated(EnumType.STRING)
    private MappingStatus mappingStatus;

    private BigDecimal fixedPrice;

    public void active(String externalIdx){
        mappingStatus = MappingStatus.ACTIVE;
        this.externalIdx = externalIdx;
    }

    public void active(){
        mappingStatus = MappingStatus.ACTIVE;
    }

    public void update(){
        mappingStatus = MappingStatus.UPDATE;
    }

    public void update(MappingStatus mappingStatus){
        this.mappingStatus = mappingStatus;
    }


    public void deActive(){
        mappingStatus = MappingStatus.DE_ACTIVE;
    }

    public void update(MappingStatus mappingStatus, String externalIdx){
        this.externalIdx = externalIdx;
        if(mappingStatus.isUpdate()){
            if(StringUtils.hasText(this.externalIdx)) this.mappingStatus = mappingStatus;
        }else{
            this.mappingStatus = mappingStatus;
        }
    }

}
