package com.ryuqq.setof.application.banner.service.command;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.banner.BannerCommandFixtures;
import com.ryuqq.setof.application.banner.dto.command.ChangeBannerGroupStatusCommand;
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
@DisplayName("ChangeBannerGroupStatusService 단위 테스트")
class ChangeBannerGroupStatusServiceTest {

    @InjectMocks private ChangeBannerGroupStatusService sut;

    @Mock private BannerGroupCommandFactory bannerGroupCommandFactory;
    @Mock private BannerGroupValidator bannerGroupValidator;
    @Mock private BannerGroupCommandManager bannerGroupCommandManager;

    @Nested
    @DisplayName("execute() - 배너 그룹 노출 상태 변경")
    class ExecuteTest {

        @Test
        @DisplayName("활성화 커맨드로 배너 그룹의 노출 상태를 변경하고 저장한다")
        void execute_ActivateCommand_ChangesStatusAndPersists() {
            // given
            long bannerGroupId = 1L;
            ChangeBannerGroupStatusCommand command =
                    BannerCommandFixtures.activateCommand(bannerGroupId);

            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            StatusChangeContext<BannerGroupId> context =
                    new StatusChangeContext<>(BannerGroupId.of(bannerGroupId), now);

            BannerGroup bannerGroup = BannerFixtures.inactiveBannerGroup();

            given(bannerGroupCommandFactory.createStatusChangeContext(command)).willReturn(context);
            given(bannerGroupValidator.findExistingOrThrow(context.id())).willReturn(bannerGroup);
            given(bannerGroupCommandManager.persist(bannerGroup)).willReturn(bannerGroupId);

            // when
            sut.execute(command);

            // then
            then(bannerGroupCommandFactory).should().createStatusChangeContext(command);
            then(bannerGroupValidator).should().findExistingOrThrow(context.id());
            then(bannerGroupCommandManager).should().persist(bannerGroup);
        }

        @Test
        @DisplayName("비활성화 커맨드로 배너 그룹의 노출 상태를 변경하고 저장한다")
        void execute_DeactivateCommand_ChangesStatusAndPersists() {
            // given
            long bannerGroupId = 1L;
            ChangeBannerGroupStatusCommand command =
                    BannerCommandFixtures.deactivateCommand(bannerGroupId);

            Instant now = Instant.parse("2025-01-01T00:00:00Z");
            StatusChangeContext<BannerGroupId> context =
                    new StatusChangeContext<>(BannerGroupId.of(bannerGroupId), now);

            BannerGroup bannerGroup = BannerFixtures.activeBannerGroup(bannerGroupId);

            given(bannerGroupCommandFactory.createStatusChangeContext(command)).willReturn(context);
            given(bannerGroupValidator.findExistingOrThrow(context.id())).willReturn(bannerGroup);
            given(bannerGroupCommandManager.persist(bannerGroup)).willReturn(bannerGroupId);

            // when
            sut.execute(command);

            // then
            then(bannerGroupCommandFactory).should().createStatusChangeContext(command);
            then(bannerGroupValidator).should().findExistingOrThrow(context.id());
            then(bannerGroupCommandManager).should().persist(bannerGroup);
        }
    }
}
