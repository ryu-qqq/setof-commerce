package com.connectly.partnerAdmin.module.generic.money.converter;


import com.connectly.partnerAdmin.module.generic.money.Money;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MoneySerializer extends JsonSerializer<Money> {

    @Override
    public void serialize(Money money, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(money.getAmount());
    }

}
