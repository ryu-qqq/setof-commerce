package com.ryuqq.setof.api.auth.mapper;

import com.ryuqq.setof.application.member.dto.command.ConsentItem;
import com.ryuqq.setof.application.member.dto.command.KakaoOAuthCommand;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * Kakao OAuth2 Mapper
 *
 * <p>카카오 OAuth2 인증 응답에서 사용자 정보를 추출하는 Mapper
 *
 * <p>카카오 응답 구조 (me.json):
 *
 * <pre>{@code
 * {
 *   "id": 123456789,
 *   "connected_at": "2024-01-21T01:23:45Z",
 *   "properties": {
 *     "nickname": "홍길동"
 *   },
 *   "kakao_account": {
 *     "profile": {
 *       "nickname": "홍길동"
 *     },
 *     "name": "홍길동",
 *     "email": "hong@test.com",
 *     "gender": "male",
 *     "birthday": "0115",
 *     "age_range": "20~29"
 *   }
 * }
 * }</pre>
 *
 * <p>주의: 카카오는 핸드폰 번호를 기본 제공하지 않음 (비즈니스 앱 권한 필요)
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class KakaoOAuth2Mapper {

    private static final String KAKAO_ACCOUNT_KEY = "kakao_account";
    private static final String PROFILE_KEY = "profile";
    private static final DateTimeFormatter BIRTHDAY_FORMATTER = DateTimeFormatter.ofPattern("MMdd");

    /**
     * OAuth2User에서 KakaoOAuthCommand 생성
     *
     * @param oAuth2User Spring Security OAuth2 사용자 객체
     * @return KakaoOAuthCommand
     */
    public KakaoOAuthCommand toKakaoOAuthCommand(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = getKakaoAccount(attributes);

        return new KakaoOAuthCommand(
                extractKakaoId(attributes),
                extractPhoneNumber(kakaoAccount),
                extractEmail(kakaoAccount),
                extractName(kakaoAccount),
                extractBirthdayAsLocalDate(kakaoAccount),
                extractGender(kakaoAccount),
                createDefaultConsents());
    }

    /**
     * 카카오 회원 ID 추출
     *
     * @param attributes OAuth2 attributes
     * @return 카카오 회원 ID (문자열)
     */
    public String extractKakaoId(Map<String, Object> attributes) {
        Object id = attributes.get("id");
        return id != null ? String.valueOf(id) : null;
    }

    /**
     * 이메일 추출
     *
     * @param kakaoAccount kakao_account 객체
     * @return 이메일 (없으면 null)
     */
    public String extractEmail(Map<String, Object> kakaoAccount) {
        return (String) kakaoAccount.get("email");
    }

    /**
     * 이름 추출 (name 필드 우선, 없으면 nickname)
     *
     * @param kakaoAccount kakao_account 객체
     * @return 이름 또는 닉네임
     */
    public String extractName(Map<String, Object> kakaoAccount) {
        // 실명이 있으면 실명 사용
        String name = (String) kakaoAccount.get("name");
        if (name != null && !name.isBlank()) {
            return name;
        }

        // 없으면 프로필 닉네임 사용
        Map<String, Object> profile = getProfile(kakaoAccount);
        return (String) profile.get("nickname");
    }

    /**
     * 닉네임 추출 (profile.nickname)
     *
     * @param kakaoAccount kakao_account 객체
     * @return 닉네임
     */
    public String extractNickname(Map<String, Object> kakaoAccount) {
        Map<String, Object> profile = getProfile(kakaoAccount);
        return (String) profile.get("nickname");
    }

    /**
     * 핸드폰 번호 추출 및 정규화
     *
     * <p>카카오는 핸드폰 번호를 기본 제공하지 않음 (비즈니스 앱 권한 필요)
     *
     * <p>형식 변환: +82 10-1234-5678 → 01012345678
     *
     * @param kakaoAccount kakao_account 객체
     * @return 정규화된 핸드폰 번호 (없으면 null)
     */
    public String extractPhoneNumber(Map<String, Object> kakaoAccount) {
        String phoneNumber = (String) kakaoAccount.get("phone_number");
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return null;
        }
        return normalizePhoneNumber(phoneNumber);
    }

    /**
     * 생년월일 추출 (문자열)
     *
     * <p>카카오 형식: "0115" (MMDD)
     *
     * @param kakaoAccount kakao_account 객체
     * @return 생년월일 (MMDD 형식, 없으면 null)
     */
    public String extractBirthday(Map<String, Object> kakaoAccount) {
        return (String) kakaoAccount.get("birthday");
    }

    /**
     * 생년월일 추출 (LocalDate)
     *
     * <p>카카오는 생년("birthyear")과 생일("birthday")을 별도로 제공
     *
     * <p>birthyear가 있으면: 완전한 LocalDate 반환
     *
     * <p>birthyear가 없으면: null 반환 (연도 없이는 LocalDate 생성 불가)
     *
     * @param kakaoAccount kakao_account 객체
     * @return LocalDate (연도 정보 없으면 null)
     */
    public LocalDate extractBirthdayAsLocalDate(Map<String, Object> kakaoAccount) {
        String birthyear = (String) kakaoAccount.get("birthyear");
        String birthday = (String) kakaoAccount.get("birthday");

        if (birthyear == null || birthday == null) {
            return null;
        }

        try {
            int year = Integer.parseInt(birthyear);
            MonthDay monthDay = MonthDay.parse(birthday, BIRTHDAY_FORMATTER);
            return monthDay.atYear(year);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 성별 추출
     *
     * <p>카카오 형식: "male" / "female"
     *
     * @param kakaoAccount kakao_account 객체
     * @return 성별 문자열 (없으면 null)
     */
    public String extractGender(Map<String, Object> kakaoAccount) {
        return (String) kakaoAccount.get("gender");
    }

    /**
     * 나이대 추출
     *
     * <p>카카오 형식: "20~29", "30~39" 등
     *
     * @param kakaoAccount kakao_account 객체
     * @return 나이대 문자열 (없으면 null)
     */
    public String extractAgeRange(Map<String, Object> kakaoAccount) {
        return (String) kakaoAccount.get("age_range");
    }

    /**
     * OAuth2 attributes에서 kakao_account 추출
     *
     * @param attributes OAuth2 attributes
     * @return kakao_account Map (없으면 빈 Map)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getKakaoAccount(Map<String, Object> attributes) {
        Object kakaoAccount = attributes.get(KAKAO_ACCOUNT_KEY);
        if (kakaoAccount instanceof Map) {
            return (Map<String, Object>) kakaoAccount;
        }
        return Map.of();
    }

    /**
     * kakao_account에서 profile 추출
     *
     * @param kakaoAccount kakao_account 객체
     * @return profile Map (없으면 빈 Map)
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getProfile(Map<String, Object> kakaoAccount) {
        Object profile = kakaoAccount.get(PROFILE_KEY);
        if (profile instanceof Map) {
            return (Map<String, Object>) profile;
        }
        return Map.of();
    }

    /**
     * 핸드폰 번호 정규화
     *
     * <p>+82 10-1234-5678 → 01012345678
     *
     * @param phoneNumber 원본 핸드폰 번호
     * @return 정규화된 핸드폰 번호
     */
    private String normalizePhoneNumber(String phoneNumber) {
        // +82 제거하고 0으로 시작하도록 변환
        String normalized = phoneNumber.replace("+82 ", "0");
        // 하이픈 제거
        return normalized.replace("-", "");
    }

    /**
     * 카카오 로그인 시 기본 동의 항목 생성
     *
     * <p>카카오 로그인은 이미 카카오에서 동의를 받았으므로 기본값 설정
     *
     * @return 기본 동의 항목 목록
     */
    private List<ConsentItem> createDefaultConsents() {
        return List.of(
                new ConsentItem("PRIVACY", true),
                new ConsentItem("SERVICE_TERMS", true),
                new ConsentItem("MARKETING", false));
    }
}
