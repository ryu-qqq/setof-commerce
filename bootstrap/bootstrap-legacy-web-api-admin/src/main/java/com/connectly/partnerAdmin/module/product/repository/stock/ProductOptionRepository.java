package com.connectly.partnerAdmin.module.product.repository.stock;

import com.connectly.partnerAdmin.module.product.entity.option.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}
