package com.ryuqq.setof.domain.displaycomponent.vo.body;

import com.ryuqq.setof.domain.displaycomponent.vo.BrandTarget;
import java.util.List;

/**
 * BrandBody - BRAND 컴포넌트 본문.
 *
 * @param targets 브랜드 타겟 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record BrandBody(List<BrandTarget> targets) implements ComponentBody {}
