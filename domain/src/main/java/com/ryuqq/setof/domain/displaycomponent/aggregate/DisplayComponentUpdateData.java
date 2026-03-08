package com.ryuqq.setof.domain.displaycomponent.aggregate;

import com.ryuqq.setof.domain.common.vo.DisplayPeriod;
import com.ryuqq.setof.domain.displaycomponent.vo.ComponentLayout;
import com.ryuqq.setof.domain.displaycomponent.vo.body.ComponentBody;
import java.time.Instant;

/**
 * DisplayComponent 수정 데이터.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DisplayComponentUpdateData(
        String name,
        ComponentLayout componentLayout,
        DisplayPeriod displayPeriod,
        int displayOrder,
        int exposedProductCount,
        boolean active,
        ComponentBody componentBody,
        Instant updatedAt) {}
