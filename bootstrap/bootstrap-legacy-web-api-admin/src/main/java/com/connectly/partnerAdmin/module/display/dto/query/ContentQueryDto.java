package com.connectly.partnerAdmin.module.display.dto.query;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ContentQueryDto {

    private long contentId;
    private String title;
    private String memo;
    private Yn displayYn;
    private String imageUrl;
    private DisplayPeriod displayPeriod;
    private List<ComponentQueryDto> componentQueries;

    @Builder
    @QueryProjection
    public ContentQueryDto(long contentId, String title, String memo, Yn displayYn, String imageUrl, DisplayPeriod displayPeriod, List<ComponentQueryDto> componentQueries) {
        this.contentId = contentId;
        this.title = title;
        this.memo = memo;
        this.displayYn = displayYn;
        this.imageUrl = imageUrl;
        this.displayPeriod = displayPeriod;
        this.componentQueries = componentQueries;
    }
}
