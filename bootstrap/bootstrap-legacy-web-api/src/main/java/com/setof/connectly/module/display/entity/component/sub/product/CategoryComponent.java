package com.setof.connectly.module.display.entity.component.sub.product;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.display.entity.component.Component;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "category_component")
@Entity
public class CategoryComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_component_id")
    private Long id;

    private long categoryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "COMPONENT_ID")
    private Component component;
}
