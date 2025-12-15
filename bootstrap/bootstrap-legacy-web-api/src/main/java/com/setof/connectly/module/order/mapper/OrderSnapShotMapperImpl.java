package com.setof.connectly.module.order.mapper;

import com.setof.connectly.module.order.dto.snapshot.OrderProductSnapShotQueryDto;
import com.setof.connectly.module.order.dto.snapshot.SnapShotQueryDto;
import com.setof.connectly.module.order.entity.snapshot.delivery.OrderSnapShotProductDelivery;
import com.setof.connectly.module.order.entity.snapshot.delivery.embedded.SnapShotProductDelivery;
import com.setof.connectly.module.order.entity.snapshot.group.OrderSnapShotProduct;
import com.setof.connectly.module.order.entity.snapshot.group.OrderSnapShotProductGroup;
import com.setof.connectly.module.order.entity.snapshot.group.embedded.SnapShotProduct;
import com.setof.connectly.module.order.entity.snapshot.group.embedded.SnapShotProductGroup;
import com.setof.connectly.module.order.entity.snapshot.image.OrderSnapShotProductGroupDetailDescription;
import com.setof.connectly.module.order.entity.snapshot.image.OrderSnapShotProductGroupImage;
import com.setof.connectly.module.order.entity.snapshot.image.embedded.SnapShotProductGroupDetailDescription;
import com.setof.connectly.module.order.entity.snapshot.image.embedded.SnapShotProductGroupImage;
import com.setof.connectly.module.order.entity.snapshot.notice.OrderSnapShotProductNotice;
import com.setof.connectly.module.order.entity.snapshot.notice.embedded.SnapShotNotice;
import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotOptionDetail;
import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotOptionGroup;
import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotProductOption;
import com.setof.connectly.module.order.entity.snapshot.option.embedded.SnapShotOptionDetail;
import com.setof.connectly.module.order.entity.snapshot.option.embedded.SnapShotOptionGroup;
import com.setof.connectly.module.order.entity.snapshot.option.embedded.SnapShotProductOption;
import com.setof.connectly.module.order.entity.snapshot.stock.OrderSnapShotProductStock;
import com.setof.connectly.module.product.entity.delivery.embedded.DeliveryNotice;
import com.setof.connectly.module.product.entity.delivery.embedded.RefundNotice;
import com.setof.connectly.module.product.entity.group.embedded.ProductGroupDetails;
import com.setof.connectly.module.product.entity.image.embedded.ImageDetail;
import com.setof.connectly.module.product.entity.notice.embedded.NoticeDetail;
import com.setof.connectly.module.product.entity.option.OptionDetail;
import com.setof.connectly.module.product.entity.option.OptionGroup;
import com.setof.connectly.module.product.entity.option.ProductOption;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderSnapShotMapperImpl implements OrderSnapShotMapper {

    @Override
    public SnapShotQueryDto toSnapShots(List<OrderProductSnapShotQueryDto> orderProducts) {

        Set<OrderSnapShotProductGroup> orderSnapShotProductGroups =
                orderProducts.stream()
                        .map(
                                op ->
                                        toSnapShotProductGroup(
                                                op.getOrderId(),
                                                op.getProductGroupId(),
                                                op.getProductGroupDetails(),
                                                op.getCommissionRate()))
                        .collect(Collectors.toSet());

        Set<OrderSnapShotProductDelivery> orderSnapShotProductDeliveries =
                orderProducts.stream()
                        .map(
                                op ->
                                        toSnapShotProductDelivery(
                                                op.getOrderId(),
                                                op.getProductGroupId(),
                                                op.getDeliveryNotice(),
                                                op.getRefundNotice()))
                        .collect(Collectors.toSet());

        Set<OrderSnapShotProductNotice> orderSnapShotProductNotices =
                orderProducts.stream()
                        .map(
                                op ->
                                        toSnapShotProductNotice(
                                                op.getOrderId(),
                                                op.getProductGroupId(),
                                                op.getNoticeDetail()))
                        .collect(Collectors.toSet());

        Set<OrderSnapShotProduct> orderSnapShotProducts =
                orderProducts.stream()
                        .map(
                                op ->
                                        toSnapShotProduct(
                                                op.getOrderId(),
                                                op.getProductId(),
                                                op.getProductGroupId()))
                        .collect(Collectors.toSet());

        Set<OrderSnapShotProductStock> orderSnapShotProductStocks =
                orderProducts.stream()
                        .map(
                                op ->
                                        toSnapShotProductStock(
                                                op.getOrderId(),
                                                op.getProductStockId(),
                                                op.getProductId(),
                                                op.getQuantity()))
                        .collect(Collectors.toSet());

        Set<OrderSnapShotProductGroupImage> orderSnapShotProductGroupImages =
                orderProducts.stream()
                        .flatMap(
                                op ->
                                        toSnapShotProductGroupImage(
                                                op.getOrderId(),
                                                op.getProductGroupId(),
                                                op.getImageGroupDetails())
                                                .stream())
                        .collect(Collectors.toSet());

        Set<OrderSnapShotProductGroupDetailDescription>
                orderSnapShotProductGroupDetailDescriptions =
                        orderProducts.stream()
                                .map(
                                        op ->
                                                toSnapShotProductGroupDetailDescription(
                                                        op.getOrderId(),
                                                        op.getProductGroupId(),
                                                        op.getImageDetailDescription()))
                                .collect(Collectors.toSet());

        Set<OrderSnapShotOptionGroup> orderSnapShotOptionGroups =
                orderProducts.stream()
                        .filter(op -> !op.getSnapShotProductOption().isEmpty())
                        .flatMap(
                                op ->
                                        toSnapShotOptionGroups(
                                                op.getOrderId(), op.getSnapShotOptionGroup())
                                                .stream())
                        .collect(Collectors.toSet());

        Set<OrderSnapShotOptionDetail> orderSnapShotOptionDetails =
                orderProducts.stream()
                        .filter(op -> !op.getSnapShotProductOption().isEmpty())
                        .flatMap(
                                op ->
                                        toSnapShotOptionDetails(
                                                op.getOrderId(), op.getSnapShotOptionDetail())
                                                .stream())
                        .collect(Collectors.toSet());

        Set<OrderSnapShotProductOption> orderSnapShotProductOptions =
                orderProducts.stream()
                        .filter(op -> !op.getSnapShotProductOption().isEmpty())
                        .flatMap(
                                op ->
                                        toSnapShotProductOptions(
                                                op.getOrderId(), op.getSnapShotProductOption())
                                                .stream())
                        .collect(Collectors.toSet());

        return SnapShotQueryDto.builder()
                .orderSnapShotProductGroups(orderSnapShotProductGroups)
                .orderSnapShotProductNotices(orderSnapShotProductNotices)
                .orderSnapShotProductDeliveries(orderSnapShotProductDeliveries)
                .orderSnapShotProductGroupImages(orderSnapShotProductGroupImages)
                .orderSnapShotProductGroupDetailDescriptions(
                        orderSnapShotProductGroupDetailDescriptions)
                .orderSnapShotProducts(orderSnapShotProducts)
                .orderSnapShotProductStocks(orderSnapShotProductStocks)
                .orderSnapShotProductOptions(orderSnapShotProductOptions)
                .orderSnapShotOptionGroups(orderSnapShotOptionGroups)
                .orderSnapShotOptionDetails(orderSnapShotOptionDetails)
                .build();
    }

    public OrderSnapShotProductGroup toSnapShotProductGroup(
            long orderId,
            long productGroupId,
            ProductGroupDetails productGroupDetails,
            double commissionRate) {
        return OrderSnapShotProductGroup.builder()
                .orderId(orderId)
                .snapShotProductGroup(
                        new SnapShotProductGroup(
                                productGroupId, productGroupDetails, commissionRate))
                .build();
    }

    public OrderSnapShotProduct toSnapShotProduct(
            long orderId, long productId, long productGroupId) {
        return OrderSnapShotProduct.builder()
                .orderId(orderId)
                .snapShotProduct(new SnapShotProduct(productId, productGroupId))
                .build();
    }

    public OrderSnapShotProductDelivery toSnapShotProductDelivery(
            long orderId,
            long productGroupId,
            DeliveryNotice deliveryNotice,
            RefundNotice refundNotice) {
        return OrderSnapShotProductDelivery.builder()
                .orderId(orderId)
                .snapShotProductDelivery(
                        new SnapShotProductDelivery(productGroupId, deliveryNotice, refundNotice))
                .build();
    }

    public Set<OrderSnapShotProductGroupImage> toSnapShotProductGroupImage(
            long orderId, long productGroupId, Set<ImageDetail> imageDetails) {
        return imageDetails.stream()
                .map(
                        imageDetail -> {
                            return OrderSnapShotProductGroupImage.builder()
                                    .orderId(orderId)
                                    .snapShotProductGroupImage(
                                            new SnapShotProductGroupImage(
                                                    productGroupId, imageDetail))
                                    .build();
                        })
                .collect(Collectors.toSet());
    }

    public OrderSnapShotProductGroupDetailDescription toSnapShotProductGroupDetailDescription(
            long orderId, long productGroupId, ImageDetail imageDetail) {
        return OrderSnapShotProductGroupDetailDescription.builder()
                .orderId(orderId)
                .snapShotProductGroupDetailDescription(
                        new SnapShotProductGroupDetailDescription(productGroupId, imageDetail))
                .build();
    }

    public OrderSnapShotProductNotice toSnapShotProductNotice(
            long orderId, long productGroupId, NoticeDetail noticeDetail) {
        return OrderSnapShotProductNotice.builder()
                .orderId(orderId)
                .snapShotNotice(new SnapShotNotice(productGroupId, noticeDetail))
                .build();
    }

    public OrderSnapShotProductStock toSnapShotProductStock(
            long orderId, long productStockId, long productId, int qty) {
        return OrderSnapShotProductStock.builder()
                .orderId(orderId)
                .productStockId(productStockId)
                .productId(productId)
                .stockQuantity(qty)
                .build();
    }

    public Set<OrderSnapShotOptionGroup> toSnapShotOptionGroups(
            long orderId, Set<OptionGroup> snapShotOptionGroup) {
        return snapShotOptionGroup.stream()
                .map(
                        optionGroup -> {
                            return OrderSnapShotOptionGroup.builder()
                                    .orderId(orderId)
                                    .snapShotOptionGroup(
                                            new SnapShotOptionGroup(
                                                    optionGroup.getId(),
                                                    optionGroup.getOptionName()))
                                    .build();
                        })
                .collect(Collectors.toSet());
    }

    public Set<OrderSnapShotOptionDetail> toSnapShotOptionDetails(
            long orderId, Set<OptionDetail> snapShotOptionDetail) {
        return snapShotOptionDetail.stream()
                .map(
                        optionDetail -> {
                            return OrderSnapShotOptionDetail.builder()
                                    .orderId(orderId)
                                    .snapShotOptionDetail(
                                            new SnapShotOptionDetail(
                                                    optionDetail.getId(),
                                                    optionDetail.getOptionGroup().getId(),
                                                    optionDetail.getOptionValue()))
                                    .build();
                        })
                .collect(Collectors.toSet());
    }

    public Set<OrderSnapShotProductOption> toSnapShotProductOptions(
            long orderId, Set<ProductOption> snapShotProductOption) {
        return snapShotProductOption.stream()
                .map(
                        po -> {
                            return OrderSnapShotProductOption.builder()
                                    .orderId(orderId)
                                    .snapShotProductOption(
                                            new SnapShotProductOption(
                                                    po.getId(),
                                                    po.getProduct().getId(),
                                                    po.getOptionGroup().getId(),
                                                    po.getOptionDetail().getId(),
                                                    po.getAdditionalPrice()))
                                    .build();
                        })
                .collect(Collectors.toSet());
    }
}
