package com.setof.connectly.module.order.dto.snapshot;

import com.setof.connectly.module.order.entity.snapshot.delivery.OrderSnapShotProductDelivery;
import com.setof.connectly.module.order.entity.snapshot.group.OrderSnapShotProduct;
import com.setof.connectly.module.order.entity.snapshot.group.OrderSnapShotProductGroup;
import com.setof.connectly.module.order.entity.snapshot.image.OrderSnapShotProductGroupDetailDescription;
import com.setof.connectly.module.order.entity.snapshot.image.OrderSnapShotProductGroupImage;
import com.setof.connectly.module.order.entity.snapshot.notice.OrderSnapShotProductNotice;
import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotOptionDetail;
import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotOptionGroup;
import com.setof.connectly.module.order.entity.snapshot.option.OrderSnapShotProductOption;
import com.setof.connectly.module.order.entity.snapshot.stock.OrderSnapShotProductStock;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class SnapShotQueryDto {

    Set<OrderSnapShotProductGroup> orderSnapShotProductGroups = new HashSet<>();
    Set<OrderSnapShotProduct> orderSnapShotProducts = new HashSet<>();
    Set<OrderSnapShotProductDelivery> orderSnapShotProductDeliveries = new HashSet<>();
    Set<OrderSnapShotProductGroupImage> orderSnapShotProductGroupImages = new HashSet<>();
    Set<OrderSnapShotProductGroupDetailDescription> orderSnapShotProductGroupDetailDescriptions =
            new HashSet<>();
    Set<OrderSnapShotProductNotice> orderSnapShotProductNotices = new HashSet<>();
    Set<OrderSnapShotProductStock> orderSnapShotProductStocks = new HashSet<>();
    Set<OrderSnapShotOptionGroup> orderSnapShotOptionGroups = new HashSet<>();
    Set<OrderSnapShotOptionDetail> orderSnapShotOptionDetails = new HashSet<>();
    Set<OrderSnapShotProductOption> orderSnapShotProductOptions = new HashSet<>();

    public Set<? extends OrderSnapShot> getSnapShot(SnapShotEnum snapShotEnum) {
        switch (snapShotEnum) {
            case PRODUCT_GROUP:
                return orderSnapShotProductGroups;
            case PRODUCT_DELIVERY:
                return orderSnapShotProductDeliveries;
            case PRODUCT_NOTICE:
                return orderSnapShotProductNotices;
            case PRODUCT_IMAGE:
                return orderSnapShotProductGroupImages;
            case PRODUCT_DESCRIPTION:
                return orderSnapShotProductGroupDetailDescriptions;
            case PRODUCT:
                return orderSnapShotProducts;
            case STOCK:
                return orderSnapShotProductStocks;
            case PRODUCT_OPTION:
                return orderSnapShotProductOptions;
            case OPTION_GROUP:
                return orderSnapShotOptionGroups;
            case OPTION_DETAIL:
                return orderSnapShotOptionDetails;

            default:
                throw new IllegalArgumentException(
                        "지원하지 않는  SnapShotEnum 입니다. ::: " + snapShotEnum);
        }
    }
}
