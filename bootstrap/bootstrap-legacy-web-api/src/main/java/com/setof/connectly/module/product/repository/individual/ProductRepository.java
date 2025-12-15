package com.setof.connectly.module.product.repository.individual;

import com.setof.connectly.module.product.entity.group.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
