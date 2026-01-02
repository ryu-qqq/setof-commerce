package com.connectly.partnerAdmin.module.external.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.external.enums.SiteType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "SITE")
@Getter
@Entity
public class Site extends BaseEntity {

    @Id
    @Column(name = "SITE_ID")
    private long id;
    @Column(name = "SITE_NAME", nullable = false, length = 20)
    private String siteName;

    @Column(name = "DESCRIPTION", nullable = true, length = 100)
    private String description;

    @Column(name = "BASE_URL", nullable = true, length = 255)
    private String baseUrl;

    //신규 추가 될 경우 새로운엔티티로 생성
    public SellerSiteRelation toSellerSiteRelation(long sellerId) {
        return SellerSiteRelation.builder()
                .siteType(SiteType.valueOf(description))
                .activeYn(Yn.Y)
                .siteId(id)
                .sellerId(sellerId)
                .build();
    }

}
