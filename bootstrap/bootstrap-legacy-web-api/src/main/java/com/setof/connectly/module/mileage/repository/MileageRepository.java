package com.setof.connectly.module.mileage.repository;

import com.setof.connectly.module.mileage.entity.Mileage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageRepository extends JpaRepository<Mileage, Long> {}
