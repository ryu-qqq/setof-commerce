package com.connectly.partnerAdmin.module.discount.dto;

import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;
import com.connectly.partnerAdmin.module.discount.entity.embedded.DiscountDetails;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiscountPolicyResponseDto {

    private long discountPolicyId;
    private DiscountDetails discountDetails;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;
    private String insertOperator;
    private String updateOperator;

    @Builder
    @QueryProjection
    public DiscountPolicyResponseDto(long discountPolicyId, DiscountDetails discountDetails, LocalDateTime insertDate, LocalDateTime updateDate, String insertOperator, String updateOperator) {
        this.discountPolicyId = discountPolicyId;
        this.discountDetails = discountDetails;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.insertOperator = insertOperator;
        this.updateOperator = updateOperator;
    }


    public static DiscountPolicyResponseDto from(DiscountPolicy discountPolicy) {
        return DiscountPolicyResponseDto.builder()
                .discountPolicyId(discountPolicy.getId())
                .discountDetails(discountPolicy.getDiscountDetails())
                .insertDate(discountPolicy.getInsertDate())
                .updateDate(discountPolicy.getUpdateDate())
                .insertOperator(discountPolicy.getInsertOperator())
                .updateOperator(discountPolicy.getUpdateOperator())
                .build();
    }
}
