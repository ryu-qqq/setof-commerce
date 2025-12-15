package com.setof.connectly.module.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.order.dto.OrderProductDto;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import com.setof.connectly.module.product.dto.option.OptionDto;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewOrderProductDto extends OrderProductDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    @QueryProjection
    public ReviewOrderProductDto(
            long paymentId,
            long sellerId,
            long orderId,
            BrandDto brand,
            long productGroupId,
            String productGroupName,
            long productId,
            String sellerName,
            String productGroupMainImageUrl,
            int productQuantity,
            OrderStatus orderStatus,
            long regularPrice,
            long currentPrice,
            long orderAmount,
            Set<OptionDto> options,
            LocalDateTime paymentDate) {
        super(
                paymentId,
                sellerId,
                orderId,
                brand,
                productGroupId,
                productGroupName,
                productId,
                sellerName,
                productGroupMainImageUrl,
                productQuantity,
                orderStatus,
                regularPrice,
                currentPrice,
                orderAmount,
                options);
        this.paymentDate = paymentDate;
    }

    @QueryProjection
    public ReviewOrderProductDto(long orderId, Set<OptionDto> options, LocalDateTime paymentDate) {
        super(orderId, options);
        this.paymentDate = paymentDate;
    }
}
