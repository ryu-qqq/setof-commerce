package com.ryuqq.setof.adapter.out.persistence.cart.adapter;

import com.ryuqq.setof.adapter.out.persistence.cart.dto.CartQueryDto;
import com.ryuqq.setof.adapter.out.persistence.cart.mapper.CartCompositeMapper;
import com.ryuqq.setof.adapter.out.persistence.cart.repository.CartCompositeQueryDslRepository;
import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.application.cart.port.out.query.CartQueryPort;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * CartCompositeQueryAdapter - 장바구니 Composite 조회 어댑터.
 *
 * <p>CartQueryPort를 구현하여 새 스키마(setof) 기반 Composite 조회를 수행합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>활성화 조건: persistence.legacy.cart.enabled=false (기본 활성)
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.legacy.cart.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class CartCompositeQueryAdapter implements CartQueryPort {

    private final CartCompositeQueryDslRepository repository;
    private final CartCompositeMapper mapper;

    public CartCompositeQueryAdapter(
            CartCompositeQueryDslRepository repository, CartCompositeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<CartItemResult> fetchCarts(CartSearchCriteria criteria) {
        List<CartQueryDto> dtos = repository.fetchCarts(criteria);
        return mapper.toResults(dtos);
    }

    @Override
    public long countCarts(CartSearchCriteria criteria) {
        return repository.countCarts(criteria.userId());
    }

    @Override
    public CartCountResult fetchCartCount(CartSearchCriteria criteria) {
        long count = repository.countCarts(criteria.userId());
        return mapper.toCountResult(count);
    }
}
