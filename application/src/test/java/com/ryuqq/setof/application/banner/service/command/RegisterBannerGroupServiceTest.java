package com.ryuqq.setof.application.banner.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.banner.BannerCommandFixtures;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerGroupCommand;
import com.ryuqq.setof.application.banner.factory.BannerGroupCommandFactory;
import com.ryuqq.setof.application.banner.manager.BannerGroupCommandManager;
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
@DisplayName("RegisterBannerGroupService 단위 테스트")
class RegisterBannerGroupServiceTest {

    @InjectMocks private RegisterBannerGroupService sut;

    @Mock private BannerGroupCommandFactory bannerGroupCommandFactory;
    @Mock private BannerGroupCommandManager bannerGroupCommandManager;

    @Nested
    @DisplayName("execute() - 배너 그룹 등록")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 배너 그룹을 등록하고 배너 그룹 ID를 반환한다")
        void execute_ValidCommand_ReturnsBannerGroupId() {
            // given
            RegisterBannerGroupCommand command = BannerCommandFixtures.registerCommand();
            BannerGroup bannerGroup = BannerFixtures.newBannerGroup();
            Long expectedId = 1L;

            given(bannerGroupCommandFactory.create(command)).willReturn(bannerGroup);
            given(bannerGroupCommandManager.persistWithSlides(bannerGroup)).willReturn(expectedId);

            // when
            Long result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(bannerGroupCommandFactory).should().create(command);
            then(bannerGroupCommandManager).should().persistWithSlides(bannerGroup);
        }
    }
}
