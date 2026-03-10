package com.ryuqq.setof.storage.legacy.user.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * DB에 소문자(none, kakao)로 저장된 social_login_type을 enum으로 변환합니다.
 *
 * <p>대소문자 무관 매핑 후, 알 수 없는 값은 EMAIL로 폴백합니다.
 */
@Converter(autoApply = false)
public class SocialLoginTypeConverter
        implements AttributeConverter<LegacyUserEntity.SocialLoginType, String> {

    @Override
    public String convertToDatabaseColumn(LegacyUserEntity.SocialLoginType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().toLowerCase();
    }

    @Override
    public LegacyUserEntity.SocialLoginType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return LegacyUserEntity.SocialLoginType.EMAIL;
        }
        try {
            return LegacyUserEntity.SocialLoginType.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            return LegacyUserEntity.SocialLoginType.EMAIL;
        }
    }
}
