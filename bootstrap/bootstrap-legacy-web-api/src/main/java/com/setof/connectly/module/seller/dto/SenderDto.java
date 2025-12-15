package com.setof.connectly.module.seller.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SenderDto {
    private long sellerId;
    private String sellerName;
    private String sellerEmail;
    private String sellerPhoneNumber;

    @QueryProjection
    public SenderDto(
            long sellerId, String sellerName, String sellerEmail, String sellerPhoneNumber) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        this.sellerPhoneNumber = sellerPhoneNumber;
    }

    @Override
    public int hashCode() {
        return String.valueOf(sellerId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SenderDto) {
            SenderDto p = (SenderDto) obj;
            return this.hashCode() == p.hashCode();
        }
        return false;
    }
}
