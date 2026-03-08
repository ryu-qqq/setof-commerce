package com.ryuqq.setof.adapter.in.rest.v1.wishlist.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.wishlist.WishlistApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.request.AddWishlistItemV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.response.WishlistItemSliceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.wishlist.dto.response.WishlistItemV1ApiResponse;
import com.ryuqq.setof.application.wishlist.dto.command.AddWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.dto.command.DeleteWishlistItemCommand;
import com.ryuqq.setof.application.wishlist.dto.query.WishlistSearchParams;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemSliceResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * WishlistV1ApiMapper 단위 테스트.
 *
 * <p>찜 V1 API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("WishlistV1ApiMapper 단위 테스트")
class WishlistV1ApiMapperTest {

    private WishlistV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new WishlistV1ApiMapper();
    }

    @Nested
    @DisplayName("toAddCommand 메서드 테스트")
    class ToAddCommandTest {

        @Test
        @DisplayName("userId와 AddRequest를 AddWishlistItemCommand로 변환한다")
        void toAddCommand_Success() {
            // given
            Long userId = 10L;
            AddWishlistItemV1ApiRequest request = WishlistApiFixtures.addRequest();

            // when
            AddWishlistItemCommand command = mapper.toAddCommand(userId, request);

            // then
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.productGroupId()).isEqualTo(1001L);
        }

        @Test
        @DisplayName("다른 userId로 변환해도 productGroupId는 동일하게 매핑된다")
        void toAddCommand_DifferentUserId() {
            // given
            Long userId = 99L;
            AddWishlistItemV1ApiRequest request = WishlistApiFixtures.addRequest();

            // when
            AddWishlistItemCommand command = mapper.toAddCommand(userId, request);

            // then
            assertThat(command.userId()).isEqualTo(99L);
            assertThat(command.productGroupId()).isEqualTo(1001L);
        }
    }

    @Nested
    @DisplayName("toDeleteCommand 메서드 테스트")
    class ToDeleteCommandTest {

        @Test
        @DisplayName("userId와 productGroupId를 DeleteWishlistItemCommand로 변환한다")
        void toDeleteCommand_Success() {
            // given
            Long userId = 10L;
            long productGroupId = 1001L;

            // when
            DeleteWishlistItemCommand command = mapper.toDeleteCommand(userId, productGroupId);

            // then
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
        }
    }

    @Nested
    @DisplayName("toSearchParams 메서드 테스트")
    class ToSearchParamsTest {

        @Test
        @DisplayName("userId, lastDomainId, size를 WishlistSearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            Long userId = 10L;
            Long lastDomainId = 5L;
            int size = 20;

            // when
            WishlistSearchParams params = mapper.toSearchParams(userId, lastDomainId, size);

            // then
            assertThat(params).isNotNull();
            assertThat(params.userId()).isEqualTo(userId);
            assertThat(params.lastDomainId()).isEqualTo(lastDomainId);
            assertThat(params.size()).isEqualTo(size);
        }

        @Test
        @DisplayName("lastDomainId가 null이어도 WishlistSearchParams로 변환된다")
        void toSearchParams_NullLastDomainId() {
            // given
            Long userId = 10L;
            Long lastDomainId = null;
            int size = 20;

            // when
            WishlistSearchParams params = mapper.toSearchParams(userId, lastDomainId, size);

            // then
            assertThat(params).isNotNull();
            assertThat(params.userId()).isEqualTo(userId);
            assertThat(params.lastDomainId()).isNull();
        }
    }

    @Nested
    @DisplayName("toItemResponse 메서드 테스트")
    class ToItemResponseTest {

        @Test
        @DisplayName("WishlistItemResult를 WishlistItemV1ApiResponse로 변환한다")
        void toItemResponse_Success() {
            // given
            WishlistItemResult result = WishlistApiFixtures.wishlistItemResult(1L);

            // when
            WishlistItemV1ApiResponse response = mapper.toItemResponse(result);

            // then
            assertThat(response.userFavoriteId()).isEqualTo(1L);
            assertThat(response.productGroupId()).isEqualTo(1001L);
            assertThat(response.sellerId()).isEqualTo(5L);
            assertThat(response.productGroupName()).isEqualTo("나이키 에어맥스 90");
            assertThat(response.imageUrl()).isEqualTo("https://example.com/image.jpg");
            assertThat(response.insertDate()).isEqualTo("2024-01-01T10:30:00");
        }

        @Test
        @DisplayName("brand 정보가 올바르게 매핑된다")
        void toItemResponse_BrandMapping() {
            // given
            WishlistItemResult result = WishlistApiFixtures.wishlistItemResult(1L);

            // when
            WishlistItemV1ApiResponse response = mapper.toItemResponse(result);

            // then
            assertThat(response.brand().brandId()).isEqualTo(3L);
            assertThat(response.brand().brandName()).isEqualTo("Nike");
        }

        @Test
        @DisplayName("price 정보가 올바르게 매핑된다")
        void toItemResponse_PriceMapping() {
            // given
            WishlistItemResult result = WishlistApiFixtures.wishlistItemResult(1L);

            // when
            WishlistItemV1ApiResponse response = mapper.toItemResponse(result);

            // then
            assertThat(response.price().regularPrice()).isEqualByComparingTo("50000");
            assertThat(response.price().currentPrice()).isEqualByComparingTo("40000");
            assertThat(response.price().discountRate()).isEqualTo(20);
        }

        @Test
        @DisplayName("productStatus가 올바르게 매핑된다 - 품절 아님")
        void toItemResponse_ProductStatusMapping_NotSoldOut() {
            // given
            WishlistItemResult result = WishlistApiFixtures.wishlistItemResult(1L);

            // when
            WishlistItemV1ApiResponse response = mapper.toItemResponse(result);

            // then
            assertThat(response.productStatus().soldOut()).isFalse();
            assertThat(response.productStatus().displayed()).isTrue();
        }

        @Test
        @DisplayName("품절 상품의 productStatus가 올바르게 매핑된다")
        void toItemResponse_ProductStatusMapping_SoldOut() {
            // given
            WishlistItemResult result = WishlistApiFixtures.soldOutWishlistItemResult(2L);

            // when
            WishlistItemV1ApiResponse response = mapper.toItemResponse(result);

            // then
            assertThat(response.productStatus().soldOut()).isTrue();
            assertThat(response.productStatus().displayed()).isTrue();
        }
    }

    @Nested
    @DisplayName("toSliceResponse 메서드 테스트")
    class ToSliceResponseTest {

        @Test
        @DisplayName("WishlistItemSliceResult를 WishlistItemSliceV1ApiResponse로 변환한다")
        void toSliceResponse_Success() {
            // given
            WishlistItemSliceResult sliceResult = WishlistApiFixtures.wishlistItemSliceResult();

            // when
            WishlistItemSliceV1ApiResponse response = mapper.toSliceResponse(sliceResult, 20);

            // then
            assertThat(response.content()).hasSize(2);
            assertThat(response.last()).isFalse();
            assertThat(response.totalElements()).isEqualTo(42L);
        }

        @Test
        @DisplayName("빈 슬라이스 결과를 빈 응답으로 변환한다")
        void toSliceResponse_Empty() {
            // given
            WishlistItemSliceResult sliceResult = WishlistApiFixtures.emptySliceResult();

            // when
            WishlistItemSliceV1ApiResponse response = mapper.toSliceResponse(sliceResult, 20);

            // then
            assertThat(response.content()).isEmpty();
            assertThat(response.last()).isTrue();
            assertThat(response.totalElements()).isEqualTo(0L);
        }

        @Test
        @DisplayName("슬라이스 내 각 항목이 올바르게 변환된다")
        void toSliceResponse_ContentMapping() {
            // given
            WishlistItemSliceResult sliceResult = WishlistApiFixtures.wishlistItemSliceResult();

            // when
            WishlistItemSliceV1ApiResponse response = mapper.toSliceResponse(sliceResult, 20);

            // then
            assertThat(response.content().get(0).userFavoriteId()).isEqualTo(1L);
            assertThat(response.content().get(1).userFavoriteId()).isEqualTo(2L);
        }
    }
}
