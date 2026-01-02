package com.setof.connectly.module.mileage.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "mileage")
@Entity
public class Mileage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mileage_id")
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "mileage_amount")
    private double mileageAmount;

    @Column(name = "used_mileage_amount")
    private double usedMileageAmount;

    @Column(name = "issued_date")
    private LocalDateTime issuedDate;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "active_yn")
    @Enumerated(EnumType.STRING)
    private Yn activeYn;

    @Column(name = "title")
    private String title;
}
