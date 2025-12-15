package com.setof.connectly.module.common;

import com.setof.connectly.module.common.enums.Yn;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;
import org.slf4j.MDC;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Enumerated(EnumType.STRING)
    private Yn deleteYn;

    @Column(updatable = false)
    private LocalDateTime insertDate;

    @LastModifiedDate private LocalDateTime updateDate;
    private String insertOperator;
    private String updateOperator;

    @PrePersist
    public void before() {
        LocalDateTime now = LocalDateTime.now();
        this.insertDate = now;
        this.updateDate = now;
        this.insertOperator = MDC.get("user") == null ? "Anonymous" : MDC.get("user");
        this.updateOperator = MDC.get("user") == null ? "Anonymous" : MDC.get("user");
        this.deleteYn = Yn.N;
    }

    @PreUpdate
    public void always() {
        this.updateDate = LocalDateTime.now();
        this.updateOperator = MDC.get("user") == null ? "Anonymous" : MDC.get("user");
    }

    public void setDeleteYn(Yn deleteYn) {
        this.deleteYn = deleteYn;
    }
}
