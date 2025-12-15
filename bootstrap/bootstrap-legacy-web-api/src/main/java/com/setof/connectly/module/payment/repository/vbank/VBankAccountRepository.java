package com.setof.connectly.module.payment.repository.vbank;

import com.setof.connectly.module.payment.entity.VBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VBankAccountRepository extends JpaRepository<VBankAccount, Long> {}
