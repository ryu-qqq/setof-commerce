package com.connectly.partnerAdmin.module.order.entity.snapshot.notice.embedded;

import com.connectly.partnerAdmin.module.product.entity.notice.ProductNotice;
import com.connectly.partnerAdmin.module.product.entity.notice.embedded.NoticeDetail;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Builder
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SnapShotNotice {

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;
    @Embedded
    private NoticeDetail noticeDetail;


    public SnapShotNotice(ProductNotice productNotice) {
        this.productGroupId = productNotice.getId();
        this.noticeDetail = productNotice.getNoticeDetail();
    }
}
