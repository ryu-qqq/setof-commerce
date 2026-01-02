package com.connectly.partnerAdmin.module.display.entity.component.sub;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "blank_component")
@Entity
public class BlankComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blank_component_id")
    private long id;

    @Column(name = "height")
    private double height;

    @Column(name = "line_yn")
    @Enumerated(EnumType.STRING)
    private Yn lineYn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "COMPONENT_ID")
    private Component component;

}
