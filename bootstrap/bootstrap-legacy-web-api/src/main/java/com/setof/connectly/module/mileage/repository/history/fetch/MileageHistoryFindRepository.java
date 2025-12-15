package com.setof.connectly.module.mileage.repository.history.fetch;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.mileage.dto.filter.MileageFilter;
import com.setof.connectly.module.user.dto.mileage.UserMileageHistoryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface MileageHistoryFindRepository {

    List<UserMileageHistoryDto> fetchMileageHistories(
            MileageFilter filter, long userId, Pageable pageable);

    JPAQuery<Long> fetchMileageHistoryCount(MileageFilter filter, long userId);
}
