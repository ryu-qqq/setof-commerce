package com.connectly.partnerAdmin.module.external.repository.order;

import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalOrderRepository extends JpaRepository<ExternalOrder, Long> {
}
