package com.ryuqq.setof.integration.test.e2e.admin.imagevariant;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.imagevariant.ImageVariantJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.imagevariant.repository.ImageVariantJpaRepository;
import com.ryuqq.setof.integration.test.common.base.AdminE2ETestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * ImageVariant Admin E2E 통합 테스트.
 *
 * <p>이미지 Variant 동기화 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository ->
 * DB
 */
@Tag(TestTags.IMAGE_VARIANT)
@DisplayName("이미지 Variant Admin API E2E 테스트")
class ImageVariantAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v2/admin/image-variants";

    @Autowired private ImageVariantJpaRepository imageVariantJpaRepository;

    @BeforeEach
    void setUp() {
        imageVariantJpaRepository.deleteAll();
    }

    // ===== Command 테스트 =====

    @Nested
    @DisplayName("PUT /v2/admin/image-variants/sync - 이미지 Variant 동기화")
    class SyncImageVariantsTest {

        @Test
        @DisplayName("유효한 요청으로 이미지 Variant 신규 동기화 성공")
        void shouldSyncImageVariantsSuccessfully() {
            // given
            Map<String, Object> request =
                    createSyncRequest(
                            100L,
                            "PRODUCT_GROUP_IMAGE",
                            List.of(
                                    Map.of(
                                            "variantType", "SMALL_WEBP",
                                            "resultAssetId", "asset-small-001",
                                            "variantUrl", "https://cdn.example.com/small.webp",
                                            "width", 300,
                                            "height", 300),
                                    Map.of(
                                            "variantType", "MEDIUM_WEBP",
                                            "resultAssetId", "asset-medium-001",
                                            "variantUrl", "https://cdn.example.com/medium.webp",
                                            "width", 600,
                                            "height", 600),
                                    Map.of(
                                            "variantType", "LARGE_WEBP",
                                            "resultAssetId", "asset-large-001",
                                            "variantUrl", "https://cdn.example.com/large.webp",
                                            "width", 1200,
                                            "height", 1200),
                                    Map.of(
                                            "variantType", "ORIGINAL_WEBP",
                                            "resultAssetId", "asset-original-001",
                                            "variantUrl", "https://cdn.example.com/original.webp",
                                            "width", 2000,
                                            "height", 2000)));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // DB 저장 확인
            var saved = imageVariantJpaRepository.findAll();
            assertThat(saved).hasSize(4);
            assertThat(saved).allMatch(e -> e.getDeletedAt() == null);
        }

        @Test
        @DisplayName("기존 Variant가 존재할 때 동기화 시 변경분만 반영")
        void shouldSyncImageVariantsWhenExistingVariantsPresent() {
            // given - 기존 데이터 등록
            imageVariantJpaRepository.save(
                    ImageVariantJpaEntityFixtures.activeEntityWithVariantType("SMALL_WEBP", 200L));
            imageVariantJpaRepository.save(
                    ImageVariantJpaEntityFixtures.activeEntityWithVariantType("MEDIUM_WEBP", 200L));

            Map<String, Object> request =
                    createSyncRequest(
                            200L,
                            "PRODUCT_GROUP_IMAGE",
                            List.of(
                                    Map.of(
                                            "variantType", "SMALL_WEBP",
                                            "resultAssetId", "asset-small-new",
                                            "variantUrl", "https://cdn.example.com/small-new.webp",
                                            "width", 300,
                                            "height", 300),
                                    Map.of(
                                            "variantType", "LARGE_WEBP",
                                            "resultAssetId", "asset-large-new",
                                            "variantUrl", "https://cdn.example.com/large-new.webp",
                                            "width", 1200,
                                            "height", 1200)));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("sourceImageId가 null이면 400 에러 반환")
        void shouldReturn400WhenSourceImageIdIsNull() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put("sourceImageId", null);
            request.put("sourceType", "PRODUCT_GROUP_IMAGE");
            request.put(
                    "variants",
                    List.of(
                            Map.of(
                                    "variantType", "SMALL_WEBP",
                                    "resultAssetId", "asset-001",
                                    "variantUrl", "https://cdn.example.com/small.webp",
                                    "width", 300,
                                    "height", 300)));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("sourceImageId가 0 이하이면 400 에러 반환")
        void shouldReturn400WhenSourceImageIdIsZeroOrNegative() {
            // given
            Map<String, Object> request =
                    createSyncRequest(
                            0L,
                            "PRODUCT_GROUP_IMAGE",
                            List.of(
                                    Map.of(
                                            "variantType", "SMALL_WEBP",
                                            "resultAssetId", "asset-001",
                                            "variantUrl", "https://cdn.example.com/small.webp",
                                            "width", 300,
                                            "height", 300)));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("sourceType이 blank이면 400 에러 반환")
        void shouldReturn400WhenSourceTypeIsBlank() {
            // given
            Map<String, Object> request =
                    createSyncRequest(
                            100L,
                            "",
                            List.of(
                                    Map.of(
                                            "variantType", "SMALL_WEBP",
                                            "resultAssetId", "asset-001",
                                            "variantUrl", "https://cdn.example.com/small.webp",
                                            "width", 300,
                                            "height", 300)));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("variants가 빈 목록이면 400 에러 반환")
        void shouldReturn400WhenVariantsEmpty() {
            // given
            Map<String, Object> request = createSyncRequest(100L, "PRODUCT_GROUP_IMAGE", List.of());

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("variantType이 blank이면 400 에러 반환")
        void shouldReturn400WhenVariantTypeIsBlank() {
            // given
            Map<String, Object> request =
                    createSyncRequest(
                            100L,
                            "PRODUCT_GROUP_IMAGE",
                            List.of(
                                    Map.of(
                                            "variantType", "",
                                            "resultAssetId", "asset-001",
                                            "variantUrl", "https://cdn.example.com/small.webp",
                                            "width", 300,
                                            "height", 300)));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("resultAssetId가 blank이면 400 에러 반환")
        void shouldReturn400WhenResultAssetIdIsBlank() {
            // given
            Map<String, Object> request =
                    createSyncRequest(
                            100L,
                            "PRODUCT_GROUP_IMAGE",
                            List.of(
                                    Map.of(
                                            "variantType", "SMALL_WEBP",
                                            "resultAssetId", "",
                                            "variantUrl", "https://cdn.example.com/small.webp",
                                            "width", 300,
                                            "height", 300)));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("variantUrl이 blank이면 400 에러 반환")
        void shouldReturn400WhenVariantUrlIsBlank() {
            // given
            Map<String, Object> request =
                    createSyncRequest(
                            100L,
                            "PRODUCT_GROUP_IMAGE",
                            List.of(
                                    Map.of(
                                            "variantType", "SMALL_WEBP",
                                            "resultAssetId", "asset-001",
                                            "variantUrl", "",
                                            "width", 300,
                                            "height", 300)));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("ORIGINAL_WEBP 타입은 width/height 없이 동기화 성공")
        void shouldSyncOriginalWebpWithoutDimensions() {
            // given - ORIGINAL_WEBP은 width/height null 허용
            Map<String, Object> variantRequest = new HashMap<>();
            variantRequest.put("variantType", "ORIGINAL_WEBP");
            variantRequest.put("resultAssetId", "asset-original-001");
            variantRequest.put("variantUrl", "https://cdn.example.com/original.webp");
            // width, height 생략 (null)

            Map<String, Object> request =
                    createSyncRequest(300L, "PRODUCT_GROUP_IMAGE", List.of(variantRequest));

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // DB 저장 확인
            var saved = imageVariantJpaRepository.findAll();
            assertThat(saved).hasSize(1);
            assertThat(saved.get(0).getWidth()).isNull();
            assertThat(saved.get(0).getHeight()).isNull();
        }
    }

    // ===== 전체 플로우 시나리오 =====

    @Nested
    @DisplayName("전체 플로우 시나리오")
    class FullFlowTest {

        @Test
        @DisplayName("신규 Variant 동기화 후 기존 Variant 교체 플로우")
        void shouldSyncAndReplaceVariantsFlow() {
            // 1단계: 초기 4가지 타입 전체 동기화
            Map<String, Object> firstRequest =
                    createSyncRequest(
                            400L,
                            "PRODUCT_GROUP_IMAGE",
                            List.of(
                                    Map.of(
                                            "variantType", "SMALL_WEBP",
                                            "resultAssetId", "asset-small-v1",
                                            "variantUrl", "https://cdn.example.com/small-v1.webp",
                                            "width", 300,
                                            "height", 300),
                                    Map.of(
                                            "variantType", "MEDIUM_WEBP",
                                            "resultAssetId", "asset-medium-v1",
                                            "variantUrl", "https://cdn.example.com/medium-v1.webp",
                                            "width", 600,
                                            "height", 600)));

            givenAdmin()
                    .body(firstRequest)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.OK.value());

            var afterFirst = imageVariantJpaRepository.findAll();
            assertThat(afterFirst).hasSize(2);

            // 2단계: 새로운 Variant 타입 추가 동기화
            Map<String, Object> secondRequest =
                    createSyncRequest(
                            400L,
                            "PRODUCT_GROUP_IMAGE",
                            List.of(
                                    Map.of(
                                            "variantType", "LARGE_WEBP",
                                            "resultAssetId", "asset-large-v2",
                                            "variantUrl", "https://cdn.example.com/large-v2.webp",
                                            "width", 1200,
                                            "height", 1200),
                                    Map.of(
                                            "variantType", "ORIGINAL_WEBP",
                                            "resultAssetId", "asset-original-v2",
                                            "variantUrl",
                                                    "https://cdn.example.com/original-v2.webp",
                                            "width", 2000,
                                            "height", 2000)));

            givenAdmin()
                    .body(secondRequest)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("여러 sourceImageId에 대한 독립적 동기화 플로우")
        void shouldSyncVariantsIndependentlyPerSourceImage() {
            // 1단계: 첫 번째 이미지 동기화
            Map<String, Object> firstImageRequest =
                    createSyncRequest(
                            500L,
                            "PRODUCT_GROUP_IMAGE",
                            List.of(
                                    Map.of(
                                            "variantType", "SMALL_WEBP",
                                            "resultAssetId", "asset-500-small",
                                            "variantUrl", "https://cdn.example.com/500/small.webp",
                                            "width", 300,
                                            "height", 300)));

            givenAdmin()
                    .body(firstImageRequest)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // 2단계: 두 번째 이미지 동기화
            Map<String, Object> secondImageRequest =
                    createSyncRequest(
                            501L,
                            "PRODUCT_GROUP_IMAGE",
                            List.of(
                                    Map.of(
                                            "variantType", "MEDIUM_WEBP",
                                            "resultAssetId", "asset-501-medium",
                                            "variantUrl", "https://cdn.example.com/501/medium.webp",
                                            "width", 600,
                                            "height", 600)));

            givenAdmin()
                    .body(secondImageRequest)
                    .when()
                    .put(BASE_PATH + "/sync")
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // 두 이미지 각각 1개씩 총 2개 저장 확인
            var allSaved = imageVariantJpaRepository.findAll();
            assertThat(allSaved).hasSize(2);
            assertThat(allSaved).extracting("sourceImageId").containsExactlyInAnyOrder(500L, 501L);
        }
    }

    // ===== Helper Methods =====

    private Map<String, Object> createSyncRequest(
            Long sourceImageId, String sourceType, List<Map<String, Object>> variants) {
        Map<String, Object> request = new HashMap<>();
        request.put("sourceImageId", sourceImageId);
        request.put("sourceType", sourceType);
        request.put("variants", variants);
        return request;
    }
}
