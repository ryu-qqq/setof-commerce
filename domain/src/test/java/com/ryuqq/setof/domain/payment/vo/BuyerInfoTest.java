package com.ryuqq.setof.domain.payment.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BuyerInfo Value Object 단위 테스트")
class BuyerInfoTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("이름, 이메일, 전화번호로 구매자 정보를 생성한다")
        void createWithAllFields() {
            // when
            BuyerInfo buyerInfo = BuyerInfo.of("홍길동", "test@example.com", "010-1234-5678");

            // then
            assertThat(buyerInfo.name()).isEqualTo("홍길동");
            assertThat(buyerInfo.email()).isEqualTo("test@example.com");
            assertThat(buyerInfo.phone()).isEqualTo("010-1234-5678");
        }

        @Test
        @DisplayName("이메일과 전화번호 없이 이름만으로 생성할 수 있다")
        void createWithNameOnly() {
            // when
            BuyerInfo buyerInfo = BuyerInfo.of("홍길동", null, null);

            // then
            assertThat(buyerInfo.name()).isEqualTo("홍길동");
            assertThat(buyerInfo.email()).isNull();
            assertThat(buyerInfo.phone()).isNull();
        }

        @Test
        @DisplayName("이름이 null이면 예외가 발생한다")
        void createWithNullName_ThrowsException() {
            assertThatThrownBy(() -> BuyerInfo.of(null, "test@example.com", "010-1234-5678"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("구매자명은 필수입니다");
        }

        @Test
        @DisplayName("이름이 빈 문자열이면 예외가 발생한다")
        void createWithBlankName_ThrowsException() {
            assertThatThrownBy(() -> BuyerInfo.of("  ", "test@example.com", "010-1234-5678"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("구매자명은 필수입니다");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 값이면 동일하다")
        void sameValuesAreEqual() {
            // given
            BuyerInfo b1 = BuyerInfo.of("홍길동", "test@example.com", "010-1234-5678");
            BuyerInfo b2 = BuyerInfo.of("홍길동", "test@example.com", "010-1234-5678");

            // then
            assertThat(b1).isEqualTo(b2);
            assertThat(b1.hashCode()).isEqualTo(b2.hashCode());
        }

        @Test
        @DisplayName("다른 이름이면 동일하지 않다")
        void differentNameNotEqual() {
            // given
            BuyerInfo b1 = BuyerInfo.of("홍길동", "test@example.com", "010-1234-5678");
            BuyerInfo b2 = BuyerInfo.of("김철수", "test@example.com", "010-1234-5678");

            // then
            assertThat(b1).isNotEqualTo(b2);
        }
    }
}
