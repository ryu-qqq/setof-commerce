package com.connectly.partnerAdmin.module.mileage.repopsitory;

import com.connectly.partnerAdmin.module.mileage.entity.Mileage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageRepository extends JpaRepository<Mileage, Long> {
}
