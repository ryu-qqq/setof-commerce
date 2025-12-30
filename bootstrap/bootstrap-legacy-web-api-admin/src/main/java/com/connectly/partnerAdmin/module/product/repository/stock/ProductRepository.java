package com.connectly.partnerAdmin.module.product.repository.stock;

import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
