package com.connectly.partnerAdmin.module.common.dto;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class CustomPageable<T> implements Page<T> {

    private final List<T> content;
    private final Pageable pageable;
    private final long totalElements;
    private final Long lastDomainId;

    public CustomPageable(List<T> content, Pageable pageable, long total, Long lastDomainId) {
        this.content = content;
        this.pageable = pageable;
        this.totalElements = total;
        this.lastDomainId = lastDomainId;
    }

    @Override
    public int getTotalPages() {
        return (int) Math.ceil((double) totalElements / pageable.getPageSize());
    }

    @Override
    public long getTotalElements() {
        return totalElements;
    }

    @NotNull
    @Override
    public <U> Page<U> map(@NotNull Function<? super T, ? extends U> converter) {
        List<U> convertedContent = content.stream().map(converter).collect(Collectors.toList());
        return new CustomPageable<>(convertedContent, pageable, totalElements, lastDomainId);
    }

    @Override
    public int getNumber() {
        return pageable.getPageNumber();
    }

    @Override
    public int getSize() {
        return pageable.getPageSize();
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @NotNull
    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @NotNull
    @Override
    public Sort getSort() {
        return pageable.getSort();
    }

    @NotNull
    @Override
    public Pageable getPageable() {
        return pageable;
    }

    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    @Override
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    @NotNull
    @Override
    public Pageable nextPageable() {
        return hasNext() ? pageable.next() : Pageable.unpaged();
    }

    @NotNull
    @Override
    public Pageable previousPageable() {
        return hasPrevious() ? pageable.previousOrFirst() : Pageable.unpaged();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }
}