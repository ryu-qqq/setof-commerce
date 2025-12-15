package com.setof.connectly.module.order.repository;

import com.setof.connectly.module.order.entity.order.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {}
