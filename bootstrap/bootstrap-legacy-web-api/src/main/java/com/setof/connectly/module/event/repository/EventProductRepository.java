package com.setof.connectly.module.event.repository;

import com.setof.connectly.module.event.entity.EventProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventProductRepository extends JpaRepository<EventProduct, Long> {}
