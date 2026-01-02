package com.connectly.partnerAdmin.module.order.repository;

import com.connectly.partnerAdmin.module.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
