package com.ryuqq.setof.adapter.in.rest.admin.integration.v2.component;

import static com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.ComponentAdminTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.command.CreateComponentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.command.UpdateComponentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.response.ComponentV2ApiResponse;
import com.ryuqq.setof.application.component.dto.command.CreateComponentCommand;
import com.ryuqq.setof.application.component.dto.command.DeleteComponentCommand;
import com.ryuqq.setof.application.component.dto.command.UpdateComponentCommand;
import com.ryuqq.setof.application.component.port.in.command.CreateComponentUseCase;
import com.ryuqq.setof.application.component.port.in.command.DeleteComponentUseCase;
import com.ryuqq.setof.application.component.port.in.command.UpdateComponentUseCase;
import com.ryuqq.setof.application.component.port.in.query.GetComponentUseCase;
import com.ryuqq.setof.application.component.port.in.query.GetComponentsByContentUseCase;
import com.ryuqq.setof.domain.cms.exception.ComponentNotFoundException;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Component Admin 통합 테스트
 *
 * <p>CQRS 구조의 컴포넌트 관리 API를 테스트합니다. TestRestTemplate 기반 E2E 테스트입니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("Component Admin 통합 테스트")
class ComponentAdminIntegrationTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = "/api/v2/admin/contents/{contentId}/components";

    @Autowired private CreateComponentUseCase createComponentUseCase;
    @Autowired private UpdateComponentUseCase updateComponentUseCase;
    @Autowired private DeleteComponentUseCase deleteComponentUseCase;
    @Autowired private GetComponentUseCase getComponentUseCase;
    @Autowired private GetComponentsByContentUseCase getComponentsByContentUseCase;

    // ========================================================================
    // Query Tests
    // ========================================================================

    @Nested
    @DisplayName("컴포넌트 조회")
    class QueryTests {

        @Test
        @DisplayName("[COMP-001] 컨텐츠별 컴포넌트 목록 조회 성공")
        void getComponentsByContent_Success() {
            // given
            given(getComponentsByContentUseCase.getByContentId(ContentId.of(CONTENT_ID)))
                    .willReturn(createComponentResponses());

            // when
            String url = BASE_URL.replace("{contentId}", String.valueOf(CONTENT_ID));
            ResponseEntity<ApiResponse<List<ComponentV2ApiResponse>>> response =
                    get(url, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data()).hasSize(2);
        }

        @Test
        @DisplayName("[COMP-002] 컴포넌트 상세 조회 성공")
        void getComponentById_Success() {
            // given
            given(getComponentUseCase.execute(ComponentId.of(COMPONENT_ID_1)))
                    .willReturn(createComponentResponse(COMPONENT_ID_1));

            // when
            String url =
                    BASE_URL.replace("{contentId}", String.valueOf(CONTENT_ID))
                            + "/"
                            + COMPONENT_ID_1;
            ResponseEntity<ApiResponse<ComponentV2ApiResponse>> response =
                    get(url, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data().componentId()).isEqualTo(COMPONENT_ID_1);
        }

        @Test
        @DisplayName("[COMP-003] 존재하지 않는 컴포넌트 조회 시 404 반환")
        void getComponentById_NotFound() {
            // given
            given(getComponentUseCase.execute(ComponentId.of(NON_EXISTENT_COMPONENT_ID)))
                    .willThrow(
                            new ComponentNotFoundException(
                                    ComponentId.of(NON_EXISTENT_COMPONENT_ID)));

            // when
            String url =
                    BASE_URL.replace("{contentId}", String.valueOf(CONTENT_ID))
                            + "/"
                            + NON_EXISTENT_COMPONENT_ID;
            ResponseEntity<java.util.Map<String, Object>> response = getExpectingError(url);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ========================================================================
    // Command Tests
    // ========================================================================

    @Nested
    @DisplayName("컴포넌트 생성/수정/삭제")
    class CommandTests {

        @Test
        @DisplayName("[COMP-010] 컴포넌트 생성 성공")
        void createComponent_Success() {
            // given
            CreateComponentV2ApiRequest request = createComponentRequest();
            given(createComponentUseCase.execute(any(CreateComponentCommand.class)))
                    .willReturn(COMPONENT_ID_1);

            // when
            String url = BASE_URL.replace("{contentId}", String.valueOf(CONTENT_ID));
            ResponseEntity<ApiResponse<Long>> response =
                    post(url, request, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data()).isEqualTo(COMPONENT_ID_1);
        }

        @Test
        @DisplayName("[COMP-011] 필수 필드 누락 시 400 반환")
        void createComponent_ValidationError() {
            // given - componentType is required
            CreateComponentV2ApiRequest request =
                    new CreateComponentV2ApiRequest(
                            null, // missing required field
                            COMPONENT_NAME,
                            DISPLAY_ORDER,
                            EXPOSED_PRODUCTS,
                            DISPLAY_START_DATE,
                            DISPLAY_END_DATE,
                            createProductDetailRequest());

            // when
            String url = BASE_URL.replace("{contentId}", String.valueOf(CONTENT_ID));
            ResponseEntity<java.util.Map<String, Object>> response =
                    postExpectingError(url, request);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("[COMP-020] 컴포넌트 수정 성공")
        void updateComponent_Success() {
            // given
            UpdateComponentV2ApiRequest request = updateComponentRequest();
            willDoNothing()
                    .given(updateComponentUseCase)
                    .execute(any(UpdateComponentCommand.class));

            // when
            String url =
                    BASE_URL.replace("{contentId}", String.valueOf(CONTENT_ID))
                            + "/"
                            + COMPONENT_ID_1;
            ResponseEntity<ApiResponse<Void>> response =
                    patch(url, request, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("[COMP-021] 존재하지 않는 컴포넌트 수정 시 404 반환")
        void updateComponent_NotFound() {
            // given
            UpdateComponentV2ApiRequest request = updateComponentRequest();
            willThrow(new ComponentNotFoundException(ComponentId.of(NON_EXISTENT_COMPONENT_ID)))
                    .given(updateComponentUseCase)
                    .execute(any(UpdateComponentCommand.class));

            // when
            String url =
                    BASE_URL.replace("{contentId}", String.valueOf(CONTENT_ID))
                            + "/"
                            + NON_EXISTENT_COMPONENT_ID;
            ResponseEntity<java.util.Map<String, Object>> response =
                    patchExpectingError(url, request);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("[COMP-030] 컴포넌트 삭제 성공")
        void deleteComponent_Success() {
            // given
            willDoNothing()
                    .given(deleteComponentUseCase)
                    .execute(any(DeleteComponentCommand.class));

            // when
            String url =
                    BASE_URL.replace("{contentId}", String.valueOf(CONTENT_ID))
                            + "/"
                            + COMPONENT_ID_1
                            + "/delete";
            ResponseEntity<ApiResponse<Void>> response =
                    postWithoutBody(url, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("[COMP-031] 존재하지 않는 컴포넌트 삭제 시 404 반환")
        void deleteComponent_NotFound() {
            // given
            willThrow(new ComponentNotFoundException(ComponentId.of(NON_EXISTENT_COMPONENT_ID)))
                    .given(deleteComponentUseCase)
                    .execute(any(DeleteComponentCommand.class));

            // when
            String url =
                    BASE_URL.replace("{contentId}", String.valueOf(CONTENT_ID))
                            + "/"
                            + NON_EXISTENT_COMPONENT_ID
                            + "/delete";
            ResponseEntity<java.util.Map<String, Object>> response =
                    postWithoutBodyExpectingError(url);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
