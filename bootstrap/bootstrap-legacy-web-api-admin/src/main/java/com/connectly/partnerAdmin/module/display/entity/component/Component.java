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
@Table(name = "component")
@Entity
public class Component extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_id")
    private long id;
    private long contentId;

    @Column(name = "component_name")
    private String componentName;
    @Embedded
    private ComponentDetails componentDetails;

    @Column(name = "exposed_products")
    private int exposedProducts;

    @Embedded
    private DisplayPeriod displayPeriod;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "display_yn")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "VIEW_EXTENSION_ID", referencedColumnName = "VIEW_EXTENSION_ID", nullable = true)
    private ViewExtension viewExtension;

}
