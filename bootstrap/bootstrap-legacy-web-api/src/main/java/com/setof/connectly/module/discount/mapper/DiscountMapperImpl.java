package com.setof.connectly.module.discount.mapper;

import com.setof.connectly.module.discount.dto.DiscountCalculateDto;
import com.setof.connectly.module.discount.dto.DiscountRedisFetchDto;
import com.setof.connectly.module.discount.dto.DiscountUseDto;
import com.setof.connectly.module.discount.entity.history.DiscountUseHistory;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class DiscountMapperImpl implements DiscountMapper {

    @Override
    public DiscountRedisFetchDto toDiscountRedisFetchDto(DiscountCalculateDto dto) {
        return DiscountRedisFetchDto.builder()
                .productGroupId(dto.getProductGroupId())
                .sellerId(dto.getSellerId())
                .build();
    }

    @Override
    public DiscountUseHistory toDiscountUseHistory(
            long discountPolicyId, DiscountUseDto discountUse) {
        return DiscountUseHistory.builder()
                .discountPolicyId(discountPolicyId)
                .paymentId(discountUse.getPaymentId())
                .orderId(discountUse.getOrderId())
                .productGroupId(discountUse.getProductGroupId())
                .useDate(LocalDateTime.now())
                .userId(discountUse.getUserId())
                .name(discountUse.getName())
                .build();
    }
}
