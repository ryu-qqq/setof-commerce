package com.connectly.partnerAdmin.module.user.repository;

import com.connectly.partnerAdmin.module.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}
