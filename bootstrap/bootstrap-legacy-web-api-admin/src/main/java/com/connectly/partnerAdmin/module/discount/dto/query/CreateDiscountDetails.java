package com.connectly.partnerAdmin.module.discount.dto.query;

import com.connectly.partnerAdmin.module.common.annotation.ValidDateRange;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.enums.DiscountType;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.connectly.partnerAdmin.module.discount.enums.PublisherType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@ValidDateRange(start = "policyStartDate", end = "policyEndDate")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateDiscountDetails {

    @NotBlank(message = "Discount policy name is required.")
    @Length(max = 50, message = "Policy name must not exceed 50 characters.")
    private String discountPolicyName;

    @NotNull(message = "Discount type is required.")
    private DiscountType discountType;

    @NotNull(message = "Publisher type is required.")
    private PublisherType publisherType;

    @NotNull(message = "Issue type is required.")
    private IssueType issueType;

    @NotNull(message = "Discount limit status is required.")
    private Yn discountLimitYn;

    @Min(value = 0, message = "Maximum discount price must be at least 0.")
    public long maxDiscountPrice;

    @NotNull(message = "Share status is required.")
    private Yn shareYn;

    @Max(value = 100, message = "Share ratio must not exceed 100.")
    @Min(value = 0, message = "Share ratio must be at least 0.")
    private double shareRatio;

    @Max(value = 100, message = "Discount ratio must not exceed 100.")
    @Min(value = 0, message = "Discount ratio must be at least 0.")
    private double discountRatio;

    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime policyStartDate;

    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime policyEndDate;

    @Length(max = 100, message = "Memo must not exceed 100 characters.")
    private String memo;

    @Max(value = 100, message = "Priority must not exceed 100.")
    @Min(value = 0, message = "Priority must be at least 0.")
    private int priority;

    @NotNull(message = "Active status is required.")
    private Yn activeYn;
}