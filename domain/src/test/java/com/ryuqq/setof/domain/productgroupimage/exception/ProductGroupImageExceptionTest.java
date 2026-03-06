package com.ryuqq.setof.domain.productgroupimage.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupImageException 테스트")
class ProductGroupImageExceptionTest {

    @Nested
    @DisplayName("기본 생성 테스트")
    class CreationTest {

        @Test
        @DisplayName("ErrorCode로 예외를 생성한다")
        void createWithErrorCode() {
            // when
            ProductGroupImageException exception =
                    new ProductGroupImageException(ProductGroupImageErrorCode.NO_THUMBNAIL);

            // then
            assertThat(exception.getMessage()).isEqualTo("대표 이미지(THUMBNAIL)가 최소 1개 필요합니다");
            assertThat(exception.code()).isEqualTo("PGI-001");
            assertThat(exception.httpStatus()).isEqualTo(400);
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지로 예외를 생성한다")
        void createWithErrorCodeAndMessage() {
            // when
            ProductGroupImageException exception =
                    new ProductGroupImageException(
                            ProductGroupImageErrorCode.NO_THUMBNAIL, "THUMBNAIL이 없습니다");

            // then
            assertThat(exception.getMessage()).isEqualTo("THUMBNAIL이 없습니다");
            assertThat(exception.code()).isEqualTo("PGI-001");
        }

        @Test
        @DisplayName("ErrorCode와 커스텀 메시지 및 args로 예외를 생성한다")
        void createWithErrorCodeAndMessageAndArgs() {
            // given
            Map<String, Object> args = Map.of("thumbnailCount", 0L);

            // when
            ProductGroupImageException exception =
                    new ProductGroupImageException(
                            ProductGroupImageErrorCode.NO_THUMBNAIL, "THUMBNAIL이 0개입니다", args);

            // then
            assertThat(exception.getMessage()).isEqualTo("THUMBNAIL이 0개입니다");
            assertThat(exception.code()).isEqualTo("PGI-001");
            assertThat(exception.args()).containsKey("thumbnailCount");
        }

        @Test
        @DisplayName("ErrorCode와 원인 예외로 예외를 생성한다")
        void createWithErrorCodeAndCause() {
            // given
            RuntimeException cause = new RuntimeException("원인 예외");

            // when
            ProductGroupImageException exception =
                    new ProductGroupImageException(ProductGroupImageErrorCode.NO_THUMBNAIL, cause);

            // then
            assertThat(exception.getCause()).isEqualTo(cause);
            assertThat(exception.code()).isEqualTo("PGI-001");
        }
    }

    @Nested
    @DisplayName("구체적 예외 클래스 테스트")
    class ConcreteExceptionTest {

        @Test
        @DisplayName("ProductGroupImageNoThumbnailException - 썸네일 개수로 생성한다")
        void createNoThumbnailExceptionWithCount() {
            // when
            ProductGroupImageNoThumbnailException exception =
                    new ProductGroupImageNoThumbnailException(0L);

            // then
            assertThat(exception.code()).isEqualTo("PGI-001");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("THUMBNAIL 이미지가 정확히 1개 필요합니다 (현재 0개)");
        }

        @Test
        @DisplayName("ProductGroupImageNoThumbnailException - THUMBNAIL 중복 시 2개로 생성한다")
        void createNoThumbnailExceptionWithDuplicateCount() {
            // when
            ProductGroupImageNoThumbnailException exception =
                    new ProductGroupImageNoThumbnailException(2L);

            // then
            assertThat(exception.code()).isEqualTo("PGI-001");
            assertThat(exception.getMessage()).isEqualTo("THUMBNAIL 이미지가 정확히 1개 필요합니다 (현재 2개)");
        }

        @Test
        @DisplayName("ProductGroupImageNoThumbnailException - 상품그룹 ID로 생성한다")
        void createNoThumbnailExceptionWithProductGroupId() {
            // when
            ProductGroupImageNoThumbnailException exception =
                    new ProductGroupImageNoThumbnailException(99L);

            // then
            assertThat(exception.code()).isEqualTo("PGI-001");
            assertThat(exception.httpStatus()).isEqualTo(400);
            assertThat(exception.getMessage()).isEqualTo("THUMBNAIL 이미지가 정확히 1개 필요합니다 (현재 99개)");
        }
    }

    @Nested
    @DisplayName("상속 관계 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("ProductGroupImageException은 DomainException을 상속한다")
        void productGroupImageExceptionExtendsDomainException() {
            // given
            ProductGroupImageException exception =
                    new ProductGroupImageException(ProductGroupImageErrorCode.NO_THUMBNAIL);

            // then
            assertThat(exception).isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("ProductGroupImageNoThumbnailException은 ProductGroupImageException을 상속한다")
        void noThumbnailExceptionExtendsProductGroupImageException() {
            // given
            ProductGroupImageNoThumbnailException exception =
                    new ProductGroupImageNoThumbnailException(0L);

            // then
            assertThat(exception).isInstanceOf(ProductGroupImageException.class);
            assertThat(exception).isInstanceOf(DomainException.class);
        }
    }
}
