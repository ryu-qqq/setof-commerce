package com.ryuqq.setof.domain.displaycomponent.vo.body;

import com.ryuqq.setof.domain.displaycomponent.vo.TabItem;
import com.ryuqq.setof.domain.displaycomponent.vo.TabMovingType;
import java.util.List;

/**
 * TabBody - TAB 컴포넌트 본문.
 *
 * @param stickyEnabled 탭 고정 여부
 * @param tabMovingType 탭 이동 방식
 * @param tabs 탭 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record TabBody(boolean stickyEnabled, TabMovingType tabMovingType, List<TabItem> tabs)
        implements ComponentBody {}
