package com.connectly.partnerAdmin.module.display.entity.component;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import lombok.*;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "COMPONENT_ITEM")
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
    private int displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPONENT_TARGET_ID", referencedColumnName = "COMPONENT_TARGET_ID")
    private ComponentTarget componentTarget;


}
