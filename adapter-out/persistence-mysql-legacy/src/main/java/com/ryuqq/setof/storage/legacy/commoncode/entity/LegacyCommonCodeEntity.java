package com.ryuqq.setof.storage.legacy.commoncode.entity;

import com.ryuqq.setof.storage.legacy.common.entity.LegacyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LegacyCommonCodeEntity - 레거시 공통 코드 JPA Entity.
 *
 * <p>luxurydb.common_code 테이블과 매핑됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "common_code")
public class LegacyCommonCodeEntity extends LegacyBaseEntity {

    @Id
    @Column(name = "code_id")
    private Long id;

    @Column(name = "CODE_GROUP_ID", nullable = false)
    private long codeGroupId;

    @Column(name = "CODE_DETAIL", nullable = false)
    private String codeDetail;

    @Column(name = "CODE_DETAIL_DISPLAY_NAME", nullable = false)
    private String codeDetailDisplayName;

    @Column(name = "DISPLAY_ORDER")
    private int displayOrder;

    @Column(name = "DELETE_YN", nullable = false)
    private String deleteYn;

    protected LegacyCommonCodeEntity() {}

    public Long getId() {
        return id;
    }

    public long getCodeGroupId() {
        return codeGroupId;
    }

    public String getCodeDetail() {
        return codeDetail;
    }

    public String getCodeDetailDisplayName() {
        return codeDetailDisplayName;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public String getDeleteYn() {
        return deleteYn;
    }

    public boolean isActive() {
        return "N".equals(deleteYn);
    }
}
