package com.connectly.partnerAdmin.module.discount.repository;

import com.connectly.partnerAdmin.module.discount.entity.DiscountTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountTargetRepository extends JpaRepository<DiscountTarget, Long> {
}
