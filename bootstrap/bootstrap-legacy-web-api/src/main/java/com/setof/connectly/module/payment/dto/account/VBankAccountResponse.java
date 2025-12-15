package com.setof.connectly.module.payment.dto.account;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VBankAccountResponse extends AccountResponse {

    private long paymentAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime vBankDueDate;

    @QueryProjection
    public VBankAccountResponse(
            String bankName, String accountNumber, long paymentAmount, LocalDateTime vBankDueDate) {
        super(bankName, accountNumber);
        this.paymentAmount = paymentAmount;
        this.vBankDueDate = vBankDueDate;
    }
}
