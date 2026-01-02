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
    @Column(name = "MILEAGE_ID")
    private long id;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "MILEAGE_AMOUNT")
    private double mileageAmount;

    @Column(name = "USED_MILEAGE_AMOUNT")
    private double usedMileageAmount;

    @Column(name = "ISSUED_DATE")
    private LocalDateTime issuedDate;

    @Column(name = "EXPIRATION_DATE")
    private LocalDateTime expirationDate;

    @Column(name = "ACTIVE_YN")
    @Enumerated(EnumType.STRING)
    private Yn activeYn;

    @Column(name = "TITLE")
    private String title;
}
