package com.connectly.partnerAdmin.module.seller.repository;

import com.connectly.partnerAdmin.module.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
