package com.setof.connectly.module.user.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.user.enums.UserGradeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "USER_GRADE")
@Entity
public class UserGrade extends BaseEntity {
    @Id
    @Column(name = "USER_GRADE_ID")
    private long id;

    @Column(name = "GRADE_NAME")
    @Enumerated(EnumType.STRING)
    private UserGradeEnum gradeName;

    @Column(name = "MILEAGE_RATE")
    private double mileageRate;
}
