package com.ryuqq.setof.domain.faq.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.faq.exception.InvalidFaqStatusException;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqContent;
import com.ryuqq.setof.domain.faq.vo.FaqStatus;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Faq Aggregate 테스트
 *
 * <p>FAQ 도메인의 핵심 비즈니스 로직을 테스트합니다.
 */
@DisplayName("Faq Aggregate")
class FaqTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("forNew() - 신규 FAQ 생성")
    class ForNew {

        @Test
        @DisplayName("신규 FAQ를 생성할 수 있다")
        void shouldCreateNewFaq() {
            // given
            FaqCategoryCode categoryCode = FaqCategoryCode.of("PAYMENT");
            FaqContent content = new FaqContent("결제가 안 됩니다", "결제 수단을 확인해주세요");
            int displayOrder = 1;

            // when
            Faq faq = Faq.forNew(categoryCode, content, displayOrder, FIXED_TIME);

            // then
            assertNotNull(faq);
            assertNotNull(faq.getId());
            assertTrue(faq.getId().isNew());
            assertEquals("PAYMENT", faq.getCategoryCodeValue());
            assertEquals(FaqStatus.DRAFT, faq.getStatus());
            assertEquals("결제가 안 됩니다", faq.getQuestion());
            assertEquals("결제 수단을 확인해주세요", faq.getAnswer());
            assertEquals(1, faq.getDisplayOrder());
            assertEquals(0, faq.getViewCount());
            assertFalse(faq.isTop());
            assertFalse(faq.isDeleted());
        }

        @Test
        @DisplayName("신규 FAQ는 DRAFT 상태로 시작한다")
        void shouldStartWithDraftStatus() {
            // given
            FaqContent content = new FaqContent("질문", "답변");

            // when
            Faq faq = Faq.forNew(FaqCategoryCode.of("GENERAL"), content, 1, FIXED_TIME);

            // then
            assertEquals(FaqStatus.DRAFT, faq.getStatus());
            assertFalse(faq.isDisplayable());
        }
    }

    @Nested
    @DisplayName("publish() - FAQ 게시")
    class Publish {

        @Test
        @DisplayName("DRAFT 상태의 FAQ를 게시할 수 있다")
        void shouldPublishDraftFaq() {
            // given
            Faq faq = createDraftFaq();

            // when
            faq.publish(FIXED_TIME);

            // then
            assertEquals(FaqStatus.PUBLISHED, faq.getStatus());
            assertTrue(faq.isDisplayable());
        }

        @Test
        @DisplayName("HIDDEN 상태의 FAQ를 다시 게시할 수 있다")
        void shouldPublishHiddenFaq() {
            // given
            Faq faq = createPublishedFaq();
            faq.hide(FIXED_TIME);

            // when
            faq.publish(FIXED_TIME);

            // then
            assertEquals(FaqStatus.PUBLISHED, faq.getStatus());
        }

        @Test
        @DisplayName("이미 게시된 FAQ를 다시 게시하면 예외가 발생한다")
        void shouldThrowExceptionWhenAlreadyPublished() {
            // given
            Faq faq = createPublishedFaq();

            // when & then
            assertThrows(InvalidFaqStatusException.class, () -> faq.publish(FIXED_TIME));
        }

        @Test
        @DisplayName("삭제된 FAQ를 게시하면 예외가 발생한다")
        void shouldThrowExceptionWhenDeleted() {
            // given
            Faq faq = createDraftFaq();
            faq.softDelete(FIXED_TIME);

            // when & then
            assertThrows(InvalidFaqStatusException.class, () -> faq.publish(FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("hide() - FAQ 숨김")
    class Hide {

        @Test
        @DisplayName("게시된 FAQ를 숨길 수 있다")
        void shouldHidePublishedFaq() {
            // given
            Faq faq = createPublishedFaq();

            // when
            faq.hide(FIXED_TIME);

            // then
            assertEquals(FaqStatus.HIDDEN, faq.getStatus());
            assertFalse(faq.isDisplayable());
        }

        @Test
        @DisplayName("DRAFT 상태의 FAQ는 숨길 수 없다")
        void shouldThrowExceptionWhenDraft() {
            // given
            Faq faq = createDraftFaq();

            // when & then
            assertThrows(InvalidFaqStatusException.class, () -> faq.hide(FIXED_TIME));
        }

        @Test
        @DisplayName("이미 숨김 처리된 FAQ를 다시 숨기면 예외가 발생한다")
        void shouldThrowExceptionWhenAlreadyHidden() {
            // given
            Faq faq = createPublishedFaq();
            faq.hide(FIXED_TIME);

            // when & then
            assertThrows(InvalidFaqStatusException.class, () -> faq.hide(FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("setTop() / unsetTop() - 상단 노출 설정")
    class TopSetting {

        @Test
        @DisplayName("FAQ를 상단 노출 설정할 수 있다")
        void shouldSetTop() {
            // given
            Faq faq = createPublishedFaq();

            // when
            faq.setTop(1, FIXED_TIME);

            // then
            assertTrue(faq.isTop());
            assertEquals(1, faq.getTopOrder());
        }

        @Test
        @DisplayName("상단 노출을 해제할 수 있다")
        void shouldUnsetTop() {
            // given
            Faq faq = createPublishedFaq();
            faq.setTop(1, FIXED_TIME);

            // when
            faq.unsetTop(FIXED_TIME);

            // then
            assertFalse(faq.isTop());
            assertEquals(0, faq.getTopOrder());
        }
    }

    @Nested
    @DisplayName("updateContent() - 내용 수정")
    class UpdateContent {

        @Test
        @DisplayName("FAQ 내용을 수정할 수 있다")
        void shouldUpdateContent() {
            // given
            Faq faq = createDraftFaq();
            FaqContent newContent = new FaqContent("수정된 질문", "수정된 답변");

            // when
            faq.updateContent(newContent, FIXED_TIME);

            // then
            assertEquals("수정된 질문", faq.getQuestion());
            assertEquals("수정된 답변", faq.getAnswer());
        }

        @Test
        @DisplayName("삭제된 FAQ는 수정할 수 없다")
        void shouldThrowExceptionWhenDeleted() {
            // given
            Faq faq = createDraftFaq();
            faq.softDelete(FIXED_TIME);
            FaqContent newContent = new FaqContent("수정된 질문", "수정된 답변");

            // when & then
            assertThrows(
                    InvalidFaqStatusException.class,
                    () -> faq.updateContent(newContent, FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("updateDisplayOrder() - 표시 순서 변경")
    class UpdateDisplayOrder {

        @Test
        @DisplayName("표시 순서를 변경할 수 있다")
        void shouldUpdateDisplayOrder() {
            // given
            Faq faq = createDraftFaq();

            // when
            faq.updateDisplayOrder(5, FIXED_TIME);

            // then
            assertEquals(5, faq.getDisplayOrder());
        }

        @Test
        @DisplayName("음수 순서는 예외가 발생한다")
        void shouldThrowExceptionWhenNegativeOrder() {
            // given
            Faq faq = createDraftFaq();

            // when & then
            assertThrows(
                    IllegalArgumentException.class, () -> faq.updateDisplayOrder(-1, FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("changeCategory() - 카테고리 변경")
    class ChangeCategory {

        @Test
        @DisplayName("카테고리를 변경할 수 있다")
        void shouldChangeCategory() {
            // given
            Faq faq = createDraftFaq();
            FaqCategoryCode newCategoryCode = FaqCategoryCode.of("DELIVERY");

            // when
            Faq updatedFaq = faq.changeCategory(newCategoryCode, FIXED_TIME);

            // then
            assertEquals("DELIVERY", updatedFaq.getCategoryCodeValue());
        }
    }

    @Nested
    @DisplayName("incrementViewCount() - 조회수 증가")
    class IncrementViewCount {

        @Test
        @DisplayName("조회수를 증가시킬 수 있다")
        void shouldIncrementViewCount() {
            // given
            Faq faq = createPublishedFaq();
            assertEquals(0, faq.getViewCount());

            // when
            faq.incrementViewCount();

            // then
            assertEquals(1, faq.getViewCount());
        }

        @Test
        @DisplayName("삭제된 FAQ는 조회수를 증가시킬 수 없다")
        void shouldThrowExceptionWhenDeleted() {
            // given
            Faq faq = createDraftFaq();
            faq.softDelete(FIXED_TIME);

            // when & then
            assertThrows(InvalidFaqStatusException.class, () -> faq.incrementViewCount());
        }
    }

    @Nested
    @DisplayName("softDelete() - 소프트 삭제")
    class SoftDelete {

        @Test
        @DisplayName("FAQ를 소프트 삭제할 수 있다")
        void shouldSoftDeleteFaq() {
            // given
            Faq faq = createDraftFaq();

            // when
            faq.softDelete(FIXED_TIME);

            // then
            assertTrue(faq.isDeleted());
            assertNotNull(faq.getDeletedAt());
        }

        @Test
        @DisplayName("이미 삭제된 FAQ를 다시 삭제하면 예외가 발생한다")
        void shouldThrowExceptionWhenAlreadyDeleted() {
            // given
            Faq faq = createDraftFaq();
            faq.softDelete(FIXED_TIME);

            // when & then
            assertThrows(InvalidFaqStatusException.class, () -> faq.softDelete(FIXED_TIME));
        }
    }

    // ===== Helper Methods =====

    private Faq createDraftFaq() {
        FaqContent content = new FaqContent("테스트 질문", "테스트 답변");
        return Faq.forNew(FaqCategoryCode.of("GENERAL"), content, 1, FIXED_TIME);
    }

    private Faq createPublishedFaq() {
        Faq faq = createDraftFaq();
        faq.publish(FIXED_TIME);
        return faq;
    }
}
