package com.setof.connectly.module.user.dto.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateRefundAccount {

    @NotNull(message = "bankName 필수입니다.")
    private String bankName;

    @Pattern(regexp = "^[0-9]+$", message = "계좌 번호는 오직 숫자만 이루어져야 합니다.")
    private String accountNumber;

    private String accountHolderName;
}
