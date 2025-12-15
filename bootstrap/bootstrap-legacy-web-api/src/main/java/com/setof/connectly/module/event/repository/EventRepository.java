package com.setof.connectly.module.event.repository;

import com.setof.connectly.module.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {}
