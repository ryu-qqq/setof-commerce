package com.ryuqq.setof.domain.productgroupimage.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupImageErrorCode 테스트")
class ProductGroupImageErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            // then
            assertThat(ProductGroupImageErrorCode.NO_THUMBNAIL).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("NO_THUMBNAIL 에러 코드 테스트")
    class NoThumbnailErrorCodeTest {

        @Test
        @DisplayName("에러 코드는 'PGI-001'이다")
        void hasCorrectCode() {
            assertThat(ProductGroupImageErrorCode.NO_THUMBNAIL.getCode()).isEqualTo("PGI-001");
        }

        @Test
        @DisplayName("HTTP 상태 코드는 400이다")
        void hasCorrectHttpStatus() {
            assertThat(ProductGroupImageErrorCode.NO_THUMBNAIL.getHttpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("메시지는 '대표 이미지(THUMBNAIL)가 최소 1개 필요합니다'이다")
        void hasCorrectMessage() {
            assertThat(ProductGroupImageErrorCode.NO_THUMBNAIL.getMessage())
                    .isEqualTo("대표 이미지(THUMBNAIL)가 최소 1개 필요합니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(ProductGroupImageErrorCode.values())
                    .containsExactly(ProductGroupImageErrorCode.NO_THUMBNAIL);
        }
    }
}
