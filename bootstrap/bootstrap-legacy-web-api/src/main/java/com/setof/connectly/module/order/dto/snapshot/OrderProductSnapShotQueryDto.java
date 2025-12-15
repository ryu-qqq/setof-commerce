package com.setof.connectly.module.order.dto.snapshot;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.entity.delivery.embedded.DeliveryNotice;
import com.setof.connectly.module.product.entity.delivery.embedded.RefundNotice;
import com.setof.connectly.module.product.entity.group.embedded.ProductGroupDetails;
import com.setof.connectly.module.product.entity.image.embedded.ImageDetail;
import com.setof.connectly.module.product.entity.notice.embedded.NoticeDetail;
import com.setof.connectly.module.product.entity.option.OptionDetail;
import com.setof.connectly.module.product.entity.option.OptionGroup;
import com.setof.connectly.module.product.entity.option.ProductOption;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProductSnapShotQueryDto {
    private long userId;
    private String name;
    private long orderId;
    private long orderAmount;
    private int quantity;
    private long productId;
    private long productStockId;
    private long productGroupId;
    private double commissionRate;
    private ProductGroupDetails productGroupDetails;
    private DeliveryNotice deliveryNotice;
    private RefundNotice refundNotice;
    private NoticeDetail noticeDetail;
    private Set<ImageDetail> imageGroupDetails;
    private ImageDetail imageDetailDescription;
    private Set<OptionGroup> snapShotOptionGroup;
    private Set<OptionDetail> snapShotOptionDetail;
    private Set<ProductOption> snapShotProductOption;

    @QueryProjection
    public OrderProductSnapShotQueryDto(
            long userId,
            String name,
            long orderId,
            long orderAmount,
            int quantity,
            long productId,
            long productStockId,
            long productGroupId,
            double commissionRate,
            ProductGroupDetails productGroupDetails,
            DeliveryNotice deliveryNotice,
            RefundNotice refundNotice,
            NoticeDetail noticeDetail,
            Set<ImageDetail> imageGroupDetails,
            ImageDetail imageDetailDescription,
            Set<OptionGroup> snapShotOptionGroup,
            Set<OptionDetail> snapShotOptionDetail,
            Set<ProductOption> snapShotProductOption) {
        this.userId = userId;
        this.name = name;
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.quantity = quantity;
        this.productId = productId;
        this.productStockId = productStockId;
        this.productGroupId = productGroupId;
        this.commissionRate = commissionRate;
        this.productGroupDetails = productGroupDetails;
        this.deliveryNotice = deliveryNotice;
        this.refundNotice = refundNotice;
        this.noticeDetail = noticeDetail;
        this.imageGroupDetails = imageGroupDetails;
        this.imageDetailDescription = imageDetailDescription;
        this.snapShotOptionGroup = snapShotOptionGroup;
        this.snapShotOptionDetail = snapShotOptionDetail;
        this.snapShotProductOption = snapShotProductOption;
    }
}
