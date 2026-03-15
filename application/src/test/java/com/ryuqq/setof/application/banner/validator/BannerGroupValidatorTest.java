package com.ryuqq.setof.application.banner.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.banner.port.out.BannerGroupQueryPort;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.exception.BannerGroupNotFoundException;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import com.setof.commerce.domain.banner.BannerFixtures;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("BannerGroupValidator 단위 테스트")
class BannerGroupValidatorTest {

    @InjectMocks private BannerGroupValidator sut;

    @Mock private BannerGroupQueryPort bannerGroupQueryPort;

    @Nested
    @DisplayName("findExistingOrThrow() - 배너 그룹 조회 또는 예외 발생")
    class FindExistingOrThrowTest {

        @Test
        @DisplayName("존재하는 배너 그룹을 반환한다")
        void findExistingOrThrow_ExistingBannerGroup_ReturnsBannerGroup() {
            // given
            BannerGroupId bannerGroupId = BannerGroupId.of(1L);
            BannerGroup expected = BannerFixtures.activeBannerGroup(1L);

            given(bannerGroupQueryPort.findById(bannerGroupId.value()))
                    .willReturn(Optional.of(expected));

            // when
            BannerGroup result = sut.findExistingOrThrow(bannerGroupId);

            // then
            assertThat(result).isEqualTo(expected);
            then(bannerGroupQueryPort).should().findById(bannerGroupId.value());
        }

        @Test
        @DisplayName("배너 그룹이 존재하지 않으면 BannerGroupNotFoundException이 발생한다")
        void findExistingOrThrow_NonExistingBannerGroup_ThrowsException() {
            // given
            BannerGroupId bannerGroupId = BannerGroupId.of(999L);

            given(bannerGroupQueryPort.findById(bannerGroupId.value()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.findExistingOrThrow(bannerGroupId))
                    .isInstanceOf(BannerGroupNotFoundException.class);
        }
    }
}
