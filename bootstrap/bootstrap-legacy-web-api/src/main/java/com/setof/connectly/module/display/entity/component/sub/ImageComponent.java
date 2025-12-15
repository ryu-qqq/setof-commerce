package com.setof.connectly.module.display.entity.component.sub;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.display.enums.ImageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "IMAGE_COMPONENT")
@Entity
public class ImageComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_COMPONENT_ID")
    private long id;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    private long componentId;
}
