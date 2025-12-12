package com.ryuqq.setof.application.seller.assembler;

import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.response.BusinessInfoResponse;
import com.ryuqq.setof.application.seller.dto.response.CsInfoResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
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
import com.ryuqq.setof.domain.seller.vo.SellerName;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Seller Assembler
 *
 * <p>Command DTO와 Domain 객체, Response DTO 간 변환을 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SellerAssembler {

    /**
     * RegisterSellerCommand를 Seller 도메인으로 변환
     *
     * @param command 등록 커맨드
     * @param now 현재 시각
     * @return Seller 도메인 객체
     */
    public Seller toDomain(RegisterSellerCommand command, Instant now) {
        SellerName sellerName = SellerName.of(command.sellerName());
        LogoUrl logoUrl = command.logoUrl() != null ? LogoUrl.of(command.logoUrl()) : null;
        Description description =
                command.description() != null ? Description.of(command.description()) : null;

        BusinessInfo businessInfo = createBusinessInfo(command);
        CustomerServiceInfo csInfo = createCustomerServiceInfo(command);

        return Seller.create(sellerName, logoUrl, description, businessInfo, csInfo, now);
    }

    private BusinessInfo createBusinessInfo(RegisterSellerCommand command) {
        RegistrationNumber registrationNumber = RegistrationNumber.of(command.registrationNumber());
        SaleReportNumber saleReportNumber =
                command.saleReportNumber() != null
                        ? SaleReportNumber.of(command.saleReportNumber())
                        : null;
        Representative representative = Representative.of(command.representative());
        BusinessAddress businessAddress =
                BusinessAddress.of(
                        command.businessAddressLine1(),
                        command.businessAddressLine2(),
                        command.businessZipCode());

        return BusinessInfo.of(
                registrationNumber, saleReportNumber, representative, businessAddress);
    }

    private CustomerServiceInfo createCustomerServiceInfo(RegisterSellerCommand command) {
        CsEmail csEmail = command.csEmail() != null ? CsEmail.of(command.csEmail()) : null;
        CsMobilePhone csMobilePhone =
                command.csMobilePhone() != null ? CsMobilePhone.of(command.csMobilePhone()) : null;
        CsLandlinePhone csLandlinePhone =
                command.csLandlinePhone() != null
                        ? CsLandlinePhone.of(command.csLandlinePhone())
                        : null;

        return CustomerServiceInfo.of(csEmail, csMobilePhone, csLandlinePhone);
    }

    /**
     * Seller 도메인을 SellerResponse로 변환
     *
     * @param seller Seller 도메인 객체
     * @return SellerResponse
     */
    public SellerResponse toSellerResponse(Seller seller) {
        BusinessInfoResponse businessInfo =
                BusinessInfoResponse.of(
                        seller.getRegistrationNumber(),
                        seller.getSaleReportNumber(),
                        seller.getRepresentative(),
                        seller.getBusinessAddressLine1(),
                        seller.getBusinessAddressLine2(),
                        seller.getBusinessZipCode());

        CsInfoResponse csInfo =
                CsInfoResponse.of(
                        seller.getCsEmail(),
                        seller.getCsMobilePhone(),
                        seller.getCsLandlinePhone());

        return SellerResponse.of(
                seller.getIdValue(),
                seller.getNameValue(),
                seller.getLogoUrlValue(),
                seller.getDescriptionValue(),
                seller.getApprovalStatusValue(),
                businessInfo,
                csInfo);
    }

    /**
     * Seller 도메인을 SellerSummaryResponse로 변환
     *
     * @param seller Seller 도메인 객체
     * @return SellerSummaryResponse
     */
    public SellerSummaryResponse toSellerSummaryResponse(Seller seller) {
        return SellerSummaryResponse.of(
                seller.getIdValue(),
                seller.getNameValue(),
                seller.getLogoUrlValue(),
                seller.getApprovalStatusValue());
    }

    /**
     * Seller 도메인 목록을 SellerSummaryResponse 목록으로 변환
     *
     * @param sellers Seller 도메인 목록
     * @return SellerSummaryResponse 목록
     */
    public List<SellerSummaryResponse> toSellerSummaryResponses(List<Seller> sellers) {
        return sellers.stream().map(this::toSellerSummaryResponse).toList();
    }
}
