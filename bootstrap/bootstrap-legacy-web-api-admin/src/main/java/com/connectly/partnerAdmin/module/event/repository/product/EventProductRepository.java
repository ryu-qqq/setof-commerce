package com.connectly.partnerAdmin.module.event.repository.product;

import com.connectly.partnerAdmin.module.event.entity.EventProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventProductRepository extends JpaRepository<EventProduct, Long> {
}
