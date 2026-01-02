package com.connectly.partnerAdmin.module.display.dto.content;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentGroupResponse {

    private long contentId;
    private DisplayPeriod displayPeriod;
    private String title;
    private String memo;
    private String imageUrl;
    private Yn displayYn;
    private List<SubComponent> components =new ArrayList<>();

    @Builder
    @QueryProjection
    public ContentGroupResponse(long contentId, DisplayPeriod displayPeriod, String title, String memo, String imageUrl, Yn displayYn, List<SubComponent> components) {
        this.contentId = contentId;
        this.displayPeriod = displayPeriod;
        this.title = title;
        this.memo = memo;
        this.imageUrl = imageUrl;
        this.displayYn = displayYn;
        this.components = components;
    }
}
