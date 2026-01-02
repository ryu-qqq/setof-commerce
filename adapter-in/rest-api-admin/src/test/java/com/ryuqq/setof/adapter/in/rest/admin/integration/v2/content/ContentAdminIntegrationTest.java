package com.ryuqq.setof.adapter.in.rest.admin.integration.v2.content;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.ContentAdminTestFixture;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.command.CreateContentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.command.UpdateContentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.response.ContentSummaryV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.content.dto.response.ContentV2ApiResponse;
import com.ryuqq.setof.application.content.dto.command.ActivateContentCommand;
import com.ryuqq.setof.application.content.dto.command.CreateContentCommand;
import com.ryuqq.setof.application.content.dto.command.DeactivateContentCommand;
import com.ryuqq.setof.application.content.dto.command.DeleteContentCommand;
import com.ryuqq.setof.application.content.dto.command.UpdateContentCommand;
import com.ryuqq.setof.application.content.dto.query.SearchContentQuery;
import com.ryuqq.setof.application.content.dto.response.ContentResponse;
import com.ryuqq.setof.application.content.dto.response.ContentSummaryResponse;
import com.ryuqq.setof.application.content.port.in.command.ActivateContentUseCase;
import com.ryuqq.setof.application.content.port.in.command.CreateContentUseCase;
import com.ryuqq.setof.application.content.port.in.command.DeactivateContentUseCase;
import com.ryuqq.setof.application.content.port.in.command.DeleteContentUseCase;
import com.ryuqq.setof.application.content.port.in.command.UpdateContentUseCase;
import com.ryuqq.setof.application.content.port.in.query.GetContentUseCase;
import com.ryuqq.setof.application.content.port.in.query.SearchContentUseCase;
import com.ryuqq.setof.domain.cms.exception.ContentNotFoundException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Content Admin API 통합 테스트
 *
 * <p>Admin Content API의 엔드포인트 동작을 검증합니다.
 *
 * <p>테스트 범위:
 *
 * <ul>
 *   <li>Query: 콘텐츠 목록 조회, 단건 조회
 *   <li>Command: 콘텐츠 생성, 수정, 활성화, 비활성화, 삭제
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("Admin Content API 통합 테스트")
class ContentAdminIntegrationTest extends ApiIntegrationTestSupport {

    // Query UseCases
    @Autowired private GetContentUseCase getContentUseCase;
    @Autowired private SearchContentUseCase searchContentUseCase;

    // Command UseCases
    @Autowired private CreateContentUseCase createContentUseCase;
    @Autowired private UpdateContentUseCase updateContentUseCase;
    @Autowired private ActivateContentUseCase activateContentUseCase;
    @Autowired private DeactivateContentUseCase deactivateContentUseCase;
    @Autowired private DeleteContentUseCase deleteContentUseCase;

    // ========================================================================
    // Query Tests
    // ========================================================================

    @Nested
    @DisplayName("콘텐츠 목록 조회")
    class SearchContentsTest {

