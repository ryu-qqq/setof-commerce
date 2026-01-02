package com.connectly.partnerAdmin.module.event.repository.mileage;

import com.connectly.partnerAdmin.module.event.entity.EventMileage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventMileageRepository extends JpaRepository<EventMileage, Long> {
}
