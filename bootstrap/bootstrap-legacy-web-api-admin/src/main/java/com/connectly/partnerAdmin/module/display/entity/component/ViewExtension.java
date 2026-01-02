package com.connectly.partnerAdmin.module.display.entity.component;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.display.entity.embedded.ViewExtensionDetails;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "VIEW_EXTENSION")
@Entity
public class ViewExtension extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VIEW_EXTENSION_ID")
    private long id;
    @Embedded
    private ViewExtensionDetails viewExtensionDetails;


    public ViewExtension(long id, ViewExtensionDetails viewExtensionDetails) {
        this.id = id;
        this.viewExtensionDetails = viewExtensionDetails;
    }
}
