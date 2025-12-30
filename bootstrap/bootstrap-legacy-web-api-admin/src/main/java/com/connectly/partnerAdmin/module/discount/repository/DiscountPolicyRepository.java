package com.connectly.partnerAdmin.module.discount.repository;

import com.connectly.partnerAdmin.module.discount.entity.DiscountPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountPolicyRepository extends JpaRepository<DiscountPolicy, Long> {
}
