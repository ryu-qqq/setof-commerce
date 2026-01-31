package com.ryuqq.setof.adapter.out.persistence.sellerapplication.mapper;

import com.ryuqq.setof.adapter.out.persistence.sellerapplication.entity.SellerApplicationJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.sellerapplication.entity.SellerApplicationJpaEntity.AddressTypeJpaValue;
import com.ryuqq.setof.adapter.out.persistence.sellerapplication.entity.SellerApplicationJpaEntity.ApplicationStatusJpaValue;
import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.vo.AddressName;
import com.ryuqq.setof.domain.seller.vo.AddressType;
import com.ryuqq.setof.domain.seller.vo.CompanyName;
import com.ryuqq.setof.domain.seller.vo.ContactInfo;
import com.ryuqq.setof.domain.seller.vo.CsContact;
import com.ryuqq.setof.domain.seller.vo.Description;
import com.ryuqq.setof.domain.seller.vo.DisplayName;
import com.ryuqq.setof.domain.seller.vo.LogoUrl;
import com.ryuqq.setof.domain.seller.vo.RegistrationNumber;
import com.ryuqq.setof.domain.seller.vo.Representative;
import com.ryuqq.setof.domain.seller.vo.SaleReportNumber;
import com.ryuqq.setof.domain.seller.vo.SellerName;
import com.ryuqq.setof.domain.sellerapplication.aggregate.SellerApplication;
import com.ryuqq.setof.domain.sellerapplication.id.SellerApplicationId;
import com.ryuqq.setof.domain.sellerapplication.vo.Agreement;
import com.ryuqq.setof.domain.sellerapplication.vo.ApplicationStatus;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * SellerApplicationJpaEntityMapper - 입점 신청 Entity-Domain 매퍼.
 *
 * <p>Entity ↔ Domain 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 */
@Component
public class SellerApplicationJpaEntityMapper {

    public SellerApplicationJpaEntity toEntity(SellerApplication domain) {
        Instant now = Instant.now();
        return SellerApplicationJpaEntity.create(
                domain.idValue(),
                domain.sellerNameValue(),
                domain.displayNameValue(),
                domain.logoUrlValue(),
                domain.descriptionValue(),
                domain.registrationNumberValue(),
                domain.companyNameValue(),
                domain.representativeValue(),
                domain.saleReportNumberValue(),
                domain.businessAddress().zipcode(),
                domain.businessAddress().line1(),
                domain.businessAddress().line2(),
                domain.csContact().phoneValue(),
                domain.csContact().emailValue(),
                toJpaAddressType(domain.addressType()),
                domain.addressNameValue(),
                domain.address().zipcode(),
                domain.address().line1(),
                domain.address().line2(),
                domain.contactInfo().name(),
                domain.contactInfo().phoneValue(),
                domain.agreement().agreedAt(),
                toJpaStatus(domain.status()),
                domain.appliedAt(),
                domain.processedAt(),
                domain.processedBy(),
                domain.rejectionReason(),
                domain.approvedSellerIdValue(),
                domain.isNew() ? now : domain.appliedAt(),
                now);
    }

    public SellerApplication toDomain(SellerApplicationJpaEntity entity) {
        return SellerApplication.reconstitute(
                SellerApplicationId.of(entity.getId()),
                SellerName.of(entity.getSellerName()),
                DisplayName.of(entity.getDisplayName()),
                entity.getLogoUrl() != null ? LogoUrl.of(entity.getLogoUrl()) : null,
                entity.getDescription() != null ? Description.of(entity.getDescription()) : null,
                RegistrationNumber.of(entity.getRegistrationNumber()),
                CompanyName.of(entity.getCompanyName()),
                Representative.of(entity.getRepresentative()),
                entity.getSaleReportNumber() != null
                        ? SaleReportNumber.of(entity.getSaleReportNumber())
                        : null,
                Address.of(
                        entity.getBusinessZipCode(),
                        entity.getBusinessBaseAddress(),
                        entity.getBusinessDetailAddress()),
                CsContact.of(entity.getCsPhoneNumber(), null, entity.getCsEmail()),
                toDomainAddressType(entity.getAddressType()),
                AddressName.of(entity.getAddressName()),
                Address.of(
                        entity.getAddressZipCode(),
                        entity.getAddressBaseAddress(),
                        entity.getAddressDetailAddress()),
                ContactInfo.of(entity.getContactName(), entity.getContactPhoneNumber()),
                Agreement.reconstitute(entity.getAgreedAt()),
                toDomainStatus(entity.getStatus()),
                entity.getAppliedAt(),
                entity.getProcessedAt(),
                entity.getProcessedBy(),
                entity.getRejectionReason(),
                entity.getApprovedSellerId() != null
                        ? SellerId.of(entity.getApprovedSellerId())
                        : null);
    }

    private AddressTypeJpaValue toJpaAddressType(AddressType addressType) {
        return switch (addressType) {
            case SHIPPING -> AddressTypeJpaValue.SHIPPING;
            case RETURN -> AddressTypeJpaValue.RETURN;
        };
    }

    private AddressType toDomainAddressType(AddressTypeJpaValue jpaValue) {
        return switch (jpaValue) {
            case SHIPPING -> AddressType.SHIPPING;
            case RETURN -> AddressType.RETURN;
        };
    }

    private ApplicationStatusJpaValue toJpaStatus(ApplicationStatus status) {
        return switch (status) {
            case PENDING -> ApplicationStatusJpaValue.PENDING;
            case APPROVED -> ApplicationStatusJpaValue.APPROVED;
            case REJECTED -> ApplicationStatusJpaValue.REJECTED;
        };
    }

    private ApplicationStatus toDomainStatus(ApplicationStatusJpaValue jpaValue) {
        return switch (jpaValue) {
            case PENDING -> ApplicationStatus.PENDING;
            case APPROVED -> ApplicationStatus.APPROVED;
            case REJECTED -> ApplicationStatus.REJECTED;
        };
    }
}
