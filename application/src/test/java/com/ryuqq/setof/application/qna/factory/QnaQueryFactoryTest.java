package com.ryuqq.setof.application.qna.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.application.qna.QnaQueryFixtures;
import com.ryuqq.setof.application.qna.dto.query.MyQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.query.ProductQnaSearchParams;
import com.ryuqq.setof.domain.qna.query.ProductQnaSearchCriteria;
import com.ryuqq.setof.domain.qna.query.QnaSearchCriteria;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaQueryFactory 단위 테스트")
class QnaQueryFactoryTest {

    private final CommonVoFactory commonVoFactory = new CommonVoFactory();
    private final QnaQueryFactory sut = new QnaQueryFactory(commonVoFactory);

    @Nested
    @DisplayName("createProductQnaCriteria() - 상품 Q&A 검색 조건 생성")
    class CreateProductQnaCriteriaTest {

        @Test
        @DisplayName("ProductQnaSearchParams로부터 ProductQnaSearchCriteria를 생성한다")
        void createProductQnaCriteria_ValidParams_ReturnsCriteria() {
            // given
            ProductQnaSearchParams params = QnaQueryFixtures.productQnaSearchParams();

            // when
            ProductQnaSearchCriteria criteria = sut.createProductQnaCriteria(params);

            // then
            assertThat(criteria).isNotNull();
            assertThat(criteria.productGroupIdValue()).isEqualTo(params.productGroupId());
        }

        @Test
        @DisplayName("page와 size가 criteria에 반영된다")
        void createProductQnaCriteria_PageAndSize_AreReflected() {
            // given
            ProductQnaSearchParams params = QnaQueryFixtures.productQnaSearchParams(1, 20);

            // when
            ProductQnaSearchCriteria criteria = sut.createProductQnaCriteria(params);

            // then
            assertThat(criteria.page()).isEqualTo(params.pageOrDefault());
            assertThat(criteria.size()).isEqualTo(params.sizeOrDefault());
        }

        @Test
        @DisplayName("기본 페이지 파라미터를 사용할 때 page=0, size=10이 적용된다")
        void createProductQnaCriteria_DefaultParams_AppliesDefaults() {
            // given
            ProductQnaSearchParams params = QnaQueryFixtures.productQnaSearchParams(0, 10);

            // when
            ProductQnaSearchCriteria criteria = sut.createProductQnaCriteria(params);

            // then
            assertThat(criteria.page()).isEqualTo(0);
            assertThat(criteria.size()).isEqualTo(10);
        }
    }

    @Nested
    @DisplayName("createMyQnaCriteria() - 내 Q&A 검색 조건 생성")
    class CreateMyQnaCriteriaTest {

        @Test
        @DisplayName("MyQnaSearchParams로부터 QnaSearchCriteria를 생성한다")
        void createMyQnaCriteria_ValidParams_ReturnsCriteria() {
            // given
            MyQnaSearchParams params = QnaQueryFixtures.myProductQnaSearchParams();

            // when
            QnaSearchCriteria criteria = sut.createMyQnaCriteria(params);

            // then
            assertThat(criteria).isNotNull();
            assertThat(criteria.qnaType()).isEqualTo(QnaType.PRODUCT);
        }

        @Test
        @DisplayName("ORDER 유형 파라미터로 ORDER QnaSearchCriteria를 생성한다")
        void createMyQnaCriteria_OrderType_ReturnsOrderTypeCriteria() {
            // given
            MyQnaSearchParams params = QnaQueryFixtures.myOrderQnaSearchParams();

            // when
            QnaSearchCriteria criteria = sut.createMyQnaCriteria(params);

            // then
            assertThat(criteria.qnaType()).isEqualTo(QnaType.ORDER);
        }

        @Test
        @DisplayName("size가 criteria에 반영된다")
        void createMyQnaCriteria_Size_IsReflected() {
            // given
            MyQnaSearchParams params = QnaQueryFixtures.myQnaSearchParams("PRODUCT");

            // when
            QnaSearchCriteria criteria = sut.createMyQnaCriteria(params);

            // then
            assertThat(criteria.size()).isEqualTo(params.sizeOrDefault());
        }

        @Test
        @DisplayName("lastQnaId가 있으면 cursor에 반영된다")
        void createMyQnaCriteria_WithLastQnaId_SetsCursorCorrectly() {
            // given
            Long lastQnaId = 1001L;
            MyQnaSearchParams params = QnaQueryFixtures.myQnaSearchParamsWithCursor(lastQnaId);

            // when
            QnaSearchCriteria criteria = sut.createMyQnaCriteria(params);

            // then
            assertThat(criteria.cursor()).isEqualTo(lastQnaId);
        }

        @Test
        @DisplayName("lastQnaId가 없으면 cursor가 null이다")
        void createMyQnaCriteria_WithoutLastQnaId_CursorIsNull() {
            // given
            MyQnaSearchParams params = QnaQueryFixtures.myProductQnaSearchParams();

            // when
            QnaSearchCriteria criteria = sut.createMyQnaCriteria(params);

            // then
            assertThat(criteria.cursor()).isNull();
        }
    }
}
