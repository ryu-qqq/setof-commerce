package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.error;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.common.mapper.ErrorMapper.MappedError;
import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.discount.exception.DiscountErrorCode;
import com.ryuqq.setof.domain.discount.exception.DiscountException;
import com.ryuqq.setof.domain.discount.exception.DiscountPolicyNotFoundException;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * DiscountErrorMapper 단위 테스트.
 *
 * <p>할인 도메인 예외를 HTTP 응답으로 변환하는 매퍼를 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("DiscountErrorMapper 단위 테스트")
class DiscountErrorMapperTest {

    private DiscountErrorMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DiscountErrorMapper();
    }

    @Nested
    @DisplayName("supports(DomainException)")
    class SupportsTest {

        @Test
        @DisplayName("DiscountException 인스턴스를 지원한다")
        void supports_DiscountException_True() {
            // given
            DiscountException exception =
                    new DiscountException(DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND);

            // when
            boolean result = mapper.supports(exception);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("DiscountException 하위 클래스를 지원한다")
        void supports_DiscountExceptionSubclass_True() {
            // given
            DiscountPolicyNotFoundException exception = new DiscountPolicyNotFoundException(1L);

            // when
            boolean result = mapper.supports(exception);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("다른 DomainException은 지원하지 않는다")
        void supports_OtherDomainException_False() {
            // given
            DomainException otherException = new OtherDomainException();

            // when
            boolean result = mapper.supports(otherException);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("map(DomainException, Locale)")
    class MapTest {

        @Test
        @DisplayName("DISCOUNT_POLICY_NOT_FOUND를 404 상태로 변환한다")
        void map_PolicyNotFound_404() {
            // given
            DiscountPolicyNotFoundException exception = new DiscountPolicyNotFoundException(1L);

            // when
            MappedError error = mapper.map(exception, Locale.KOREA);

            // then
            assertThat(error.status()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("INVALID_DISCOUNT_CONFIG를 400 상태로 변환한다")
        void map_InvalidConfig_400() {
            // given
            DiscountException exception =
                    new DiscountException(DiscountErrorCode.INVALID_DISCOUNT_CONFIG);

            // when
            MappedError error = mapper.map(exception, Locale.KOREA);

            // then
            assertThat(error.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("title은 'Discount Error'로 고정 반환한다")
        void map_Title_FixedToDiscountError() {
            // given
            DiscountException exception =
                    new DiscountException(DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND);

            // when
            MappedError error = mapper.map(exception, Locale.KOREA);

            // then
            assertThat(error.title()).isEqualTo("Discount Error");
        }

        @Test
        @DisplayName("detail은 예외 메시지를 그대로 반환한다")
        void map_Detail_ExceptionMessage() {
            // given
            DiscountPolicyNotFoundException exception = new DiscountPolicyNotFoundException(42L);

            // when
            MappedError error = mapper.map(exception, Locale.KOREA);

            // then
            assertThat(error.detail()).isEqualTo(exception.getMessage());
        }

        @Test
        @DisplayName("type URI는 /errors/discount/ 접두사로 시작한다")
        void map_TypeUri_StartsWithErrorsDiscount() {
            // given
            DiscountException exception =
                    new DiscountException(DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND);

            // when
            MappedError error = mapper.map(exception, Locale.KOREA);

            // then
            assertThat(error.type().toString()).startsWith("/errors/discount/");
        }

        @Test
        @DisplayName("type URI는 에러 코드(소문자)를 포함한다")
        void map_TypeUri_ContainsLowerCaseCode() {
            // given
            DiscountException exception =
                    new DiscountException(DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND);
            String expectedCode = DiscountErrorCode.DISCOUNT_POLICY_NOT_FOUND.getCode().toLowerCase();

            // when
            MappedError error = mapper.map(exception, Locale.KOREA);

            // then
            assertThat(error.type().toString()).contains(expectedCode);
        }

        @Test
        @DisplayName("INSUFFICIENT_BUDGET를 400 상태로 변환한다")
        void map_InsufficientBudget_400() {
            // given
            DiscountException exception =
                    new DiscountException(DiscountErrorCode.INSUFFICIENT_BUDGET);

            // when
            MappedError error = mapper.map(exception, Locale.KOREA);

            // then
            assertThat(error.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 테스트용 다른 도메인 예외 - DiscountException이 아닌 DomainException.
     */
    private static class OtherDomainException extends DomainException {

        private static final com.ryuqq.setof.domain.common.exception.ErrorCode OTHER_CODE =
                new com.ryuqq.setof.domain.common.exception.ErrorCode() {
                    @Override
                    public String getCode() {
                        return "OTHER-001";
                    }

                    @Override
                    public int getHttpStatus() {
                        return 400;
                    }

                    @Override
                    public String getMessage() {
                        return "Other error";
                    }
                };

        public OtherDomainException() {
            super(OTHER_CODE);
        }
    }
}
