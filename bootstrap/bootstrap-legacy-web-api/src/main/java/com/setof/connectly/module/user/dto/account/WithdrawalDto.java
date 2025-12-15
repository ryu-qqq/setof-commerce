package com.setof.connectly.module.user.dto.account;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.user.enums.WithdrawalReason;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawalDto {

    private Yn agreementYn;

    @NotNull(message = "reason은 필수입니다.")
    private WithdrawalReason reason;
}
