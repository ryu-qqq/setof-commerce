package com.connectly.partnerAdmin.module.utils;

import java.util.List;

import com.connectly.partnerAdmin.module.common.exception.JsonSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtils {
    private static final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jdk8.Jdk8Module());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        objectMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        objectMapper.setDefaultPropertyInclusion(
            com.fasterxml.jackson.annotation.JsonInclude.Value.construct(
                com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL,
                com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS
            )
        );
        return objectMapper;
    }

    public static <T> T fromJson(String json, Class<T> valueType){
        try{
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(json, valueType);
        }catch (JsonProcessingException e){
            throw new JsonSerializationException(e.getMessage());
        }
    }

    public static <T> String toJson(T valueType){
        try{
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(valueType);
        }catch (JsonProcessingException e){
            throw new JsonSerializationException(e.getMessage());
        }
    }

    public static <T> List<T> fromJsonList(String json, Class<T> valueType) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, valueType);
            return objectMapper.readValue(json, listType);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(e.getMessage());
        }
    }

}
