package com.ryuqq.setof.adapter.out.persistence.checkout.adapter;

import com.ryuqq.setof.adapter.out.persistence.checkout.entity.CheckoutItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.checkout.entity.CheckoutJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.checkout.mapper.CheckoutJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.checkout.repository.CheckoutItemQueryDslRepository;
import com.ryuqq.setof.adapter.out.persistence.checkout.repository.CheckoutQueryDslRepository;
import com.ryuqq.setof.application.checkout.port.out.query.CheckoutQueryPort;
import com.ryuqq.setof.domain.checkout.aggregate.Checkout;
import com.ryuqq.setof.domain.checkout.exception.CheckoutNotFoundException;
import com.ryuqq.setof.domain.checkout.vo.CheckoutId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * CheckoutQueryAdapter - Checkout Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Checkout 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여 반환합니다.
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>CheckoutJpaEntity와 CheckoutItemJpaEntity 별도 조회
 *   <li>Mapper에 함께 전달하여 Domain 조립
 * </ul>
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>ID로 단건 조회 - 필수 (getById)
 *   <li>QueryDslRepository 호출
 *   <li>Mapper를 통한 Entity → Domain 변환
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정/삭제 금지 (PersistenceAdapter로 분리)
 *   <li>JPAQueryFactory 직접 사용 금지 (QueryDslRepository에서 처리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CheckoutQueryAdapter implements CheckoutQueryPort {

    private final CheckoutQueryDslRepository checkoutQueryDslRepository;
    private final CheckoutItemQueryDslRepository checkoutItemQueryDslRepository;
    private final CheckoutJpaEntityMapper checkoutJpaEntityMapper;

    public CheckoutQueryAdapter(
            CheckoutQueryDslRepository checkoutQueryDslRepository,
            CheckoutItemQueryDslRepository checkoutItemQueryDslRepository,
            CheckoutJpaEntityMapper checkoutJpaEntityMapper) {
        this.checkoutQueryDslRepository = checkoutQueryDslRepository;
        this.checkoutItemQueryDslRepository = checkoutItemQueryDslRepository;
        this.checkoutJpaEntityMapper = checkoutJpaEntityMapper;
    }

    /**
     * ID로 Checkout 단건 조회
     *
     * @param checkoutId Checkout ID (Value Object)
     * @return Checkout Domain (Optional)
     */
    @Override
    public Optional<Checkout> findById(CheckoutId checkoutId) {
        Optional<CheckoutJpaEntity> entityOpt =
                checkoutQueryDslRepository.findById(checkoutId.value());

        if (entityOpt.isEmpty()) {
            return Optional.empty();
        }

        CheckoutJpaEntity entity = entityOpt.get();
        List<CheckoutItemJpaEntity> itemEntities =
                checkoutItemQueryDslRepository.findByCheckoutId(entity.getId());

        return Optional.of(checkoutJpaEntityMapper.toDomain(entity, itemEntities));
    }

    /**
     * ID로 Checkout 단건 조회 (필수)
     *
     * @param checkoutId Checkout ID (Value Object)
     * @return Checkout Domain
     * @throws CheckoutNotFoundException Checkout이 존재하지 않는 경우
     */
    @Override
    public Checkout getById(CheckoutId checkoutId) {
        return findById(checkoutId).orElseThrow(() -> new CheckoutNotFoundException(checkoutId));
    }
}
