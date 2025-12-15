package com.setof.connectly.module.mileage.repository.query;

import com.setof.connectly.module.mileage.entity.MileageTransaction;
import java.util.List;

public interface MileageTransactionJdbcRepository {

    void saveAll(List<MileageTransaction> mileageTransactions);
}
