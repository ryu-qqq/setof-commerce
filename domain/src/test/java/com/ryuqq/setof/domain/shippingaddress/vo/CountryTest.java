package com.ryuqq.setof.domain.shippingaddress.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Country Enum 단위 테스트")
class CountryTest {

    @Nested
    @DisplayName("displayName() 테스트")
    class DisplayNameTest {

        @Test
        @DisplayName("KR의 표시명은 '대한민국'이다")
        void krDisplayName() {
            assertThat(Country.KR.displayName()).isEqualTo("대한민국");
        }

        @Test
        @DisplayName("US의 표시명은 '미국'이다")
        void usDisplayName() {
            assertThat(Country.US.displayName()).isEqualTo("미국");
        }

        @Test
        @DisplayName("JP의 표시명은 '일본'이다")
        void jpDisplayName() {
            assertThat(Country.JP.displayName()).isEqualTo("일본");
        }

        @Test
        @DisplayName("CN의 표시명은 '중국'이다")
        void cnDisplayName() {
            assertThat(Country.CN.displayName()).isEqualTo("중국");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 Country 값이 존재한다")
        void allValuesExist() {
            assertThat(Country.values())
                    .containsExactly(Country.KR, Country.US, Country.JP, Country.CN);
        }

        @Test
        @DisplayName("name()으로 문자열에서 Country를 조회할 수 있다")
        void valueOfByName() {
            assertThat(Country.valueOf("KR")).isEqualTo(Country.KR);
            assertThat(Country.valueOf("US")).isEqualTo(Country.US);
        }
    }
}
