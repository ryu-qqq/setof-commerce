package com.setof.connectly.module.user.repository.shipping;

import com.setof.connectly.module.user.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {}
