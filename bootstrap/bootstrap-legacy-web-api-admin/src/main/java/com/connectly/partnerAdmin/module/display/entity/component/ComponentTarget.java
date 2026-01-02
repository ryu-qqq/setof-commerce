package com.connectly.partnerAdmin.module.display.entity.component;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.enums.SortType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "component_target")
@Entity
public class ComponentTarget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_target_id")
    private long id;

    @Column(name = "sort_type")
    @Enumerated(EnumType.STRING)
    private SortType sortType;

    @Column(name = "tab_id")
    private long tabId;

    @Column(name = "component_id")
    private long componentId;

    @OneToMany(mappedBy = "componentTarget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComponentItem> componentItems = new ArrayList<>();


    public void delete(){
        this.setDeleteYn(Yn.Y);
        if(!this.componentItems.isEmpty()){
            componentItems.forEach(componentItem -> componentItem.setDeleteYn(Yn.Y));
        }
    }


}
