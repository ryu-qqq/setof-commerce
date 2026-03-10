package com.ryuqq.setof.domain.qna.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaProduct Aggregate 단위 테스트")
class QnaProductTest {

    @Nested
    @DisplayName("forNew() - 신규 Q&A 상품 매핑 생성")
    class ForNewTest {

        @Test
        @DisplayName("상품그룹 ID로 신규 QnaProduct를 생성한다")
        void createNewQnaProduct() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(500L);
            Instant now = CommonVoFixtures.now();

            // when
            QnaProduct qnaProduct = QnaProduct.forNew(productGroupId, now);

            // then
            assertThat(qnaProduct.productGroupId()).isEqualTo(productGroupId);
            assertThat(qnaProduct.productGroupIdValue()).isEqualTo(500L);
            assertThat(qnaProduct.qnaId()).isNull();
            assertThat(qnaProduct.qnaIdValue()).isNull();
            assertThat(qnaProduct.createdAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("productGroupId가 null이면 예외가 발생한다")
        void createWithNullProductGroupIdThrowsException() {
            // when & then
            assertThatThrownBy(() -> QnaProduct.forNew(null, CommonVoFixtures.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("productGroupId");
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 QnaProduct를 복원한다")
        void reconstituteQnaProduct() {
            // when
            QnaProduct qnaProduct = QnaFixtures.activeQnaProduct();

            // then
            assertThat(qnaProduct.qnaId()).isNotNull();
            assertThat(qnaProduct.productGroupId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("withQnaId() - qnaId 설정")
    class WithQnaIdTest {

        @Test
        @DisplayName("Qna persist 후 qnaId를 설정한 새 인스턴스를 반환한다")
        void withQnaIdReturnNewInstanceWithQnaId() {
            // given
            QnaProduct qnaProduct = QnaFixtures.newQnaProduct();
            LegacyQnaId qnaId = QnaFixtures.defaultLegacyQnaId();

            // when
            QnaProduct withId = qnaProduct.withQnaId(qnaId);

            // then
            assertThat(withId.qnaId()).isEqualTo(qnaId);
            assertThat(withId.qnaIdValue()).isEqualTo(1001L);
            assertThat(withId.productGroupId()).isEqualTo(qnaProduct.productGroupId());
        }

        @Test
        @DisplayName("withQnaId()는 원본 인스턴스를 변경하지 않는다")
        void withQnaIdDoesNotMutateOriginal() {
            // given
            QnaProduct original = QnaFixtures.newQnaProduct();

            // when
            original.withQnaId(QnaFixtures.defaultLegacyQnaId());

            // then
            assertThat(original.qnaId()).isNull();
        }
    }
}
