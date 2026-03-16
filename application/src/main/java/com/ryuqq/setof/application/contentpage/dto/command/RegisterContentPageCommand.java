package com.ryuqq.setof.application.contentpage.dto.command;

import java.time.Instant;
import java.util.List;

/**
 * RegisterContentPageCommand - 콘텐츠 페이지 등록 Command.
 *
 * <p>APP-CMD-001: Command는 record 기반 순수 데이터 객체입니다.
 *
 * @param title 콘텐츠 제목
 * @param memo 메모
 * @param imageUrl 대표 이미지
 * @param displayStartAt 전시 시작일
 * @param displayEndAt 전시 종료일
 * @param active 전시 여부
 * @param components 하위 컴포넌트 등록 Command 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RegisterContentPageCommand(
        String title,
        String memo,
        String imageUrl,
        Instant displayStartAt,
        Instant displayEndAt,
        boolean active,
        List<RegisterDisplayComponentCommand> components) {}
