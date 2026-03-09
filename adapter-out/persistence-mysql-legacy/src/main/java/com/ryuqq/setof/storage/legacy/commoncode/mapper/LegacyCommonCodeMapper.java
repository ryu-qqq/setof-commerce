package com.ryuqq.setof.storage.legacy.commoncode.mapper;

import com.ryuqq.setof.domain.commoncode.aggregate.CommonCode;
import com.ryuqq.setof.domain.commoncode.id.CommonCodeId;
import com.ryuqq.setof.domain.commoncodetype.id.CommonCodeTypeId;
import com.ryuqq.setof.storage.legacy.commoncode.entity.LegacyCommonCodeEntity;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyCommonCodeMapper - 레거시 공통 코드 Entity → Domain 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyCommonCodeMapper {

    /**
     * 레거시 Entity → CommonCode 도메인 변환.
     *
     * @param entity 레거시 공통 코드 엔티티
     * @return CommonCode 도메인 객체
     */
    public CommonCode toDomain(LegacyCommonCodeEntity entity) {
        return CommonCode.reconstitute(
                CommonCodeId.of(entity.getId()),
                CommonCodeTypeId.of(entity.getCodeGroupId()),
                entity.getCodeDetail(),
                entity.getCodeDetailDisplayName(),
                entity.getDisplayOrder(),
                entity.isActive(),
                null,
                entity.getInsertDate() != null
                        ? entity.getInsertDate().atZone(ZoneId.systemDefault()).toInstant()
                        : null,
                entity.getUpdateDate() != null
                        ? entity.getUpdateDate().atZone(ZoneId.systemDefault()).toInstant()
                        : null);
    }

    /**
     * 레거시 Entity 목록 → CommonCode 도메인 목록 변환.
     *
     * @param entities 레거시 엔티티 목록
     * @return CommonCode 도메인 목록
     */
    public List<CommonCode> toDomains(List<LegacyCommonCodeEntity> entities) {
        return entities.stream().map(this::toDomain).toList();
    }
}
