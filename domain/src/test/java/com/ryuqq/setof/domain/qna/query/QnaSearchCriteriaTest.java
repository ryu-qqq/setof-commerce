package com.ryuqq.setof.domain.qna.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("QnaSearchCriteria 단위 테스트")
class QnaSearchCriteriaTest {

    @Nested
    @DisplayName("ofMyQnas() - 내 Q&A 조회 조건 생성")
    class OfMyQnasTest {

        @Test
        @DisplayName("내 Q&A 리스트 조회 조건을 생성한다")
        void createMyQnaSearchCriteria() {
            // given
            LegacyMemberId memberId = LegacyMemberId.of(100L);
            CursorQueryContext<QnaSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(QnaSortKey.defaultKey());

            // when
            QnaSearchCriteria criteria = QnaSearchCriteria.ofMyQnas(
                    memberId,
                    QnaType.PRODUCT,
                    null,
                    null,
                    queryContext);

            // then
            assertThat(criteria.memberId()).isEqualTo(memberId);
            assertThat(criteria.qnaType()).isEqualTo(QnaType.PRODUCT);
            assertThat(criteria.rootOnly()).isTrue();
            assertThat(criteria.parentId()).isNull();
            assertThat(criteria.hasMemberId()).isTrue();
            assertThat(criteria.hasParentId()).isFalse();
            assertThat(criteria.hasQnaType()).isTrue();
        }

        @Test
        @DisplayName("날짜 범위가 있는 내 Q&A 조회 조건을 생성한다")
        void createMyQnaSearchCriteriaWithDateRange() {
            // given
            LocalDateTime startDate = LocalDateTime.now().minusDays(30);
            LocalDateTime endDate = LocalDateTime.now();
            CursorQueryContext<QnaSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(QnaSortKey.defaultKey());

            // when
            QnaSearchCriteria criteria = QnaSearchCriteria.ofMyQnas(
                    LegacyMemberId.of(100L),
                    null,
                    startDate,
                    endDate,
                    queryContext);

            // then
            assertThat(criteria.startDate()).isEqualTo(startDate);
            assertThat(criteria.endDate()).isEqualTo(endDate);
            assertThat(criteria.hasDateRange()).isTrue();
            assertThat(criteria.hasQnaType()).isFalse();
        }
    }

    @Nested
    @DisplayName("ofChildren() - 대댓글 조회 조건 생성")
    class OfChildrenTest {

        @Test
        @DisplayName("대댓글 조회 조건을 생성한다")
        void createChildrenSearchCriteria() {
            // given
            LegacyQnaId parentId = LegacyQnaId.of(1001L);
            CursorQueryContext<QnaSortKey, Long> queryContext =
                    CursorQueryContext.defaultOf(QnaSortKey.defaultKey());

            // when
            QnaSearchCriteria criteria = QnaSearchCriteria.ofChildren(parentId, queryContext);

            // then
            assertThat(criteria.parentId()).isEqualTo(parentId);
            assertThat(criteria.rootOnly()).isFalse();
            assertThat(criteria.memberId()).isNull();
            assertThat(criteria.hasParentId()).isTrue();
            assertThat(criteria.hasMemberId()).isFalse();
        }
    }

    @Nested
    @DisplayName("queryContext가 null인 경우 기본값 적용")
    class DefaultQueryContextTest {

        @Test
        @DisplayName("queryContext가 null이면 기본 컨텍스트가 적용된다")
        void nullQueryContextUsesDefault() {
            // when
            QnaSearchCriteria criteria = new QnaSearchCriteria(
                    LegacyMemberId.of(100L),
                    null,
                    null,
                    null,
                    null,
                    true,
                    null);

            // then
            assertThat(criteria.queryContext()).isNotNull();
            assertThat(criteria.size()).isPositive();
        }
    }

    @Nested
    @DisplayName("조회 조건 확인 메서드 테스트")
    class ConditionCheckTest {

        @Test
        @DisplayName("memberIdValue()는 memberId 값을 반환한다")
        void memberIdValueReturnsLongValue() {
            // given
            QnaSearchCriteria criteria = QnaSearchCriteria.ofMyQnas(
                    LegacyMemberId.of(100L),
                    null,
                    null,
                    null,
                    CursorQueryContext.defaultOf(QnaSortKey.defaultKey()));

            // then
            assertThat(criteria.memberIdValue()).isEqualTo(100L);
        }

        @Test
        @DisplayName("qnaTypeValue()는 QnaType 이름을 반환한다")
        void qnaTypeValueReturnsName() {
            // given
            QnaSearchCriteria criteria = QnaSearchCriteria.ofMyQnas(
                    LegacyMemberId.of(100L),
                    QnaType.ORDER,
                    null,
                    null,
                    CursorQueryContext.defaultOf(QnaSortKey.defaultKey()));

            // then
            assertThat(criteria.qnaTypeValue()).isEqualTo("ORDER");
        }

        @Test
        @DisplayName("memberId가 없으면 memberIdValue()는 null을 반환한다")
        void memberIdValueReturnsNullWhenNoMemberId() {
            // given
            QnaSearchCriteria criteria = QnaSearchCriteria.ofChildren(
                    LegacyQnaId.of(1001L),
                    CursorQueryContext.defaultOf(QnaSortKey.defaultKey()));

            // then
            assertThat(criteria.memberIdValue()).isNull();
            assertThat(criteria.qnaTypeValue()).isNull();
        }

        @Test
        @DisplayName("cursor()는 현재 커서 값을 반환한다")
        void cursorReturnsCurrentCursorValue() {
            // given
            QnaSearchCriteria criteria = QnaSearchCriteria.ofMyQnas(
                    LegacyMemberId.of(100L),
                    null,
                    null,
                    null,
                    CursorQueryContext.defaultOf(QnaSortKey.defaultKey()));

            // then
            assertThat(criteria.hasCursor()).isFalse();
            assertThat(criteria.cursor()).isNull();
        }
    }
}
