package com.connectly.partnerAdmin.module.order.repository;

import com.connectly.partnerAdmin.module.order.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}
