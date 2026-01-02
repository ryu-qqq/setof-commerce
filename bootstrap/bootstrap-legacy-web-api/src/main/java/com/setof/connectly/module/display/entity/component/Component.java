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
    @Column(name = "COMPONENT_ID")
    private long id;

    private long contentId;

    @Column(name = "COMPONENT_NAME")
    private String componentName;

    @Embedded private ComponentDetails componentDetails;

    @Column(name = "EXPOSED_PRODUCTS")
    private int exposedProducts;

    @Embedded private DisplayPeriod displayPeriod;

    @Column(name = "DISPLAY_ORDER")
    private int displayOrder;

    @Column(name = "DISPLAY_YN")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "VIEW_EXTENSION_ID",
            referencedColumnName = "VIEW_EXTENSION_ID",
            nullable = true)
    private ViewExtension viewExtension;
}
