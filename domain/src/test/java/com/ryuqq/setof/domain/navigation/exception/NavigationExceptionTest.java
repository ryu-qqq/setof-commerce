package com.ryuqq.setof.domain.navigation.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("NavigationException 테스트")
class NavigationExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            NavigationException exception =
                    new NavigationException(NavigationErrorCode.NAVIGATION_MENU_NOT_FOUND);

            // then
            assertThat(exception.getMessage()).isEqualTo("네비게이션 메뉴를 찾을 수 없습니다");
            assertThat(exception.code()).isEqualTo("NAV-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            NavigationException exception =
                    new NavigationException(
                            NavigationErrorCode.NAVIGATION_MENU_NOT_FOUND, "ID 100 네비게이션 메뉴 없음");

            // then
            assertThat(exception.getMessage()).isEqualTo("ID 100 네비게이션 메뉴 없음");
            assertThat(exception.code()).isEqualTo("NAV-001");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            NavigationException exception =
                    new NavigationException(NavigationErrorCode.NAVIGATION_MENU_NOT_FOUND, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("NAV-001");
        }
    }

    @Nested
    @DisplayName("구체적 예외 클래스 테스트")
    class ConcreteExceptionTest {

        @Test
        @DisplayName("NavigationMenuNotFoundException 기본 생성")
        void createNavigationMenuNotFoundException() {
            // when
            NavigationMenuNotFoundException exception = new NavigationMenuNotFoundException();

            // then
            assertThat(exception.code()).isEqualTo("NAV-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).isEqualTo("네비게이션 메뉴를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("NavigationMenuNotFoundException 상세 메시지 포함 생성")
        void createNavigationMenuNotFoundExceptionWithDetail() {
            // when
            NavigationMenuNotFoundException exception =
                    new NavigationMenuNotFoundException("ID: 999");

            // then
            assertThat(exception.code()).isEqualTo("NAV-001");
            assertThat(exception.httpStatus()).isEqualTo(404);
            assertThat(exception.getMessage()).contains("네비게이션 메뉴를 찾을 수 없습니다");
            assertThat(exception.getMessage()).contains("ID: 999");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("NavigationException은 DomainException을 상속한다")
        void navigationExceptionExtendsDomainException() {
            // given
            NavigationException exception =
                    new NavigationException(NavigationErrorCode.NAVIGATION_MENU_NOT_FOUND);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("NavigationMenuNotFoundException은 NavigationException을 상속한다")
        void navigationMenuNotFoundExceptionExtendsNavigationException() {
            // given
            NavigationMenuNotFoundException exception = new NavigationMenuNotFoundException();

            // then
            assertThat(exception).isInstanceOf(NavigationException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
