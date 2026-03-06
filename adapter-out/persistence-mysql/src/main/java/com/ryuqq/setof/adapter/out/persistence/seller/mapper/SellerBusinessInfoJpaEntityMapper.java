package com.ryuqq.setof.adapter.out.persistence.seller.mapper;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerBusinessInfoJpaEntity;
import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.id.SellerBusinessInfoId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.vo.CompanyName;
import com.ryuqq.setof.domain.seller.vo.RegistrationNumber;
import com.ryuqq.setof.domain.seller.vo.Representative;
import com.ryuqq.setof.domain.seller.vo.SaleReportNumber;
import org.springframework.stereotype.Component;

/**
 * SellerBusinessInfoJpaEntityMapper - 셀러 사업자 정보 Entity-Domain 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerBusinessInfoJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain SellerBusinessInfo 도메인 객체
     * @return SellerBusinessInfoJpaEntity
     */
    public SellerBusinessInfoJpaEntity toEntity(SellerBusinessInfo domain) {
        return SellerBusinessInfoJpaEntity.create(
                domain.idValue(),
                domain.sellerIdValue(),
                domain.registrationNumberValue(),
                domain.companyNameValue(),
                domain.representativeValue(),
                domain.saleReportNumberValue(),
                domain.businessAddressZipCode(),
                domain.businessAddressRoad(),
                domain.businessAddressDetail(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity SellerBusinessInfoJpaEntity
     * @return SellerBusinessInfo 도메인 객체
     */
    public SellerBusinessInfo toDomain(SellerBusinessInfoJpaEntity entity) {
        Address address =
                entity.getBusinessZipcode() != null && entity.getBusinessAddress() != null
                        ? Address.of(
                                entity.getBusinessZipcode(),
                                entity.getBusinessAddress(),
                                entity.getBusinessAddressDetail())
                        : null;

        return SellerBusinessInfo.reconstitute(
                SellerBusinessInfoId.of(entity.getId()),
                SellerId.of(entity.getSellerId()),
                RegistrationNumber.of(entity.getRegistrationNumber()),
                CompanyName.of(entity.getCompanyName()),
                Representative.of(entity.getRepresentative()),
                entity.getSaleReportNumber() != null
                        ? SaleReportNumber.of(entity.getSaleReportNumber())
                        : null,
                address,
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
