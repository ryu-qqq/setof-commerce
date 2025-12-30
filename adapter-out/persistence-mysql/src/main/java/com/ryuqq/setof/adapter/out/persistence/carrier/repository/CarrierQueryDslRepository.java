package com.ryuqq.setof.adapter.out.persistence.carrier.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.carrier.entity.CarrierJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.carrier.entity.QCarrierJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * CarrierQueryDslRepository - Carrier QueryDSL Repository
 *
 * <p>복잡한 조회 쿼리를 담당하는 QueryDSL Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class CarrierQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public CarrierQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private static final QCarrierJpaEntity carrier = QCarrierJpaEntity.carrierJpaEntity;

    /**
     * ID로 Carrier 조회
     *
     * @param id Carrier ID
     * @return CarrierJpaEntity (Optional)
     */
    public Optional<CarrierJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(carrier).where(carrier.id.eq(id)).fetchOne());
    }

    /**
     * 택배사 코드로 Carrier 조회
     *
     * @param code 택배사 코드
     * @return CarrierJpaEntity (Optional)
     */
    public Optional<CarrierJpaEntity> findByCode(String code) {
        return Optional.ofNullable(
                queryFactory.selectFrom(carrier).where(carrier.code.eq(code)).fetchOne());
    }

    /**
     * 활성 상태 Carrier 전체 조회 (displayOrder 순)
     *
     * @return CarrierJpaEntity 목록
     */
    public List<CarrierJpaEntity> findAllActive() {
        return queryFactory
                .selectFrom(carrier)
                .where(carrier.status.eq("ACTIVE"))
                .orderBy(carrier.displayOrder.asc().nullsLast(), carrier.name.asc())
                .fetch();
    }

    /**
     * 전체 Carrier 조회 (displayOrder 순)
     *
     * @return CarrierJpaEntity 목록
     */
    public List<CarrierJpaEntity> findAll() {
        return queryFactory
                .selectFrom(carrier)
                .orderBy(carrier.displayOrder.asc().nullsLast(), carrier.name.asc())
                .fetch();
    }

    /**
     * ID 존재 여부 확인
     *
     * @param id Carrier ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        return queryFactory.selectOne().from(carrier).where(carrier.id.eq(id)).fetchFirst() != null;
    }

    /**
     * 택배사 코드 존재 여부 확인
     *
     * @param code 택배사 코드
     * @return 존재 여부
     */
    public boolean existsByCode(String code) {
        return queryFactory.selectOne().from(carrier).where(carrier.code.eq(code)).fetchFirst()
                != null;
    }
}
