package com.setof.connectly.module.news.dto.faq.filter;

import com.setof.connectly.module.news.enums.FaqType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FaqFilter {

    @NotNull(message = "FAQ 타입은 필수 입니다.")
    private FaqType faqType;
}
