package com.connectly.partnerAdmin.module.external.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "external_qna")
@Entity
public class ExternalQna extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "external_product_id")
    private long id;

    @Column(name = "site_id")
    private long siteId;

    @Column(name = "qna_id")
    private long qnaId;

    @Column(name = "external_idx")
    private long externalIdx;

}
