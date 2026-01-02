package com.connectly.partnerAdmin.module.display.entity.component.sub.product;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.enums.TabMovingType;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "tab_component")
@Entity
public class TabComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tab_component_id")
    private long id;

    @Column(name = "sticky_yn")
    @Enumerated(EnumType.STRING)
    private Yn stickyYn;

    @Column(name = "tab_moving_type")
    @Enumerated(EnumType.STRING)
    private TabMovingType tabMovingType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "COMPONENT_ID")
    private Component component;

    @Column(name = "display_order")
    private int displayOrder;


}
