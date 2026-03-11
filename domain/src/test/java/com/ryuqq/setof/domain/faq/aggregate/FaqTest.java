package com.ryuqq.setof.domain.faq.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.faq.FaqFixtures;
import com.ryuqq.setof.domain.faq.id.FaqId;
import com.ryuqq.setof.domain.faq.vo.FaqContents;
import com.ryuqq.setof.domain.faq.vo.FaqDisplayOrder;
import com.ryuqq.setof.domain.faq.vo.FaqTitle;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Faq Aggregate 테스트")
class FaqTest {

    @Nested
    @DisplayName("reconstitute() - 영속성에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("일반 FAQ를 복원한다")
        void reconstituteNormalFaq() {
            // given
            FaqId id = FaqFixtures.defaultFaqId();
            FaqTitle title = FaqFixtures.defaultFaqTitle();
            FaqContents contents = FaqFixtures.defaultFaqContents();
            FaqDisplayOrder displayOrder = FaqFixtures.defaultFaqDisplayOrder();

            // when
            Faq faq =
                    Faq.reconstitute(
                            id,
                            FaqType.SHIPPING,
                            title,
                            contents,
                            displayOrder,
                            null,
                            DeletionStatus.active(),
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            // then
            assertThat(faq.id()).isEqualTo(id);
            assertThat(faq.faqType()).isEqualTo(FaqType.SHIPPING);
            assertThat(faq.title()).isEqualTo(title);
            assertThat(faq.contents()).isEqualTo(contents);
            assertThat(faq.displayOrder()).isEqualTo(displayOrder);
            assertThat(faq.topDisplayOrder()).isNull();
            assertThat(faq.createdAt()).isNotNull();
            assertThat(faq.updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("TOP FAQ를 복원한다")
        void reconstituteTopFaq() {
            // given
            FaqId id = FaqId.of(2L);

            // when
            Faq faq =
                    Faq.reconstitute(
                            id,
                            FaqType.TOP,
                            FaqTitle.of("자주 묻는 질문 1위"),
                            FaqContents.of("상단 고정 FAQ 내용입니다."),
                            FaqDisplayOrder.defaultOrder(),
                            1,
                            DeletionStatus.active(),
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            // then
            assertThat(faq.isNew()).isFalse();
            assertThat(faq.isTop()).isTrue();
            assertThat(faq.hasTopDisplayOrder()).isTrue();
            assertThat(faq.topDisplayOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("topDisplayOrder가 null인 FAQ를 복원한다")
        void reconstituteWithNullTopDisplayOrder() {
            // when
            Faq faq = FaqFixtures.shippingFaq();

            // then
            assertThat(faq.topDisplayOrder()).isNull();
            assertThat(faq.hasTopDisplayOrder()).isFalse();
        }
    }

    @Nested
    @DisplayName("isNew() - 신규 여부 확인")
    class IsNewTest {

        @Test
        @DisplayName("ID가 없는 FAQ는 신규이다")
        void faqWithNullIdIsNew() {
            // given
            Faq faq =
                    Faq.reconstitute(
                            FaqId.forNew(),
                            FaqType.SHIPPING,
                            FaqFixtures.defaultFaqTitle(),
                            FaqFixtures.defaultFaqContents(),
                            FaqFixtures.defaultFaqDisplayOrder(),
                            null,
                            DeletionStatus.active(),
                            CommonVoFixtures.now(),
                            CommonVoFixtures.now());

            // then
            assertThat(faq.isNew()).isTrue();
        }

        @Test
        @DisplayName("ID가 있는 FAQ는 신규가 아니다")
        void faqWithIdIsNotNew() {
            // when
            Faq faq = FaqFixtures.shippingFaq();

            // then
            assertThat(faq.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("isTop() - TOP FAQ 여부 확인")
    class IsTopTest {

        @Test
        @DisplayName("TOP 유형의 FAQ는 isTop()이 true이다")
        void topFaqIsTop() {
            // when
            Faq faq = FaqFixtures.topFaq();

            // then
            assertThat(faq.isTop()).isTrue();
        }

        @Test
        @DisplayName("TOP 유형이 아닌 FAQ는 isTop()이 false이다")
        void nonTopFaqIsNotTop() {
            // when
            Faq faq = FaqFixtures.shippingFaq();

            // then
            assertThat(faq.isTop()).isFalse();
        }

        @Test
        @DisplayName("MEMBER_LOGIN 유형의 FAQ는 isTop()이 false이다")
        void memberLoginFaqIsNotTop() {
            // given
            Faq faq =
                    Faq.reconstitute(
                            FaqFixtures.defaultFaqId(),
                            FaqType.MEMBER_LOGIN,
                            FaqFixtures.defaultFaqTitle(),
                            FaqFixtures.defaultFaqContents(),
                            FaqFixtures.defaultFaqDisplayOrder(),
                            null,
                            DeletionStatus.active(),
                            CommonVoFixtures.yesterday(),
                            CommonVoFixtures.yesterday());

            // then
            assertThat(faq.isTop()).isFalse();
        }
    }

    @Nested
    @DisplayName("hasTopDisplayOrder() - 상단 고정 순서 존재 여부")
    class HasTopDisplayOrderTest {

        @Test
        @DisplayName("topDisplayOrder가 있으면 true를 반환한다")
        void returnsTrueWhenTopDisplayOrderExists() {
            // when
            Faq faq = FaqFixtures.topFaq();

            // then
            assertThat(faq.hasTopDisplayOrder()).isTrue();
        }

        @Test
        @DisplayName("topDisplayOrder가 null이면 false를 반환한다")
        void returnsFalseWhenTopDisplayOrderIsNull() {
            // when
            Faq faq = FaqFixtures.shippingFaq();

            // then
            assertThat(faq.hasTopDisplayOrder()).isFalse();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("idValue()는 ID의 Long 값을 반환한다")
        void idValueReturnsLongValue() {
            // given
            Faq faq = FaqFixtures.shippingFaq(100L);

            // then
            assertThat(faq.idValue()).isEqualTo(100L);
        }

        @Test
        @DisplayName("titleValue()는 제목의 문자열을 반환한다")
        void titleValueReturnsStringValue() {
            // given
            Faq faq = FaqFixtures.shippingFaq();

            // then
            assertThat(faq.titleValue()).isEqualTo(FaqFixtures.DEFAULT_TITLE);
        }

        @Test
        @DisplayName("contentsValue()는 내용의 문자열을 반환한다")
        void contentsValueReturnsStringValue() {
            // given
            Faq faq = FaqFixtures.shippingFaq();

            // then
            assertThat(faq.contentsValue()).isEqualTo(FaqFixtures.DEFAULT_CONTENTS);
        }

        @Test
        @DisplayName("displayOrderValue()는 표시 순서의 int 값을 반환한다")
        void displayOrderValueReturnsIntValue() {
            // given
            Faq faq = FaqFixtures.shippingFaq();

            // then
            assertThat(faq.displayOrderValue()).isEqualTo(FaqFixtures.DEFAULT_DISPLAY_ORDER);
        }
    }
}
