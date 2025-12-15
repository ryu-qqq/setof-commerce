package com.setof.connectly.module.mileage.dto.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.setof.connectly.module.user.dto.mileage.UserMileageDto;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Getter
public class MileagePage<T> implements Page<T> {

    private UserMileageDto userMileage;
    private final List<T> content;
    private final Pageable pageable;
    private final boolean last;
    private final int totalPages;
    private final long totalElements;
    private final boolean first;
    private final int number;
    private final Sort sort;
    private final int size;
    private final int numberOfElements;
    private final boolean empty;
    private final Long lastDomainId;

    @JsonIgnore private final Page<T> originalPage;

    @Override
    public boolean hasContent() {
        return originalPage.hasContent();
    }

    @Override
    public boolean hasNext() {
        return originalPage.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return originalPage.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return originalPage.nextPageable();
    }

    @Override
    public Pageable previousPageable() {
        return originalPage.previousPageable();
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return originalPage.map(converter);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return originalPage.iterator();
    }

    public void setUserMileage(UserMileageDto userMileage) {
        this.userMileage = userMileage;
    }
}
