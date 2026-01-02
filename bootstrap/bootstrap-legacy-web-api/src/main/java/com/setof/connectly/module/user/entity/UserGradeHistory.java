package com.setof.connectly.module.user.entity;

import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_grade_history")
@Entity
public class UserGradeHistory extends BaseEntity {

    @Id
    @Column(name = "USER_GRADE_HISTORY_ID")
    private long id;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "GRADE_ID")
    private long gradeId;
}
