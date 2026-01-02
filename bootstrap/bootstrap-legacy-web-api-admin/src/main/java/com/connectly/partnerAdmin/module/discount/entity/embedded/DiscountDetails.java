package com.connectly.partnerAdmin.module.discount.entity.embedded;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.enums.DiscountType;
import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import com.connectly.partnerAdmin.module.discount.enums.PublisherType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class DiscountDetails {

    @Column(name = "DISCOUNT_POLICY_NAME", length = 50, nullable = false)
    private String discountPolicyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISCOUNT_TYPE", length = 20, nullable = false)
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "PUBLISHER_TYPE", length = 20, nullable = false)
    private PublisherType publisherType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ISSUE_TYPE", length = 50, nullable = false)
    private IssueType issueType;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISCOUNT_LIMIT_YN", length = 1, nullable = false)
    private Yn discountLimitYn;

    @Column(name = "MAX_DISCOUNT_PRICE", nullable = false)
    public long maxDiscountPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "SHARE_YN", length = 1, nullable = false)
    private Yn shareYn;

    @Column(name = "SHARE_RATIO", nullable = false)
    private double shareRatio;

    @Column(name = "DISCOUNT_RATIO", nullable = false)
    private double discountRatio;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "POLICY_START_DATE", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime policyStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "POLICY_END_DATE", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime policyEndDate;

    @Column(name = "MEMO", length = 100, nullable = true)
    private String memo;

    @Column(name = "PRIORITY", nullable = false)
    private int priority;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "ACTIVE_YN", length = 1, nullable = false)
    private Yn activeYn;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscountDetails)) return false;
        DiscountDetails that = (DiscountDetails) o;
        return maxDiscountPrice == that.maxDiscountPrice &&
                Double.compare(that.shareRatio, shareRatio) == 0 &&
                Double.compare(that.discountRatio, discountRatio) == 0 &&
                priority == that.priority &&
                discountPolicyName.equals(that.discountPolicyName) &&
                discountType == that.discountType &&
                publisherType == that.publisherType &&
                issueType == that.issueType &&
                discountLimitYn == that.discountLimitYn &&
                shareYn == that.shareYn &&
                Objects.equals(policyStartDate, that.policyStartDate) &&
                Objects.equals(policyEndDate, that.policyEndDate) &&
                Objects.equals(memo, that.memo) &&
                activeYn == that.activeYn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountPolicyName, discountType, publisherType, issueType, discountLimitYn, maxDiscountPrice, shareYn, shareRatio, discountRatio, policyStartDate, policyEndDate, memo, priority, activeYn);
    }

}
