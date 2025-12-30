package com.connectly.partnerAdmin.module.external.repository.product;

import com.connectly.partnerAdmin.module.external.entity.ExternalProduct;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExternalProductRepository extends JpaRepository<ExternalProduct, Long> {
}
