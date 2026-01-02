package com.connectly.partnerAdmin.module.common.entity;

import java.time.LocalDateTime;

import org.slf4j.MDC;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Setter
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "DELETE_YN", length = 1, nullable = false)
    private Yn deleteYn;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(updatable = false, name = "INSERT_DATE", nullable = false)
    private LocalDateTime insertDate;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @LastModifiedDate
    @Column(name = "UPDATE_DATE", nullable = false)
    private LocalDateTime updateDate;

    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "INSERT_OPERATOR", length = 50, nullable = false)
    private String insertOperator;

    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "UPDATE_OPERATOR", length = 50, nullable = false)
    private String updateOperator;

    @PrePersist
    public void before() {
        LocalDateTime now = LocalDateTime.now();
        this.insertDate = now;
        this.updateDate = now;
        this.insertOperator = MDC.get("user") ==null ? "Anonymous" : MDC.get("user");
        this.updateOperator = MDC.get("user") ==null ? "Anonymous" : MDC.get("user");
        this.deleteYn = Yn.N;
    }

    @PreUpdate
    public void always() {
        this.updateDate = LocalDateTime.now();
        this.updateOperator = MDC.get("user") == null ? "Anonymous" : MDC.get("user");
    }


}

