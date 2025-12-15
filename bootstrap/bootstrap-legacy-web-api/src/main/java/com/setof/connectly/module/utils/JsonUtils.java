package com.setof.connectly.module.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.setof.connectly.module.exception.common.JsonSerializationException;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonUtils {

    private static final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }

    public static <T> T fromJson(String json, Class<T> valueType) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException | JsonSerializationException e) {
            throw new JsonSerializationException(e.getMessage());
        }
    }

    public static <T> String toJson(T valueType) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(valueType);
        } catch (JsonProcessingException | JsonSerializationException e) {
            throw new JsonSerializationException(e.getMessage());
        }
    }

    public static <T> List<T> fromJsonList(String json, Class<T> valueType) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            JavaType listType =
                    objectMapper.getTypeFactory().constructCollectionType(List.class, valueType);
            return objectMapper.readValue(json, listType);
        } catch (JsonProcessingException | JsonSerializationException e) {
            throw new JsonSerializationException(e.getMessage());
        }
    }
}
