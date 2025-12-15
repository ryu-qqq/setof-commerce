package com.setof.connectly.module.cart.service.query;

import com.setof.connectly.module.cart.dto.CartDeleteRequestDto;
import com.setof.connectly.module.cart.entity.Cart;
import com.setof.connectly.module.cart.entity.embedded.CartDetails;
import com.setof.connectly.module.cart.mapper.CartMapper;
import com.setof.connectly.module.cart.repository.CartRepository;
import com.setof.connectly.module.cart.service.fetch.CartFindService;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CartQueryServiceImpl implements CartQueryService {

    private final CartFindService cartFindService;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartCountRedisQueryService cartCountRedisQueryService;

    @Override
    public List<CartDetails> insertOrUpdate(List<CartDetails> cartDetails) {

        List<CartDetails> results = new ArrayList<>();

        Map<Long, CartDetails> cartDetailsMap =
                cartDetails.stream()
                        .collect(Collectors.toMap(CartDetails::getProductId, Function.identity()));

        List<Cart> carts =
                cartFindService.fetchExistingCartEntityByProductIds(
                        new ArrayList<>(cartDetailsMap.keySet()));

        for (Cart cart : carts) {
            CartDetails findCartDetails = cartDetailsMap.get(cart.getCartDetails().getProductId());
            if (findCartDetails != null) {
                cart.updateQuantity(findCartDetails.getQuantity());
                results.add(findCartDetails);
                cartDetailsMap.remove(cart.getCartDetails().getProductId());
            }
        }

        long userId = SecurityUtils.currentUserId();
        cartDetailsMap.forEach(
                (key, value) -> {
                    Cart cart = cartMapper.toEntity(userId, value);
                    cartRepository.save(cart);
                    results.add(value);
                });

        cartCountRedisQueryService.updateCartCountInCache(userId);

        return results;
    }

    @Override
    public CartDetails updateCart(long cartId, CartDetails cartDetails) {
        Cart cart = cartFindService.fetchCartEntity(cartId);
        cart.updateQuantity(cartDetails.getQuantity());
        return cartDetails;
    }

    @Override
    public int delete(CartDeleteRequestDto requestDto) {
        long cartId = requestDto.cartId();
        List<Cart> carts = cartFindService.fetchCartEntities(List.of(cartId));
        carts.forEach(Cart::delete);
        cartCountRedisQueryService.updateCartCountInCache(SecurityUtils.currentUserId());
        return carts.size();
    }

    @Override
    public void delete(List<Long> productIds) {
        List<Cart> carts = cartFindService.fetchExistingCartEntityByProductIds(productIds);
        if (!carts.isEmpty()) {
            carts.forEach(Cart::delete);
            cartCountRedisQueryService.updateCartCountInCache(SecurityUtils.currentUserId());
        }
    }

    @Override
    public void rollBack(List<Long> cartIds) {
        List<Cart> carts = cartFindService.fetchCartEntities(cartIds);
        carts.forEach(Cart::rollBack);
        cartCountRedisQueryService.updateCartCountInCache(SecurityUtils.currentUserId());
    }
}
