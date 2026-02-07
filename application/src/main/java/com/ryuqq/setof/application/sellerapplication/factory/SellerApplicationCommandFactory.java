package com.ryuqq.setof.application.sellerapplication.factory;

import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.sellerapplication.dto.bundle.SellerCreationBundle;
import com.ryuqq.setof.application.sellerapplication.dto.command.ApplySellerApplicationCommand;
import com.ryuqq.setof.application.sellerapplication.dto.command.RejectSellerApplicationCommand;
import com.ryuqq.setof.domain.common.vo.Address;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.aggregate.SellerAuthOutbox;
import com.ryuqq.setof.domain.seller.aggregate.SellerBusinessInfo;
import com.ryuqq.setof.domain.seller.aggregate.SellerContract;
import com.ryuqq.setof.domain.seller.aggregate.SellerCs;
import com.ryuqq.setof.domain.seller.aggregate.SellerSettlement;
import com.ryuqq.setof.domain.seller.vo.AddressName;
import com.ryuqq.setof.domain.seller.vo.AddressType;
import com.ryuqq.setof.domain.seller.vo.BankAccount;
import com.ryuqq.setof.domain.seller.vo.CommissionRate;
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
import com.ryuqq.setof.domain.seller.vo.SettlementCycle;
import com.ryuqq.setof.domain.sellerapplication.aggregate.SellerApplication;
import com.ryuqq.setof.domain.sellerapplication.id.SellerApplicationId;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * SellerApplication Command Factory.
 *
 * <p>APP-TIM-001: TimeProvider.now()는 Factory에서만 호출합니다.
 *
 * @author ryu-qqq
 */
@Component
public class SellerApplicationCommandFactory {

    private final TimeProvider timeProvider;

    public SellerApplicationCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    /**
     * 신청 Command로부터 SellerApplication 도메인 객체 생성.
     *
     * @param command 신청 Command
     * @return SellerApplication 도메인 객체
     */
    public SellerApplication create(ApplySellerApplicationCommand command) {
        Instant now = timeProvider.now();

        return SellerApplication.apply(
                toSellerName(command.sellerInfo()),
                toDisplayName(command.sellerInfo()),
                toLogoUrl(command.sellerInfo()),
                toDescription(command.sellerInfo()),
                toRegistrationNumber(command.businessInfo()),
                toCompanyName(command.businessInfo()),
                toRepresentative(command.businessInfo()),
                toSaleReportNumber(command.businessInfo()),
                toBusinessAddress(command.businessInfo()),
                toCsContact(command.csContact()),
                toAddressType(command.addressInfo()),
                toAddressName(command.addressInfo()),
                toAddress(command.addressInfo()),
                toContactInfo(command.addressInfo()),
                toBankAccount(command.settlementInfo()),
                toSettlementCycle(command.settlementInfo()),
                command.settlementInfo().settlementDay(),
                now);
    }

    /**
     * 거절 Command로부터 StatusChangeContext 생성.
     *
     * @param command 거절 Command
     * @return StatusChangeContext (신청 ID, 변경 시간)
     */
    public StatusChangeContext<SellerApplicationId> createRejectContext(
            RejectSellerApplicationCommand command) {
        return new StatusChangeContext<>(
                SellerApplicationId.of(command.sellerApplicationId()), timeProvider.now());
    }

    /**
     * 입점 신청으로부터 셀러 생성 번들을 생성합니다.
     *
     * <p>승인 시 SellerApplication 정보를 기반으로 Seller 관련 도메인 객체를 생성합니다.
     *
     * @param application 승인된 입점 신청
     * @return SellerCreationBundle
     */
    public SellerCreationBundle createSellerBundle(SellerApplication application) {
        Instant now = timeProvider.now();

        Seller seller =
                Seller.forNew(
                        application.sellerName(),
                        application.displayName(),
                        application.logoUrl(),
                        application.description(),
                        now);

        SellerBusinessInfo businessInfo =
                SellerBusinessInfo.forNew(
                        application.registrationNumber(),
                        application.companyName(),
                        application.representative(),
                        application.saleReportNumber(),
                        application.businessAddress(),
                        now);

        SellerAddress address =
                SellerAddress.forNew(
                        application.addressType(),
                        application.addressName(),
                        application.address(),
                        application.contactInfo(),
                        true,
                        now);

        SellerCs sellerCs = SellerCs.defaultCs(null, application.csContact(), now);

        SellerContract sellerContract =
                SellerContract.defaultContract(null, CommissionRate.zero(), now);

        SellerSettlement sellerSettlement =
                SellerSettlement.forNew(
                        application.bankAccount(),
                        application.settlementCycle(),
                        application.settlementDay(),
                        now);

        SellerAuthOutbox authOutbox =
                SellerAuthOutbox.forNew(buildAuthOutboxPayload(application), now);

        return new SellerCreationBundle(
                seller,
                businessInfo,
                address,
                sellerCs,
                sellerContract,
                sellerSettlement,
                authOutbox,
                now);
    }

