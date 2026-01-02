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
@Table(name = "view_extension")
@Entity
public class ViewExtension extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_extension_id")
    private Long id;

    @Embedded private ViewExtensionDetails viewExtensionDetails;
}
