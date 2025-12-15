package com.setof.connectly.module.news.dto.faq;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.news.enums.FaqType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FaqDto {
    private FaqType faqType;
    private String title;
    private String contents;

    @QueryProjection
    public FaqDto(FaqType faqType, String title, String contents) {
        this.faqType = faqType;
        this.title = title;
        this.contents = contents;
    }
}
