package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.RegisterSellerApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerApiRequest;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import org.springframework.stereotype.Component;

/**
 * SellerCommandApiMapper - 셀러 Command API 변환 매퍼.
 *
 * <p>API Request와 Application Command 간 변환을 담당합니다.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * <p>CQRS 분리: Command 전용 Mapper (QueryApiMapper와 분리).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class SellerCommandApiMapper {

    /**
     * RegisterSellerApiRequest -> RegisterSellerCommand 변환.
     *
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public RegisterSellerCommand toCommand(RegisterSellerApiRequest request) {
        RegisterSellerApiRequest.SellerInfoRequest sellerInfo = request.sellerInfo();
        RegisterSellerApiRequest.BusinessInfoRequest bizInfo = request.businessInfo();
        RegisterSellerApiRequest.AddressRequest addr = bizInfo.businessAddress();
        RegisterSellerApiRequest.CsContactRequest cs = bizInfo.csContact();

        return new RegisterSellerCommand(
                new RegisterSellerCommand.SellerInfoCommand(
                        sellerInfo.sellerName(),
                        sellerInfo.displayName(),
                        sellerInfo.logoUrl(),
                        sellerInfo.description()),
                new RegisterSellerCommand.SellerBusinessInfoCommand(
                        bizInfo.registrationNumber(),
                        bizInfo.companyName(),
                        bizInfo.representative(),
                        bizInfo.saleReportNumber(),
                        new RegisterSellerCommand.AddressCommand(
                                addr.zipCode(), addr.line1(), addr.line2()),
                        new RegisterSellerCommand.CsContactCommand(
                                cs.phone(), cs.email(), cs.mobile())));
    }

    /**
     * UpdateSellerApiRequest + PathVariable sellerId -> UpdateSellerCommand 변환.
     *
     * <p>API-DTO-004: Update Request에 ID 포함 금지 → PathVariable에서 전달.
     *
     * @param sellerId 셀러 ID (PathVariable)
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public UpdateSellerCommand toCommand(Long sellerId, UpdateSellerApiRequest request) {
        UpdateSellerCommand.CsInfoCommand csInfo = null;
        if (request.csInfo() != null) {
            csInfo =
                    new UpdateSellerCommand.CsInfoCommand(
                            request.csInfo().phone(),
                            request.csInfo().email(),
                            request.csInfo().mobile());
        }

        UpdateSellerCommand.BusinessInfoCommand businessInfo = null;
        if (request.businessInfo() != null) {
            UpdateSellerApiRequest.BusinessInfoRequest biz = request.businessInfo();
            UpdateSellerCommand.AddressCommand addr = null;
            if (biz.businessAddress() != null) {
                addr =
                        new UpdateSellerCommand.AddressCommand(
                                biz.businessAddress().zipCode(),
                                biz.businessAddress().line1(),
                                biz.businessAddress().line2());
            }
            businessInfo =
                    new UpdateSellerCommand.BusinessInfoCommand(
                            biz.registrationNumber(),
                            biz.companyName(),
                            biz.representative(),
                            biz.saleReportNumber(),
                            addr);
        }

        return new UpdateSellerCommand(
                sellerId,
                request.sellerName(),
                request.displayName(),
                request.logoUrl(),
                request.description(),
                csInfo,
                businessInfo);
    }
}
