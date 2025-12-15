package com.setof.connectly.module.display.dto.content;

import com.querydsl.core.annotations.QueryProjection;
import java.util.HashSet;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnDisplayContent {
    private HashSet<Long> contentIds = new HashSet<>();

    @Builder
    @QueryProjection
    public OnDisplayContent(HashSet<Long> contentIds) {
        this.contentIds = contentIds;
    }
}
