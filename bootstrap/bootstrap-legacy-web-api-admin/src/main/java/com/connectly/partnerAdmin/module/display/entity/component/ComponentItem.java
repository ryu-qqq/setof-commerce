package com.connectly.partnerAdmin.module.display.entity.component;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import lombok.*;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "component_item")
@Entity
public class ComponentItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_item_id")
    private long id;

    @Column(name = "product_group_id")
    private long productGroupId;

    @Column(name = "product_display_name")
    private String productDisplayName;

    @Column(name = "product_display_image")
    private String productDisplayImage;

    @Column(name = "display_order")
    private int displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPONENT_TARGET_ID", referencedColumnName = "COMPONENT_TARGET_ID")
    private ComponentTarget componentTarget;


}
