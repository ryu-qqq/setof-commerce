package com.setof.connectly.module.display.entity.component;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.display.entity.embedded.ViewExtensionDetails;
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
@Table(name = "VIEW_EXTENSION")
@Entity
public class ViewExtension extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VIEW_EXTENSION_ID")
    private long id;

    @Embedded private ViewExtensionDetails viewExtensionDetails;
}
