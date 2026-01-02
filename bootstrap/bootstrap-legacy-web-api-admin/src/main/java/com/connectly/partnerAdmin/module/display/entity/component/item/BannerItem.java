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
    @Embedded
    private DisplayPeriod displayPeriod;

    @Column(name = "display_order")
    private int displayOrder;

    @Column(name = "display_yn")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "banner_id")
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
