package com.setof.connectly.module.mileage.repository;

import com.setof.connectly.module.mileage.entity.MileageTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageTransactionRepository extends JpaRepository<MileageTransaction, Long> {}
