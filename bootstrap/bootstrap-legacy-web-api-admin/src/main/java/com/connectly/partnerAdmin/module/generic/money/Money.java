package com.connectly.partnerAdmin.module.generic.money;

import com.connectly.partnerAdmin.module.generic.money.converter.MoneyDeserializer;
import com.connectly.partnerAdmin.module.generic.money.converter.MoneySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@JsonSerialize(using = MoneySerializer.class)
@JsonDeserialize(using = MoneyDeserializer.class)
@Getter
public class Money {
    public static final Money ZERO = Money.wons(0);

    @QueryType(PropertyType.NUMERIC)
    private final BigDecimal amount;

    public static Money wons(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money wons(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    Money(BigDecimal amount) {
        this.amount = amount.setScale(0, RoundingMode.HALF_UP);
    }

    public Money plus(Money money) {
        return new Money(this.amount.add(money.amount));
    }

    public Money minus(Money money) {
        return new Money(this.amount.subtract(money.amount));
    }

    public Money times(double percent) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(percent)));
    }

    public BigDecimal divide(Money divisor, int scale, RoundingMode roundingMode) {
        if (divisor.amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return this.amount.divide(divisor.amount, scale, roundingMode);
    }


    public boolean isLessThan(Money money) {
        return amount.compareTo(money.amount) < 0;
    }

    public boolean isGreaterThan(Money money) {
        return amount.compareTo(money.amount) > 0;
    }

    public boolean isGreaterThanOrEqual(Money money) {
        return amount.compareTo(money.amount) >= 0;
    }

    public boolean between(Money min, Money max) {
        return this.amount.compareTo(min.amount) >= 0 && this.amount.compareTo(max.amount) <= 0;
    }


    public long toPlainStringWithoutDecimal() {
        BigDecimal roundedAmount = this.amount.setScale(0, RoundingMode.HALF_UP);
        return roundedAmount.longValue();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Money other)) {
            return false;
        }

        return Objects.equals(amount.doubleValue(), other.amount.doubleValue());
    }

    public int hashCode() {
        return Objects.hashCode(amount);
    }

    public String toString() {
        return amount.toString();
    }
}
