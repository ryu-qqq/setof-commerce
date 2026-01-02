package com.connectly.partnerAdmin.module.display.entity.component.sub.product;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.display.entity.component.Component;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "product_component")
@Entity
public class ProductComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_component_id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "COMPONENT_ID")
    private Component component;


}
