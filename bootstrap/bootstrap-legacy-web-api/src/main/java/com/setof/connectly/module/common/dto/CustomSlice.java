package com.setof.connectly.module.common.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Getter
public class CustomSlice<T> {
    private final List<T> content;
    private final boolean last;
    private final boolean first;
    private final int number;
    private final Sort sort;
    private final int size;
    private final int numberOfElements;
    private final boolean empty;
    private Long lastDomainId;
    private String cursorValue;
    private Long totalElements;

    public void setLastDomainId(Long lastDomainId) {
        this.lastDomainId = lastDomainId;
    }

    public void setCursorValue(String cursorValue) {
        this.cursorValue = cursorValue;
    }
}
