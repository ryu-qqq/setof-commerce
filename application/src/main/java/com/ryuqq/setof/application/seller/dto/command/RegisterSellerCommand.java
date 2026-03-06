package com.ryuqq.setof.application.seller.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterSellerCommand(
        @NotNull SellerInfoCommand seller, @NotNull SellerBusinessInfoCommand businessInfo) {
    public record SellerInfoCommand(
            @NotBlank String sellerName,
            @NotBlank String displayName,
            @NotBlank String logoUrl,
            @NotBlank String description) {}

    public record SellerBusinessInfoCommand(
            @NotBlank String registrationNumber,
            @NotBlank String companyName,
            @NotBlank String representative,
            @NotBlank String saleReportNumber,
            @NotNull AddressCommand businessAddress,
            @NotNull CsContactCommand csContact) {}

    public record AddressCommand(
            @NotBlank String zipCode, @NotBlank String line1, @NotBlank String line2) {}

    public record CsContactCommand(
            @NotBlank String phone, @NotBlank String email, @NotBlank String mobile) {}
}
