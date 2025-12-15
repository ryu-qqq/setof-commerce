package com.setof.connectly.module.display.dto.content;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.display.dto.component.ComponentDetailResponse;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentGroupResponse {

    private long contentId;
    private DisplayPeriod displayPeriod;
    private String title;
    private String memo;
    private String imageUrl;
    private List<ComponentDetailResponse> componentDetails = new ArrayList<>();

    @Builder
    @QueryProjection
    public ContentGroupResponse(
            long contentId,
            DisplayPeriod displayPeriod,
            String title,
            String memo,
            String imageUrl,
            List<ComponentDetailResponse> componentDetails) {
        this.contentId = contentId;
        this.displayPeriod = displayPeriod;
        this.title = title;
        this.memo = memo;
        this.imageUrl = imageUrl;
        this.componentDetails = componentDetails;
    }

    @QueryProjection
    public ContentGroupResponse(
            long contentId,
            DisplayPeriod displayPeriod,
            String title,
            String memo,
            String imageUrl) {
        this.contentId = contentId;
        this.displayPeriod = displayPeriod;
        this.title = title;
        this.memo = memo;
        this.imageUrl = imageUrl;
    }
}
