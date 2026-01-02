package com.ryuqq.setof.adapter.out.persistence.checkout.adapter;

import com.ryuqq.setof.adapter.out.persistence.checkout.entity.CheckoutItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.checkout.entity.CheckoutJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.checkout.mapper.CheckoutJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.checkout.repository.CheckoutItemJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.checkout.repository.CheckoutItemQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.checkout.repository.CheckoutJpaRepository;
import com.ryuqq.setof.application.checkout.port.out.command.CheckoutPersistencePort;
import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * CheckoutPersistenceAdapter - Checkout Command Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, Checkout 저장 요청을 JpaRepository에 위임합니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>CheckoutJpaEntity와 CheckoutItemJpaEntity 별도 저장
 *   <li>JPA Cascade 사용 금지
 * </ul>
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>Checkout 저장 (persist)
 *   <li>CheckoutItems 별도 저장
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>조회 로직 금지 (QueryAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CheckoutPersistenceAdapter implements CheckoutPersistencePort {

    private final CheckoutJpaRepository checkoutJpaRepository;
    private final CheckoutItemJpaRepository checkoutItemJpaRepository;
    private final CheckoutItemQueryDslRepository checkoutItemQueryDslRepository;
    private final CheckoutJpaEntityMapper checkoutJpaEntityMapper;

    public CheckoutPersistenceAdapter(
            CheckoutJpaRepository checkoutJpaRepository,
            CheckoutItemJpaRepository checkoutItemJpaRepository,
            CheckoutItemQueryDslRepository checkoutItemQueryDslRepository,
            CheckoutJpaEntityMapper checkoutJpaEntityMapper) {
        this.checkoutJpaRepository = checkoutJpaRepository;
        this.checkoutItemJpaRepository = checkoutItemJpaRepository;
        this.checkoutItemQueryDslRepository = checkoutItemQueryDslRepository;
        this.checkoutJpaEntityMapper = checkoutJpaEntityMapper;
    }

    /**
     * Checkout 저장 (생성/수정)
     *
     * <p>Checkout과 Items를 별도로 저장합니다. (Long FK 전략)
     *
     * @param checkout Checkout 도메인
     * @return 저장된 CheckoutId
     */
    @Override
    public CheckoutId persist(Checkout checkout) {
        UUID checkoutId = checkout.id().value();

        // Parent Entity 저장
        CheckoutJpaEntity entity = checkoutJpaEntityMapper.toEntity(checkout);
        checkoutJpaRepository.save(entity);

        // 기존 Items 삭제 (Update 케이스 대응)
        List<CheckoutItemJpaEntity> existingItems =
                checkoutItemQueryDslRepository.findByCheckoutId(checkoutId);
        if (!existingItems.isEmpty()) {
            checkoutItemJpaRepository.deleteAll(existingItems);
        }

        // 새 Items 저장
        List<CheckoutItemJpaEntity> itemEntities =
                checkoutJpaEntityMapper.toItemEntities(checkoutId, checkout.items());
        checkoutItemJpaRepository.saveAll(itemEntities);

        return CheckoutId.of(checkoutId);
    }
}
