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
    @Column(name = "category_id")
    private long id;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_depth")
    private int categoryDepth;

    @Column(name = "parent_category_id")
    private long parentCategoryId;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "display_yn")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Column(name = "target_group")
    @Enumerated(EnumType.STRING)
    private TargetGroup targetGroup;

    @Column(name = "path")
    private String path;
}
