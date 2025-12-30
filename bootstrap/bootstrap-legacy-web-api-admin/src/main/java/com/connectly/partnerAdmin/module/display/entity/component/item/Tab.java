package com.connectly.partnerAdmin.module.display.entity.component.item;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "TAB")
@Entity
public class Tab extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAB_ID")
    private long id;

    @Column(name = "TAB_NAME")
    private String tabName;

    @Column(name = "DISPLAY_ORDER")
    private int displayOrder;
    @Column(name = "TAB_COMPONENT_ID")
    private long tabComponentId;


}
