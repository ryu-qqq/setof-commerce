package com.connectly.partnerAdmin.module.common.entity;

import jakarta.persistence.*;
import lombok.*;




@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "COMMON_CODE")
@Entity
public class CommonCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODE_ID")
    private long id;
    private long codeGroupId;
    private String codeDetail;
    private String codeDetailDisplayName;
    private int displayOrder;
}
