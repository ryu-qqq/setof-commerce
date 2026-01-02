package com.ryuqq.setof.adapter.in.rest.admin.integration.v2.gnb;

import static com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.GnbAdminTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.command.CreateGnbV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.command.UpdateGnbV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.gnb.dto.response.GnbV2ApiResponse;
import com.ryuqq.setof.application.gnb.dto.command.CreateGnbCommand;
import com.ryuqq.setof.application.gnb.dto.command.DeleteGnbCommand;
import com.ryuqq.setof.application.gnb.dto.command.UpdateGnbCommand;
import com.ryuqq.setof.application.gnb.port.in.command.CreateGnbUseCase;
import com.ryuqq.setof.application.gnb.port.in.command.DeleteGnbUseCase;
import com.ryuqq.setof.application.gnb.port.in.command.UpdateGnbUseCase;
import com.ryuqq.setof.application.gnb.port.in.query.GetAllGnbUseCase;
import com.ryuqq.setof.application.gnb.port.in.query.GetGnbUseCase;
import com.ryuqq.setof.domain.cms.exception.GnbNotFoundException;
import com.ryuqq.setof.domain.cms.vo.GnbId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * GNB Admin 통합 테스트
 *
 * <p>CQRS 구조의 GNB 관리 API를 테스트합니다. TestRestTemplate 기반 E2E 테스트입니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("GNB Admin 통합 테스트")
class GnbAdminIntegrationTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = "/api/v2/admin/gnbs";

    @Autowired private CreateGnbUseCase createGnbUseCase;
    @Autowired private UpdateGnbUseCase updateGnbUseCase;
    @Autowired private DeleteGnbUseCase deleteGnbUseCase;
    @Autowired private GetGnbUseCase getGnbUseCase;
    @Autowired private GetAllGnbUseCase getAllGnbUseCase;

    // ========================================================================
    // Query Tests
    // ========================================================================

    @Nested
    @DisplayName("GNB 조회")
    class QueryTests {

        @Test
        @DisplayName("[GNB-001] 전체 GNB 목록 조회 성공")
        void getAllGnbs_Success() {
            // given
            given(getAllGnbUseCase.execute()).willReturn(createGnbResponses());

            // when
            ResponseEntity<ApiResponse<List<GnbV2ApiResponse>>> response =
                    get(BASE_URL, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data()).hasSize(3);
        }

        @Test
        @DisplayName("[GNB-002] GNB 단건 조회 성공")
        void getGnbById_Success() {
            // given
            given(getGnbUseCase.execute(GnbId.of(GNB_ID_1)))
                    .willReturn(createGnbResponse(GNB_ID_1));

            // when
            String url = BASE_URL + "/" + GNB_ID_1;
            ResponseEntity<ApiResponse<GnbV2ApiResponse>> response =
                    get(url, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data().gnbId()).isEqualTo(GNB_ID_1);
        }

        @Test
        @DisplayName("[GNB-003] 존재하지 않는 GNB 조회 시 404 반환")
        void getGnbById_NotFound() {
            // given
            given(getGnbUseCase.execute(GnbId.of(NON_EXISTENT_GNB_ID)))
                    .willThrow(new GnbNotFoundException(GnbId.of(NON_EXISTENT_GNB_ID)));

            // when
            String url = BASE_URL + "/" + NON_EXISTENT_GNB_ID;
            ResponseEntity<java.util.Map<String, Object>> response = getExpectingError(url);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ========================================================================
    // Command Tests
    // ========================================================================

    @Nested
    @DisplayName("GNB 생성/수정/삭제")
    class CommandTests {

        @Test
        @DisplayName("[GNB-010] GNB 생성 성공")
        void createGnb_Success() {
            // given
            CreateGnbV2ApiRequest request = createGnbRequest();
            given(createGnbUseCase.execute(any(CreateGnbCommand.class))).willReturn(GNB_ID_1);

            // when
            ResponseEntity<ApiResponse<Long>> response =
                    post(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data()).isEqualTo(GNB_ID_1);
        }

        @Test
        @DisplayName("[GNB-011] 필수 필드 누락 시 400 반환")
        void createGnb_ValidationError() {
            // given - title is required
            CreateGnbV2ApiRequest request =
                    new CreateGnbV2ApiRequest(
                            null, // missing required field
                            GNB_LINK_HOME,
                            DISPLAY_ORDER_1,
                            DISPLAY_START_DATE,
                            DISPLAY_END_DATE);

            // when
            ResponseEntity<java.util.Map<String, Object>> response =
                    postExpectingError(BASE_URL, request);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("[GNB-020] GNB 수정 성공")
        void updateGnb_Success() {
            // given
            UpdateGnbV2ApiRequest request = updateGnbRequest();
            willDoNothing().given(updateGnbUseCase).execute(any(UpdateGnbCommand.class));

            // when
            String url = BASE_URL + "/" + GNB_ID_1;
            ResponseEntity<ApiResponse<Void>> response =
                    patch(url, request, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("[GNB-021] 존재하지 않는 GNB 수정 시 404 반환")
        void updateGnb_NotFound() {
            // given
            UpdateGnbV2ApiRequest request = updateGnbRequest();
            willThrow(new GnbNotFoundException(GnbId.of(NON_EXISTENT_GNB_ID)))
                    .given(updateGnbUseCase)
                    .execute(any(UpdateGnbCommand.class));

            // when
            String url = BASE_URL + "/" + NON_EXISTENT_GNB_ID;
            ResponseEntity<java.util.Map<String, Object>> response =
                    patchExpectingError(url, request);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("[GNB-030] GNB 삭제 성공")
        void deleteGnb_Success() {
            // given
            willDoNothing().given(deleteGnbUseCase).execute(any(DeleteGnbCommand.class));

            // when
            String url = BASE_URL + "/" + GNB_ID_1 + "/delete";
            ResponseEntity<ApiResponse<Void>> response =
                    postWithoutBody(url, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("[GNB-031] 존재하지 않는 GNB 삭제 시 404 반환")
        void deleteGnb_NotFound() {
            // given
            willThrow(new GnbNotFoundException(GnbId.of(NON_EXISTENT_GNB_ID)))
                    .given(deleteGnbUseCase)
                    .execute(any(DeleteGnbCommand.class));

            // when
            String url = BASE_URL + "/" + NON_EXISTENT_GNB_ID + "/delete";
            ResponseEntity<java.util.Map<String, Object>> response =
                    postWithoutBodyExpectingError(url);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
