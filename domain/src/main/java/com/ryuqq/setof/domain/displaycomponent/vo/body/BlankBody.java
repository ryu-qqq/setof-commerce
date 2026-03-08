package com.ryuqq.setof.domain.displaycomponent.vo.body;

/**
 * BlankBody - BLANK 컴포넌트 본문.
 *
 * @param height 공백 높이
 * @param showLine 구분선 표시 여부
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BlankBody(double height, boolean showLine) implements ComponentBody {}
