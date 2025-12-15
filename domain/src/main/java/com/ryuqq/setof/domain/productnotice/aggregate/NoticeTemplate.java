package com.ryuqq.setof.domain.productnotice.aggregate;

import com.ryuqq.setof.domain.productnotice.vo.NoticeField;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * NoticeTemplate Aggregate Root
 *
 * <p>상품고시 템플릿을 나타내는 도메인 엔티티입니다. 카테고리별로 필수/선택 고시 필드를 정의합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드 + 방어적 복사
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 *   <li>Long FK 전략 - categoryId는 Long 타입
 * </ul>
 */
@SuppressWarnings("PMD.DomainLayerDemeterStrict")
public class NoticeTemplate {

    private static final int MAX_NAME_LENGTH = 100;

    private final NoticeTemplateId id;
    private final Long categoryId;
    private final String templateName;
    private final List<NoticeField> requiredFields;
    private final List<NoticeField> optionalFields;
    private final Instant createdAt;
    private final Instant updatedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private NoticeTemplate(
            NoticeTemplateId id,
            Long categoryId,
            String templateName,
            List<NoticeField> requiredFields,
            List<NoticeField> optionalFields,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.templateName = templateName;
        this.requiredFields = List.copyOf(requiredFields);
        this.optionalFields = List.copyOf(optionalFields);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 상품고시 템플릿 생성용 Static Factory Method
     *
     * <p>ID 없이 신규 생성
     *
     * @param categoryId 카테고리 ID
     * @param templateName 템플릿명 (예: "의류", "신발", "가방")
     * @param requiredFields 필수 필드 목록
     * @param optionalFields 선택 필드 목록
     * @param createdAt 생성일시
     * @return NoticeTemplate 인스턴스
     */
    public static NoticeTemplate create(
            Long categoryId,
            String templateName,
            List<NoticeField> requiredFields,
            List<NoticeField> optionalFields,
            Instant createdAt) {
        validateCreate(categoryId, templateName, requiredFields, optionalFields);
        return new NoticeTemplate(
                NoticeTemplateId.forNew(),
                categoryId,
                templateName,
                requiredFields != null ? requiredFields : List.of(),
                optionalFields != null ? optionalFields : List.of(),
                createdAt,
                createdAt);
    }

    /**
     * 필수 필드만으로 신규 템플릿 생성
     *
     * @param categoryId 카테고리 ID
     * @param templateName 템플릿명
     * @param requiredFields 필수 필드 목록
     * @param createdAt 생성일시
     * @return NoticeTemplate 인스턴스
     */
    public static NoticeTemplate createWithRequiredFieldsOnly(
            Long categoryId,
            String templateName,
            List<NoticeField> requiredFields,
            Instant createdAt) {
        return create(categoryId, templateName, requiredFields, List.of(), createdAt);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 템플릿 ID
     * @param categoryId 카테고리 ID
     * @param templateName 템플릿명
     * @param requiredFields 필수 필드 목록
     * @param optionalFields 선택 필드 목록
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return NoticeTemplate 인스턴스
     */
    public static NoticeTemplate reconstitute(
            NoticeTemplateId id,
            Long categoryId,
            String templateName,
            List<NoticeField> requiredFields,
            List<NoticeField> optionalFields,
            Instant createdAt,
            Instant updatedAt) {
        return new NoticeTemplate(
                id,
                categoryId,
                templateName,
                requiredFields != null ? requiredFields : List.of(),
                optionalFields != null ? optionalFields : List.of(),
                createdAt,
                updatedAt);
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 템플릿명 변경
     *
     * @param newName 새로운 템플릿명
     * @param updatedAt 수정일시
     * @return 템플릿명이 변경된 NoticeTemplate 인스턴스
     */
    public NoticeTemplate rename(String newName, Instant updatedAt) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("템플릿명은 필수입니다");
        }
        if (newName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("템플릿명 길이가 %d자를 초과합니다: %d", MAX_NAME_LENGTH, newName.length()));
        }
        return new NoticeTemplate(
                this.id,
                this.categoryId,
                newName,
                this.requiredFields,
                this.optionalFields,
                this.createdAt,
                updatedAt);
    }

