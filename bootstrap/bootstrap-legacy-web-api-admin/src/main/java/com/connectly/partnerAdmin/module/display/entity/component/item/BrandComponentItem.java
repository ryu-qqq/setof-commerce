package com.connectly.partnerAdmin.module.display.entity.component.item;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import lombok.*;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "brand_component_item")
@Entity
public class BrandComponentItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_component_item_id")
    private long id;

    @Column(name = "brand_id")
    private long brandId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "brand_component_id")
    private long brandComponentId;
}
