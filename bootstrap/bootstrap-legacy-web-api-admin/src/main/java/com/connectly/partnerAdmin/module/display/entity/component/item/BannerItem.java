package com.connectly.partnerAdmin.module.display.entity.component.item;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.display.entity.embedded.ImageSize;
import jakarta.persistence.*;
import lombok.*;



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
    @Embedded
    private DisplayPeriod displayPeriod;

    @Column(name = "DISPLAY_ORDER")
    private int displayOrder;

    @Column(name = "DISPLAY_YN")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "BANNER_ID")
    private long bannerId;

    @Embedded
    private ImageSize imageSize;


    public void update(BannerItem bannerItem){
        title = bannerItem.getTitle();
        imageUrl = bannerItem.getImageUrl();
        linkUrl = bannerItem.getLinkUrl();
        displayPeriod = bannerItem.getDisplayPeriod();
        displayOrder = bannerItem.getDisplayOrder();
        displayYn = bannerItem.getDisplayYn();
        imageSize = bannerItem.getImageSize();
    }

    public void delete(){
        setDeleteYn(Yn.Y);
    }
}
