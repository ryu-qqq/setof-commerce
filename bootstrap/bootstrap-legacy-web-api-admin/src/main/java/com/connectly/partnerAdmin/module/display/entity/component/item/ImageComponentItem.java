package com.connectly.partnerAdmin.module.display.entity.component.item;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.display.entity.embedded.ImageSize;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@Table(name = "image_component_item")
@Entity
public class ImageComponentItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_component_item_id")
    private long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "image_component_id")
    private long imageComponentId;

    @Embedded
    private ImageSize imageSize;

}
