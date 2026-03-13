package com.ryuqq.setof.domain.contentpage.query;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ContentPageSearchCriteria 테스트")
class ContentPageSearchCriteriaTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("contentPageId와 bypass로 검색 조건을 생성한다")
        void createWithAllParameters() {
            // given
            Long contentPageId = 1L;
            boolean bypass = false;

            // when
            ContentPageSearchCriteria criteria =
                    new ContentPageSearchCriteria(contentPageId, bypass);

            // then
            assertThat(criteria.contentPageId()).isEqualTo(contentPageId);
            assertThat(criteria.bypass()).isFalse();
        }

        @Test
        @DisplayName("contentPageId가 null인 검색 조건을 생성한다")
        void createWithNullContentPageId() {
            // when
            ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(null, false);

            // then
            assertThat(criteria.contentPageId()).isNull();
        }

        @Test
        @DisplayName("bypass가 true이면 삭제된 항목을 포함한다")
        void createWithBypassTrue() {
            // when
            ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(null, true);

            // then
            assertThat(criteria.bypass()).isTrue();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값이면 동등하다")
        void sameValuesAreEqual() {
            // given
            ContentPageSearchCriteria criteria1 = new ContentPageSearchCriteria(1L, false);
            ContentPageSearchCriteria criteria2 = new ContentPageSearchCriteria(1L, false);

            // then
            assertThat(criteria1).isEqualTo(criteria2);
            assertThat(criteria1.hashCode()).isEqualTo(criteria2.hashCode());
        }

        @Test
        @DisplayName("다른 값이면 동등하지 않다")
        void differentValuesAreNotEqual() {
            // given
            ContentPageSearchCriteria criteria1 = new ContentPageSearchCriteria(1L, false);
            ContentPageSearchCriteria criteria2 = new ContentPageSearchCriteria(2L, false);

            // then
            assertThat(criteria1).isNotEqualTo(criteria2);
        }

        @Test
        @DisplayName("bypass 값이 다르면 동등하지 않다")
        void differentBypassAreNotEqual() {
            // given
            ContentPageSearchCriteria criteria1 = new ContentPageSearchCriteria(1L, false);
            ContentPageSearchCriteria criteria2 = new ContentPageSearchCriteria(1L, true);

            // then
            assertThat(criteria1).isNotEqualTo(criteria2);
        }
    }
}
