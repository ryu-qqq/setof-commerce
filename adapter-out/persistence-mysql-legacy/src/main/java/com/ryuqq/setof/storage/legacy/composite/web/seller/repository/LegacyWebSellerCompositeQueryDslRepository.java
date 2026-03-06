package com.ryuqq.setof.storage.legacy.composite.web.seller.repository;

import static com.ryuqq.setof.storage.legacy.seller.entity.QLegacySellerBusinessInfoEntity.legacySellerBusinessInfoEntity;
import static com.ryuqq.setof.storage.legacy.seller.entity.QLegacySellerEntity.legacySellerEntity;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.composite.web.seller.condition.LegacyWebSellerCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.web.seller.dto.LegacyWebSellerQueryDto;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebSellerCompositeQueryDslRepository - 레거시 Web 판매자 Composite 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebSellerCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebSellerCompositeConditionBuilder conditionBuilder;

    public LegacyWebSellerCompositeQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebSellerCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 판매자 단건 조회 (ID).
     *
     * @param condition 검색 조건
     * @return 판매자 Optional
     */
    public Optional<LegacyWebSellerQueryDto> fetchSeller(Long sellerId) {
        LegacyWebSellerQueryDto dto =
                queryFactory
                        .select(createProjection())
                        .from(legacySellerEntity)
                        .innerJoin(legacySellerBusinessInfoEntity)
                        .on(legacySellerBusinessInfoEntity.id.eq(legacySellerEntity.id))
                        .where(conditionBuilder.sellerIdEq(sellerId))
                        .fetchFirst();
        return Optional.ofNullable(dto);
    }

    /**
     * Projections.constructor()로 Projection 생성.
     *
     * <p>@QueryProjection 대신 사용. SellerCompositeResult 변환에 필요한 개별 필드를 모두 포함.
     */
    /**
     * 사업자등록번호 존재 여부 조회.
     *
     * @param registrationNumber 사업자등록번호
     * @return 존재하면 true
     */
    public boolean existsByRegistrationNumber(String registrationNumber) {
        Long result =
                queryFactory
                        .select(legacySellerBusinessInfoEntity.id)
                        .from(legacySellerBusinessInfoEntity)
                        .where(conditionBuilder.registrationNumberEq(registrationNumber))
                        .fetchFirst();
        return result != null;
    }

    private ConstructorExpression<LegacyWebSellerQueryDto> createProjection() {
        return Projections.constructor(
                LegacyWebSellerQueryDto.class,
                legacySellerEntity.id,
                legacySellerEntity.sellerName,
                legacySellerBusinessInfoEntity.companyName,
                legacySellerEntity.sellerLogoUrl,
                legacySellerEntity.sellerDescription.coalesce(""),
                legacySellerEntity.commissionRate,
                legacySellerBusinessInfoEntity.businessAddressLine1,
                legacySellerBusinessInfoEntity.businessAddressLine2,
                legacySellerBusinessInfoEntity.businessAddressZipCode,
                legacySellerBusinessInfoEntity.csNumber,
                legacySellerBusinessInfoEntity.csPhoneNumber,
                legacySellerBusinessInfoEntity.csEmail,
                legacySellerBusinessInfoEntity.registrationNumber,
                legacySellerBusinessInfoEntity.saleReportNumber,
                legacySellerBusinessInfoEntity.representative,
                legacySellerBusinessInfoEntity.bankName,
                legacySellerBusinessInfoEntity.accountNumber,
                legacySellerBusinessInfoEntity.accountHolderName,
                legacySellerEntity.insertDate,
                legacySellerEntity.updateDate);
    }
}
