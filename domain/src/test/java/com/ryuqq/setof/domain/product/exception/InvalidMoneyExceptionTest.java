package com.ryuqq.setof.domain.product.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** InvalidMoneyException 단위 테스트 */
@DisplayName("InvalidMoneyException 단위 테스트")
class InvalidMoneyExceptionTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("성공 - value와 reason으로 예외를 생성한다")
        void shouldCreateWithValueAndReason() {
            // Given
            BigDecimal value = BigDecimal.valueOf(-1000);
            String reason = "금액은 음수일 수 없습니다";

            // When
            InvalidMoneyException exception = new InvalidMoneyException(value, reason);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 금액");
            assertThat(exception.getMessage()).contains("-1000");
            assertThat(exception.getMessage()).contains(reason);
            assertThat(exception.getErrorCode()).isEqualTo(ProductGroupErrorCode.INVALID_MONEY);
        }

        @Test
        @DisplayName("성공 - null value로 예외를 생성한다")
        void shouldCreateWithNullValue() {
            // Given
            String reason = "금액은 null일 수 없습니다";

            // When
            InvalidMoneyException exception = new InvalidMoneyException(null, reason);

            // Then
            assertThat(exception.getMessage()).contains("유효하지 않은 금액");
            assertThat(exception.getMessage()).contains("null");
            assertThat(exception.getMessage()).contains(reason);
            assertThat(exception.getErrorCode()).isEqualTo(ProductGroupErrorCode.INVALID_MONEY);
        }
    }
}
