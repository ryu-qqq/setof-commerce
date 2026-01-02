package com.connectly.partnerAdmin.module.external.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "EXTERNAL_QNA")
@Entity
public class ExternalQna extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXTERNAL_PRODUCT_ID")
    private long id;

    @Column(name = "SITE_ID")
    private long siteId;

    @Column(name = "QNA_ID")
    private long qnaId;

    @Column(name = "EXTERNAL_IDX")
    private long externalIdx;

}
