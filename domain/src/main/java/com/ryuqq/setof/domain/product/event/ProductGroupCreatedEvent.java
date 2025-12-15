package com.ryuqq.setof.domain.product.event;

import com.ryuqq.setof.domain.brand.vo.BrandId;
import com.ryuqq.setof.domain.category.vo.CategoryId;
import com.ryuqq.setof.domain.common.event.DomainEvent;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.OptionType;
import com.ryuqq.setof.domain.product.vo.Price;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductGroupName;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import java.time.Instant;

/**
 * 상품그룹 생성 이벤트
 *
 * <p>상품그룹이 생성되었을 때 발행되는 도메인 이벤트입니다.
 *
 * <p>Domain Event 규칙:
 *
 * <ul>
 *   <li>Record 타입 필수 (불변성 보장)
 *   <li>occurredAt 필드 필수 (Instant)
 *   <li>from() 정적 팩토리 메서드 필수
 *   <li>과거형 네이밍 (*CreatedEvent)
 * </ul>
 *
 * @param productGroupId 상품그룹 ID
 * @param sellerId 셀러 ID
 * @param categoryId 카테고리 ID
 * @param brandId 브랜드 ID
 * @param name 상품그룹명
 * @param optionType 옵션 타입
 * @param price 가격
 * @param occurredAt 이벤트 발생 시각
 */
public record ProductGroupCreatedEvent(
        ProductGroupId productGroupId,
        SellerId sellerId,
        CategoryId categoryId,
        BrandId brandId,
        ProductGroupName name,
        OptionType optionType,
        Price price,
        Instant occurredAt)
        implements DomainEvent {

    /**
     * ProductGroup으로부터 이벤트 생성
     *
     * @param productGroup 생성된 상품그룹
     * @param occurredAt 이벤트 발생 시각
     * @return ProductGroupCreatedEvent 인스턴스
     */
    public static ProductGroupCreatedEvent from(ProductGroup productGroup, Instant occurredAt) {
        return new ProductGroupCreatedEvent(
                productGroup.getId(),
                productGroup.getSellerId(),
                productGroup.getCategoryId(),
                productGroup.getBrandId(),
                productGroup.getName(),
                productGroup.getOptionType(),
                productGroup.getPrice(),
                occurredAt);
    }
}
