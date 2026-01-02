package com.connectly.partnerAdmin.module.product.repository.group;

import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long> {
}

