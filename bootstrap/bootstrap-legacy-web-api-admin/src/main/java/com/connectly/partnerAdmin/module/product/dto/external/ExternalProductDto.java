package com.connectly.partnerAdmin.module.product.dto.external;

import com.connectly.partnerAdmin.module.external.enums.MappingStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalProductDto {

    private String siteName;
    private String externalIdx;
    private MappingStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    @QueryProjection
    public ExternalProductDto(long siteId, String externalIdx, MappingStatus status, LocalDateTime insertDate, LocalDateTime updateDate) {
        this.siteName = SiteName.of(siteId).getName();
        this.externalIdx = externalIdx;
        this.status = status;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(siteName, externalIdx, status, insertDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalProductDto that = (ExternalProductDto) o;
        return Objects.equals(siteName, that.siteName) &&
                Objects.equals(externalIdx, that.externalIdx) &&
                Objects.equals(status, that.status) &&
                Objects.equals(insertDate, that.insertDate);
    }
}
