package com.setof.connectly.module.display.entity.component.item;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "BANNER_ITEM")
@Entity
public class BannerItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BANNER_ITEM_ID")
    private long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "LINK_URL")
    private String linkUrl;

    @Embedded private DisplayPeriod displayPeriod;

    @Column(name = "DISPLAY_ORDER")
    private int displayOrder;

    @Column(name = "DISPLAY_YN")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "BANNER_ID")
    private long bannerId;

    public void update(BannerItem bannerItem) {
        title = bannerItem.getTitle();
        imageUrl = bannerItem.getImageUrl();
        linkUrl = bannerItem.getLinkUrl();
        displayPeriod = bannerItem.getDisplayPeriod();
        displayOrder = bannerItem.getDisplayOrder();
        displayYn = bannerItem.getDisplayYn();
    }

    public void delete() {
        setDeleteYn(Yn.Y);
    }
}
