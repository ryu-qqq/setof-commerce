package com.ryuqq.setof.application.contentpage.port.out;

import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import java.util.List;

/**
 * DisplayComponentCommandPort - 디스플레이 컴포넌트 저장 Port-Out.
 *
 * <p>APP-PRT-001: Port-Out은 interface이며, Adapter-Out이 구현합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DisplayComponentCommandPort {

    /**
     * 디스플레이 컴포넌트 목록을 신규 저장합니다.
     *
     * @param components 저장할 컴포넌트 목록
     */
    void persistAll(List<DisplayComponent> components);

    /**
     * 기존 디스플레이 컴포넌트 목록을 갱신합니다 (수정/소프트 삭제).
     *
     * @param components 갱신할 컴포넌트 목록
     */
    void updateAll(List<DisplayComponent> components);
}
