package com.ryuqq.setof.application.contentpage.dto.bundle;

import com.ryuqq.setof.application.contentpage.dto.command.RegisterDisplayComponentCommand;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import java.time.Instant;
import java.util.List;

/**
 * ContentPageUpdateBundle - 콘텐츠 페이지 수정 번들.
 *
 * <p>기존 ContentPage + 기존 DisplayComponent + 수정 커맨드(컴포넌트 Commands)를 하나의 번들로 묶어 전달합니다.
 *
 * @param contentPage 기존 콘텐츠 페이지 도메인 객체
 * @param existingComponents 기존 디스플레이 컴포넌트 목록
 * @param incomingComponentCommands 요청으로 들어온 컴포넌트 Command 목록
 * @param updatedAt 수정 시각
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ContentPageUpdateBundle(
        ContentPage contentPage,
        List<DisplayComponent> existingComponents,
        List<RegisterDisplayComponentCommand> incomingComponentCommands,
        Instant updatedAt) {}
