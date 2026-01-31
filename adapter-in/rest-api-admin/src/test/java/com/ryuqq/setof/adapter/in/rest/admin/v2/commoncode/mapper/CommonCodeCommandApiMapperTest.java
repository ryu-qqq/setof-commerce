package com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.commoncode.CommonCodeApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.command.ChangeActiveStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.command.RegisterCommonCodeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.command.UpdateCommonCodeApiRequest;
import com.ryuqq.setof.application.commoncode.dto.command.ChangeCommonCodeStatusCommand;
import com.ryuqq.setof.application.commoncode.dto.command.RegisterCommonCodeCommand;
import com.ryuqq.setof.application.commoncode.dto.command.UpdateCommonCodeCommand;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * CommonCodeCommandApiMapper 단위 테스트.
 *
 * <p>Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CommonCodeCommandApiMapper 단위 테스트")
class CommonCodeCommandApiMapperTest {

    private CommonCodeCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CommonCodeCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(RegisterCommonCodeApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("등록 요청을 Command로 변환한다")
        void toCommand_Register_Success() {
            // given
            RegisterCommonCodeApiRequest request = CommonCodeApiFixtures.registerRequest();

            // when
            RegisterCommonCodeCommand command = mapper.toCommand(request);

            // then
            assertThat(command.commonCodeTypeId()).isEqualTo(request.commonCodeTypeId());
            assertThat(command.code()).isEqualTo(request.code());
            assertThat(command.displayName()).isEqualTo(request.displayName());
            assertThat(command.displayOrder()).isEqualTo(request.displayOrder());
        }
    }

    @Nested
    @DisplayName("toCommand(Long, UpdateCommonCodeApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("수정 요청을 Command로 변환한다")
        void toCommand_Update_Success() {
            // given
            Long codeId = 100L;
            UpdateCommonCodeApiRequest request = CommonCodeApiFixtures.updateRequest();

            // when
            UpdateCommonCodeCommand command = mapper.toCommand(codeId, request);

            // then
            assertThat(command.id()).isEqualTo(codeId);
            assertThat(command.displayName()).isEqualTo(request.displayName());
            assertThat(command.displayOrder()).isEqualTo(request.displayOrder());
        }
    }

    @Nested
    @DisplayName("toCommand(ChangeActiveStatusApiRequest)")
    class ToChangeStatusCommandTest {

        @Test
        @DisplayName("상태 변경 요청을 Command로 변환한다")
        void toCommand_ChangeStatus_Success() {
            // given
            List<Long> ids = List.of(1L, 2L, 3L);
            ChangeActiveStatusApiRequest request = CommonCodeApiFixtures.activateRequest(ids);

            // when
            ChangeCommonCodeStatusCommand command = mapper.toCommand(request);

            // then
            assertThat(command.ids()).containsExactlyElementsOf(ids);
            assertThat(command.active()).isTrue();
        }

        @Test
        @DisplayName("비활성화 요청을 Command로 변환한다")
        void toCommand_Deactivate_Success() {
            // given
            List<Long> ids = List.of(1L, 2L);
            ChangeActiveStatusApiRequest request = CommonCodeApiFixtures.deactivateRequest(ids);

            // when
            ChangeCommonCodeStatusCommand command = mapper.toCommand(request);

            // then
            assertThat(command.active()).isFalse();
        }
    }
}
