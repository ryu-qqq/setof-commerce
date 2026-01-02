package com.connectly.partnerAdmin.module.external.dto.order.oco.query;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class AbstractOcoOrderUpdate implements OcoOrderUpdate{

    private String otid;

    public AbstractOcoOrderUpdate(long otid) {
        this.otid = String.valueOf(otid);
    }

}
