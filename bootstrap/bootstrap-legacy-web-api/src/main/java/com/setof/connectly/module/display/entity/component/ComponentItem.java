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
    @Column(name = "COMPONENT_ITEM_ID")
    private long id;

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

    @Column(name = "PRODUCT_DISPLAY_NAME")
    private String productDisplayName;

    @Column(name = "PRODUCT_DISPLAY_IMAGE")
    private String productDisplayImage;

    @Column(name = "DISPLAY_ORDER")
    private Long displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPONENT_TARGET_ID", referencedColumnName = "COMPONENT_TARGET_ID")
    private ComponentTarget componentTarget;
}
