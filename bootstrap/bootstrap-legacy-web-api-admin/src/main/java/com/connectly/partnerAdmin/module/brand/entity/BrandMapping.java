package com.connectly.partnerAdmin.module.brand.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "brand_mapping")
@Entity
public class BrandMapping  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_mapping_id")
    private long id;

    private long siteId;

    private String siteName;

    private long brandId;

    private String mappingBrandId;

}
