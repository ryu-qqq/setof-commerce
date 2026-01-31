package com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.commoncodetype.CommonCodeTypeApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.dto.command.ChangeActiveStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.dto.command.RegisterCommonCodeTypeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.dto.command.UpdateCommonCodeTypeApiRequest;
import com.ryuqq.setof.application.commoncodetype.dto.command.ChangeActiveStatusCommand;
import com.ryuqq.setof.application.commoncodetype.dto.command.RegisterCommonCodeTypeCommand;
import com.ryuqq.setof.application.commoncodetype.dto.command.UpdateCommonCodeTypeCommand;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * CommonCodeTypeCommandApiMapper 단위 테스트.
 *
 * <p>Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CommonCodeTypeCommandApiMapper 단위 테스트")
class CommonCodeTypeCommandApiMapperTest {

    private CommonCodeTypeCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CommonCodeTypeCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(RegisterCommonCodeTypeApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("등록 요청을 Command로 변환한다")
        void toCommand_Register_Success() {
            // given
            RegisterCommonCodeTypeApiRequest request = CommonCodeTypeApiFixtures.registerRequest();

            // when
            RegisterCommonCodeTypeCommand command = mapper.toCommand(request);

            // then
            assertThat(command.code()).isEqualTo(request.code());
            assertThat(command.name()).isEqualTo(request.name());
            assertThat(command.description()).isEqualTo(request.description());
            assertThat(command.displayOrder()).isEqualTo(request.displayOrder());
        }

        @Test
        @DisplayName("null description을 그대로 전달한다")
        void toCommand_NullDescription_Success() {
            // given
            RegisterCommonCodeTypeApiRequest request =
                    CommonCodeTypeApiFixtures.registerRequest("TEST_CODE", "테스트");

            // when
            RegisterCommonCodeTypeCommand command = mapper.toCommand(request);

            // then
            assertThat(command.description()).isNull();
        }
    }

    @Nested
    @DisplayName("toCommand(Long, UpdateCommonCodeTypeApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("수정 요청을 Command로 변환한다")
        void toCommand_Update_Success() {
            // given
            Long typeId = 1L;
            UpdateCommonCodeTypeApiRequest request = CommonCodeTypeApiFixtures.updateRequest();

            // when
            UpdateCommonCodeTypeCommand command = mapper.toCommand(typeId, request);

            // then
            assertThat(command.id()).isEqualTo(typeId);
            assertThat(command.name()).isEqualTo(request.name());
            assertThat(command.description()).isEqualTo(request.description());
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
            ChangeActiveStatusApiRequest request = CommonCodeTypeApiFixtures.activateRequest(ids);

            // when
            ChangeActiveStatusCommand command = mapper.toCommand(request);

            // then
            assertThat(command.ids()).containsExactlyElementsOf(ids);
            assertThat(command.active()).isTrue();
        }

        @Test
        @DisplayName("비활성화 요청을 Command로 변환한다")
        void toCommand_Deactivate_Success() {
            // given
            List<Long> ids = List.of(1L, 2L);
            ChangeActiveStatusApiRequest request = CommonCodeTypeApiFixtures.deactivateRequest(ids);

            // when
            ChangeActiveStatusCommand command = mapper.toCommand(request);

            // then
            assertThat(command.active()).isFalse();
        }
    }
}
