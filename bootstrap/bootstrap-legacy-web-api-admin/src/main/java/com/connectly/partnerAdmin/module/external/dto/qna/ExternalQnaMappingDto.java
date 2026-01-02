package com.connectly.partnerAdmin.module.external.dto.qna;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalQnaMappingDto {
    private long externalIdx;
    private long siteId;

    @QueryProjection
    public ExternalQnaMappingDto(long externalIdx, long siteId) {
        this.externalIdx = externalIdx;
        this.siteId = siteId;
    }
}
