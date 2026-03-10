package com.ryuqq.setof.application.productgroup.dto.bundle;

import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import java.time.Instant;

/**
 * 상품그룹 등록 번들.
 *
 * <p>도메인 객체와 하위 도메인 등록에 필요한 커맨드를 하나의 번들로 묶어 전달합니다.
 *
 * @param productGroup 상품그룹 도메인 객체
 * @param images 이미지 커맨드 (productGroup의 images와 별도로 커맨드 원본 보관)
 * @param optionGroups 옵션 그룹 커맨드
 * @param products 상품 커맨드
 * @param description 상세설명 커맨드 (nullable)
 * @param notice 상품고시 커맨드 (nullable)
 * @param createdAt 생성 시각
 */
public record ProductGroupRegistrationBundle(
        ProductGroup productGroup, RegisterProductGroupCommand command, Instant createdAt) {}
