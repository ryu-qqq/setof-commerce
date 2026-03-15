package com.ryuqq.setof.adapter.out.persistence.displaycomponent.repository;

import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.ComponentFixedProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentFixedProductJpaRepository
        extends JpaRepository<ComponentFixedProductJpaEntity, Long> {}
