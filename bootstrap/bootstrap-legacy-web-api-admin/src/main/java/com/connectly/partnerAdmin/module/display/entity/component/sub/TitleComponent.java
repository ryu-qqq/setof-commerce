package com.connectly.partnerAdmin.module.display.entity.component.sub;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import com.connectly.partnerAdmin.module.display.entity.embedded.TitleDetails;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "title_component")
@Entity
public class TitleComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "title_component_id")
    private long id;

    @Embedded
    private TitleDetails titleDetails;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "COMPONENT_ID")
    private Component component;


}
