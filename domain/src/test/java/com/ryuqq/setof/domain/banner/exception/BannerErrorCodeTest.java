package com.ryuqq.setof.domain.banner.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BannerErrorCode 테스트")
class BannerErrorCodeTest {

    @Nested
    @DisplayName("ErrorCode 인터페이스 구현 테스트")
    class ErrorCodeInterfaceTest {

        @Test
        @DisplayName("ErrorCode 인터페이스를 구현한다")
        void implementsErrorCode() {
            // then
            assertThat(BannerErrorCode.BANNER_GROUP_NOT_FOUND).isInstanceOf(ErrorCode.class);
        }
    }

    @Nested
    @DisplayName("배너 관련 에러 코드 테스트")
    class BannerErrorCodesTest {

        @Test
        @DisplayName("BANNER_GROUP_NOT_FOUND 에러 코드를 검증한다")
        void bannerGroupNotFound() {
            // then
            assertThat(BannerErrorCode.BANNER_GROUP_NOT_FOUND.getCode()).isEqualTo("BNR-001");
            assertThat(BannerErrorCode.BANNER_GROUP_NOT_FOUND.getHttpStatus()).isEqualTo(404);
            assertThat(BannerErrorCode.BANNER_GROUP_NOT_FOUND.getMessage())
                    .isEqualTo("배너 그룹을 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 에러 코드 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(BannerErrorCode.values())
                    .containsExactly(BannerErrorCode.BANNER_GROUP_NOT_FOUND);
        }
    }
}
