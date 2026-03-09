package com.ryuqq.setof.storage.legacy.composite.cart.adapter;

import com.ryuqq.setof.application.cart.dto.response.CartCountResult;
import com.ryuqq.setof.application.cart.dto.response.CartItemResult;
import com.ryuqq.setof.application.cart.port.out.query.CartQueryPort;
import com.ryuqq.setof.domain.cart.query.CartSearchCriteria;
import com.ryuqq.setof.domain.legacy.cart.dto.query.LegacyCartSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.cart.dto.LegacyWebCartQueryDto;
import com.ryuqq.setof.storage.legacy.composite.cart.mapper.LegacyWebCartMapper;
import com.ryuqq.setof.storage.legacy.composite.cart.repository.LegacyWebCartCompositeQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebCartCompositeQueryAdapter - 레거시 장바구니 Composite 조회 Adapter.
 *
 * <p>Domain SearchCriteria를 Legacy SearchCondition으로 변환하여 Repository에 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebCartCompositeQueryAdapter implements CartQueryPort {

    private final LegacyWebCartCompositeQueryDslRepository repository;
    private final LegacyWebCartMapper mapper;

    public LegacyWebCartCompositeQueryAdapter(
            LegacyWebCartCompositeQueryDslRepository repository, LegacyWebCartMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<CartItemResult> fetchCarts(CartSearchCriteria criteria) {
        LegacyCartSearchCondition condition = toLegacyCondition(criteria);
        List<LegacyWebCartQueryDto> dtos = repository.fetchCarts(condition);
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

    private LegacyCartSearchCondition toLegacyCondition(CartSearchCriteria criteria) {
        return LegacyCartSearchCondition.ofCursor(
                criteria.userId(), criteria.cursor(), criteria.size());
    }
}
