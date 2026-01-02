package com.connectly.partnerAdmin.module.generic.money.converter;

import com.connectly.partnerAdmin.module.generic.money.Money;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class MoneyDeserializer extends JsonDeserializer<Money> {

    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        BigDecimal amount = p.getDecimalValue();
        return Money.wons(amount);
    }

}
