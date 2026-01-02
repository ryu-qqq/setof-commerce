package com.setof.connectly.module.display.entity.component.sub;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.display.entity.embedded.TitleDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "title_component")
@Entity
public class TitleComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "title_component_id")
    private long id;

    @Embedded private TitleDetails titleDetails;
    private long componentId;
}
