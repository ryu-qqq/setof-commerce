package com.connectly.partnerAdmin.module.display.entity.component;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.ComponentDetails;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "COMPONENT")
@Entity
public class Component extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPONENT_ID")
    private long id;
    private long contentId;

    @Column(name = "COMPONENT_NAME")
    private String componentName;
    @Embedded
    private ComponentDetails componentDetails;

    @Column(name = "EXPOSED_PRODUCTS")
    private int exposedProducts;

    @Embedded
    private DisplayPeriod displayPeriod;

    @Column(name = "DISPLAY_ORDER")
    private int displayOrder;

    @Column(name = "DISPLAY_YN")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "VIEW_EXTENSION_ID", referencedColumnName = "VIEW_EXTENSION_ID", nullable = true)
    private ViewExtension viewExtension;

}
