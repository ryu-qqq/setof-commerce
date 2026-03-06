package com.ryuqq.setof.application.seller.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateSellerCommand(
        @NotNull Long sellerId,
        @NotBlank String sellerName,
        @NotBlank String displayName,
        @NotBlank String logoUrl,
        @NotBlank String description,
        CsInfoCommand csInfo,
        BusinessInfoCommand businessInfo) {
    public record CsInfoCommand(@NotBlank String phone, @NotBlank String email, String mobile) {}

    public record BusinessInfoCommand(
            @NotBlank String registrationNumber,
            @NotBlank String companyName,
            @NotBlank String representative,
            @NotBlank String saleReportNumber,
            AddressCommand businessAddress) {}

    public record AddressCommand(
            @NotBlank String zipCode, @NotBlank String line1, @NotBlank String line2) {}
}
