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
@Table(name = "view_extension")
@Entity
public class ViewExtension extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_extension_id")
    private long id;
    @Embedded
    private ViewExtensionDetails viewExtensionDetails;


    public ViewExtension(long id, ViewExtensionDetails viewExtensionDetails) {
        this.id = id;
        this.viewExtensionDetails = viewExtensionDetails;
    }
}
