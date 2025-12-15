package com.setof.connectly.module.display.entity.component.item;

import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "IMAGE_COMPONENT_ITEM")
@Entity
public class ImageComponentItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_COMPONENT_ITEM_ID")
    private long id;

    private String imageUrl;
    private int displayOrder;
    private long imageComponentId;
    private String linkUrl;
}
