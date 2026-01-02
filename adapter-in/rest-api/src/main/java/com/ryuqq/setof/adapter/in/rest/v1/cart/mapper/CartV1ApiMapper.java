package com.ryuqq.setof.adapter.in.rest.v1.cart.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.command.CreateCartV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.command.DeleteCartV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartCountV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSearchV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSearchV1ApiResponse.CartSearchCategoryV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSearchV1ApiResponse.CartSearchPriceV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.CartSearchV1ApiResponse.CartSearchProductStatusV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.cart.dto.response.DeleteCartV1ApiResponse;
import com.ryuqq.setof.application.cart.dto.command.AddCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.RemoveCartItemCommand;
import com.ryuqq.setof.application.cart.dto.command.UpdateCartItemQuantityCommand;
import com.ryuqq.setof.application.cart.dto.response.EnrichedCartItemResponse;
import com.ryuqq.setof.application.cart.dto.response.EnrichedCartItemResponse.CategoryInfo;
import com.ryuqq.setof.application.cart.dto.response.EnrichedCartResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Cart V1 API Mapper
 *
 * <p>Legacy V1 API 호환성을 위한 Mapper입니다. V1 Request → Application Command, Application Response → V1
 * Response 변환을 담당합니다.
 *
 * <p>주요 매핑:
 *
 * <ul>
 *   <li>V1 productId → V2 productStockId (Legacy에서는 동일 개념)
 *   <li>V1 cartId → V2 cartItemId
 *   <li>V1 Response의 상품 상세 정보는 별도 조회 필요 (현재 기본값 사용)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CartV1ApiMapper {

    private static final BigDecimal DEFAULT_UNIT_PRICE = BigDecimal.ZERO;
    private static final double DEFAULT_MILEAGE_RATE = 0.0;

    /**
     * V1 장바구니 추가 요청을 Command로 변환
     *
     * <p>V1 API에서 productId로 받은 값을 productStockId로 사용합니다. Legacy 시스템에서는 productId가 SKU 레벨을 나타냈습니다.
     *
     * @param memberId 회원 ID (UUID)
     * @param request V1 장바구니 추가 요청
     * @return AddCartItemCommand
     */
    public AddCartItemCommand toAddItemCommand(UUID memberId, CreateCartV1ApiRequest request) {
        return new AddCartItemCommand(
                memberId,
                request.productId(),
                request.productId(),
                request.productGroupId(),
                request.sellerId(),
                request.quantity(),
                DEFAULT_UNIT_PRICE);
    }

    /**
     * V1 장바구니 수정 요청을 Command로 변환
     *
     * <p>V1 API에서 cartId로 받은 값을 cartItemId로 사용합니다.
     *
     * @param memberId 회원 ID (UUID)
     * @param cartId V1 장바구니 ID (V2 cartItemId)
     * @param request V1 장바구니 수정 요청
     * @return UpdateCartItemQuantityCommand
     */
    public UpdateCartItemQuantityCommand toUpdateQuantityCommand(
            UUID memberId, Long cartId, CreateCartV1ApiRequest request) {
        return new UpdateCartItemQuantityCommand(memberId, cartId, request.quantity());
    }

    /**
     * V1 장바구니 삭제 요청을 Command로 변환
     *
     * @param memberId 회원 ID (UUID)
     * @param request V1 장바구니 삭제 요청
     * @return RemoveCartItemCommand
     */
    public RemoveCartItemCommand toRemoveItemCommand(
            UUID memberId, DeleteCartV1ApiRequest request) {
        return new RemoveCartItemCommand(memberId, List.of(request.cartId()));
    }

    /**
     * 장바구니 개수 응답 생성
     *
     * @param count 장바구니 아이템 개수
     * @return CartCountV1ApiResponse
     */
    public CartCountV1ApiResponse toCountResponse(int count) {
        return new CartCountV1ApiResponse(count);
    }

    /**
     * 장바구니 삭제 응답 생성
     *
     * @param deletedCount 삭제된 아이템 개수
     * @return DeleteCartV1ApiResponse
     */
    public DeleteCartV1ApiResponse toDeleteResponse(int deletedCount) {
        return new DeleteCartV1ApiResponse(deletedCount);
    }

    /**
     * Enriched CartResponse를 V1 Response 목록으로 변환
     *
     * <p>상품 상세 정보가 포함된 EnrichedCartResponse를 V1 형식으로 변환합니다.
     *
     * @param response Enriched Cart Response
     * @return V1 장바구니 응답 목록
     */
    public List<CartSearchV1ApiResponse> toV1ResponseList(EnrichedCartResponse response) {
        return response.items().stream().map(this::toV1Response).toList();
    }

    /**
     * EnrichedCartItemResponse를 V1 단건 응답으로 변환
     *
     * @param item EnrichedCartItemResponse
     * @return CartSearchV1ApiResponse
     */
    private CartSearchV1ApiResponse toV1Response(EnrichedCartItemResponse item) {
        return new CartSearchV1ApiResponse(
                item.brandId(),
                item.brandName(),
                item.productGroupName(),
                item.sellerId(),
                item.sellerName(),
                item.productId(),
                toV1PriceResponse(item.regularPrice(), item.salePrice(), item.discountRate()),
                item.quantity(),
                item.stockQuantity(),
                item.optionValue(),
                item.cartItemId(),
                item.imageUrl(),
                item.productGroupId(),
                toProductStatus(item.soldOut()),
                DEFAULT_MILEAGE_RATE,
                calculateExpectedMileage(item.unitPrice(), item.quantity()),
                toCategoryResponses(item.categories()));
    }

    /**
     * V1 가격 응답 생성
     *
     * @param regularPrice 정가
     * @param salePrice 판매가
     * @param discountRate 할인율
     * @return CartSearchPriceV1ApiResponse
     */
    private CartSearchPriceV1ApiResponse toV1PriceResponse(
            BigDecimal regularPrice, BigDecimal salePrice, int discountRate) {
        long regular = regularPrice != null ? regularPrice.longValue() : 0L;
        long sale = salePrice != null ? salePrice.longValue() : regular;
        long directDiscount = regular - sale;

        return new CartSearchPriceV1ApiResponse(
                regular, sale, sale, directDiscount, discountRate, discountRate);
    }

    /**
     * 상품 상태 응답 생성
     *
     * @param soldOut 품절 여부
     * @return CartSearchProductStatusV1ApiResponse
     */
    private CartSearchProductStatusV1ApiResponse toProductStatus(boolean soldOut) {
        return new CartSearchProductStatusV1ApiResponse(soldOut ? "Y" : "N", "Y");
    }

    /**
     * 카테고리 목록 변환
     *
     * @param categories 카테고리 정보 목록
     * @return V1 카테고리 응답 목록
     */
    private List<CartSearchCategoryV1ApiResponse> toCategoryResponses(
            List<CategoryInfo> categories) {
        if (categories == null || categories.isEmpty()) {
            return List.of();
        }

        return categories.stream()
                .map(
                        cat ->
                                new CartSearchCategoryV1ApiResponse(
                                        cat.categoryId(),
                                        cat.categoryName(),
                                        cat.categoryName(),
                                        findParentCategoryId(categories, cat.depth()),
                                        cat.depth()))
                .toList();
    }

    /**
     * 부모 카테고리 ID 조회
     *
     * @param categories 카테고리 목록
     * @param currentDepth 현재 뎁스
     * @return 부모 카테고리 ID (없으면 null)
     */
    private Long findParentCategoryId(List<CategoryInfo> categories, int currentDepth) {
        if (currentDepth <= 1) {
            return null;
        }
        return categories.stream()
                .filter(cat -> cat.depth() == currentDepth - 1)
                .map(CategoryInfo::categoryId)
                .findFirst()
                .orElse(null);
    }

    /**
     * 예상 마일리지 계산
     *
     * @param unitPrice 단가
     * @param quantity 수량
     * @return 예상 마일리지 금액
     */
    private double calculateExpectedMileage(BigDecimal unitPrice, int quantity) {
        if (unitPrice == null) {
            return 0.0;
        }
        return unitPrice
                .multiply(BigDecimal.valueOf(quantity))
                .multiply(BigDecimal.valueOf(DEFAULT_MILEAGE_RATE))
                .doubleValue();
    }
}
