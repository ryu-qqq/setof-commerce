package com.ryuqq.setof.domain.faq.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.faq.exception.InvalidFaqCategoryStatusException;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryStatus;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * FaqCategory Aggregate 테스트
 *
 * <p>FAQ 카테고리 도메인의 핵심 비즈니스 로직을 테스트합니다.
 */
@DisplayName("FaqCategory Aggregate")
class FaqCategoryTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("forNew() - 신규 카테고리 생성")
    class ForNew {

        @Test
        @DisplayName("신규 FAQ 카테고리를 생성할 수 있다")
        void shouldCreateNewFaqCategory() {
            // given
            FaqCategoryCode code = FaqCategoryCode.of("PAYMENT");
            String name = "결제 관련";
            String description = "결제 관련 자주 묻는 질문";
            int displayOrder = 1;

            // when
            FaqCategory category =
                    FaqCategory.forNew(code, name, description, displayOrder, FIXED_TIME);

            // then
            assertNotNull(category);
            assertNotNull(category.getId());
            assertTrue(category.getId().isNew());
            assertEquals("PAYMENT", category.getCodeValue());
            assertEquals("결제 관련", category.getName());
            assertEquals("결제 관련 자주 묻는 질문", category.getDescription());
            assertEquals(1, category.getDisplayOrder());
            assertEquals(FaqCategoryStatus.ACTIVE, category.getStatus());
            assertFalse(category.isDeleted());
        }

        @Test
        @DisplayName("신규 카테고리는 ACTIVE 상태로 시작한다")
        void shouldStartWithActiveStatus() {
            // given
            FaqCategoryCode code = FaqCategoryCode.of("GENERAL");

            // when
            FaqCategory category = FaqCategory.forNew(code, "일반", null, 1, FIXED_TIME);

            // then
            assertEquals(FaqCategoryStatus.ACTIVE, category.getStatus());
            assertTrue(category.isDisplayable());
            assertTrue(category.isActive());
        }

        @Test
        @DisplayName("카테고리명이 없으면 예외가 발생한다")
        void shouldThrowExceptionWhenNameIsBlank() {
            // given
            FaqCategoryCode code = FaqCategoryCode.of("GENERAL");

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> FaqCategory.forNew(code, "", null, 1, FIXED_TIME));
        }

        @Test
        @DisplayName("카테고리명이 100자를 초과하면 예외가 발생한다")
        void shouldThrowExceptionWhenNameTooLong() {
            // given
            FaqCategoryCode code = FaqCategoryCode.of("GENERAL");
            String longName = "a".repeat(101);

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> FaqCategory.forNew(code, longName, null, 1, FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("activate() - 카테고리 활성화")
    class Activate {

        @Test
        @DisplayName("비활성 카테고리를 활성화할 수 있다")
        void shouldActivateInactiveCategory() {
            // given
            FaqCategory category = createActiveCategory();
            category.deactivate(FIXED_TIME);

            // when
            category.activate(FIXED_TIME);

            // then
            assertEquals(FaqCategoryStatus.ACTIVE, category.getStatus());
            assertTrue(category.isDisplayable());
        }

        @Test
        @DisplayName("이미 활성화된 카테고리를 다시 활성화하면 예외가 발생한다")
        void shouldThrowExceptionWhenAlreadyActive() {
            // given
            FaqCategory category = createActiveCategory();

            // when & then
            assertThrows(
                    InvalidFaqCategoryStatusException.class, () -> category.activate(FIXED_TIME));
        }

        @Test
        @DisplayName("삭제된 카테고리를 활성화하면 예외가 발생한다")
        void shouldThrowExceptionWhenDeleted() {
            // given
            FaqCategory category = createActiveCategory();
            category.softDelete(FIXED_TIME);

            // when & then
            assertThrows(
                    InvalidFaqCategoryStatusException.class, () -> category.activate(FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("deactivate() - 카테고리 비활성화")
    class Deactivate {

        @Test
        @DisplayName("활성 카테고리를 비활성화할 수 있다")
        void shouldDeactivateActiveCategory() {
            // given
            FaqCategory category = createActiveCategory();

            // when
            category.deactivate(FIXED_TIME);

            // then
            assertEquals(FaqCategoryStatus.INACTIVE, category.getStatus());
            assertFalse(category.isDisplayable());
        }

        @Test
        @DisplayName("이미 비활성화된 카테고리를 다시 비활성화하면 예외가 발생한다")
        void shouldThrowExceptionWhenAlreadyInactive() {
            // given
            FaqCategory category = createActiveCategory();
            category.deactivate(FIXED_TIME);

            // when & then
            assertThrows(
                    InvalidFaqCategoryStatusException.class, () -> category.deactivate(FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("updateInfo() - 정보 수정")
    class UpdateInfo {

        @Test
        @DisplayName("카테고리 정보를 수정할 수 있다")
        void shouldUpdateInfo() {
            // given
            FaqCategory category = createActiveCategory();

            // when
            category.updateInfo("수정된 이름", "수정된 설명", FIXED_TIME);

            // then
            assertEquals("수정된 이름", category.getName());
            assertEquals("수정된 설명", category.getDescription());
        }

        @Test
        @DisplayName("삭제된 카테고리는 수정할 수 없다")
        void shouldThrowExceptionWhenDeleted() {
            // given
            FaqCategory category = createActiveCategory();
            category.softDelete(FIXED_TIME);

            // when & then
            assertThrows(
                    InvalidFaqCategoryStatusException.class,
                    () -> category.updateInfo("새 이름", "새 설명", FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("updateDisplayOrder() - 표시 순서 변경")
    class UpdateDisplayOrder {

        @Test
        @DisplayName("표시 순서를 변경할 수 있다")
        void shouldUpdateDisplayOrder() {
            // given
            FaqCategory category = createActiveCategory();

            // when
            category.updateDisplayOrder(5, FIXED_TIME);

            // then
            assertEquals(5, category.getDisplayOrder());
        }

        @Test
        @DisplayName("음수 순서는 예외가 발생한다")
        void shouldThrowExceptionWhenNegativeOrder() {
            // given
            FaqCategory category = createActiveCategory();

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> category.updateDisplayOrder(-1, FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("changeCode() - 코드 변경")
    class ChangeCode {

        @Test
        @DisplayName("카테고리 코드를 변경할 수 있다")
        void shouldChangeCode() {
            // given
            FaqCategory category = createActiveCategory();
            FaqCategoryCode newCode = FaqCategoryCode.of("NEW_CODE");

            // when
            category.changeCode(newCode, FIXED_TIME);

            // then
            assertEquals("NEW_CODE", category.getCodeValue());
        }
    }

    @Nested
    @DisplayName("softDelete() - 소프트 삭제")
    class SoftDelete {

        @Test
        @DisplayName("카테고리를 소프트 삭제할 수 있다")
        void shouldSoftDeleteCategory() {
            // given
            FaqCategory category = createActiveCategory();

            // when
            category.softDelete(FIXED_TIME);

            // then
            assertTrue(category.isDeleted());
            assertNotNull(category.getDeletedAt());
        }

        @Test
        @DisplayName("이미 삭제된 카테고리를 다시 삭제하면 예외가 발생한다")
        void shouldThrowExceptionWhenAlreadyDeleted() {
            // given
            FaqCategory category = createActiveCategory();
            category.softDelete(FIXED_TIME);

            // when & then
            assertThrows(
                    InvalidFaqCategoryStatusException.class, () -> category.softDelete(FIXED_TIME));
        }
    }

    // ===== Helper Methods =====

    private FaqCategory createActiveCategory() {
        FaqCategoryCode code = FaqCategoryCode.of("GENERAL");
        return FaqCategory.forNew(code, "일반", "일반 FAQ", 1, FIXED_TIME);
    }
}
