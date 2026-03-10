package com.ryuqq.setof.application.productgroup.dto.bundle;

import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupFullCommand;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import java.time.Instant;

/**
 * 상품그룹 수정 번들.
 *
 * <p>기존 도메인 객체와 수정 커맨드를 하나의 번들로 묶어 전달합니다.
 *
 * @param productGroup 기존 상품그룹 도메인 객체
 * @param command 수정 커맨드
 * @param updatedAt 수정 시각
 */
public record ProductGroupUpdateBundle(
        ProductGroup productGroup, UpdateProductGroupFullCommand command, Instant updatedAt) {}
