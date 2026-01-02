package com.connectly.partnerAdmin.module.external.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.external.enums.SiteType;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "SELLER_SITE_RELATION")
@Getter
@Entity
@Builder
public class SellerSiteRelation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SELLER_SITE_RELATION_ID")
    private long id;

    @Column(name = "SELLER_ID", nullable = false)
    private long sellerId;

    @Column(name = "SITE_ID", nullable = false)
    private long siteId;

    @Column(name = "SITE_TYPE", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SiteType siteType;

    @Column(name = "ACTIVE_YN", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private Yn activeYn;

    public void updateActiveYn(Yn activeYn){
        this.activeYn = activeYn;
    }
}
