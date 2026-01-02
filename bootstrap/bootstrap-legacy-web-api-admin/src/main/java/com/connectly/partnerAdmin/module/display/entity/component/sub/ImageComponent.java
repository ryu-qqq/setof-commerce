package com.connectly.partnerAdmin.module.display.entity.component.sub;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.enums.ImageType;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@Table(name = "image_component")
@Entity
public class ImageComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_component_id")
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "image_type")
    private ImageType imageType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "COMPONENT_ID")
    private Component component;

}