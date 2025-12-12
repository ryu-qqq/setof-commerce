package com.ryuqq.setof.application.seller.service.command;

import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.manager.command.SellerPersistenceManager;
import com.ryuqq.setof.application.seller.manager.query.SellerReadManager;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
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
import org.springframework.stereotype.Service;

/**
 * 셀러 수정 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>SellerReadManager로 기존 셀러 조회
 *   <li>VO 생성 및 update 메서드 호출
 *   <li>SellerPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateSellerService implements UpdateSellerUseCase {

    private final SellerReadManager sellerReadManager;
    private final SellerPersistenceManager sellerPersistenceManager;
    private final ClockHolder clockHolder;

    public UpdateSellerService(
            SellerReadManager sellerReadManager,
            SellerPersistenceManager sellerPersistenceManager,
            ClockHolder clockHolder) {
        this.sellerReadManager = sellerReadManager;
        this.sellerPersistenceManager = sellerPersistenceManager;
        this.clockHolder = clockHolder;
    }

    @Override
    public void execute(UpdateSellerCommand command) {
        Seller existingSeller = sellerReadManager.findById(command.sellerId());

        SellerName sellerName = SellerName.of(command.sellerName());
        LogoUrl logoUrl = command.logoUrl() != null ? LogoUrl.of(command.logoUrl()) : null;
        Description description =
                command.description() != null ? Description.of(command.description()) : null;
        BusinessInfo businessInfo = createBusinessInfo(command);
        CustomerServiceInfo csInfo = createCustomerServiceInfo(command);

        Seller updatedSeller =
                existingSeller.update(
                        sellerName,
                        logoUrl,
                        description,
                        businessInfo,
                        csInfo,
                        Instant.now(clockHolder.getClock()));

        sellerPersistenceManager.persist(updatedSeller);
    }

    private BusinessInfo createBusinessInfo(UpdateSellerCommand command) {
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

    private CustomerServiceInfo createCustomerServiceInfo(UpdateSellerCommand command) {
        CsEmail csEmail = command.csEmail() != null ? CsEmail.of(command.csEmail()) : null;
        CsMobilePhone csMobilePhone =
                command.csMobilePhone() != null ? CsMobilePhone.of(command.csMobilePhone()) : null;
        CsLandlinePhone csLandlinePhone =
                command.csLandlinePhone() != null
                        ? CsLandlinePhone.of(command.csLandlinePhone())
                        : null;

        return CustomerServiceInfo.of(csEmail, csMobilePhone, csLandlinePhone);
    }
}
