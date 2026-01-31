package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("CursorPageRequest Value Object 테스트")
class CursorPageRequestTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 CursorPageRequest를 생성한다")
        void createWithOf() {
            // when
            CursorPageRequest<Long> request = CursorPageRequest.of(100L, 20);

            // then
            assertThat(request.cursor()).isEqualTo(100L);
            assertThat(request.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("first()로 첫 페이지 요청을 생성한다")
        void createFirst() {
            // when
            CursorPageRequest<Long> request = CursorPageRequest.first(25);

            // then
            assertThat(request.cursor()).isNull();
            assertThat(request.size()).isEqualTo(25);
            assertThat(request.isFirstPage()).isTrue();
        }

        @Test
        @DisplayName("defaultPage()로 기본 설정 요청을 생성한다")
        void createDefaultPage() {
            // when
            CursorPageRequest<Long> request = CursorPageRequest.defaultPage();

            // then
            assertThat(request.cursor()).isNull();
            assertThat(request.size()).isEqualTo(CursorPageRequest.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("afterId()로 Long ID 기반 커서 요청을 생성한다")
        void createAfterId() {
            // when
            CursorPageRequest<Long> request = CursorPageRequest.afterId(50L, 30);

            // then
            assertThat(request.cursor()).isEqualTo(50L);
            assertThat(request.size()).isEqualTo(30);
        }

        @Test
        @DisplayName("ofString()으로 String 기반 커서 요청을 생성한다")
        void createOfString() {
            // when
            CursorPageRequest<String> request = CursorPageRequest.ofString("cursor-abc", 20);

            // then
            assertThat(request.cursor()).isEqualTo("cursor-abc");
            assertThat(request.size()).isEqualTo(20);
        }
    }

    @Nested
    @DisplayName("유효성 검증 테스트")
    class ValidationTest {

        @Test
        @DisplayName("0 이하 size는 기본값으로 정규화된다")
        void zeroSizeNormalizesToDefault() {
            // when
            CursorPageRequest<Long> request = CursorPageRequest.of(100L, 0);

            // then
            assertThat(request.size()).isEqualTo(CursorPageRequest.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("음수 size는 기본값으로 정규화된다")
        void negativeSizeNormalizesToDefault() {
            // when
            CursorPageRequest<Long> request = CursorPageRequest.of(100L, -10);

            // then
            assertThat(request.size()).isEqualTo(CursorPageRequest.DEFAULT_SIZE);
        }

        @Test
        @DisplayName("MAX_SIZE 초과 size는 MAX_SIZE로 정규화된다")
        void exceedingMaxSizeNormalizesToMax() {
            // when
            CursorPageRequest<Long> request = CursorPageRequest.of(100L, 200);

            // then
            assertThat(request.size()).isEqualTo(CursorPageRequest.MAX_SIZE);
        }

        @Test
        @DisplayName("빈 문자열 커서는 null로 정규화된다")
        void blankStringCursorNormalizesToNull() {
            // when
            CursorPageRequest<String> request = CursorPageRequest.ofString("   ", 20);

            // then
            assertThat(request.cursor()).isNull();
            assertThat(request.isFirstPage()).isTrue();
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("isFirstPage()는 커서가 null이면 true를 반환한다")
        void isFirstPageReturnsTrueWhenNoCursor() {
            // given
            CursorPageRequest<Long> request = CursorPageRequest.first(20);

            // then
            assertThat(request.isFirstPage()).isTrue();
        }

        @Test
        @DisplayName("isFirstPage()는 커서가 있으면 false를 반환한다")
        void isFirstPageReturnsFalseWhenCursorExists() {
            // given
            CursorPageRequest<Long> request = CursorPageRequest.afterId(100L, 20);

            // then
            assertThat(request.isFirstPage()).isFalse();
        }

        @Test
        @DisplayName("hasCursor()는 커서가 있으면 true를 반환한다")
        void hasCursorReturnsTrueWhenCursorExists() {
            // given
            CursorPageRequest<Long> request = CursorPageRequest.of(100L, 20);

            // then
            assertThat(request.hasCursor()).isTrue();
        }

        @Test
        @DisplayName("hasCursor()는 커서가 없으면 false를 반환한다")
        void hasCursorReturnsFalseWhenNoCursor() {
            // given
            CursorPageRequest<Long> request = CursorPageRequest.first(20);

            // then
            assertThat(request.hasCursor()).isFalse();
        }
    }

    @Nested
    @DisplayName("네비게이션 테스트")
    class NavigationTest {

        @Test
        @DisplayName("next()로 다음 페이지 요청을 생성한다")
        void createsNextPage() {
            // given
            CursorPageRequest<Long> current = CursorPageRequest.afterId(100L, 20);

            // when
            CursorPageRequest<Long> next = current.next(200L);

            // then
            assertThat(next.cursor()).isEqualTo(200L);
            assertThat(next.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("fetchSize()는 hasNext 판단용 +1 값을 반환한다")
        void fetchSizeReturnsIncremented() {
            // given
            CursorPageRequest<Long> request = CursorPageRequest.of(100L, 20);

            // when
            int fetchSize = request.fetchSize();

            // then
            assertThat(fetchSize).isEqualTo(21);
        }
    }
}
