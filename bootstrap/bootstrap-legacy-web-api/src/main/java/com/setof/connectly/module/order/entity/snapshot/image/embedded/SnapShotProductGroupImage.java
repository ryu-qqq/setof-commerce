package com.setof.connectly.module.order.entity.snapshot.image.embedded;

import com.setof.connectly.module.product.entity.image.embedded.ImageDetail;
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
public class SnapShotProductGroupImage {

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

    @Embedded private ImageDetail imageDetail;
}
