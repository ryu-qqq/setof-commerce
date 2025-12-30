package com.connectly.partnerAdmin.module.display.entity.component.item;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.display.entity.embedded.ImageSize;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@Table(name = "IMAGE_COMPONENT_ITEM")
@Entity
public class ImageComponentItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_COMPONENT_ITEM_ID")
    private long id;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "DISPLAY_ORDER")
    private int displayOrder;

    @Column(name = "LINK_URL")
    private String linkUrl;

    @Column(name = "IMAGE_COMPONENT_ID")
    private long imageComponentId;

    @Embedded
    private ImageSize imageSize;

}
