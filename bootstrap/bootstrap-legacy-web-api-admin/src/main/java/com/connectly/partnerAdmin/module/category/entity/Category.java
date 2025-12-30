package com.connectly.partnerAdmin.module.category.entity;


import com.connectly.partnerAdmin.module.category.enums.CategoryType;
import com.connectly.partnerAdmin.module.category.enums.TargetGroup;
import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "CATEGORY")
@Entity
public class Category extends BaseEntity {

    @Id
    @Column(name = "CATEGORY_ID")
    private long id;

    @Column(name = "CATEGORY_NAME", length = 50, nullable = false)
    private String categoryName;

    @Column(name = "CATEGORY_DEPTH", nullable = false)
    private int categoryDepth;

    @Column(name = "PARENT_CATEGORY_ID", nullable = false)
    private long parentCategoryId;

    @Column(name = "DISPLAY_NAME", length = 50, nullable = false)
    private String displayName;

    @Column(name = "DISPLAY_YN", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "TARGET_GROUP", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private TargetGroup targetGroup;

    @Column(name = "CATEGORY_TYPE", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Column(name = "PATH", length = 50, nullable = false)
    private String path;


}