        @Test
        @DisplayName("CON-001: 콘텐츠 목록 조회 성공")
        void searchContents_returnsContentList() {
            // Given
            List<ContentSummaryResponse> summaryResponses =
                    ContentAdminTestFixture.createContentSummaryResponses();

            given(searchContentUseCase.execute(any(SearchContentQuery.class)))
                    .willReturn(summaryResponses);

            // When
            String url = ApiV2Paths.Contents.BASE;
            ResponseEntity<ApiResponse<List<ContentSummaryV2ApiResponse>>> response =
                    get(url, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isNotNull();
            assertThat(response.getBody().data()).hasSize(3);
            assertThat(response.getBody().data().get(0).contentId())
                    .isEqualTo(ContentAdminTestFixture.CONTENT_ID_1);
        }

        @Test
        @DisplayName("CON-002: 빈 콘텐츠 목록 조회 성공")
        void searchContents_whenNoContents_returnsEmptyList() {
            // Given
            given(searchContentUseCase.execute(any(SearchContentQuery.class)))
                    .willReturn(List.of());

            // When
            String url = ApiV2Paths.Contents.BASE;
            ResponseEntity<ApiResponse<List<ContentSummaryV2ApiResponse>>> response =
                    get(url, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEmpty();
        }

        @Test
        @DisplayName("CON-003: 제목 검색으로 콘텐츠 목록 조회")
        void searchContents_withTitleFilter_returnsFilteredList() {
            // Given
            List<ContentSummaryResponse> summaryResponses =
                    List.of(
                            ContentAdminTestFixture.createContentSummaryResponse(
                                    ContentAdminTestFixture.CONTENT_ID_1, "메인 페이지", "ACTIVE", 3));

            given(searchContentUseCase.execute(any(SearchContentQuery.class)))
                    .willReturn(summaryResponses);

            // When
            String url = ApiV2Paths.Contents.BASE + "?title=메인";
            ResponseEntity<ApiResponse<List<ContentSummaryV2ApiResponse>>> response =
                    get(url, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("콘텐츠 단건 조회")
    class GetContentTest {

        @Test
        @DisplayName("CON-010: 콘텐츠 단건 조회 성공")
        void getContent_byId_returnsContent() {
            // Given
            Long contentId = ContentAdminTestFixture.CONTENT_ID_1;
            ContentResponse contentResponse =
                    ContentAdminTestFixture.createContentResponse(contentId);

            given(getContentUseCase.execute(contentId)).willReturn(contentResponse);

            // When
            String url = ApiV2Paths.Contents.BASE + "/" + contentId;
            ResponseEntity<ApiResponse<ContentV2ApiResponse>> response =
                    get(url, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isNotNull();
            assertThat(response.getBody().data().contentId()).isEqualTo(contentId);
            assertThat(response.getBody().data().title())
                    .isEqualTo(ContentAdminTestFixture.CONTENT_TITLE);
        }

        @Test
        @DisplayName("CON-011: 존재하지 않는 콘텐츠 조회 시 404 반환")
        void getContent_withNonExistentId_returns404() {
            // Given
            Long nonExistentId = ContentAdminTestFixture.NON_EXISTENT_CONTENT_ID;

            given(getContentUseCase.execute(nonExistentId))
                    .willThrow(new ContentNotFoundException(nonExistentId));

            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    getExpectingError(ApiV2Paths.Contents.BASE + "/" + nonExistentId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ========================================================================
    // Command Tests - Create
    // ========================================================================

    @Nested
    @DisplayName("콘텐츠 생성")
    class CreateContentTest {

        @Test
        @DisplayName("CON-020: 콘텐츠 생성 성공")
        void createContent_success() {
            // Given
            Long createdContentId = ContentAdminTestFixture.CONTENT_ID_1;
            CreateContentV2ApiRequest request = ContentAdminTestFixture.createContentRequest();

            given(createContentUseCase.execute(any(CreateContentCommand.class)))
                    .willReturn(createdContentId);

            // When
            String url = ApiV2Paths.Contents.BASE;
            ResponseEntity<ApiResponse<Long>> response =
                    post(url, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(createdContentId);
        }

        @Test
        @DisplayName("CON-021: 제목 없이 콘텐츠 생성 시 400 반환")
        void createContent_withoutTitle_returns400() {
            // Given
            CreateContentV2ApiRequest request =
                    new CreateContentV2ApiRequest(
                            null, // title is required
                            ContentAdminTestFixture.CONTENT_MEMO,
                            ContentAdminTestFixture.CONTENT_IMAGE_URL,
                            ContentAdminTestFixture.DISPLAY_START_DATE,
                            ContentAdminTestFixture.DISPLAY_END_DATE);

            // When
            String url = ApiV2Paths.Contents.BASE;
            ResponseEntity<ApiResponse<Long>> response =
                    post(url, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("CON-022: 빈 제목으로 콘텐츠 생성 시 400 반환")
        void createContent_withBlankTitle_returns400() {
            // Given
            CreateContentV2ApiRequest request =
                    new CreateContentV2ApiRequest(
                            "", // blank title
                            ContentAdminTestFixture.CONTENT_MEMO,
                            ContentAdminTestFixture.CONTENT_IMAGE_URL,
                            ContentAdminTestFixture.DISPLAY_START_DATE,
                            ContentAdminTestFixture.DISPLAY_END_DATE);

            // When
            String url = ApiV2Paths.Contents.BASE;
            ResponseEntity<ApiResponse<Long>> response =
                    post(url, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("CON-023: 노출 시작일 없이 콘텐츠 생성 시 400 반환")
        void createContent_withoutDisplayStartDate_returns400() {
            // Given
            CreateContentV2ApiRequest request =
                    new CreateContentV2ApiRequest(
                            ContentAdminTestFixture.CONTENT_TITLE,
                            ContentAdminTestFixture.CONTENT_MEMO,
                            ContentAdminTestFixture.CONTENT_IMAGE_URL,
                            null, // displayStartDate is required
                            ContentAdminTestFixture.DISPLAY_END_DATE);

            // When
            String url = ApiV2Paths.Contents.BASE;
            ResponseEntity<ApiResponse<Long>> response =
                    post(url, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    // ========================================================================
    // Command Tests - Update
    // ========================================================================

    @Nested
    @DisplayName("콘텐츠 수정")
    class UpdateContentTest {

        @Test
        @DisplayName("CON-030: 콘텐츠 수정 성공")
        void updateContent_success() {
            // Given
            Long contentId = ContentAdminTestFixture.CONTENT_ID_1;
            UpdateContentV2ApiRequest request = ContentAdminTestFixture.updateContentRequest();

            willDoNothing().given(updateContentUseCase).execute(any(UpdateContentCommand.class));

            // When
            String url = ApiV2Paths.Contents.BASE + "/" + contentId;
            ResponseEntity<ApiResponse<Void>> response =
                    patch(url, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("CON-031: 존재하지 않는 콘텐츠 수정 시 404 반환")
        void updateContent_withNonExistentId_returns404() {
            // Given
            Long nonExistentId = ContentAdminTestFixture.NON_EXISTENT_CONTENT_ID;
            UpdateContentV2ApiRequest request = ContentAdminTestFixture.updateContentRequest();

            willThrow(new ContentNotFoundException(nonExistentId))
                    .given(updateContentUseCase)
                    .execute(any(UpdateContentCommand.class));

            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    patchExpectingError(ApiV2Paths.Contents.BASE + "/" + nonExistentId, request);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ========================================================================
    // Command Tests - Activate / Deactivate
    // ========================================================================

    @Nested
    @DisplayName("콘텐츠 활성화")
    class ActivateContentTest {

        @Test
        @DisplayName("CON-040: 콘텐츠 활성화 성공")
        void activateContent_success() {
            // Given
            Long contentId = ContentAdminTestFixture.CONTENT_ID_1;

            willDoNothing()
                    .given(activateContentUseCase)
                    .execute(any(ActivateContentCommand.class));

            // When
            String url = ApiV2Paths.Contents.BASE + "/" + contentId + "/activate";
            ResponseEntity<ApiResponse<Void>> response =
                    postWithoutBody(url, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("CON-041: 존재하지 않는 콘텐츠 활성화 시 404 반환")
        void activateContent_withNonExistentId_returns404() {
            // Given
            Long nonExistentId = ContentAdminTestFixture.NON_EXISTENT_CONTENT_ID;

            willThrow(new ContentNotFoundException(nonExistentId))
                    .given(activateContentUseCase)
                    .execute(any(ActivateContentCommand.class));

            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    postWithoutBodyExpectingError(
                            ApiV2Paths.Contents.BASE + "/" + nonExistentId + "/activate");

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("콘텐츠 비활성화")
    class DeactivateContentTest {

        @Test
        @DisplayName("CON-050: 콘텐츠 비활성화 성공")
        void deactivateContent_success() {
            // Given
            Long contentId = ContentAdminTestFixture.CONTENT_ID_1;

            willDoNothing()
                    .given(deactivateContentUseCase)
                    .execute(any(DeactivateContentCommand.class));

            // When
            String url = ApiV2Paths.Contents.BASE + "/" + contentId + "/deactivate";
            ResponseEntity<ApiResponse<Void>> response =
                    postWithoutBody(url, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("CON-051: 존재하지 않는 콘텐츠 비활성화 시 404 반환")
        void deactivateContent_withNonExistentId_returns404() {
            // Given
            Long nonExistentId = ContentAdminTestFixture.NON_EXISTENT_CONTENT_ID;

            willThrow(new ContentNotFoundException(nonExistentId))
                    .given(deactivateContentUseCase)
                    .execute(any(DeactivateContentCommand.class));

            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    postWithoutBodyExpectingError(
                            ApiV2Paths.Contents.BASE + "/" + nonExistentId + "/deactivate");

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    // ========================================================================
    // Command Tests - Delete
    // ========================================================================

    @Nested
    @DisplayName("콘텐츠 삭제")
    class DeleteContentTest {

        @Test
        @DisplayName("CON-060: 콘텐츠 삭제 성공")
        void deleteContent_success() {
            // Given
            Long contentId = ContentAdminTestFixture.CONTENT_ID_1;

            willDoNothing().given(deleteContentUseCase).execute(any(DeleteContentCommand.class));

            // When
            String url = ApiV2Paths.Contents.BASE + "/" + contentId + "/delete";
            ResponseEntity<ApiResponse<Void>> response =
                    postWithoutBody(url, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("CON-061: 존재하지 않는 콘텐츠 삭제 시 404 반환")
        void deleteContent_withNonExistentId_returns404() {
            // Given
            Long nonExistentId = ContentAdminTestFixture.NON_EXISTENT_CONTENT_ID;

            willThrow(new ContentNotFoundException(nonExistentId))
                    .given(deleteContentUseCase)
                    .execute(any(DeleteContentCommand.class));

            // When
            ResponseEntity<java.util.Map<String, Object>> response =
                    postWithoutBodyExpectingError(
                            ApiV2Paths.Contents.BASE + "/" + nonExistentId + "/delete");

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
