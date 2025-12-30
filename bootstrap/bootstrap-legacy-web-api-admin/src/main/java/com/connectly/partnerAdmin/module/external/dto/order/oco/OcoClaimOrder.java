package com.connectly.partnerAdmin.module.external.dto.order.oco;


import com.connectly.partnerAdmin.module.external.core.ExMallClaimOrder;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("ocoClaimOrder")
public class OcoClaimOrder implements ExMallClaimOrder {

    private long oid;
    private long ocid;
    @JsonProperty("change_type")
    private String changeType;

    private List<OcoClaimOrderItem> orderItemChangeList;


    @Override
    public long getSiteId() {
        return SiteName.OCO.getSiteId();
    }

    @Override
    public SiteName getSiteName() {
        return SiteName.OCO;
    }

    @Override
    public long getExMallOrderId() {
        return oid;
    }


    public boolean isCancelRequest(){
        return this.changeType.equals("C");
    }


    @Override
    public String getClaimReason() {
        return "단순 변심";
    }

    @Override
    public String getClaimDetailReason() {
        return "";
    }


}
