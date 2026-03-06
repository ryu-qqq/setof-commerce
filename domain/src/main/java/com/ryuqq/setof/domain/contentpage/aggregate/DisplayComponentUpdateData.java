package com.ryuqq.setof.domain.contentpage.aggregate;

import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.contentpage.vo.ComponentSpec;
import com.ryuqq.setof.domain.contentpage.vo.DisplayConfig;
import com.ryuqq.setof.domain.contentpage.vo.ViewExtension;
import java.time.Instant;

/**
 * 디스플레이 컴포넌트 수정 데이터.
 *
 * <p>Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 *
 * @param name 컴포넌트 이름
 * @param displayOrder 노출 순서
 * @param displayConfig 표시 설정
 * @param viewExtension 뷰 확장 설정
 * @param componentSpec 컴포넌트 스펙
 * @param displayPeriod 노출 기간
 * @param active 노출 여부
 * @param updatedAt 수정 시각
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DisplayComponentUpdateData(
        String name,
        int displayOrder,
        DisplayConfig displayConfig,
        ViewExtension viewExtension,
        ComponentSpec componentSpec,
        DisplayPeriod displayPeriod,
        boolean active,
        Instant updatedAt) {}
