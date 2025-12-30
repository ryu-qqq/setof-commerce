package com.ryuqq.setof.adapter.in.rest.admin.integration.v2.banner;

import static com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.BannerAdminTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.CreateBannerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.command.UpdateBannerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banner.dto.response.BannerV2ApiResponse;
import com.ryuqq.setof.application.banner.dto.command.ActivateBannerCommand;
import com.ryuqq.setof.application.banner.dto.command.CreateBannerCommand;
import com.ryuqq.setof.application.banner.dto.command.DeactivateBannerCommand;
import com.ryuqq.setof.application.banner.dto.command.DeleteBannerCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerCommand;
import com.ryuqq.setof.application.banner.dto.query.SearchBannerQuery;
import com.ryuqq.setof.application.banner.port.in.command.ActivateBannerUseCase;
import com.ryuqq.setof.application.banner.port.in.command.CreateBannerUseCase;
import com.ryuqq.setof.application.banner.port.in.command.DeactivateBannerUseCase;
import com.ryuqq.setof.application.banner.port.in.command.DeleteBannerUseCase;
import com.ryuqq.setof.application.banner.port.in.command.UpdateBannerUseCase;
import com.ryuqq.setof.application.banner.port.in.query.GetBannerUseCase;
import com.ryuqq.setof.application.banner.port.in.query.SearchBannerUseCase;
import com.ryuqq.setof.domain.cms.exception.BannerNotFoundException;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Banner Admin 통합 테스트
 *
 * <p>CQRS 구조의 배너 관리 API를 테스트합니다. TestRestTemplate 기반 E2E 테스트입니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("Banner Admin 통합 테스트")
class BannerAdminIntegrationTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = "/api/v2/admin/banners";

    @Autowired private CreateBannerUseCase createBannerUseCase;
    @Autowired private UpdateBannerUseCase updateBannerUseCase;
    @Autowired private DeleteBannerUseCase deleteBannerUseCase;
    @Autowired private ActivateBannerUseCase activateBannerUseCase;
    @Autowired private DeactivateBannerUseCase deactivateBannerUseCase;
    @Autowired private GetBannerUseCase getBannerUseCase;
    @Autowired private SearchBannerUseCase searchBannerUseCase;

    // ========================================================================
    // Query Tests
    // ========================================================================

    @Nested
    @DisplayName("배너 조회")
    class QueryTests {

        @Test
        @DisplayName("[BANNER-001] 배너 목록 조회 성공")
        void searchBanners_Success() {
            // given
            given(searchBannerUseCase.execute(any(SearchBannerQuery.class)))
                    .willReturn(createBannerResponses());

            // when
            ResponseEntity<ApiResponse<List<BannerV2ApiResponse>>> response =
                    get(BASE_URL, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data()).hasSize(2);
        }

        @Test
        @DisplayName("[BANNER-002] 배너 단건 조회 성공")
        void getBannerById_Success() {
            // given
            given(getBannerUseCase.execute(BannerId.of(BANNER_ID_1)))
                    .willReturn(createBannerResponse(BANNER_ID_1));

            // when
            String url = BASE_URL + "/" + BANNER_ID_1;
            ResponseEntity<ApiResponse<BannerV2ApiResponse>> response =
                    get(url, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data().bannerId()).isEqualTo(BANNER_ID_1);
        }

        @Test
        @DisplayName("[BANNER-003] 존재하지 않는 배너 조회 시 404 반환")
        void getBannerById_NotFound() {
            // given
            given(getBannerUseCase.execute(BannerId.of(NON_EXISTENT_BANNER_ID)))
                    .willThrow(new BannerNotFoundException(BannerId.of(NON_EXISTENT_BANNER_ID)));

            // when
            String url = BASE_URL + "/" + NON_EXISTENT_BANNER_ID;
            ResponseEntity<java.util.Map<String, Object>> response = getExpectingError(url);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ========================================================================
    // Command Tests
    // ========================================================================

    @Nested
    @DisplayName("배너 생성/수정/삭제")
    class CommandTests {

        @Test
        @DisplayName("[BANNER-010] 배너 생성 성공")
        void createBanner_Success() {
            // given
            CreateBannerV2ApiRequest request = createBannerRequest();
            given(createBannerUseCase.execute(any(CreateBannerCommand.class)))
                    .willReturn(BANNER_ID_1);

            // when
            ResponseEntity<ApiResponse<Long>> response =
                    post(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data()).isEqualTo(BANNER_ID_1);
        }

        @Test
        @DisplayName("[BANNER-011] 필수 필드 누락 시 400 반환")
        void createBanner_ValidationError() {
            // given - title is required
            CreateBannerV2ApiRequest request =
                    new CreateBannerV2ApiRequest(
                            null, // missing required field
                            BANNER_TYPE_CATEGORY,
                            DISPLAY_START_DATE,
                            DISPLAY_END_DATE);

            // when
            ResponseEntity<java.util.Map<String, Object>> response =
                    postExpectingError(BASE_URL, request);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("[BANNER-020] 배너 수정 성공")
        void updateBanner_Success() {
            // given
            UpdateBannerV2ApiRequest request = updateBannerRequest();
            willDoNothing().given(updateBannerUseCase).execute(any(UpdateBannerCommand.class));

            // when
            String url = BASE_URL + "/" + BANNER_ID_1;
            ResponseEntity<ApiResponse<Void>> response =
                    patch(url, request, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("[BANNER-021] 존재하지 않는 배너 수정 시 404 반환")
        void updateBanner_NotFound() {
            // given
            UpdateBannerV2ApiRequest request = updateBannerRequest();
            willThrow(new BannerNotFoundException(BannerId.of(NON_EXISTENT_BANNER_ID)))
                    .given(updateBannerUseCase)
                    .execute(any(UpdateBannerCommand.class));

            // when
            String url = BASE_URL + "/" + NON_EXISTENT_BANNER_ID;
            ResponseEntity<java.util.Map<String, Object>> response =
                    patchExpectingError(url, request);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("[BANNER-030] 배너 활성화 성공")
        void activateBanner_Success() {
            // given
            willDoNothing().given(activateBannerUseCase).execute(any(ActivateBannerCommand.class));

            // when
            String url = BASE_URL + "/" + BANNER_ID_1 + "/activate";
            ResponseEntity<ApiResponse<Void>> response =
                    postWithoutBody(url, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("[BANNER-031] 존재하지 않는 배너 활성화 시 404 반환")
        void activateBanner_NotFound() {
            // given
            willThrow(new BannerNotFoundException(BannerId.of(NON_EXISTENT_BANNER_ID)))
                    .given(activateBannerUseCase)
                    .execute(any(ActivateBannerCommand.class));

            // when
            String url = BASE_URL + "/" + NON_EXISTENT_BANNER_ID + "/activate";
            ResponseEntity<java.util.Map<String, Object>> response =
                    postWithoutBodyExpectingError(url);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("[BANNER-040] 배너 비활성화 성공")
        void deactivateBanner_Success() {
            // given
            willDoNothing()
                    .given(deactivateBannerUseCase)
                    .execute(any(DeactivateBannerCommand.class));

            // when
            String url = BASE_URL + "/" + BANNER_ID_1 + "/deactivate";
            ResponseEntity<ApiResponse<Void>> response =
                    postWithoutBody(url, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("[BANNER-041] 존재하지 않는 배너 비활성화 시 404 반환")
        void deactivateBanner_NotFound() {
            // given
            willThrow(new BannerNotFoundException(BannerId.of(NON_EXISTENT_BANNER_ID)))
                    .given(deactivateBannerUseCase)
                    .execute(any(DeactivateBannerCommand.class));

            // when
            String url = BASE_URL + "/" + NON_EXISTENT_BANNER_ID + "/deactivate";
            ResponseEntity<java.util.Map<String, Object>> response =
                    postWithoutBodyExpectingError(url);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("[BANNER-050] 배너 삭제 성공")
        void deleteBanner_Success() {
            // given
            willDoNothing().given(deleteBannerUseCase).execute(any(DeleteBannerCommand.class));

            // when
            String url = BASE_URL + "/" + BANNER_ID_1 + "/delete";
            ResponseEntity<ApiResponse<Void>> response =
                    postWithoutBody(url, new ParameterizedTypeReference<>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }

        @Test
        @DisplayName("[BANNER-051] 존재하지 않는 배너 삭제 시 404 반환")
        void deleteBanner_NotFound() {
            // given
            willThrow(new BannerNotFoundException(BannerId.of(NON_EXISTENT_BANNER_ID)))
                    .given(deleteBannerUseCase)
                    .execute(any(DeleteBannerCommand.class));

            // when
            String url = BASE_URL + "/" + NON_EXISTENT_BANNER_ID + "/delete";
            ResponseEntity<java.util.Map<String, Object>> response =
                    postWithoutBodyExpectingError(url);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
