package com.setof.connectly.module.order.entity.snapshot.notice.embedded;

import com.setof.connectly.module.product.entity.notice.embedded.NoticeDetail;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SnapShotNotice {

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

    @Embedded private NoticeDetail noticeDetail;
}
