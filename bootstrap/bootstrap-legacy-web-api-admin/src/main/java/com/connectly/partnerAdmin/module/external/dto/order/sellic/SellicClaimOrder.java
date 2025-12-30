package com.connectly.partnerAdmin.module.external.dto.order.sellic;


import com.connectly.partnerAdmin.module.external.core.ExMallClaimOrder;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("sellicClaimOrder")
public class SellicClaimOrder implements ExMallClaimOrder {

    @JsonProperty("IDX")
    private long idx;

    @JsonProperty("ORIGINAL_ORDER_ID")
    private String originalOrderId;

    @JsonProperty("CLAIM_STATUS")
    private long claimStatus;

    @JsonProperty("CLAIM_STATUS_GUBUN")
    private String claimReason;

    @JsonProperty("CLAIM_COMMENT")
    private String detailReason;

    public boolean isCancelRequest(){
        return this.claimStatus == 1448;
    }

    @Override
    public long getSiteId() {
        return SiteName.SEWON.getSiteId();
    }

    @Override
    public SiteName getSiteName() {
        return SiteName.SEWON;
    }

    @Override
    public long getExMallOrderId() {
        return idx;
    }

    @Override
    public String getClaimReason() {
        return claimReason;
    }

    @Override
    public String getClaimDetailReason() {
        return detailReason;
    }

}
