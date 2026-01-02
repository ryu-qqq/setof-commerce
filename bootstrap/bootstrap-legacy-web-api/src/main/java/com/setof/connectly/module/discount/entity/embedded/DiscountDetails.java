package com.setof.connectly.module.discount.entity.embedded;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.discount.enums.DiscountType;
import com.setof.connectly.module.discount.enums.IssueType;
import com.setof.connectly.module.discount.enums.PublisherType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class DiscountDetails {

    @NotBlank(message = "instantDiscountPolicyName 필수입니다")
    @Size(min = 1, max = 25, message = "정책 이름은 1~25자 사이여야 합니다.")
    @Column(name = "discount_policy_name")
    private String discountPolicyName;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "discountType은 필수입니다.")
    @Column(name = "discount_type")
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "publisherType은 필수입니다.")
    @Column(name = "publisher_type")
    private PublisherType publisherType;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "issueType은 필수입니다.")
    @Column(name = "issue_type")
    private IssueType issueType;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "discountLimitYn은 필수입니다.")
    @Column(name = "discount_limit_yn")
    private Yn discountLimitYn;

    @Max(value = 1000000, message = "최대값 1000000을 넘을 수 없습니다.")
    @Min(value = 0, message = "최소 0 이상이여야 합니다.")
    @Column(name = "max_discount_price")
    public long maxDiscountPrice;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "shareYn은 필수입니다.")
    @Column(name = "share_yn")
    private Yn shareYn;

    @Max(value = 100, message = "최대값 100을 넘을 수 없습니다.")
    @Min(value = 0, message = "최소 0 이상이여야 합니다.")
    @Column(name = "share_ratio")
    private double shareRatio;

    @Max(value = 100, message = "최대값 100을 넘을 수 없습니다.")
    @Min(value = 0, message = "최소 0 이상이여야 합니다.")
    @Column(name = "discount_ratio")
    private double discountRatio;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "policyStartDate은 필수입니다.")
    @Column(name = "policy_start_date")
    private LocalDateTime policyStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "policyEndDate은 필수입니다.")
    @Column(name = "policy_end_date")
    private LocalDateTime policyEndDate;

    @Length(max = 50, message = "memo 50자를 넘어갈 수 없습니다.")
    @Column(name = "memo")
    private String memo;

    @Max(value = 100, message = "최대값 100을 넘을 수 없습니다.")
    @Min(value = 0, message = "최소 0 이상이여야 합니다.")
    @Column(name = "priority")
    private int priority;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "activeYn은 필수입니다.")
    @Column(name = "active_yn")
    private Yn activeYn;

    public void setActiveYn(Yn activeYn) {
        this.activeYn = activeYn;
    }

    public DiscountDetails(
            String discountPolicyName,
            DiscountType discountType,
            PublisherType publisherType,
            IssueType issueType,
            Yn discountLimitYn,
            long maxDiscountPrice,
            Yn shareYn,
            double shareRatio,
            double discountRatio,
            LocalDateTime policyStartDate,
            LocalDateTime policyEndDate,
            String memo,
            int priority,
            Yn activeYn) {
        this.discountPolicyName = discountPolicyName;
        this.discountType = discountType;
        this.publisherType = publisherType;
        this.issueType = issueType;
        this.discountLimitYn = discountLimitYn;
        this.maxDiscountPrice = maxDiscountPrice;
        this.shareYn = shareYn;
        this.shareRatio = shareRatio;
        this.discountRatio = discountRatio;
        this.policyStartDate = policyStartDate;
        this.policyEndDate = policyEndDate;
        this.memo = memo;
        this.priority = priority;
        this.activeYn = activeYn;
    }
}
