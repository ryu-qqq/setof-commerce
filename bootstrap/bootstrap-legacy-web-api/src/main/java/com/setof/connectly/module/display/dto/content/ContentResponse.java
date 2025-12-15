package com.setof.connectly.module.display.dto.content;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentResponse {

    private long contentId;
    private List<ComponentDetail> componentDetails;

    public ContentResponse(long contentId, List<ComponentDetail> componentDetails) {
        this.contentId = contentId;
        this.componentDetails = componentDetails;
    }

    public static ContentResponse toResponse(
            long contentId, List<ComponentDetail> componentDetails) {
        return ContentResponse.builder()
                .contentId(contentId)
                .componentDetails(componentDetails)
                .build();
    }
}
