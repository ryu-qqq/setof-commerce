package com.setof.connectly.module.user.repository.refund;

import com.setof.connectly.module.user.entity.RefundAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundAccountRepository extends JpaRepository<RefundAccount, Long> {}
