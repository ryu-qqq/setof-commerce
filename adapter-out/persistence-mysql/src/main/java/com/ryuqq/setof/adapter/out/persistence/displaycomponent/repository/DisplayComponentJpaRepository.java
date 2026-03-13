package com.ryuqq.setof.adapter.out.persistence.displaycomponent.repository;

import com.ryuqq.setof.adapter.out.persistence.displaycomponent.entity.DisplayComponentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisplayComponentJpaRepository
        extends JpaRepository<DisplayComponentJpaEntity, Long> {}
