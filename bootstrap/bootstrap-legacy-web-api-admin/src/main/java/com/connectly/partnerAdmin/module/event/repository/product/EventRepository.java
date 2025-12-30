package com.connectly.partnerAdmin.module.event.repository.product;

import com.connectly.partnerAdmin.module.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
