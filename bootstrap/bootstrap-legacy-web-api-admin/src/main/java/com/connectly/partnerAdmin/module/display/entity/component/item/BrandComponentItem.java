package com.connectly.partnerAdmin.module.display.entity.component.item;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import lombok.*;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "BRAND_COMPONENT_ITEM")
@Entity
public class BrandComponentItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRAND_COMPONENT_ITEM_ID")
    private long id;

    @Column(name = "BRAND_ID")
    private long brandId;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    @Column(name = "BRAND_COMPONENT_ID")
    private long brandComponentId;
}
