package com.setof.connectly.module.mileage.repository.history;

import com.setof.connectly.module.mileage.entity.MileageHistory;
import java.util.List;

public interface MileageHistoryJdbcRepository {

    void saveAll(List<MileageHistory> mileageHistories);
}
