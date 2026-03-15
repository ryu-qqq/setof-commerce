package com.ryuqq.setof.application.banner.service.command;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.banner.BannerCommandFixtures;
import com.ryuqq.setof.application.banner.dto.command.RemoveBannerGroupCommand;
import com.ryuqq.setof.application.banner.factory.BannerGroupCommandFactory;
import com.ryuqq.setof.application.banner.manager.BannerGroupCommandManager;
import com.ryuqq.setof.application.banner.validator.BannerGroupValidator;
import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import com.setof.commerce.domain.banner.BannerFixtures;
import java.time.Instant;
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
@DisplayName("RemoveBannerGroupService 단위 테스트")
class RemoveBannerGroupServiceTest {

    @InjectMocks private RemoveBannerGroupService sut;

    @Mock private BannerGroupCommandFactory bannerGroupCommandFactory;
    @Mock private BannerGroupValidator bannerGroupValidator;
    @Mock private BannerGroupCommandManager bannerGroupCommandManager;

    @Nested
    @DisplayName("execute() - 배너 그룹 삭제")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 배너 그룹을 소프트 삭제하고 저장한다")
        void execute_ValidCommand_RemovesBannerGroupAndPersists() {
            // given
            long bannerGroupId = 1L;
            RemoveBannerGroupCommand command = BannerCommandFixtures.removeCommand(bannerGroupId);

            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            StatusChangeContext<BannerGroupId> context =
                    new StatusChangeContext<>(BannerGroupId.of(bannerGroupId), now);

            BannerGroup bannerGroup = BannerFixtures.activeBannerGroup(bannerGroupId);

            given(bannerGroupCommandFactory.createRemoveContext(command)).willReturn(context);
            given(bannerGroupValidator.findExistingOrThrow(context.id())).willReturn(bannerGroup);
            given(bannerGroupCommandManager.persist(bannerGroup)).willReturn(bannerGroupId);

            // when
            sut.execute(command);

            // then
            then(bannerGroupCommandFactory).should().createRemoveContext(command);
            then(bannerGroupValidator).should().findExistingOrThrow(context.id());
            then(bannerGroupCommandManager).should().persist(bannerGroup);
        }
    }
}
