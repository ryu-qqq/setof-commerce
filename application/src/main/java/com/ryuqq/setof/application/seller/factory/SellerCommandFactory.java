package com.ryuqq.setof.application.seller.factory;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.seller.dto.bundle.SellerRegistrationBundle;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.vo.CompanyName;
import com.ryuqq.setof.domain.seller.vo.Description;
import com.ryuqq.setof.domain.seller.vo.DisplayName;
import com.ryuqq.setof.domain.seller.vo.LogoUrl;
import com.ryuqq.setof.domain.seller.vo.RegistrationNumber;
import com.ryuqq.setof.domain.seller.vo.Representative;
import com.ryuqq.setof.domain.seller.vo.SaleReportNumber;
import com.ryuqq.setof.domain.seller.vo.SellerName;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class SellerCommandFactory {

    private final TimeProvider timeProvider;

    public SellerCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public SellerRegistrationBundle createRegistrationBundle(RegisterSellerCommand command) {
        Instant now = timeProvider.now();
        Seller seller = createSeller(command.seller(), now);
        SellerBusinessInfo businessInfo = createBusinessInfo(command.businessInfo(), now);
        return new SellerRegistrationBundle(seller, businessInfo);
    }

    private Seller createSeller(RegisterSellerCommand.SellerInfoCommand info, Instant now) {
        return Seller.forNew(
                SellerName.of(info.sellerName()),
                DisplayName.of(info.displayName()),
                LogoUrl.of(info.logoUrl()),
                Description.of(info.description()),
                now);
    }

    private SellerBusinessInfo createBusinessInfo(
            RegisterSellerCommand.SellerBusinessInfoCommand info, Instant now) {
        RegisterSellerCommand.AddressCommand addr = info.businessAddress();
        Address address = Address.of(addr.zipCode(), addr.line1(), addr.line2());
        return SellerBusinessInfo.forNew(
                RegistrationNumber.of(info.registrationNumber()),
                CompanyName.of(info.companyName()),
                Representative.of(info.representative()),
                SaleReportNumber.of(info.saleReportNumber()),
                address,
                now);
    }
}
