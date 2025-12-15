package com.setof.connectly.module.cart.repository;

import com.setof.connectly.module.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {}
