package com.setof.connectly.module.category.repository;

import com.setof.connectly.module.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
