package com.connectly.partnerAdmin.module.display.entity.component.item;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "tab")
@Entity
public class Tab extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tab_id")
    private long id;

    @Column(name = "tab_name")
    private String tabName;

    @Column(name = "display_order")
    private int displayOrder;
    @Column(name = "tab_component_id")
    private long tabComponentId;


}
