package com.setof.connectly.module.display.entity.component.item;

import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private long categoryId;

    @Column(name = "BRAND_COMPONENT_ID")
    private long brandComponentId;
}
