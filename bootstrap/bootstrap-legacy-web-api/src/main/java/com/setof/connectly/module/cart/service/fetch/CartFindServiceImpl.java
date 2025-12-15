package com.setof.connectly.module.cart.service.fetch;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.cart.dto.CartCountDto;
import com.setof.connectly.module.cart.dto.CartFilter;
import com.setof.connectly.module.cart.dto.CartResponse;
import com.setof.connectly.module.cart.entity.Cart;
import com.setof.connectly.module.cart.mapper.CartMapper;
import com.setof.connectly.module.cart.mapper.CartSliceMapper;
import com.setof.connectly.module.cart.repository.fetch.CartFindRepository;
import com.setof.connectly.module.cart.service.query.CartCountRedisQueryService;
import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.exception.cart.CartNotFoundException;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import com.setof.connectly.module.product.service.category.ProductCategoryFetchService;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartFindServiceImpl implements CartFindService {

    private final CartCountRedisFindService cartCountRedisFindService;
    private final CartCountRedisQueryService cartCountRedisQueryService;
    private final CartFindRepository cartFindRepository;
    private final CartMapper cartMapper;
    private final CartSliceMapper cartSliceMapper;
    private final ProductCategoryFetchService productCategoryFetchService;

    @Override
    public List<Cart> fetchCartEntities(List<Long> cartIdList) {
        return cartFindRepository.fetchCartEntities(cartIdList, SecurityUtils.currentUserId());
    }

    @Override
    public List<CartResponse> fetchCarts(CartFilter filter, Pageable pageable) {
        return cartFindRepository.fetchCartList(SecurityUtils.currentUserId(), filter, pageable);
    }

    @Override
    public Cart fetchCartEntity(long cartId) {
        return cartFindRepository
                .fetchCartEntity(cartId, SecurityUtils.currentUserId())
                .orElseThrow(() -> new CartNotFoundException(cartId));
    }

    @Override
    public CustomSlice<CartResponse> fetchCartList(CartFilter filter, Pageable pageable) {
        List<CartResponse> findCarts = fetchCarts(filter, pageable);
        CartCountDto cartCountDto = fetchCartCountQuery(SecurityUtils.currentUserId());
        List<ProductCategoryDto> productCategories = fetchCategories(findCarts);

        List<CartResponse> cartResponses = cartMapper.toCartResponses(findCarts, productCategories);

        return cartSliceMapper.toSlice(cartResponses, pageable, cartCountDto.getCartQuantity());
    }

    @Override
    public Optional<Cart> fetchExistingCartEntityByProductId(long productId) {
        return cartFindRepository.fetchExistingCartEntityByProductId(
                productId, SecurityUtils.currentUserId());
    }

    @Override
    public List<Cart> fetchExistingCartEntityByProductIds(List<Long> productIds) {
        return cartFindRepository.fetchExistingCartEntityByProductIds(
                productIds, SecurityUtils.currentUserId());
    }

    @Override
    public CartCountDto fetchCartCountQuery(long userId) {
        Long count = cartCountRedisFindService.fetchCartCountInCache(userId);
        if (count != null) return new CartCountDto(userId, count);
        else return fetchCartCountQueryInDb(userId);
    }

    public CartCountDto fetchCartCountQueryInDb(long userId) {
        JPAQuery<Long> countQuery = cartFindRepository.fetchCartCountQuery(userId);
        cartCountRedisQueryService.insertCartCountInCache(userId, countQuery.fetchCount());
        return new CartCountDto(userId, countQuery.fetchCount());
    }

    private List<ProductCategoryDto> fetchCategories(List<CartResponse> cartResponses) {
        Set<String> categoryIds =
                cartResponses.stream().map(CartResponse::getPath).collect(Collectors.toSet());
        return productCategoryFetchService.fetchProductCategories(categoryIds);
    }
}
