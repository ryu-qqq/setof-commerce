package com.setof.connectly.module.news.entity.faq;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.news.enums.FaqType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "Faq")
@Entity
public class Faq extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAQ_ID")
    private long id;

    @Enumerated(EnumType.STRING)
    private FaqType faqType;

    private String title;
    private String contents;
    private int displayOrder;
    private int topDisplayOrder;
}
