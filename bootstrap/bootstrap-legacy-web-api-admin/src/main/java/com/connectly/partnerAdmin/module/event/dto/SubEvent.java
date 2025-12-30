package com.connectly.partnerAdmin.module.event.dto;


import com.connectly.partnerAdmin.module.event.enums.EventType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateProductEvent.class, name = "productEvent"),
        @JsonSubTypes.Type(value = CreateMileageEvent.class, name = "mileageEvent")
})
public interface SubEvent {

    EventType getEventType();
}
