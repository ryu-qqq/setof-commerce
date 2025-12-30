package com.connectly.partnerAdmin.module.product.entity.notice;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.notice.embedded.NoticeDetail;
import lombok.*;
import jakarta.persistence.*;




@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "PRODUCT_NOTICE")
@Entity
public class ProductNotice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_GROUP_ID")
    private long id;

    @Setter
    @Embedded
    private NoticeDetail noticeDetail;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "PRODUCT_GROUP_ID")
    private ProductGroup productGroup;

    public void setProductGroup(ProductGroup productGroup) {
        if (this.productGroup != null && this.productGroup.equals(productGroup)) {
            return;
        }
        this.productGroup = productGroup;
        if (productGroup != null && !productGroup.getProductNotice().equals(this)) {
            productGroup.setProductNotice(this);
        }
    }


}
