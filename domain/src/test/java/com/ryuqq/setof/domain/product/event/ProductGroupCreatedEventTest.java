package com.ryuqq.setof.domain.product.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ryuqq.setof.domain.product.ProductGroupFixture;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** ProductGroupCreatedEvent 단위 테스트 */
@DisplayName("ProductGroupCreatedEvent")
class ProductGroupCreatedEventTest {

    private static final Instant FIXED_INSTANT = Instant.parse("2025-01-15T10:00:00Z");

    @Nested
    @DisplayName("from 팩토리 메서드")
    class FromMethodTest {

        @Test
        @DisplayName("ProductGroup Aggregate에서 이벤트 생성")
        void shouldCreateEventFromProductGroup() {
            // Given
            ProductGroup productGroup = ProductGroupFixture.create();

            // When
            ProductGroupCreatedEvent event =
                    ProductGroupCreatedEvent.from(productGroup, FIXED_INSTANT);

            // Then
            assertNotNull(event);
            assertEquals(productGroup.getId(), event.productGroupId());
            assertEquals(productGroup.getSellerId(), event.sellerId());
            assertEquals(productGroup.getCategoryId(), event.categoryId());
            assertEquals(productGroup.getBrandId(), event.brandId());
            assertEquals(productGroup.getName(), event.name());
            assertEquals(productGroup.getOptionType(), event.optionType());
            assertEquals(productGroup.getPrice(), event.price());
            assertEquals(FIXED_INSTANT, event.occurredAt());
        }

        @Test
        @DisplayName("단일 옵션 상품그룹에서 이벤트 생성")
        void shouldCreateEventFromSingleOptionProductGroup() {
            // Given
            ProductGroup productGroup = ProductGroupFixture.createSingleOption();

            // When
            ProductGroupCreatedEvent event =
                    ProductGroupCreatedEvent.from(productGroup, FIXED_INSTANT);

            // Then
            assertNotNull(event);
            assertEquals(productGroup.getId(), event.productGroupId());
            assertEquals(productGroup.getOptionType(), event.optionType());
        }

        @Test
        @DisplayName("1단계 옵션 상품그룹에서 이벤트 생성")
        void shouldCreateEventFromOneLevelOptionProductGroup() {
            // Given
            ProductGroup productGroup = ProductGroupFixture.createOneLevelOption();

            // When
            ProductGroupCreatedEvent event =
                    ProductGroupCreatedEvent.from(productGroup, FIXED_INSTANT);

            // Then
            assertNotNull(event);
            assertEquals(productGroup.getId(), event.productGroupId());
            assertEquals(productGroup.getOptionType(), event.optionType());
        }
    }

    @Nested
    @DisplayName("DomainEvent 인터페이스")
    class DomainEventInterfaceTest {

        @Test
        @DisplayName("occurredAt 필드가 올바르게 설정됨")
        void shouldHaveCorrectOccurredAt() {
            // Given
            ProductGroup productGroup = ProductGroupFixture.create();

            // When
            ProductGroupCreatedEvent event =
                    ProductGroupCreatedEvent.from(productGroup, FIXED_INSTANT);

            // Then
            assertEquals(FIXED_INSTANT, event.occurredAt());
        }
    }
}