    /**
     * 인증 서버 Outbox payload 생성.
     *
     * <p>인증 서버에 Tenant/Organization 생성 시 필요한 정보를 JSON으로 변환합니다.
     *
     * @param application 입점 신청
     * @return JSON payload
     */
    private String buildAuthOutboxPayload(SellerApplication application) {
        return String.format(
                "{\"sellerName\":\"%s\",\"companyName\":\"%s\",\"registrationNumber\":\"%s\"}",
                application.sellerNameValue(),
                application.companyNameValue(),
                application.registrationNumberValue());
    }

    private SellerName toSellerName(ApplySellerApplicationCommand.SellerInfoCommand info) {
        return SellerName.of(info.sellerName());
    }

    private DisplayName toDisplayName(ApplySellerApplicationCommand.SellerInfoCommand info) {
        return DisplayName.of(info.displayName());
    }

    private LogoUrl toLogoUrl(ApplySellerApplicationCommand.SellerInfoCommand info) {
        return info.logoUrl() != null ? LogoUrl.of(info.logoUrl()) : null;
    }

    private Description toDescription(ApplySellerApplicationCommand.SellerInfoCommand info) {
        return info.description() != null ? Description.of(info.description()) : null;
    }

    private RegistrationNumber toRegistrationNumber(
            ApplySellerApplicationCommand.BusinessInfoCommand info) {
        return RegistrationNumber.of(info.registrationNumber());
    }

    private CompanyName toCompanyName(ApplySellerApplicationCommand.BusinessInfoCommand info) {
        return CompanyName.of(info.companyName());
    }

    private Representative toRepresentative(
            ApplySellerApplicationCommand.BusinessInfoCommand info) {
        return Representative.of(info.representative());
    }

    private SaleReportNumber toSaleReportNumber(
            ApplySellerApplicationCommand.BusinessInfoCommand info) {
        return info.saleReportNumber() != null
                ? SaleReportNumber.of(info.saleReportNumber())
                : null;
    }

    private Address toBusinessAddress(ApplySellerApplicationCommand.BusinessInfoCommand info) {
        ApplySellerApplicationCommand.AddressCommand addr = info.businessAddress();
        if (addr == null) {
            return null;
        }
        return Address.of(addr.zipCode(), addr.line1(), addr.line2());
    }

    private CsContact toCsContact(ApplySellerApplicationCommand.CsContactCommand cmd) {
        if (cmd == null) {
            return null;
        }
        return CsContact.of(cmd.phone(), cmd.mobile(), cmd.email());
    }

    private AddressType toAddressType(ApplySellerApplicationCommand.AddressInfoCommand info) {
        return AddressType.valueOf(info.addressType());
    }

    private AddressName toAddressName(ApplySellerApplicationCommand.AddressInfoCommand info) {
        return AddressName.of(info.addressName());
    }

    private Address toAddress(ApplySellerApplicationCommand.AddressInfoCommand info) {
        ApplySellerApplicationCommand.AddressCommand addr = info.address();
        if (addr == null) {
            return null;
        }
        return Address.of(addr.zipCode(), addr.line1(), addr.line2());
    }

    private ContactInfo toContactInfo(ApplySellerApplicationCommand.AddressInfoCommand info) {
        ApplySellerApplicationCommand.ContactInfoCommand contact = info.contactInfo();
        if (contact == null) {
            return null;
        }
        return ContactInfo.of(contact.name(), contact.phone());
    }

    private BankAccount toBankAccount(ApplySellerApplicationCommand.SettlementInfoCommand info) {
        return BankAccount.of(
                info.bankCode(), info.bankName(), info.accountNumber(), info.accountHolderName());
    }

    private SettlementCycle toSettlementCycle(
            ApplySellerApplicationCommand.SettlementInfoCommand info) {
        return SettlementCycle.valueOf(info.settlementCycle());
    }
}