    /**
     * 필수 필드 추가
     *
     * @param field 추가할 필수 필드
     * @param updatedAt 수정일시
     * @return 필수 필드가 추가된 NoticeTemplate 인스턴스
     */
    public NoticeTemplate addRequiredField(NoticeField field, Instant updatedAt) {
        if (field == null) {
            throw new IllegalArgumentException("추가할 필드는 필수입니다");
        }
        validateNoDuplicateKey(field.key(), requiredFields, optionalFields);

        List<NoticeField> newRequiredFields = new ArrayList<>(this.requiredFields);
        newRequiredFields.add(field);

        return new NoticeTemplate(
                this.id,
                this.categoryId,
                this.templateName,
                newRequiredFields,
                this.optionalFields,
                this.createdAt,
                updatedAt);
    }

    /**
     * 선택 필드 추가
     *
     * @param field 추가할 선택 필드
     * @param updatedAt 수정일시
     * @return 선택 필드가 추가된 NoticeTemplate 인스턴스
     */
    public NoticeTemplate addOptionalField(NoticeField field, Instant updatedAt) {
        if (field == null) {
            throw new IllegalArgumentException("추가할 필드는 필수입니다");
        }
        validateNoDuplicateKey(field.key(), requiredFields, optionalFields);

        List<NoticeField> newOptionalFields = new ArrayList<>(this.optionalFields);
        newOptionalFields.add(field);

        return new NoticeTemplate(
                this.id,
                this.categoryId,
                this.templateName,
                this.requiredFields,
                newOptionalFields,
                this.createdAt,
                updatedAt);
    }

    /**
     * 필드 제거 (키 기준)
     *
     * @param fieldKey 제거할 필드의 키
     * @param updatedAt 수정일시
     * @return 필드가 제거된 NoticeTemplate 인스턴스
     */
    public NoticeTemplate removeField(String fieldKey, Instant updatedAt) {
        if (fieldKey == null || fieldKey.isBlank()) {
            throw new IllegalArgumentException("필드 키는 필수입니다");
        }

        List<NoticeField> newRequiredFields =
                this.requiredFields.stream().filter(f -> !f.key().equals(fieldKey)).toList();

        List<NoticeField> newOptionalFields =
                this.optionalFields.stream().filter(f -> !f.key().equals(fieldKey)).toList();

        return new NoticeTemplate(
                this.id,
                this.categoryId,
                this.templateName,
                newRequiredFields,
                newOptionalFields,
                this.createdAt,
                updatedAt);
    }

    /**
     * 모든 필드 교체 (전체 업데이트)
     *
     * @param newRequiredFields 새로운 필수 필드 목록
     * @param newOptionalFields 새로운 선택 필드 목록
     * @param updatedAt 수정일시
     * @return 필드가 교체된 NoticeTemplate 인스턴스
     */
    public NoticeTemplate replaceAllFields(
            List<NoticeField> newRequiredFields,
            List<NoticeField> newOptionalFields,
            Instant updatedAt) {
        List<NoticeField> safeRequired = newRequiredFields != null ? newRequiredFields : List.of();
        List<NoticeField> safeOptional = newOptionalFields != null ? newOptionalFields : List.of();
        validateNoDuplicateKeys(safeRequired, safeOptional);

        return new NoticeTemplate(
                this.id,
                this.categoryId,
                this.templateName,
                safeRequired,
                safeOptional,
                this.createdAt,
                updatedAt);
    }

    // ========== 상태 확인 메서드 ==========

