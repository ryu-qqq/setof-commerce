package com.ryuqq.setof.domain.banner.id;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BannerSlideId Value Object 테스트")
class BannerSlideIdTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("of()로 BannerSlideId를 생성한다")
        void createWithOf() {
            // when
            BannerSlideId id = BannerSlideId.of(1L);

            // then
            assertThat(id.value()).isEqualTo(1L);
            assertThat(id.isNew()).isFalse();
        }

        @Test
        @DisplayName("forNew()로 신규 ID를 생성한다")
        void createWithForNew() {
            // when
            BannerSlideId id = BannerSlideId.forNew();

            // then
            assertThat(id.value()).isNull();
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("of()에 null을 전달하면 예외가 발생한다")
        void ofWithNullThrowsException() {
            // when & then
            assertThatThrownBy(() -> BannerSlideId.of(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("of()에 0을 전달하면 예외가 발생한다")
        void ofWithZeroThrowsException() {
            // when & then
            assertThatThrownBy(() -> BannerSlideId.of(0L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("of()에 음수를 전달하면 예외가 발생한다")
        void ofWithNegativeThrowsException() {
            // when & then
            assertThatThrownBy(() -> BannerSlideId.of(-1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("상태 확인 테스트")
    class StateCheckTest {

        @Test
        @DisplayName("isNew()는 value가 null이면 true를 반환한다")
        void isNewReturnsTrueWhenNull() {
            // given
            BannerSlideId id = BannerSlideId.forNew();

            // then
            assertThat(id.isNew()).isTrue();
        }

        @Test
        @DisplayName("isNew()는 value가 있으면 false를 반환한다")
        void isNewReturnsFalseWhenValueExists() {
            // given
            BannerSlideId id = BannerSlideId.of(100L);

            // then
            assertThat(id.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값을 가진 BannerSlideId는 동등하다")
        void sameValueEquals() {
            // given
            BannerSlideId id1 = BannerSlideId.of(100L);
            BannerSlideId id2 = BannerSlideId.of(100L);

            // then
            assertThat(id1).isEqualTo(id2);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 BannerSlideId는 동등하지 않다")
        void differentValueNotEquals() {
            // given
            BannerSlideId id1 = BannerSlideId.of(100L);
            BannerSlideId id2 = BannerSlideId.of(200L);

            // then
            assertThat(id1).isNotEqualTo(id2);
        }
    }
}
