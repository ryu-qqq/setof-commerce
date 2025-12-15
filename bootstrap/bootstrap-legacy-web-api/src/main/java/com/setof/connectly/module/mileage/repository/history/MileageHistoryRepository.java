package com.setof.connectly.module.mileage.repository.history;

import com.setof.connectly.module.mileage.entity.MileageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageHistoryRepository extends JpaRepository<MileageHistory, Long> {}
