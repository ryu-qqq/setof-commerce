package com.connectly.partnerAdmin.module.brand.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "BRAND_MAPPING")
@Entity
public class BrandMapping  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRAND_MAPPING_ID")
    private long id;

    private long siteId;

    private String siteName;

    private long brandId;

    private String mappingBrandId;

}
