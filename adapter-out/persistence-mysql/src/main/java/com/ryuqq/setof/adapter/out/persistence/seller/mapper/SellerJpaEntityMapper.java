package com.ryuqq.setof.adapter.out.persistence.seller.mapper;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerCsInfoJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.vo.ApprovalStatus;
import com.ryuqq.setof.domain.seller.vo.BusinessAddress;
import com.ryuqq.setof.domain.seller.vo.BusinessInfo;
import com.ryuqq.setof.domain.seller.vo.CsEmail;
import com.ryuqq.setof.domain.seller.vo.CsLandlinePhone;
import com.ryuqq.setof.domain.seller.vo.CsMobilePhone;
import com.ryuqq.setof.domain.seller.vo.CustomerServiceInfo;
import com.ryuqq.setof.domain.seller.vo.Description;
import com.ryuqq.setof.domain.seller.vo.LogoUrl;
import com.ryuqq.setof.domain.seller.vo.RegistrationNumber;
import com.ryuqq.setof.domain.seller.vo.Representative;
import com.ryuqq.setof.domain.seller.vo.SaleReportNumber;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import com.ryuqq.setof.domain.seller.vo.SellerName;
import org.springframework.stereotype.Component;

/**
 * SellerJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Seller 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Seller -> SellerJpaEntity, SellerCsInfoJpaEntity (저장용)
 *   <li>SellerJpaEntity + SellerCsInfoJpaEntity -> Seller (조회용)
 *   <li>Value Object 추출 및 재구성
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SellerJpaEntityMapper {

    /**
     * Domain -> Seller Entity 변환
     *
     * @param domain Seller 도메인
     * @return SellerJpaEntity
     */
    public SellerJpaEntity toSellerEntity(Seller domain) {
        return SellerJpaEntity.of(
                domain.getIdValue(),
                domain.getNameValue(),
                domain.getLogoUrlValue(),
                domain.getDescriptionValue(),
                domain.getApprovalStatusValue(),
                domain.getRegistrationNumber(),
                domain.getSaleReportNumber(),
                domain.getRepresentative(),
                domain.getBusinessAddressLine1(),
                domain.getBusinessAddressLine2(),
                domain.getBusinessZipCode(),
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt());
    }

    /**
     * Domain -> CS Info Entity 변환
     *
     * @param domain Seller 도메인
     * @param existingCsInfoId 기존 CS Info ID (null이면 신규)
     * @return SellerCsInfoJpaEntity
     */
    public SellerCsInfoJpaEntity toCsInfoEntity(Seller domain, Long existingCsInfoId) {
        return SellerCsInfoJpaEntity.of(
                existingCsInfoId,
                domain.getIdValue(),
                domain.getCsEmail(),
                domain.getCsMobilePhone(),
                domain.getCsLandlinePhone(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * Entity -> Domain 변환
     *
     * @param sellerEntity SellerJpaEntity
     * @param csInfoEntity SellerCsInfoJpaEntity (nullable)
     * @return Seller 도메인
     */
    public Seller toDomain(SellerJpaEntity sellerEntity, SellerCsInfoJpaEntity csInfoEntity) {
        BusinessInfo businessInfo =
                BusinessInfo.of(
                        RegistrationNumber.of(sellerEntity.getRegistrationNumber()),
                        SaleReportNumber.of(sellerEntity.getSaleReportNumber()),
                        Representative.of(sellerEntity.getRepresentative()),
                        BusinessAddress.of(
                                sellerEntity.getBusinessAddressLine1(),
                                sellerEntity.getBusinessAddressLine2(),
                                sellerEntity.getBusinessZipCode()));

        CustomerServiceInfo customerServiceInfo = null;
        if (csInfoEntity != null) {
            customerServiceInfo =
                    CustomerServiceInfo.of(
                            csInfoEntity.getEmail() != null
                                    ? CsEmail.of(csInfoEntity.getEmail())
                                    : null,
                            csInfoEntity.getMobilePhone() != null
                                    ? CsMobilePhone.of(csInfoEntity.getMobilePhone())
                                    : null,
                            csInfoEntity.getLandlinePhone() != null
                                    ? CsLandlinePhone.of(csInfoEntity.getLandlinePhone())
                                    : null);
        }

        return Seller.reconstitute(
                SellerId.of(sellerEntity.getId()),
                SellerName.of(sellerEntity.getSellerName()),
                sellerEntity.getLogoUrl() != null ? LogoUrl.of(sellerEntity.getLogoUrl()) : null,
                sellerEntity.getDescription() != null
                        ? Description.of(sellerEntity.getDescription())
                        : null,
                ApprovalStatus.valueOf(sellerEntity.getApprovalStatus()),
                businessInfo,
                customerServiceInfo,
                sellerEntity.getCreatedAt(),
                sellerEntity.getUpdatedAt(),
                sellerEntity.getDeletedAt());
    }
}
