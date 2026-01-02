package com.setof.connectly.module.category.entity;

import com.setof.connectly.module.category.enums.TargetGroup;
import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "category")
@Entity
public class Category extends BaseEntity {

    @Id
    @Column(name = "CATEGORY_ID")
    private long id;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @Column(name = "CATEGORY_DEPTH")
    private int categoryDepth;

    @Column(name = "PARENT_CATEGORY_ID")
    private long parentCategoryId;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(name = "DISPLAY_YN")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "TARGET_GROUP")
    @Enumerated(EnumType.STRING)
    private TargetGroup targetGroup;

    @Column(name = "PATH")
    private String path;
}
