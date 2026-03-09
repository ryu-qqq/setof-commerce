package com.ryuqq.setof.storage.legacy.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyTitleComponentEntity - 레거시 타이틀 컴포넌트 엔티티.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "title_component")
public class LegacyTitleComponentEntity {

    @Id
    @Column(name = "title_component_id")
    private Long id;

    @Column(name = "component_id")
    private long componentId;

    @Column(name = "title1")
    private String title1;

    @Column(name = "title2")
    private String title2;

    @Column(name = "sub_title1")
    private String subTitle1;

    @Column(name = "sub_title2")
    private String subTitle2;

    @Column(name = "delete_yn")
    private String deleteYn;

    protected LegacyTitleComponentEntity() {}

    public Long getId() {
        return id;
    }

    public long getComponentId() {
        return componentId;
    }

    public String getTitle1() {
        return title1;
    }

    public String getTitle2() {
        return title2;
    }

    public String getSubTitle1() {
        return subTitle1;
    }

    public String getSubTitle2() {
        return subTitle2;
    }

    public String getDeleteYn() {
        return deleteYn;
    }
}
