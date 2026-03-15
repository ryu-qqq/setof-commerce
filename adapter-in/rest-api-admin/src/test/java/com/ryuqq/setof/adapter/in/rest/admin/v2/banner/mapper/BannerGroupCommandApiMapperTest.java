package com.ryuqq.setof.adapter.in.rest.admin.v2.banner.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.banner.BannerGroupApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.ChangeBannerGroupStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.RegisterBannerGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerGroupApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerSlideApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerSlidesApiRequest;
import com.ryuqq.setof.application.banner.dto.command.ChangeBannerGroupStatusCommand;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.RegisterBannerSlideCommand;
import com.ryuqq.setof.application.banner.dto.command.RemoveBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerGroupCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlideCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlidesCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * BannerGroupCommandApiMapper 단위 테스트.
 *
 * <p>배너 그룹 Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("BannerGroupCommandApiMapper 단위 테스트")
class BannerGroupCommandApiMapperTest {

    private BannerGroupCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BannerGroupCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(RegisterBannerGroupApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("등록 요청을 RegisterBannerGroupCommand로 변환한다")
        void toCommand_Register_Success() {
            // given
            RegisterBannerGroupApiRequest request = BannerGroupApiFixtures.registerRequest();

            // when
            RegisterBannerGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.title()).isEqualTo(request.title());
            assertThat(command.bannerType()).isEqualTo(request.bannerType());
            assertThat(command.displayStartAt()).isEqualTo(request.displayStartAt());
            assertThat(command.displayEndAt()).isEqualTo(request.displayEndAt());
            assertThat(command.active()).isEqualTo(request.active());
        }

