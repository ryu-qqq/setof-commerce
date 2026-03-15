package com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.navigation.GnbCommandV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateGnbV1ApiRequest;
import com.ryuqq.setof.application.navigation.dto.command.RegisterNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.RemoveNavigationMenuCommand;
import com.ryuqq.setof.application.navigation.dto.command.UpdateNavigationMenuCommand;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * GnbCommandV1ApiMapper 단위 테스트.
 *
 * <p>v1 GNB Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("GnbCommandV1ApiMapper 단위 테스트")
class GnbCommandV1ApiMapperTest {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private GnbCommandV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new GnbCommandV1ApiMapper();
    }

    private Instant toKstInstant(LocalDateTime ldt) {
        return ldt.atZone(KST).toInstant();
    }

    @Nested
    @DisplayName("toRegisterCommand(CreateGnbV1ApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("gnbId가 null인 등록 요청을 RegisterNavigationMenuCommand로 변환한다")
        void toRegisterCommand_Success() {
            // given
            CreateGnbV1ApiRequest request = GnbCommandV1ApiFixtures.createRequest();

            // when
            RegisterNavigationMenuCommand command = mapper.toRegisterCommand(request);

            // then
            assertThat(command.title()).isEqualTo("홈");
            assertThat(command.linkUrl()).isEqualTo("/");
            assertThat(command.displayOrder()).isEqualTo(1);
        }

        @Test
        @DisplayName("displayYn 'Y'를 active=true로 변환한다")
        void toRegisterCommand_DisplayYnY_ActiveTrue() {
            // given
            CreateGnbV1ApiRequest request = GnbCommandV1ApiFixtures.createRequest();

            // when
            RegisterNavigationMenuCommand command = mapper.toRegisterCommand(request);

            // then
            assertThat(command.active()).isTrue();
        }

        @Test
        @DisplayName("LocalDateTime을 KST 기준 Instant로 변환한다")
        void toRegisterCommand_LocalDateTimeToInstant() {
            // given
            CreateGnbV1ApiRequest request = GnbCommandV1ApiFixtures.createRequest();
            Instant expectedStart = toKstInstant(GnbCommandV1ApiFixtures.DEFAULT_START);
            Instant expectedEnd = toKstInstant(GnbCommandV1ApiFixtures.DEFAULT_END);

            // when
            RegisterNavigationMenuCommand command = mapper.toRegisterCommand(request);

            // then
            assertThat(command.displayStartAt()).isEqualTo(expectedStart);
            assertThat(command.displayEndAt()).isEqualTo(expectedEnd);
        }

        @Test
        @DisplayName("커스텀 title, linkUrl 요청도 올바르게 변환한다")
        void toRegisterCommand_CustomTitleAndLinkUrl() {
            // given
            CreateGnbV1ApiRequest request = GnbCommandV1ApiFixtures.createRequest("여성", "/women");

            // when
            RegisterNavigationMenuCommand command = mapper.toRegisterCommand(request);

            // then
            assertThat(command.title()).isEqualTo("여성");
            assertThat(command.linkUrl()).isEqualTo("/women");
        }
    }

    @Nested
    @DisplayName("toUpdateCommand(CreateGnbV1ApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("gnbId가 있는 수정 요청을 UpdateNavigationMenuCommand로 변환한다")
        void toUpdateCommand_Success() {
            // given
            CreateGnbV1ApiRequest request = GnbCommandV1ApiFixtures.updateRequest(5L);

            // when
            UpdateNavigationMenuCommand command = mapper.toUpdateCommand(request);

            // then
            assertThat(command.id()).isEqualTo(5L);
            assertThat(command.title()).isEqualTo("남성");
            assertThat(command.linkUrl()).isEqualTo("/men");
            assertThat(command.displayOrder()).isEqualTo(2);
        }

        @Test
        @DisplayName("displayYn 'N'을 active=false로 변환한다")
        void toUpdateCommand_DisplayYnN_ActiveFalse() {
            // given
            CreateGnbV1ApiRequest request = GnbCommandV1ApiFixtures.updateRequest(5L);

            // when
            UpdateNavigationMenuCommand command = mapper.toUpdateCommand(request);

            // then
            assertThat(command.active()).isFalse();
        }

        @Test
        @DisplayName("LocalDateTime을 KST 기준 Instant로 변환한다")
        void toUpdateCommand_LocalDateTimeToInstant() {
            // given
            CreateGnbV1ApiRequest request = GnbCommandV1ApiFixtures.updateRequest(5L);
            Instant expectedStart = toKstInstant(GnbCommandV1ApiFixtures.DEFAULT_START);
            Instant expectedEnd = toKstInstant(GnbCommandV1ApiFixtures.DEFAULT_END);

            // when
            UpdateNavigationMenuCommand command = mapper.toUpdateCommand(request);

            // then
            assertThat(command.displayStartAt()).isEqualTo(expectedStart);
            assertThat(command.displayEndAt()).isEqualTo(expectedEnd);
        }
    }

    @Nested
    @DisplayName("toRemoveCommand(long)")
    class ToRemoveCommandTest {

        @Test
        @DisplayName("gnbId를 RemoveNavigationMenuCommand로 변환한다")
        void toRemoveCommand_Success() {
            // given
            long gnbId = 3L;

            // when
            RemoveNavigationMenuCommand command = mapper.toRemoveCommand(gnbId);

            // then
            assertThat(command.id()).isEqualTo(3L);
        }

        @Test
        @DisplayName("다른 gnbId도 올바르게 변환한다")
        void toRemoveCommand_DifferentId() {
            // given
            long gnbId = 99L;

            // when
            RemoveNavigationMenuCommand command = mapper.toRemoveCommand(gnbId);

            // then
            assertThat(command.id()).isEqualTo(99L);
        }
    }
}
