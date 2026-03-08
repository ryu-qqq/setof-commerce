package com.ryuqq.setof.domain.displaycomponent.vo.body;

/**
 * TitleBody - TITLE 컴포넌트 본문.
 *
 * @param title1 제목 1
 * @param title2 제목 2
 * @param subTitle1 부제목 1
 * @param subTitle2 부제목 2
 * @author ryu-qqq
 * @since 1.1.0
 */
public record TitleBody(String title1, String title2, String subTitle1, String subTitle2)
        implements ComponentBody {}
