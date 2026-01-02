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
@Table(name = "BLANK_COMPONENT")
@Entity
public class BlankComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BLANK_COMPONENT_ID")
    private long id;

    @Column(name = "HEIGHT")
    private double height;

    @Column(name = "LINE_YN")
    @Enumerated(EnumType.STRING)
    private Yn lineYn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "COMPONENT_ID")
    private Component component;

}
