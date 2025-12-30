package com.connectly.partnerAdmin.module.external.dto.product.buyma;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BuymaProductDto {

    private long id;
    @JsonProperty(value = "request_uid")
    private String requestUid;
    @JsonProperty(value = "reference_number")
    private String referenceNumber;


    public long getProductGroupId(){
        if(this.referenceNumber != null){
            if(referenceNumber.contains("L")) return 0L;

            String[] split = referenceNumber.split("_");
            if(split.length>0){
                return Long.parseLong(split[0]);
            }
        }
        return 0L;
    }


    @Override
    public String toString() {
        return String.format("id %s, request_uid %s, reference_number %s", id, requestUid, referenceNumber);
    }
}
