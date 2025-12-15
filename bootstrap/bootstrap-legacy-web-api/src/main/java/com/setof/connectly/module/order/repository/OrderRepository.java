package com.setof.connectly.module.order.repository;

import com.setof.connectly.module.order.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderJdbcRepository {}
