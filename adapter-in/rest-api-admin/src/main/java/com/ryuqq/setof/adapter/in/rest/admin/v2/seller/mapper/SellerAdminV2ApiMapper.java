package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.RegisterSellerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerV2ApiRequest;
import com.ryuqq.setof.application.seller.dto.command.DeleteSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateApprovalStatusCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchQuery;
import org.springframework.stereotype.Component;

/**
 * Seller Admin V2 API Mapper
 *
 * <p>셀러 관리 API DTO ↔ Application Command 변환
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>@Component로 DI (Static 금지)
 *   <li>비즈니스 로직 금지 - 순수 변환만
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class SellerAdminV2ApiMapper {

    public RegisterSellerCommand toRegisterCommand(RegisterSellerV2ApiRequest request) {
        return new RegisterSellerCommand(
                request.sellerName(),
                request.logoUrl(),
                request.description(),
                request.registrationNumber(),
                request.saleReportNumber(),
                request.representative(),
                request.businessAddressLine1(),
                request.businessAddressLine2(),
                request.businessZipCode(),
                request.csEmail(),
                request.csMobilePhone(),
                request.csLandlinePhone());
    }

    public UpdateSellerCommand toUpdateCommand(Long sellerId, UpdateSellerV2ApiRequest request) {
        return new UpdateSellerCommand(
                sellerId,
                request.sellerName(),
                request.logoUrl(),
                request.description(),
                request.registrationNumber(),
                request.saleReportNumber(),
                request.representative(),
                request.businessAddressLine1(),
                request.businessAddressLine2(),
                request.businessZipCode(),
                request.csEmail(),
                request.csMobilePhone(),
                request.csLandlinePhone());
    }

    public UpdateApprovalStatusCommand toApproveCommand(Long sellerId) {
        return new UpdateApprovalStatusCommand(sellerId, "APPROVED");
    }

    public UpdateApprovalStatusCommand toRejectCommand(Long sellerId) {
        return new UpdateApprovalStatusCommand(sellerId, "REJECTED");
    }

    public UpdateApprovalStatusCommand toSuspendCommand(Long sellerId) {
        return new UpdateApprovalStatusCommand(sellerId, "SUSPENDED");
    }

    public DeleteSellerCommand toDeleteCommand(Long sellerId) {
        return new DeleteSellerCommand(sellerId);
    }

    public SellerSearchQuery toSearchQuery(
            String sellerName, String approvalStatus, int page, int size) {
        return new SellerSearchQuery(sellerName, approvalStatus, page, size);
    }
}
