package com.connectly.partnerAdmin.module.seller.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateSellerSettlementAccount {

    @NotNull(message = "은행명은 필수입니다.")
    private String bankName;

    @Pattern(regexp = "^[0-9]+$", message = "계좌 번호는 오직 숫자만 이루어져야 합니다.")
    private String accountNumber;

    @NotNull(message = "예금주 이름은 필수입니다.")
    private String accountHolderName;

}