        @Test
        @DisplayName("슬라이드 목록을 RegisterBannerSlideCommand 목록으로 변환한다")
        void toCommand_Register_SlidesMapping() {
            // given
            RegisterBannerGroupApiRequest request = BannerGroupApiFixtures.registerRequest();

            // when
            RegisterBannerGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.slides()).hasSize(1);
            RegisterBannerSlideCommand slideCommand = command.slides().get(0);
            var slideRequest = request.slides().get(0);
            assertThat(slideCommand.title()).isEqualTo(slideRequest.title());
            assertThat(slideCommand.imageUrl()).isEqualTo(slideRequest.imageUrl());
            assertThat(slideCommand.linkUrl()).isEqualTo(slideRequest.linkUrl());
            assertThat(slideCommand.displayOrder()).isEqualTo(slideRequest.displayOrder());
            assertThat(slideCommand.displayStartAt()).isEqualTo(slideRequest.displayStartAt());
            assertThat(slideCommand.displayEndAt()).isEqualTo(slideRequest.displayEndAt());
            assertThat(slideCommand.active()).isEqualTo(slideRequest.active());
        }

        @Test
        @DisplayName("여러 슬라이드를 모두 변환한다")
        void toCommand_Register_MultipleSlides() {
            // given
            RegisterBannerGroupApiRequest request =
                    BannerGroupApiFixtures.registerRequestWithMultipleSlides();

            // when
            RegisterBannerGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.slides()).hasSize(2);
        }

        @Test
        @DisplayName("active가 false인 경우도 올바르게 변환한다")
        void toCommand_Register_InactiveGroup() {
            // given
            RegisterBannerGroupApiRequest request =
                    BannerGroupApiFixtures.registerRequestInactive();

            // when
            RegisterBannerGroupCommand command = mapper.toCommand(request);

            // then
            assertThat(command.active()).isFalse();
        }
    }

    @Nested
    @DisplayName("toCommand(long, UpdateBannerGroupApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("수정 요청을 UpdateBannerGroupCommand로 변환한다")
        void toCommand_Update_Success() {
            // given
            long bannerGroupId = BannerGroupApiFixtures.DEFAULT_BANNER_GROUP_ID;
            UpdateBannerGroupApiRequest request = BannerGroupApiFixtures.updateRequest();

            // when
            UpdateBannerGroupCommand command = mapper.toCommand(bannerGroupId, request);

            // then
            assertThat(command.id()).isEqualTo(bannerGroupId);
            assertThat(command.title()).isEqualTo(request.title());
            assertThat(command.bannerType()).isEqualTo(request.bannerType());
            assertThat(command.displayStartAt()).isEqualTo(request.displayStartAt());
            assertThat(command.displayEndAt()).isEqualTo(request.displayEndAt());
            assertThat(command.active()).isEqualTo(request.active());
        }

        @Test
        @DisplayName("PathVariable의 bannerGroupId를 Command에 올바르게 설정한다")
        void toCommand_Update_BannerGroupIdFromPath() {
            // given
            long bannerGroupId = 999L;
            UpdateBannerGroupApiRequest request = BannerGroupApiFixtures.updateRequest();

            // when
            UpdateBannerGroupCommand command = mapper.toCommand(bannerGroupId, request);

            // then
            assertThat(command.id()).isEqualTo(999L);
        }

        @Test
        @DisplayName("active가 false인 수정 요청도 올바르게 변환한다")
        void toCommand_Update_InactiveGroup() {
            // given
            long bannerGroupId = BannerGroupApiFixtures.DEFAULT_BANNER_GROUP_ID;
            UpdateBannerGroupApiRequest request = BannerGroupApiFixtures.updateRequestInactive();

            // when
            UpdateBannerGroupCommand command = mapper.toCommand(bannerGroupId, request);

            // then
            assertThat(command.active()).isFalse();
        }
    }

    @Nested
    @DisplayName("toSlidesCommand(long, UpdateBannerSlidesApiRequest)")
    class ToUpdateSlidesCommandTest {

        @Test
        @DisplayName("슬라이드 수정 요청을 UpdateBannerSlidesCommand로 변환한다")
        void toSlidesCommand_Success() {
            // given
            long bannerGroupId = BannerGroupApiFixtures.DEFAULT_BANNER_GROUP_ID;
            UpdateBannerSlidesApiRequest request = BannerGroupApiFixtures.updateSlidesRequest();

            // when
            UpdateBannerSlidesCommand command = mapper.toSlidesCommand(bannerGroupId, request);

            // then
            assertThat(command.bannerGroupId()).isEqualTo(bannerGroupId);
            assertThat(command.slides()).hasSize(2);
        }

        @Test
        @DisplayName("slideId가 있는 슬라이드를 UpdateBannerSlideCommand로 변환한다 (기존 슬라이드 수정)")
        void toSlidesCommand_SlideWithIdMapping() {
            // given
            long bannerGroupId = BannerGroupApiFixtures.DEFAULT_BANNER_GROUP_ID;
            UpdateBannerSlidesApiRequest request =
                    BannerGroupApiFixtures.updateSlidesRequestWithExistingSlideOnly();

            // when
            UpdateBannerSlidesCommand command = mapper.toSlidesCommand(bannerGroupId, request);

            // then
            assertThat(command.slides()).hasSize(1);
            UpdateBannerSlideCommand slideCommand = command.slides().get(0);
            UpdateBannerSlideApiRequest slideRequest = request.slides().get(0);
            assertThat(slideCommand.slideId()).isEqualTo(BannerGroupApiFixtures.DEFAULT_SLIDE_ID);
            assertThat(slideCommand.slideId()).isEqualTo(slideRequest.slideId());
            assertThat(slideCommand.title()).isEqualTo(slideRequest.title());
            assertThat(slideCommand.imageUrl()).isEqualTo(slideRequest.imageUrl());
            assertThat(slideCommand.linkUrl()).isEqualTo(slideRequest.linkUrl());
            assertThat(slideCommand.displayOrder()).isEqualTo(slideRequest.displayOrder());
            assertThat(slideCommand.displayStartAt()).isEqualTo(slideRequest.displayStartAt());
            assertThat(slideCommand.displayEndAt()).isEqualTo(slideRequest.displayEndAt());
            assertThat(slideCommand.active()).isEqualTo(slideRequest.active());
        }

        @Test
        @DisplayName("slideId가 null인 슬라이드를 UpdateBannerSlideCommand로 변환한다 (신규 슬라이드 등록)")
        void toSlidesCommand_SlideWithNullIdMapping() {
            // given
            long bannerGroupId = BannerGroupApiFixtures.DEFAULT_BANNER_GROUP_ID;
            UpdateBannerSlidesApiRequest request =
                    BannerGroupApiFixtures.updateSlidesRequestWithNewSlideOnly();

            // when
            UpdateBannerSlidesCommand command = mapper.toSlidesCommand(bannerGroupId, request);

            // then
            assertThat(command.slides()).hasSize(1);
            UpdateBannerSlideCommand slideCommand = command.slides().get(0);
            assertThat(slideCommand.slideId()).isNull();
        }

        @Test
        @DisplayName("slideId 혼합 목록 (null + 값 있음)을 모두 올바르게 변환한다")
        void toSlidesCommand_MixedSlideIds() {
            // given
            long bannerGroupId = BannerGroupApiFixtures.DEFAULT_BANNER_GROUP_ID;
            UpdateBannerSlidesApiRequest request = BannerGroupApiFixtures.updateSlidesRequest();

            // when
            UpdateBannerSlidesCommand command = mapper.toSlidesCommand(bannerGroupId, request);

            // then
            assertThat(command.slides()).hasSize(2);
            assertThat(command.slides().get(0).slideId())
                    .isEqualTo(BannerGroupApiFixtures.DEFAULT_SLIDE_ID);
            assertThat(command.slides().get(1).slideId()).isNull();
        }
    }

    @Nested
    @DisplayName("toCommand(long, ChangeBannerGroupStatusApiRequest)")
    class ToChangeStatusCommandTest {

        @Test
        @DisplayName("노출 상태 활성화 요청을 ChangeBannerGroupStatusCommand로 변환한다")
        void toCommand_ChangeStatus_Active() {
            // given
            long bannerGroupId = BannerGroupApiFixtures.DEFAULT_BANNER_GROUP_ID;
            ChangeBannerGroupStatusApiRequest request =
                    BannerGroupApiFixtures.changeStatusActiveRequest();

            // when
            ChangeBannerGroupStatusCommand command = mapper.toCommand(bannerGroupId, request);

            // then
            assertThat(command.id()).isEqualTo(bannerGroupId);
            assertThat(command.active()).isTrue();
        }

        @Test
        @DisplayName("노출 상태 비활성화 요청을 ChangeBannerGroupStatusCommand로 변환한다")
        void toCommand_ChangeStatus_Inactive() {
            // given
            long bannerGroupId = BannerGroupApiFixtures.DEFAULT_BANNER_GROUP_ID;
            ChangeBannerGroupStatusApiRequest request =
                    BannerGroupApiFixtures.changeStatusInactiveRequest();

            // when
            ChangeBannerGroupStatusCommand command = mapper.toCommand(bannerGroupId, request);

            // then
            assertThat(command.id()).isEqualTo(bannerGroupId);
            assertThat(command.active()).isFalse();
        }

        @Test
        @DisplayName("PathVariable의 bannerGroupId를 Command에 올바르게 설정한다")
        void toCommand_ChangeStatus_BannerGroupIdFromPath() {
            // given
            long bannerGroupId = 777L;
            ChangeBannerGroupStatusApiRequest request =
                    BannerGroupApiFixtures.changeStatusActiveRequest();

            // when
            ChangeBannerGroupStatusCommand command = mapper.toCommand(bannerGroupId, request);

            // then
            assertThat(command.id()).isEqualTo(777L);
        }
    }

    @Nested
    @DisplayName("toCommand(long) - RemoveBannerGroupCommand")
    class ToRemoveCommandTest {

        @Test
        @DisplayName("bannerGroupId를 RemoveBannerGroupCommand로 변환한다")
        void toCommand_Remove_Success() {
            // given
            long bannerGroupId = BannerGroupApiFixtures.DEFAULT_BANNER_GROUP_ID;

            // when
            RemoveBannerGroupCommand command = mapper.toCommand(bannerGroupId);

            // then
            assertThat(command.id()).isEqualTo(bannerGroupId);
        }

        @Test
        @DisplayName("다른 bannerGroupId도 올바르게 변환한다")
        void toCommand_Remove_DifferentId() {
            // given
            long bannerGroupId = 555L;

            // when
            RemoveBannerGroupCommand command = mapper.toCommand(bannerGroupId);

            // then
            assertThat(command.id()).isEqualTo(555L);
        }
    }
}
