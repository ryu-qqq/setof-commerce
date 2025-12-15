package com.setof.connectly.module.user.repository;

import com.setof.connectly.module.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {}
