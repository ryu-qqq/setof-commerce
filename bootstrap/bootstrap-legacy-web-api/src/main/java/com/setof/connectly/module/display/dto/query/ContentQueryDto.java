package com.setof.connectly.module.display.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentQueryDto {
    private long contentId;
    private String title;
    private String imageUrl;
    private DisplayPeriod displayPeriod;
    private List<ComponentQueryDto> componentQueries;

    @QueryProjection
    public ContentQueryDto(
            long contentId,
            String title,
            String imageUrl,
            DisplayPeriod displayPeriod,
            List<ComponentQueryDto> componentQueries) {
        this.contentId = contentId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.displayPeriod = displayPeriod;
        this.componentQueries = componentQueries;
    }
}
