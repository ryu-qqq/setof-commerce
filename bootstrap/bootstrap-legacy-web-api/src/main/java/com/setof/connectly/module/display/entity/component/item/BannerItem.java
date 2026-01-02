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
@Table(name = "banner_item")
@Entity
public class BannerItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_item_id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "link_url")
    private String linkUrl;

    @Embedded private DisplayPeriod displayPeriod;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "display_yn")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "banner_id")
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
