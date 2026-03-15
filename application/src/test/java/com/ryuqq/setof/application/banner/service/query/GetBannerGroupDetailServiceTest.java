package com.ryuqq.setof.application.banner.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.banner.manager.BannerGroupReadManager;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.setof.commerce.domain.banner.BannerFixtures;
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
@DisplayName("GetBannerGroupDetailService 단위 테스트")
class GetBannerGroupDetailServiceTest {

    @InjectMocks private GetBannerGroupDetailService sut;

    @Mock private BannerGroupReadManager readManager;

    @Nested
    @DisplayName("execute() - 배너 그룹 상세 조회")
    class ExecuteTest {

        @Test
        @DisplayName("배너 그룹 ID로 상세 정보를 조회하고 도메인 객체를 반환한다")
        void execute_ValidId_ReturnsBannerGroup() {
            // given
            long bannerGroupId = 1L;
            BannerGroup expected = BannerFixtures.activeBannerGroup(bannerGroupId);

            given(readManager.getById(bannerGroupId)).willReturn(expected);

            // when
            BannerGroup result = sut.execute(bannerGroupId);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.idValue()).isEqualTo(bannerGroupId);
            then(readManager).should().getById(bannerGroupId);
        }

        @Test
        @DisplayName("ReadManager에게 조회를 위임하고 결과를 그대로 반환한다")
        void execute_DelegatesToReadManager_ReturnsResult() {
            // given
            long bannerGroupId = 2L;
            BannerGroup expected = BannerFixtures.activeBannerGroup(bannerGroupId);

            given(readManager.getById(bannerGroupId)).willReturn(expected);

            // when
            BannerGroup result = sut.execute(bannerGroupId);

            // then
            assertThat(result).isSameAs(expected);
            then(readManager).should().getById(bannerGroupId);
            then(readManager).shouldHaveNoMoreInteractions();
        }
    }
}