    /**
     * 특정 키가 필수 필드인지 확인
     *
     * @param fieldKey 확인할 필드 키
     * @return 필수 필드면 true
     */
    public boolean isRequiredField(String fieldKey) {
        return requiredFields.stream().anyMatch(f -> f.key().equals(fieldKey));
    }

    /**
     * 특정 키가 선택 필드인지 확인
     *
     * @param fieldKey 확인할 필드 키
     * @return 선택 필드면 true
     */
    public boolean isOptionalField(String fieldKey) {
        return optionalFields.stream().anyMatch(f -> f.key().equals(fieldKey));
    }

    /**
     * 특정 키가 템플릿에 존재하는지 확인
     *
     * @param fieldKey 확인할 필드 키
     * @return 존재하면 true
     */
    public boolean containsField(String fieldKey) {
        return isRequiredField(fieldKey) || isOptionalField(fieldKey);
    }

    /**
     * 필수 필드가 있는지 확인
     *
     * @return 필수 필드가 1개 이상이면 true
     */
    public boolean hasRequiredFields() {
        return !requiredFields.isEmpty();
    }

    /**
     * 선택 필드가 있는지 확인
     *
     * @return 선택 필드가 1개 이상이면 true
     */
    public boolean hasOptionalFields() {
        return !optionalFields.isEmpty();
    }

    /**
     * 전체 필드 개수 반환
     *
     * @return 필수 필드 + 선택 필드 개수
     */
    public int getTotalFieldCount() {
        return requiredFields.size() + optionalFields.size();
    }

    /**
     * 필수 필드 키 목록 반환
     *
     * @return 필수 필드 키 Set
     */
    public Set<String> getRequiredFieldKeys() {
        return requiredFields.stream().map(NoticeField::key).collect(Collectors.toSet());
    }

    /**
     * ID 존재 여부 확인 (영속화 여부)
     *
     * @return ID가 있으면 true
     */
    public boolean hasId() {
        return id != null && !id.isNew();
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 템플릿 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 템플릿 ID Long 값, ID가 없으면 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    // ========== 검증 메서드 ==========

    private static void validateCreate(
            Long categoryId,
            String templateName,
            List<NoticeField> requiredFields,
            List<NoticeField> optionalFields) {
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("CategoryId is required and must be positive");
        }
        if (templateName == null || templateName.isBlank()) {
            throw new IllegalArgumentException("TemplateName is required");
        }
        if (templateName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    String.format(
                            "TemplateName length exceeds %d: %d",
                            MAX_NAME_LENGTH, templateName.length()));
        }

        List<NoticeField> safeRequired = requiredFields != null ? requiredFields : List.of();
        List<NoticeField> safeOptional = optionalFields != null ? optionalFields : List.of();
        validateNoDuplicateKeys(safeRequired, safeOptional);
    }

    private static void validateNoDuplicateKeys(
            List<NoticeField> requiredFields, List<NoticeField> optionalFields) {
        List<String> allKeys = new ArrayList<>();
        requiredFields.forEach(f -> allKeys.add(f.key()));
        optionalFields.forEach(f -> allKeys.add(f.key()));

        Set<String> uniqueKeys = Set.copyOf(allKeys);
        if (uniqueKeys.size() != allKeys.size()) {
            throw new IllegalArgumentException("중복된 필드 키가 존재합니다");
        }
    }

    private void validateNoDuplicateKey(
            String newKey, List<NoticeField> requiredFields, List<NoticeField> optionalFields) {
        boolean existsInRequired = requiredFields.stream().anyMatch(f -> f.key().equals(newKey));
        boolean existsInOptional = optionalFields.stream().anyMatch(f -> f.key().equals(newKey));
        if (existsInRequired || existsInOptional) {
            throw new IllegalArgumentException("이미 존재하는 필드 키입니다: " + newKey);
        }
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public NoticeTemplateId getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public List<NoticeField> getRequiredFields() {
        return requiredFields;
    }

    public List<NoticeField> getOptionalFields() {
        return optionalFields;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
