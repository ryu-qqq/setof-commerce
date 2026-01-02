package com.connectly.partnerAdmin.module.category.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;




@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "category_mapping")
@Entity
public class CategoryMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_mapping_id")
    private long id;

    private long siteId;

    private long categoryId;

    private String mappingCategoryId;

    private String description;
}
