package com.setof.connectly.module.product.dto.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import com.setof.connectly.module.product.dto.delivery.RefundNoticeDto;
import com.setof.connectly.module.product.entity.delivery.embedded.DeliveryNotice;
import com.setof.connectly.module.product.entity.group.embedded.Price;
import com.setof.connectly.module.product.entity.group.embedded.ProductStatus;
import com.setof.connectly.module.product.enums.option.OptionType;
import java.time.LocalDateTime;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductGroupDto {

    private long productGroupId;
    private String productGroupName;
    private long sellerId;
    private String sellerName;
    private BrandDto brand;
    private long categoryId;
    private Price price;
    private OptionType optionType;

    private ClothesDetailDto clothesDetailInfo;
    private DeliveryNotice deliveryNotice;
    private RefundNoticeDto refundNotice;
    private String productGroupMainImageUrl;
    private ProductStatus productStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    private String insertOperator;
    private String updateOperator;
    private double averageRating;
    private long reviewCount;

    public void setPrice(Price price) {
        this.price = price;
    }
}
