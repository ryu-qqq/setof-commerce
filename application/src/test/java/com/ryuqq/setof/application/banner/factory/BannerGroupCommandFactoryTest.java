package com.ryuqq.setof.application.banner.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.application.banner.BannerCommandFixtures;
import com.ryuqq.setof.application.banner.dto.command.ChangeBannerGroupStatusCommand;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.RemoveBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerGroupCommand;
import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroupUpdateData;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import com.ryuqq.setof.domain.banner.vo.BannerType;
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
@DisplayName("BannerGroupCommandFactory 단위 테스트")
class BannerGroupCommandFactoryTest {

    @InjectMocks private BannerGroupCommandFactory sut;

    @Mock private TimeProvider timeProvider;

    private static final Instant FIXED_NOW = Instant.parse("2025-01-01T00:00:00Z");

    @Nested
    @DisplayName("create() - RegisterBannerGroupCommand → BannerGroup 변환")
    class CreateTest {

        @Test
        @DisplayName("등록 커맨드를 BannerGroup 도메인 객체로 변환한다")
        void create_ValidCommand_ReturnsBannerGroup() {
            // given
            RegisterBannerGroupCommand command = BannerCommandFixtures.registerCommand();

            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            BannerGroup result = sut.create(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.title()).isEqualTo(command.title());
            assertThat(result.bannerType()).isEqualTo(BannerType.valueOf(command.bannerType()));
            assertThat(result.isActive()).isEqualTo(command.active());
        }

        @Test
        @DisplayName("커맨드의 슬라이드 목록이 BannerGroup에 포함된다")
        void create_WithSlides_SlidesIncludedInBannerGroup() {
            // given
            RegisterBannerGroupCommand command = BannerCommandFixtures.registerCommand();

            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            BannerGroup result = sut.create(command);

            // then
            assertThat(result.slides()).hasSize(command.slides().size());
        }

        @Test
        @DisplayName("비활성 상태 커맨드로 생성된 BannerGroup은 active가 false이다")
        void create_InactiveCommand_ReturnsBannerGroupWithInactiveStatus() {
            // given
            RegisterBannerGroupCommand command =
                    BannerCommandFixtures.registerCommandWithInactiveStatus();

            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            BannerGroup result = sut.create(command);

            // then
            assertThat(result.isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("createUpdateContext() - UpdateBannerGroupCommand → UpdateContext 변환")
    class CreateUpdateContextTest {

        @Test
        @DisplayName("수정 커맨드를 UpdateContext로 변환한다")
        void createUpdateContext_ValidCommand_ReturnsUpdateContext() {
            // given
            long bannerGroupId = 1L;
            UpdateBannerGroupCommand command = BannerCommandFixtures.updateCommand(bannerGroupId);

            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            UpdateContext<BannerGroupId, BannerGroupUpdateData> result =
                    sut.createUpdateContext(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(BannerGroupId.of(bannerGroupId));
            assertThat(result.changedAt()).isEqualTo(FIXED_NOW);
        }

        @Test
        @DisplayName("커맨드의 타이틀이 UpdateContext의 updateData에 반영된다")
        void createUpdateContext_TitleReflected_InUpdateData() {
            // given
            UpdateBannerGroupCommand command = BannerCommandFixtures.updateCommand(1L);

            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            UpdateContext<BannerGroupId, BannerGroupUpdateData> result =
                    sut.createUpdateContext(command);

            // then
            assertThat(result.updateData().title()).isEqualTo(command.title());
        }

        @Test
        @DisplayName("그룹 전용 수정이므로 UpdateContext의 slideEntries는 비어있다")
        void createUpdateContext_GroupOnly_SlideEntriesIsEmpty() {
            // given
            UpdateBannerGroupCommand command = BannerCommandFixtures.updateCommand(1L);

            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            UpdateContext<BannerGroupId, BannerGroupUpdateData> result =
                    sut.createUpdateContext(command);

            // then
            assertThat(result.updateData().slideEntries()).isEmpty();
        }
    }

    @Nested
    @DisplayName(
            "createStatusChangeContext() - ChangeBannerGroupStatusCommand → StatusChangeContext 변환")
    class CreateStatusChangeContextTest {

        @Test
        @DisplayName("상태 변경 커맨드를 StatusChangeContext로 변환한다")
        void createStatusChangeContext_ValidCommand_ReturnsStatusChangeContext() {
            // given
            long bannerGroupId = 1L;
            ChangeBannerGroupStatusCommand command =
                    BannerCommandFixtures.activateCommand(bannerGroupId);

            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            StatusChangeContext<BannerGroupId> result = sut.createStatusChangeContext(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(BannerGroupId.of(bannerGroupId));
            assertThat(result.changedAt()).isEqualTo(FIXED_NOW);
        }
    }

    @Nested
    @DisplayName("createRemoveContext() - RemoveBannerGroupCommand → StatusChangeContext 변환")
    class CreateRemoveContextTest {

        @Test
        @DisplayName("삭제 커맨드를 StatusChangeContext로 변환한다")
        void createRemoveContext_ValidCommand_ReturnsStatusChangeContext() {
            // given
            long bannerGroupId = 1L;
            RemoveBannerGroupCommand command = BannerCommandFixtures.removeCommand(bannerGroupId);

            given(timeProvider.now()).willReturn(FIXED_NOW);

            // when
            StatusChangeContext<BannerGroupId> result = sut.createRemoveContext(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(BannerGroupId.of(bannerGroupId));
            assertThat(result.changedAt()).isEqualTo(FIXED_NOW);
        }
    }
}
