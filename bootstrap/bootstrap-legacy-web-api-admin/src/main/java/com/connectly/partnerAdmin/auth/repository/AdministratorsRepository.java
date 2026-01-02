package com.connectly.partnerAdmin.auth.repository;

import com.connectly.partnerAdmin.auth.entity.Administrators;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorsRepository extends JpaRepository<Administrators, Long> {
}
