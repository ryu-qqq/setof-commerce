package com.ryuqq.setof.adapter.out.persistence.contentpage.repository;

import com.ryuqq.setof.adapter.out.persistence.contentpage.entity.ContentPageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentPageJpaRepository extends JpaRepository<ContentPageJpaEntity, Long> {}
