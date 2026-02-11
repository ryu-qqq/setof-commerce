package com.ryuqq.setof.domain.productgroup.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupErrorCode 테스트")
class ProductGroupErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            // then
            assertThat(ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("PRODUCT_GROUP_NOT_FOUND 테스트")
    class ProductGroupNotFoundTest {

        @Test
        @DisplayName("에러 코드는 'PG-001'이다")
        void hasCorrectCode() {
            assertThat(ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND.getCode()).isEqualTo("PG-001");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 404이다")
        void hasCorrectHttpStatus() {
            assertThat(ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND.getHttpStatus())
                    .isEqualTo(404);
        }

        @Test
        @DisplayName("메시지는 '상품그룹을 찾을 수 없습니다'이다")
        void hasCorrectMessage() {
            assertThat(ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND.getMessage())
                    .isEqualTo("상품그룹을 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("PRODUCT_GROUP_ALREADY_DELETED 테스트")
    class ProductGroupAlreadyDeletedTest {

        @Test
        @DisplayName("에러 코드는 'PG-002'이다")
        void hasCorrectCode() {
            assertThat(ProductGroupErrorCode.PRODUCT_GROUP_ALREADY_DELETED.getCode())
                    .isEqualTo("PG-002");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(ProductGroupErrorCode.PRODUCT_GROUP_ALREADY_DELETED.getHttpStatus())
                    .isEqualTo(400);
        }

        @Test
        @DisplayName("메시지는 '이미 삭제된 상품그룹입니다'이다")
        void hasCorrectMessage() {
            assertThat(ProductGroupErrorCode.PRODUCT_GROUP_ALREADY_DELETED.getMessage())
                    .isEqualTo("이미 삭제된 상품그룹입니다");
        }
    }

    @Nested
    @DisplayName("INVALID_STATUS_TRANSITION 테스트")
    class InvalidStatusTransitionTest {

        @Test
        @DisplayName("에러 코드는 'PG-003'이다")
        void hasCorrectCode() {
            assertThat(ProductGroupErrorCode.INVALID_STATUS_TRANSITION.getCode())
                    .isEqualTo("PG-003");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(ProductGroupErrorCode.INVALID_STATUS_TRANSITION.getHttpStatus())
                    .isEqualTo(400);
        }

        @Test
        @DisplayName("메시지는 '유효하지 않은 상태 전이입니다'이다")
        void hasCorrectMessage() {
            assertThat(ProductGroupErrorCode.INVALID_STATUS_TRANSITION.getMessage())
                    .isEqualTo("유효하지 않은 상태 전이입니다");
        }
    }

    @Nested
    @DisplayName("INVALID_PRICE 테스트")
    class InvalidPriceTest {

        @Test
        @DisplayName("에러 코드는 'PG-004'이다")
        void hasCorrectCode() {
            assertThat(ProductGroupErrorCode.INVALID_PRICE.getCode()).isEqualTo("PG-004");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(ProductGroupErrorCode.INVALID_PRICE.getHttpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("메시지는 '유효하지 않은 가격 정보입니다'이다")
        void hasCorrectMessage() {
            assertThat(ProductGroupErrorCode.INVALID_PRICE.getMessage())
                    .isEqualTo("유효하지 않은 가격 정보입니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(ProductGroupErrorCode.values())
                    .containsExactly(
                            ProductGroupErrorCode.PRODUCT_GROUP_NOT_FOUND,
                            ProductGroupErrorCode.PRODUCT_GROUP_ALREADY_DELETED,
                            ProductGroupErrorCode.INVALID_STATUS_TRANSITION,
                            ProductGroupErrorCode.INVALID_PRICE);
        }
    }
}
