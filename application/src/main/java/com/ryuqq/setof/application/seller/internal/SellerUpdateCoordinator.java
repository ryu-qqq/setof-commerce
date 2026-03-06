package com.ryuqq.setof.application.seller.internal;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.validator.SellerBusinessInfoValidator;
import com.ryuqq.setof.application.seller.validator.SellerCsValidator;
import com.ryuqq.setof.application.seller.validator.SellerValidator;
import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfoUpdateData;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.aggregate.SellerUpdateData;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.vo.CompanyName;
import com.ryuqq.setof.domain.seller.vo.CsContact;
import com.ryuqq.setof.domain.seller.vo.RegistrationNumber;
import com.ryuqq.setof.domain.seller.vo.Representative;
import com.ryuqq.setof.domain.seller.vo.SaleReportNumber;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class SellerUpdateCoordinator {

    private final SellerValidator sellerValidator;
    private final SellerBusinessInfoValidator sellerBusinessInfoValidator;
    private final SellerCsValidator sellerCsValidator;
    private final SellerCommandFacade sellerCommandFacade;
    private final TimeProvider timeProvider;

    public SellerUpdateCoordinator(
            SellerValidator sellerValidator,
            SellerBusinessInfoValidator sellerBusinessInfoValidator,
            SellerCsValidator sellerCsValidator,
            SellerCommandFacade sellerCommandFacade,
            TimeProvider timeProvider) {
        this.sellerValidator = sellerValidator;
        this.sellerBusinessInfoValidator = sellerBusinessInfoValidator;
        this.sellerCsValidator = sellerCsValidator;
        this.sellerCommandFacade = sellerCommandFacade;
        this.timeProvider = timeProvider;
    }

    public void update(UpdateSellerCommand command) {
        Instant now = timeProvider.now();
        SellerId sellerId = SellerId.of(command.sellerId());

        Seller seller = sellerValidator.findExistingOrThrow(sellerId);
        sellerValidator.validateSellerNameNotDuplicateExcluding(command.sellerName(), sellerId);

        SellerUpdateData updateData =
                SellerUpdateData.of(
                        command.sellerName(),
                        command.displayName(),
                        command.logoUrl(),
                        command.description());
        seller.update(updateData, now);

        SellerCs sellerCs = null;
        if (command.csInfo() != null) {
            sellerCs = sellerCsValidator.findExistingOrThrow(sellerId);
            UpdateSellerCommand.CsInfoCommand csInfo = command.csInfo();
            CsContact csContact = CsContact.of(csInfo.phone(), csInfo.mobile(), csInfo.email());
            sellerCs.updateContact(csContact, now);
        }

        SellerBusinessInfo businessInfo = null;
        if (command.businessInfo() != null) {
            businessInfo = sellerBusinessInfoValidator.findExistingOrThrow(sellerId);
            UpdateSellerCommand.BusinessInfoCommand bi = command.businessInfo();
            sellerBusinessInfoValidator.validateRegistrationNumberNotDuplicateExcluding(
                    bi.registrationNumber(), sellerId);

            UpdateSellerCommand.AddressCommand addr = bi.businessAddress();
            Address address =
                    addr != null
                            ? Address.of(addr.zipCode(), addr.line1(), addr.line2())
                            : businessInfo.businessAddress();

            SellerBusinessInfoUpdateData updateBusinessData =
                    SellerBusinessInfoUpdateData.of(
                            RegistrationNumber.of(bi.registrationNumber()),
                            CompanyName.of(bi.companyName()),
                            Representative.of(bi.representative()),
                            SaleReportNumber.of(bi.saleReportNumber()),
                            address);
            businessInfo.update(updateBusinessData, now);
        }

        sellerCommandFacade.updateSeller(seller, businessInfo, sellerCs);
    }
}
