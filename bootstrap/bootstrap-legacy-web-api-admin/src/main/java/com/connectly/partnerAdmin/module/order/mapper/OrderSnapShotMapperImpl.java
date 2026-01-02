package com.connectly.partnerAdmin.module.order.mapper;


import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotProductGroupQueryDto;
import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotProductQueryDto;
import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotQueryDto;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.entity.snapshot.delivery.OrderSnapShotProductDelivery;
import com.connectly.partnerAdmin.module.order.entity.snapshot.delivery.embedded.SnapShotProductDelivery;
import com.connectly.partnerAdmin.module.order.entity.snapshot.group.OrderSnapShotProductGroup;
import com.connectly.partnerAdmin.module.order.entity.snapshot.group.embedded.SnapShotProduct;
import com.connectly.partnerAdmin.module.order.entity.snapshot.group.embedded.SnapShotProductGroup;
import com.connectly.partnerAdmin.module.order.entity.snapshot.image.OrderSnapShotProductGroupDetailDescription;
import com.connectly.partnerAdmin.module.order.entity.snapshot.image.OrderSnapShotProductGroupImage;
import com.connectly.partnerAdmin.module.order.entity.snapshot.image.embedded.SnapShotProductGroupDetailDescription;
import com.connectly.partnerAdmin.module.order.entity.snapshot.image.embedded.SnapShotProductGroupImage;
import com.connectly.partnerAdmin.module.order.entity.snapshot.notice.OrderSnapShotProductNotice;
import com.connectly.partnerAdmin.module.order.entity.snapshot.notice.embedded.SnapShotNotice;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.OrderSnapShotOptionDetail;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.OrderSnapShotOptionGroup;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.OrderSnapShotProductOption;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.embedded.SnapShotOptionDetail;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.embedded.SnapShotOptionGroup;
import com.connectly.partnerAdmin.module.order.entity.snapshot.option.embedded.SnapShotProductOption;
import com.connectly.partnerAdmin.module.order.entity.snapshot.stock.OrderSnapShotProduct;
import com.connectly.partnerAdmin.module.order.entity.snapshot.stock.OrderSnapShotProductStock;
import com.connectly.partnerAdmin.module.product.entity.delivery.ProductDelivery;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupDetailDescription;
import com.connectly.partnerAdmin.module.product.entity.image.ProductGroupImage;
import com.connectly.partnerAdmin.module.product.entity.notice.ProductNotice;
import com.connectly.partnerAdmin.module.product.entity.option.OptionDetail;
import com.connectly.partnerAdmin.module.product.entity.option.OptionGroup;
import com.connectly.partnerAdmin.module.product.entity.option.ProductOption;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.connectly.partnerAdmin.module.product.entity.stock.ProductStock;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderSnapShotMapperImpl implements OrderSnapShotMapper{

    @Override
    public void setSnapShot(Order order, OrderSnapShotQueryDto orderSnapShotQueryDto) {

        OrderSnapShotProductGroupQueryDto orderSnapShotProductGroup = orderSnapShotQueryDto.getOrderSnapShotProductGroup();
        OrderSnapShotProductQueryDto orderSnapShotProduct = orderSnapShotQueryDto.getOrderSnapShotProduct();

        ProductGroup productGroup = orderSnapShotProductGroup.getProductGroup();

        OrderSnapShotProductGroup snapShotProductGroup = toSnapShotProductGroup(productGroup);
        OrderSnapShotProductDelivery snapShotProductDelivery = toSnapShotProductDelivery(orderSnapShotProductGroup.getProductDelivery());
        OrderSnapShotProductNotice snapShotProductNotice = toSnapShotProductNotice(orderSnapShotProductGroup.getProductNotice());
        Set<OrderSnapShotProductGroupImage> snapShotProductGroupImage = toSnapShotProductGroupImage(productGroup.getId(), orderSnapShotProductGroup.getProductGroupImageList());
        OrderSnapShotProductGroupDetailDescription snapShotProductGroupDetailDescription = toSnapShotProductGroupDetailDescription(orderSnapShotProductGroup.getProductGroupDetailDescription());

        OrderSnapShotProduct snapShotProduct = toSnapShotProduct(orderSnapShotProduct.getProduct());
        OrderSnapShotProductStock snapShotProductStock = toSnapShotProductStock(orderSnapShotProduct.getProductStock());
        Set<OrderSnapShotProductOption> snapShotProductOptions = toSnapShotProductOptions(orderSnapShotProduct.getProductOptions());
        Set<OrderSnapShotOptionGroup> snapShotOptionGroups = toSnapShotOptionGroups(orderSnapShotProduct.getProductOptions());
        Set<OrderSnapShotOptionDetail> snapShotOptionDetails = toSnapShotOptionDetails(orderSnapShotProduct.getProductOptions());


        order.setOrderSnapShotProductGroup(snapShotProductGroup);
        order.setOrderSnapShotProductDelivery(snapShotProductDelivery);
        order.setOrderSnapShotProductNotice(snapShotProductNotice);
        order.setProductGroupDetailDescription(snapShotProductGroupDetailDescription);
        order.setOrderSnapShotProduct(snapShotProduct);
        order.setOrderSnapShotProductStock(snapShotProductStock);

        snapShotProductGroupImage.forEach(order::addOrderSnapShotProductGroupImage);
        snapShotProductOptions.forEach(order::addOrderSnapShotProductOption);
        snapShotOptionGroups.forEach(order::addOrderSnapShotOptionGroup);
        snapShotOptionDetails.forEach(order::addOrderSnapShotOptionDetail);

    }

    public OrderSnapShotProductGroup toSnapShotProductGroup(ProductGroup productGroup){
        return OrderSnapShotProductGroup.builder()
                .snapShotProductGroup(new SnapShotProductGroup(productGroup))
                .build();
    }


    public OrderSnapShotProduct toSnapShotProduct(Product product){
        return OrderSnapShotProduct.builder()
                .snapShotProduct(new SnapShotProduct(product))
                .build();
    }

    public OrderSnapShotProductDelivery toSnapShotProductDelivery(ProductDelivery productDelivery){
        return OrderSnapShotProductDelivery.builder()
                .snapShotProductDelivery(new SnapShotProductDelivery(productDelivery))
                .build();
    }


    public Set<OrderSnapShotProductGroupImage> toSnapShotProductGroupImage(long productGroupId, Set<ProductGroupImage> productGroupImageList){
        return productGroupImageList.stream().map(image ->
                OrderSnapShotProductGroupImage.builder()
                .snapShotProductGroupImage(new SnapShotProductGroupImage(productGroupId, image.getImageDetail()))
                .build())
                .collect(Collectors.toSet());
    }



    public OrderSnapShotProductGroupDetailDescription toSnapShotProductGroupDetailDescription(ProductGroupDetailDescription productGroupDetailDescription){
        return OrderSnapShotProductGroupDetailDescription.builder()
                .snapShotProductGroupDetailDescription(new SnapShotProductGroupDetailDescription(productGroupDetailDescription))
                .build();
    }

    public OrderSnapShotProductNotice toSnapShotProductNotice(ProductNotice productNotice){
        return OrderSnapShotProductNotice.builder()
                .snapShotNotice(new SnapShotNotice(productNotice))
                .build();
    }

    public OrderSnapShotProductStock toSnapShotProductStock(ProductStock productStock){
        return OrderSnapShotProductStock.builder()
                .productId(productStock.getId())
                .productStockId(productStock.getId())
                .stockQuantity(productStock.getStockQuantity())
                .build();
    }

    public Set<OrderSnapShotOptionGroup> toSnapShotOptionGroups(Set<ProductOption> productOptions){
        return productOptions.stream().map( productOption -> {
            OptionGroup optionGroup = productOption.getOptionGroup();

            return OrderSnapShotOptionGroup.builder()
                            .snapShotOptionGroup(new SnapShotOptionGroup(optionGroup.getId(), optionGroup.getOptionName()))
                            .build();
                }

        ).collect(Collectors.toSet());
    }

    public Set<OrderSnapShotOptionDetail> toSnapShotOptionDetails(Set<ProductOption> productOptions){
        return productOptions.stream().map( productOption -> {
            OptionDetail optionDetail = productOption.getOptionDetail();
            return OrderSnapShotOptionDetail.builder()
                    .snapShotOptionDetail(new SnapShotOptionDetail(optionDetail.getId(), optionDetail.getOptionGroup().getId(), optionDetail.getOptionValue()))
                    .build();
                }

        ).collect(Collectors.toSet());
    }


    public Set<OrderSnapShotProductOption> toSnapShotProductOptions(Set<ProductOption> productOptions){
        return productOptions.stream().map( po -> {
                    return OrderSnapShotProductOption.builder()
                            .snapShotProductOption(new SnapShotProductOption(po.getId(), po.getProduct().getId(), po.getOptionGroup().getId(), po.getOptionDetail().getId(), po.getAdditionalPrice()))
                            .build();
                }

        ).collect(Collectors.toSet());
    }

}
