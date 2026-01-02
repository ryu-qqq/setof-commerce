package com.connectly.partnerAdmin.module.seller.dto;

import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;
import com.connectly.partnerAdmin.module.external.dto.SiteResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerDetailResponse {
    private long sellerId;
    private String sellerName;
    private String logoUrl;
    private Double commissionRate;
    private ApprovalStatus approvalStatus;
    private String sellerDescription;
    private String businessAddressLine1;
    private String businessAddressLine2;
    private String businessAddressZipCode;
    private String returnAddressLine1;
    private String returnAddressLine2;
    private String returnAddressZipCode;
    private String csPhoneNumber;
    private String csNumber;
    private String csEmail;
    private String registrationNumber;
    private String saleReportNumber;
    private String representative;
    private String bankName;
    private String accountNumber;
    private String accountHolderName;
    private List<SiteResponse> sites;

    @QueryProjection
    public SellerDetailResponse(long sellerId, String sellerName, String logoUrl, Double commissionRate, ApprovalStatus approvalStatus, String sellerDescription, String businessAddressLine1, String businessAddressLine2, String businessAddressZipCode, String returnAddressLine1, String returnAddressLine2, String returnAddressZipCode, String csPhoneNumber, String csNumber, String csEmail, String registrationNumber, String saleReportNumber, String representative, String bankName, String accountNumber, String accountHolderName, List<SiteResponse> sites) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.logoUrl = logoUrl;
        this.commissionRate = commissionRate;
        this.approvalStatus = approvalStatus;
        this.sellerDescription = sellerDescription;
        this.businessAddressLine1 = businessAddressLine1;
        this.businessAddressLine2 = businessAddressLine2;
        this.businessAddressZipCode = businessAddressZipCode;
        this.returnAddressLine1 = returnAddressLine1;
        this.returnAddressLine2 = returnAddressLine2;
        this.returnAddressZipCode = returnAddressZipCode;
        this.csPhoneNumber = csPhoneNumber;
        this.csNumber = csNumber;
        this.csEmail = csEmail;
        this.registrationNumber = registrationNumber;
        this.saleReportNumber = saleReportNumber;
        this.representative = representative;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.sites = sites;
    }

    public void setFilteredSites() {
        this.sites = this.sites.stream()
                .filter(site -> site.getSiteId() != 0)
                .collect(Collectors.toList());
    }
}
