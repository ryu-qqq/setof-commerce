package com.ryuqq.setof.domain.qna.vo;

import com.ryuqq.setof.domain.qna.exception.InvalidWriterIdException;
import java.util.Objects;
import java.util.UUID;

public final class WriterId {

    private final UUID value;

    private WriterId(UUID value) {
        if (value == null) {
            throw new InvalidWriterIdException();
        }
        this.value = value;
    }

    public static WriterId forNew(UUID uuid) {
        return new WriterId(uuid);
    }

    public static WriterId of(UUID uuid) {
        return new WriterId(uuid);
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WriterId writerId = (WriterId) o;
        return Objects.equals(value, writerId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "WriterId{value=" + value + "}";
    }
}
