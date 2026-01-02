package com.setof.connectly.module.display.entity.component;

import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "component_item")
@Entity
public class ComponentItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_item_id")
    private Long id;

    @Column(name = "product_group_id")
    private long productGroupId;

    @Column(name = "product_display_name")
    private String productDisplayName;

    @Column(name = "product_display_image")
    private String productDisplayImage;

    @Column(name = "display_order")
    private Long displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPONENT_TARGET_ID", referencedColumnName = "COMPONENT_TARGET_ID")
    private ComponentTarget componentTarget;
}
