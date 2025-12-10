package com.ryuqq.setof.adapter.out.persistence.shippingaddress.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.QShippingAddressJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.shippingaddress.entity.ShippingAddressJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * ShippingAddressQueryDslRepository - ShippingAddress QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(Long id): ID로 단건 조회
 *   <li>findByMemberId(String memberId): 회원별 배송지 목록 조회
 *   <li>findDefaultByMemberId(String memberId): 회원의 기본 배송지 조회
 *   <li>countByMemberId(String memberId): 회원별 배송지 개수 조회
 *   <li>findLatestExcluding(String memberId, Long excludeId): 특정 ID 제외 최신 배송지 조회
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ShippingAddressQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QShippingAddressJpaEntity qShippingAddress =
            QShippingAddressJpaEntity.shippingAddressJpaEntity;

    public ShippingAddressQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 ShippingAddress 단건 조회 (삭제되지 않은 것만)
     *
     * @param id ShippingAddress ID
     * @return ShippingAddressJpaEntity (Optional)
     */
    public Optional<ShippingAddressJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qShippingAddress)
                        .where(qShippingAddress.id.eq(id), isNotDeleted())
                        .fetchOne());
    }

    /**
     * ID로 ShippingAddress 단건 조회 (삭제된 것 포함)
     *
     * @param id ShippingAddress ID
     * @return ShippingAddressJpaEntity (Optional)
     */
    public Optional<ShippingAddressJpaEntity> findByIdIncludeDeleted(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qShippingAddress)
                        .where(qShippingAddress.id.eq(id))
                        .fetchOne());
    }

    /**
     * 회원별 배송지 목록 조회 (최신순)
     *
     * @param memberId 회원 ID (UUID v7 문자열)
     * @return ShippingAddressJpaEntity 목록
     */
    public List<ShippingAddressJpaEntity> findByMemberId(String memberId) {
        return queryFactory
                .selectFrom(qShippingAddress)
                .where(qShippingAddress.memberId.eq(memberId), isNotDeleted())
                .orderBy(qShippingAddress.isDefault.desc(), qShippingAddress.updatedAt.desc())
                .fetch();
    }

    /**
     * 회원의 기본 배송지 조회
     *
     * @param memberId 회원 ID (UUID v7 문자열)
     * @return ShippingAddressJpaEntity (Optional)
     */
    public Optional<ShippingAddressJpaEntity> findDefaultByMemberId(String memberId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qShippingAddress)
                        .where(
                                qShippingAddress.memberId.eq(memberId),
                                qShippingAddress.isDefault.isTrue(),
                                isNotDeleted())
                        .fetchOne());
    }

    /**
     * 회원별 배송지 개수 조회
     *
     * @param memberId 회원 ID (UUID v7 문자열)
     * @return 배송지 개수
     */
    public long countByMemberId(String memberId) {
        Long count =
                queryFactory
                        .select(qShippingAddress.count())
                        .from(qShippingAddress)
                        .where(qShippingAddress.memberId.eq(memberId), isNotDeleted())
                        .fetchOne();
        return count != null ? count : 0L;
    }

    /**
     * 특정 ID 제외 최신 배송지 조회 (기본 배송지 재지정용)
     *
     * @param memberId 회원 ID (UUID v7 문자열)
     * @param excludeId 제외할 배송지 ID
     * @return ShippingAddressJpaEntity (Optional)
     */
    public Optional<ShippingAddressJpaEntity> findLatestExcluding(String memberId, Long excludeId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(qShippingAddress)
                        .where(
                                qShippingAddress.memberId.eq(memberId),
                                qShippingAddress.id.ne(excludeId),
                                isNotDeleted())
                        .orderBy(qShippingAddress.updatedAt.desc())
                        .fetchFirst());
    }

    // ========== Private Helper Methods ==========

    /**
     * 삭제되지 않은 레코드 조건
     *
     * @return BooleanExpression (deletedAt IS NULL)
     */
    private BooleanExpression isNotDeleted() {
        return qShippingAddress.deletedAt.isNull();
    }
}
