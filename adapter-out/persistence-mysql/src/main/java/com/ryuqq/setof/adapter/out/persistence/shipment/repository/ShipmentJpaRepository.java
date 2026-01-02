package com.ryuqq.setof.adapter.out.persistence.shipment.repository;

import com.ryuqq.setof.adapter.out.persistence.shipment.entity.ShipmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ShipmentJpaRepository - Shipment JPA Repository
 *
 * <p>기본 CRUD 작업을 담당하는 Spring Data JPA Repository입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public interface ShipmentJpaRepository extends JpaRepository<ShipmentJpaEntity, Long> {}
