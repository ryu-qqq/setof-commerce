package com.ryuqq.setof.adapter.out.persistence.composite.seller.repository;

import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerAddressJpaEntity.sellerAddressJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerBusinessInfoJpaEntity.sellerBusinessInfoJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerContractJpaEntity.sellerContractJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerCsJpaEntity.sellerCsJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerJpaEntity.sellerJpaEntity;
import static com.ryuqq.setof.adapter.out.persistence.seller.entity.QSellerSettlementJpaEntity.sellerSettlementJpaEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.composite.seller.condition.SellerCompositeConditionBuilder;
import com.ryuqq.setof.adapter.out.persistence.composite.seller.dto.SellerAdminCompositeDto;
import com.ryuqq.setof.adapter.out.persistence.composite.seller.dto.SellerCompositeDto;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * SellerCompositeQueryDslRepository - 셀러 Composite 조회 Repository.
 *
 * <p>Seller + SellerAddress + SellerBusinessInfo + SellerCs 크로스 도메인 조인 쿼리.
 *
 * <p>GetSellerForCustomerService, GetSellerForAdminService에서 사용.
 */
@Repository
public class SellerCompositeQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final SellerCompositeConditionBuilder conditionBuilder;

    public SellerCompositeQueryDslRepository(
            JPAQueryFactory queryFactory, SellerCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    public Optional<SellerCompositeDto> findBySellerId(Long sellerId) {
        SellerCompositeDto result =
                queryFactory
                        .select(
                                Projections.constructor(
                                        SellerCompositeDto.class,
                                        sellerJpaEntity.id,
                                        sellerJpaEntity.sellerName,
                                        sellerJpaEntity.displayName,
                                        sellerJpaEntity.logoUrl,
                                        sellerJpaEntity.description,
                                        sellerJpaEntity.active,
                                        sellerJpaEntity.createdAt,
                                        sellerJpaEntity.updatedAt,
                                        sellerAddressJpaEntity.id,
                                        sellerAddressJpaEntity.addressType,
                                        sellerAddressJpaEntity.addressName,
                                        sellerAddressJpaEntity.zipcode,
                                        sellerAddressJpaEntity.address,
                                        sellerAddressJpaEntity.addressDetail,
                                        sellerAddressJpaEntity.contactName,
                                        sellerAddressJpaEntity.contactPhone,
                                        sellerAddressJpaEntity.defaultAddress,
                                        sellerBusinessInfoJpaEntity.id,
                                        sellerBusinessInfoJpaEntity.registrationNumber,
                                        sellerBusinessInfoJpaEntity.companyName,
                                        sellerBusinessInfoJpaEntity.representative,
                                        sellerBusinessInfoJpaEntity.saleReportNumber,
                                        sellerBusinessInfoJpaEntity.businessZipcode,
                                        sellerBusinessInfoJpaEntity.businessAddress,
                                        sellerBusinessInfoJpaEntity.businessAddressDetail,
                                        sellerCsJpaEntity.id,
                                        sellerCsJpaEntity.csPhone,
                                        sellerCsJpaEntity.csMobile,
                                        sellerCsJpaEntity.csEmail,
                                        sellerCsJpaEntity.operatingStartTime,
                                        sellerCsJpaEntity.operatingEndTime,
                                        sellerCsJpaEntity.operatingDays,
                                        sellerCsJpaEntity.kakaoChannelUrl))
                        .from(sellerJpaEntity)
                        .leftJoin(sellerAddressJpaEntity)
                        .on(conditionBuilder.addressJoinCondition())
                        .leftJoin(sellerBusinessInfoJpaEntity)
                        .on(conditionBuilder.businessInfoJoinCondition())
                        .leftJoin(sellerCsJpaEntity)
                        .on(conditionBuilder.csJoinCondition())
                        .where(
                                conditionBuilder.sellerIdEq(sellerId),
                                conditionBuilder.sellerNotDeleted())
                        .fetchOne();

        return Optional.ofNullable(result);
    }

    /**
     * Admin용 셀러 Composite 조회 (CS + Contract + Settlement 포함).
     *
     * @param sellerId 셀러 ID
     * @return Admin Composite 결과
     */
    public Optional<SellerAdminCompositeDto> findAdminCompositeById(Long sellerId) {
        SellerAdminCompositeDto result =
                queryFactory
                        .select(
                                Projections.constructor(
                                        SellerAdminCompositeDto.class,
                                        // Seller
                                        sellerJpaEntity.id,
                                        sellerJpaEntity.sellerName,
                                        sellerJpaEntity.displayName,
                                        sellerJpaEntity.logoUrl,
                                        sellerJpaEntity.description,
                                        sellerJpaEntity.active,
                                        sellerJpaEntity.createdAt,
                                        sellerJpaEntity.updatedAt,
                                        // Address
                                        sellerAddressJpaEntity.id,
                                        sellerAddressJpaEntity.addressType,
                                        sellerAddressJpaEntity.addressName,
                                        sellerAddressJpaEntity.zipcode,
                                        sellerAddressJpaEntity.address,
                                        sellerAddressJpaEntity.addressDetail,
                                        sellerAddressJpaEntity.contactName,
                                        sellerAddressJpaEntity.contactPhone,
                                        sellerAddressJpaEntity.defaultAddress,
                                        // BusinessInfo
                                        sellerBusinessInfoJpaEntity.id,
                                        sellerBusinessInfoJpaEntity.registrationNumber,
                                        sellerBusinessInfoJpaEntity.companyName,
                                        sellerBusinessInfoJpaEntity.representative,
                                        sellerBusinessInfoJpaEntity.saleReportNumber,
                                        sellerBusinessInfoJpaEntity.businessZipcode,
                                        sellerBusinessInfoJpaEntity.businessAddress,
                                        sellerBusinessInfoJpaEntity.businessAddressDetail,
                                        // Cs
                                        sellerCsJpaEntity.id,
                                        sellerCsJpaEntity.csPhone,
                                        sellerCsJpaEntity.csMobile,
                                        sellerCsJpaEntity.csEmail,
                                        sellerCsJpaEntity.operatingStartTime,
                                        sellerCsJpaEntity.operatingEndTime,
                                        sellerCsJpaEntity.operatingDays,
                                        sellerCsJpaEntity.kakaoChannelUrl,
                                        // Contract
                                        sellerContractJpaEntity.id,
                                        sellerContractJpaEntity.commissionRate,
                                        sellerContractJpaEntity.contractStartDate,
                                        sellerContractJpaEntity.contractEndDate,
                                        sellerContractJpaEntity.status.stringValue(),
                                        sellerContractJpaEntity.specialTerms,
                                        sellerContractJpaEntity.createdAt,
                                        sellerContractJpaEntity.updatedAt,
                                        // Settlement
                                        sellerSettlementJpaEntity.id,
                                        sellerSettlementJpaEntity.bankCode,
                                        sellerSettlementJpaEntity.bankName,
                                        sellerSettlementJpaEntity.accountNumber,
                                        sellerSettlementJpaEntity.accountHolderName,
                                        sellerSettlementJpaEntity.settlementCycle.stringValue(),
                                        sellerSettlementJpaEntity.settlementDay,
                                        sellerSettlementJpaEntity.verified,
                                        sellerSettlementJpaEntity.verifiedAt,
                                        sellerSettlementJpaEntity.createdAt,
                                        sellerSettlementJpaEntity.updatedAt))
                        .from(sellerJpaEntity)
                        .leftJoin(sellerAddressJpaEntity)
                        .on(conditionBuilder.addressJoinCondition())
                        .leftJoin(sellerBusinessInfoJpaEntity)
                        .on(conditionBuilder.businessInfoJoinCondition())
                        .leftJoin(sellerCsJpaEntity)
                        .on(conditionBuilder.csJoinCondition())
                        .leftJoin(sellerContractJpaEntity)
                        .on(conditionBuilder.contractJoinCondition())
                        .leftJoin(sellerSettlementJpaEntity)
                        .on(conditionBuilder.settlementJoinCondition())
                        .where(
                                conditionBuilder.sellerIdEq(sellerId),
                                conditionBuilder.sellerNotDeleted())
                        .fetchOne();

        return Optional.ofNullable(result);
    }

    /**
     * 사업자등록번호로 존재 여부 확인.
     *
     * @param registrationNumber 사업자등록번호
     * @return 존재하면 true
     */
    public boolean existsByRegistrationNumber(String registrationNumber) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(sellerJpaEntity)
                        .leftJoin(sellerBusinessInfoJpaEntity)
                        .on(conditionBuilder.businessInfoJoinCondition())
                        .where(
                                sellerBusinessInfoJpaEntity.registrationNumber.eq(
                                        registrationNumber),
                                conditionBuilder.sellerNotDeleted())
                        .fetchFirst();

        return count != null;
    }
}
