package com.setof.connectly.module.display.entity.component;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.display.enums.SortType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "component_target")
@Entity
public class ComponentTarget extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_target_id")
    private Long id;

    @Column(name = "sort_type")
    @Enumerated(EnumType.STRING)
    private SortType sortType;

    @Column(name = "tab_id")
    private long tabId;

    @Column(name = "component_id")
    private long componentId;

    @OneToMany(mappedBy = "componentTarget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComponentItem> componentItems = new ArrayList<>();
}
