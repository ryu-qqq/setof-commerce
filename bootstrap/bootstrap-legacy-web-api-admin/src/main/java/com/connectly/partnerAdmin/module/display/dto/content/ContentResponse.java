package com.connectly.partnerAdmin.module.display.dto.content;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentResponse {

    private long contentId;
    private Yn displayYn;
    private String title;
    private DisplayPeriod displayPeriod;
    private String insertOperator;
    private String updateOperator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insertDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    @Builder
    @QueryProjection
    public ContentResponse(long contentId, Yn displayYn, String title, DisplayPeriod displayPeriod, String insertOperator, LocalDateTime insertDate, String updateOperator, LocalDateTime updateDate) {
        this.contentId = contentId;
        this.displayYn = displayYn;
        this.title = title;
        this.displayPeriod = displayPeriod;
        this.insertOperator = insertOperator;
        this.insertDate = insertDate;
        this.updateOperator = updateOperator;
        this.updateDate = updateDate;
    }

}
