package com.setof.connectly.module.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Getter
public class SearchSlice<T> implements Slice<T> {

    private final List<T> content;
    private final boolean last;
    private final boolean first;
    private final int number;
    private final Sort sort;
    private final int size;
    private final int numberOfElements;
    private final boolean empty;
    private final Long lastDomainId;
    private final String cursorValue;
    private Long totalElements;

    @JsonIgnore private final Slice<T> originSlice;

    @Override
    public int getNumber() {
        return originSlice.getNumber();
    }

    @Override
    public int getSize() {
        return originSlice.getSize();
    }

    @Override
    public int getNumberOfElements() {
        return originSlice.getNumberOfElements();
    }

    @Override
    public List<T> getContent() {
        return originSlice.getContent();
    }

    @Override
    public boolean hasContent() {
        return originSlice.hasContent();
    }

    @Override
    public Sort getSort() {
        return originSlice.getSort();
    }

    @Override
    public boolean isFirst() {
        return originSlice.isFirst();
    }

    @Override
    public boolean isLast() {
        return originSlice.isLast();
    }

    @Override
    public boolean hasNext() {
        return originSlice.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return originSlice.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return originSlice.nextPageable();
    }

    @Override
    public Pageable previousPageable() {
        return originSlice.previousPageable();
    }

    @Override
    public <U> Slice<U> map(Function<? super T, ? extends U> converter) {
        return originSlice.map(converter);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return originSlice.iterator();
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
}
