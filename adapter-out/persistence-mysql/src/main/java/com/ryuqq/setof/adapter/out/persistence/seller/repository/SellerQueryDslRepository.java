package com.ryuqq.setof.adapter.out.persistence.seller.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerCsInfoJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerCsInfoJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * SellerQueryDslRepository - Seller QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(Long id): ID로 Seller 단건 조회
 *   <li>findByCondition(condition): 검색 조건으로 목록 조회
 *   <li>countByCondition(condition): 검색 조건으로 총 개수 조회
 *   <li>existsById(Long id): 존재 여부 확인
 *   <li>findCsInfoBySellerId(Long sellerId): Seller ID로 CS Info 조회
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
public class SellerQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QSellerJpaEntity qSeller = QSellerJpaEntity.sellerJpaEntity;
    private static final QSellerCsInfoJpaEntity qCsInfo =
            QSellerCsInfoJpaEntity.sellerCsInfoJpaEntity;

    public SellerQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 Seller 단건 조회
     *
     * @param id Seller ID
     * @return SellerJpaEntity (Optional)
     */
    public Optional<SellerJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qSeller).where(qSeller.id.eq(id), notDeleted()).fetchOne());
    }

    /**
     * ID로 Seller 단건 조회 (삭제 포함)
     *
     * @param id Seller ID
     * @return SellerJpaEntity (Optional)
     */
    public Optional<SellerJpaEntity> findByIdIncludeDeleted(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qSeller).where(qSeller.id.eq(id)).fetchOne());
    }

    /**
     * 검색 조건으로 Seller 목록 조회 (페이징)
     *
     * @param keyword 검색 키워드 (nullable)
     * @param status 승인 상태 (nullable)
     * @param includeDeleted 삭제 포함 여부
     * @param offset 시작 위치
     * @param limit 조회 개수
     * @return SellerJpaEntity 목록
     */
    public List<SellerJpaEntity> findByCondition(
            String keyword, String status, boolean includeDeleted, long offset, int limit) {
        BooleanBuilder builder = buildCondition(keyword, status, includeDeleted);

        return queryFactory
                .selectFrom(qSeller)
                .where(builder)
                .orderBy(qSeller.id.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    /**
     * 검색 조건으로 총 개수 조회
     *
     * @param keyword 검색 키워드 (nullable)
     * @param status 승인 상태 (nullable)
     * @param includeDeleted 삭제 포함 여부
     * @return 총 개수
     */
    public long countByCondition(String keyword, String status, boolean includeDeleted) {
        BooleanBuilder builder = buildCondition(keyword, status, includeDeleted);

        Long count = queryFactory.select(qSeller.count()).from(qSeller).where(builder).fetchOne();

        return count != null ? count : 0L;
    }

    /**
     * ID로 Seller 존재 여부 확인
     *
     * @param id Seller ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qSeller)
                        .where(qSeller.id.eq(id), notDeleted())
                        .fetchFirst();
        return count != null;
    }

    /**
     * Seller ID로 CS Info 조회
     *
     * @param sellerId Seller ID
     * @return SellerCsInfoJpaEntity (Optional)
     */
    public Optional<SellerCsInfoJpaEntity> findCsInfoBySellerId(Long sellerId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qCsInfo).where(qCsInfo.sellerId.eq(sellerId)).fetchOne());
    }

    /** 검색 조건 빌더 */
    private BooleanBuilder buildCondition(String keyword, String status, boolean includeDeleted) {
        BooleanBuilder builder = new BooleanBuilder();

        // 키워드 검색 (셀러명, 대표자명)
        if (keyword != null && !keyword.isBlank()) {
            builder.and(
                    qSeller.sellerName
                            .containsIgnoreCase(keyword)
                            .or(qSeller.representative.containsIgnoreCase(keyword)));
        }

        // 상태 필터
        if (status != null && !status.isBlank()) {
            builder.and(qSeller.approvalStatus.eq(status));
        }

        // 삭제 여부 필터
        if (!includeDeleted) {
            builder.and(notDeleted());
        }

        return builder;
    }

    /** 삭제되지 않은 조건 */
    private com.querydsl.core.types.dsl.BooleanExpression notDeleted() {
        return qSeller.deletedAt.isNull();
    }
}
