package com.connectly.partnerAdmin.module.generic.money.converter;

import com.connectly.partnerAdmin.module.generic.money.Money;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Money money) {
        return (money != null) ? money.getAmount() : BigDecimal.ZERO;
    }

    @Override
    public Money convertToEntityAttribute(BigDecimal amount) {
        return (amount != null) ? Money.wons(amount) : Money.ZERO;
    }
}
