package com.ryuqq.setof.domain.navigation.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("NavigationErrorCode 테스트")
class NavigationErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            // then
            assertThat(NavigationErrorCode.NAVIGATION_MENU_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("네비게이션 관련 에러 코드 테스트")
    class NavigationErrorCodesTest {

        @Test
        @DisplayName("NAVIGATION_MENU_NOT_FOUND 에러 코드를 검증한다")
        void navigationMenuNotFound() {
            // then
            assertThat(NavigationErrorCode.NAVIGATION_MENU_NOT_FOUND.getCode())
                    .isEqualTo("NAV-001");
            assertThat(NavigationErrorCode.NAVIGATION_MENU_NOT_FOUND.getHttpStatus())
                    .isEqualTo(404);
            assertThat(NavigationErrorCode.NAVIGATION_MENU_NOT_FOUND.getMessage())
                    .isEqualTo("네비게이션 메뉴를 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(NavigationErrorCode.values())
                    .containsExactly(NavigationErrorCode.NAVIGATION_MENU_NOT_FOUND);
        }
    }
}
