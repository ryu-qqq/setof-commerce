package com.connectly.partnerAdmin.module.user.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.user.enums.UserGradeEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;


@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "USER_GRADE")
@Entity
public class UserGrade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_GRADE_ID")
    private long id;

    @Column(name = "GRADE_NAME")
    @Enumerated(EnumType.STRING)
    private UserGradeEnum gradeName;

    @Column(name = "MILEAGE_RATE")
    private double mileageRate;

    private UserGrade(long id, UserGradeEnum gradeName) {
        this.id = id;
        this.gradeName = gradeName;
        this.mileageRate = gradeName.getMileageReserveRate();
    }

    public static UserGrade normalGradeUser() {
        return new UserGrade(1L, UserGradeEnum.NORMAL_GRADE);
    }

}
