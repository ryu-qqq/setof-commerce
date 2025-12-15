package com.setof.connectly.module.discount.repository.history;

import com.setof.connectly.module.discount.entity.history.DiscountUseHistory;
import java.util.List;

public interface DiscountUseHistoryJdbcRepository {

    void saveAll(List<DiscountUseHistory> discountUseHistories);
}
