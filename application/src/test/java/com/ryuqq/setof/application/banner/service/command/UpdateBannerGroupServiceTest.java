package com.ryuqq.setof.application.banner.service.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.banner.BannerCommandFixtures;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerGroupCommand;
import com.ryuqq.setof.application.banner.factory.BannerGroupCommandFactory;
import com.ryuqq.setof.application.banner.manager.BannerGroupCommandManager;
import com.ryuqq.setof.application.banner.validator.BannerGroupValidator;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroupUpdateData;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import com.ryuqq.setof.domain.banner.vo.BannerSlideDiff;
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
@DisplayName("UpdateBannerGroupService 단위 테스트")
class UpdateBannerGroupServiceTest {

    @InjectMocks private UpdateBannerGroupService sut;

    @Mock private BannerGroupCommandFactory bannerGroupCommandFactory;
    @Mock private BannerGroupValidator bannerGroupValidator;
    @Mock private BannerGroupCommandManager bannerGroupCommandManager;

    @Nested
    @DisplayName("execute() - 배너 그룹 수정")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 커맨드로 배너 그룹 정보를 수정하고 저장한다")
        void execute_ValidCommand_UpdatesBannerGroupAndPersists() {
            // given
            long bannerGroupId = 1L;
            UpdateBannerGroupCommand command = BannerCommandFixtures.updateCommand(bannerGroupId);

            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            BannerGroupUpdateData updateData = BannerFixtures.defaultBannerGroupUpdateData();
            UpdateContext<BannerGroupId, BannerGroupUpdateData> context =
                    new UpdateContext<>(BannerGroupId.of(bannerGroupId), updateData, now);

            BannerGroup bannerGroup = BannerFixtures.activeBannerGroup(bannerGroupId);

            given(bannerGroupCommandFactory.createUpdateContext(command)).willReturn(context);
            given(bannerGroupValidator.findExistingOrThrow(context.id())).willReturn(bannerGroup);
            given(bannerGroupCommandManager.persist(bannerGroup)).willReturn(bannerGroupId);

            // when
            sut.execute(command);

            // then
            then(bannerGroupCommandFactory).should().createUpdateContext(command);
            then(bannerGroupValidator).should().findExistingOrThrow(context.id());
            then(bannerGroupCommandManager).should().persist(bannerGroup);
        }

        @Test
        @DisplayName("그룹 전용 수정이므로 persistSlideDiff를 호출하지 않는다")
        void execute_GroupOnlyUpdate_NoPersistSlideDiffCalled() {
            // given
            long bannerGroupId = 2L;
            UpdateBannerGroupCommand command = BannerCommandFixtures.updateCommand(bannerGroupId);

            Instant now = Instant.parse("2025-06-01T00:00:00Z");
            BannerGroupUpdateData updateData = BannerFixtures.defaultBannerGroupUpdateData();
            UpdateContext<BannerGroupId, BannerGroupUpdateData> context =
                    new UpdateContext<>(BannerGroupId.of(bannerGroupId), updateData, now);

            BannerGroup bannerGroup = BannerFixtures.activeBannerGroup(bannerGroupId);

            given(bannerGroupCommandFactory.createUpdateContext(command)).willReturn(context);
            given(bannerGroupValidator.findExistingOrThrow(context.id())).willReturn(bannerGroup);
            given(bannerGroupCommandManager.persist(bannerGroup)).willReturn(bannerGroupId);

            // when
            sut.execute(command);

            // then
            then(bannerGroupCommandFactory).should().createUpdateContext(command);
            then(bannerGroupValidator).should().findExistingOrThrow(context.id());
            then(bannerGroupCommandManager).should().persist(bannerGroup);
            then(bannerGroupCommandManager)
                    .should(never())
                    .persistSlideDiff(any(Long.class), any(BannerSlideDiff.class));
        }
    }
}
