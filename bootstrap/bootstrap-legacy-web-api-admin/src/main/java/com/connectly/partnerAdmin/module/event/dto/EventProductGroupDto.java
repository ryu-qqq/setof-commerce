package com.connectly.partnerAdmin.module.event.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventProductGroupDto {

    private long productGroupId;
    private Set<Long> productIds;

    @QueryProjection
    public EventProductGroupDto(long productGroupId, Set<Long> productIds) {
        this.productGroupId = productGroupId;
        this.productIds = productIds;
    }
}
