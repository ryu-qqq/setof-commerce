package com.ryuqq.setof.adapter.in.rest.admin.integration.v2.banneritem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.BannerItemAdminTestFixture;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banneritem.dto.command.CreateBannerItemV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banneritem.dto.response.BannerItemV2ApiResponse;
import com.ryuqq.setof.application.banneritem.dto.command.CreateBannerItemCommand;
import com.ryuqq.setof.application.banneritem.dto.response.BannerItemResponse;
import com.ryuqq.setof.application.banneritem.port.in.command.CreateBannerItemUseCase;
import com.ryuqq.setof.application.banneritem.port.in.query.GetBannerItemsUseCase;
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
 * BannerItem Admin API 통합 테스트
 *
 * <p>Admin BannerItem API의 엔드포인트 동작을 검증합니다.
 *
 * <p>테스트 범위:
 *
 * <ul>
 *   <li>Query: 배너 아이템 목록 조회
 *   <li>Command: 배너 아이템 생성, 일괄 생성
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("Admin BannerItem API 통합 테스트")
class BannerItemAdminIntegrationTest extends ApiIntegrationTestSupport {

    @Autowired private CreateBannerItemUseCase createBannerItemUseCase;

    @Autowired private GetBannerItemsUseCase getBannerItemsUseCase;

    // ========================================================================
    // Query Tests
    // ========================================================================

    @Nested
    @DisplayName("배너 아이템 목록 조회")
    class GetBannerItemsTest {

        @Test
        @DisplayName("ABI-001: 배너 ID로 배너 아이템 목록 조회 성공")
        void getBannerItems_byBannerId_returnsBannerItemList() {
            // Given
            Long bannerId = BannerItemAdminTestFixture.EXISTING_BANNER_ID;
            List<BannerItemResponse> bannerItemResponses =
                    BannerItemAdminTestFixture.createBannerItemResponses(bannerId);

            given(getBannerItemsUseCase.getActiveByBannerId(any(BannerId.class)))
                    .willReturn(bannerItemResponses);

            // When
            String url = ApiV2Paths.Banners.BASE + "/" + bannerId + "/items";
            ResponseEntity<ApiResponse<List<BannerItemV2ApiResponse>>> response =
                    get(url, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isNotNull();
            assertThat(response.getBody().data()).hasSize(3);
            assertThat(response.getBody().data().get(0).bannerItemId())
                    .isEqualTo(BannerItemAdminTestFixture.BANNER_ITEM_ID_1);
            assertThat(response.getBody().data().get(0).bannerId()).isEqualTo(bannerId);
        }

        @Test
        @DisplayName("ABI-002: 배너 아이템이 없는 경우 빈 목록 반환")
        void getBannerItems_whenNoItems_returnsEmptyList() {
            // Given
            Long bannerId = BannerItemAdminTestFixture.EXISTING_BANNER_ID;

            given(getBannerItemsUseCase.getActiveByBannerId(any(BannerId.class)))
                    .willReturn(List.of());

            // When
            String url = ApiV2Paths.Banners.BASE + "/" + bannerId + "/items";
            ResponseEntity<ApiResponse<List<BannerItemV2ApiResponse>>> response =
                    get(url, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEmpty();
        }
    }

    // ========================================================================
    // Command Tests - Create
    // ========================================================================

    @Nested
    @DisplayName("배너 아이템 생성")
    class CreateBannerItemTest {

        @Test
        @DisplayName("ABI-010: 배너 아이템 단건 생성 성공")
        void createBannerItem_success() {
            // Given
            Long bannerId = BannerItemAdminTestFixture.EXISTING_BANNER_ID;
            Long createdBannerItemId = BannerItemAdminTestFixture.BANNER_ITEM_ID_1;
            CreateBannerItemV2ApiRequest request =
                    BannerItemAdminTestFixture.createBannerItemRequest();

            given(createBannerItemUseCase.create(any(CreateBannerItemCommand.class)))
                    .willReturn(createdBannerItemId);

            // When
            String url = ApiV2Paths.Banners.BASE + "/" + bannerId + "/items";
            ResponseEntity<ApiResponse<Long>> response =
                    post(url, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(createdBannerItemId);
        }

        @Test
        @DisplayName("ABI-011: 이미지 URL 없이 생성 시 400 반환")
        void createBannerItem_withoutImageUrl_returns400() {
            // Given
            Long bannerId = BannerItemAdminTestFixture.EXISTING_BANNER_ID;
            CreateBannerItemV2ApiRequest request =
                    new CreateBannerItemV2ApiRequest(
                            "배너 제목",
                            null, // imageUrl is required
                            "https://example.com/link",
                            1,
                            null,
                            null,
                            null,
                            null);

            // When
            String url = ApiV2Paths.Banners.BASE + "/" + bannerId + "/items";
            ResponseEntity<ApiResponse<Long>> response =
                    post(url, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("ABI-012: 노출 순서 없이 생성 시 400 반환")
        void createBannerItem_withoutDisplayOrder_returns400() {
            // Given
            Long bannerId = BannerItemAdminTestFixture.EXISTING_BANNER_ID;
            CreateBannerItemV2ApiRequest request =
                    new CreateBannerItemV2ApiRequest(
                            "배너 제목",
                            "https://cdn.example.com/banner.jpg",
                            "https://example.com/link",
                            null, // displayOrder is required
                            null,
                            null,
                            null,
                            null);

            // When
            String url = ApiV2Paths.Banners.BASE + "/" + bannerId + "/items";
            ResponseEntity<ApiResponse<Long>> response =
                    post(url, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("배너 아이템 일괄 생성")
    class CreateBannerItemsBatchTest {

        @Test
        @DisplayName("ABI-020: 배너 아이템 일괄 생성 성공")
        void createBannerItemsBatch_success() {
            // Given
            Long bannerId = BannerItemAdminTestFixture.EXISTING_BANNER_ID;
            List<CreateBannerItemV2ApiRequest> requests =
                    BannerItemAdminTestFixture.createBannerItemBatchRequests();
            List<Long> createdIds =
                    List.of(
                            BannerItemAdminTestFixture.BANNER_ITEM_ID_1,
                            BannerItemAdminTestFixture.BANNER_ITEM_ID_2,
                            BannerItemAdminTestFixture.BANNER_ITEM_ID_3);

            given(createBannerItemUseCase.createAll(anyList())).willReturn(createdIds);

            // When
            String url = ApiV2Paths.Banners.BASE + "/" + bannerId + "/items/batch";
            ResponseEntity<ApiResponse<List<Long>>> response =
                    post(url, requests, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).hasSize(3);
            assertThat(response.getBody().data())
                    .containsExactly(
                            BannerItemAdminTestFixture.BANNER_ITEM_ID_1,
                            BannerItemAdminTestFixture.BANNER_ITEM_ID_2,
                            BannerItemAdminTestFixture.BANNER_ITEM_ID_3);
        }

        @Test
        @DisplayName("ABI-021: 빈 목록으로 일괄 생성 시 빈 목록 반환")
        void createBannerItemsBatch_withEmptyList_returnsEmptyList() {
            // Given
            Long bannerId = BannerItemAdminTestFixture.EXISTING_BANNER_ID;
            List<CreateBannerItemV2ApiRequest> requests = List.of();

            given(createBannerItemUseCase.createAll(anyList())).willReturn(List.of());

            // When
            String url = ApiV2Paths.Banners.BASE + "/" + bannerId + "/items/batch";
            ResponseEntity<ApiResponse<List<Long>>> response =
                    post(url, requests, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEmpty();
        }
    }
}
