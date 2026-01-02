package com.setof.connectly.module.display.entity.component;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.entity.embedded.ComponentDetails;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "component")
@Entity
public class Component extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_id")
    private Long id;

    private long contentId;

    @Column(name = "component_name")
    private String componentName;

    @Embedded private ComponentDetails componentDetails;

    @Column(name = "exposed_products")
    private int exposedProducts;

    @Embedded private DisplayPeriod displayPeriod;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "display_yn")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "VIEW_EXTENSION_ID",
            referencedColumnName = "VIEW_EXTENSION_ID",
            nullable = true)
    private ViewExtension viewExtension;
}
