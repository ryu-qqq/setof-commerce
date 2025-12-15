package com.ryuqq.setof.domain.productnotice.aggregate;

import com.ryuqq.setof.domain.productnotice.exception.RequiredNoticeFieldMissingException;
import com.ryuqq.setof.domain.productnotice.vo.NoticeItem;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import com.ryuqq.setof.domain.productnotice.vo.ProductNoticeId;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ProductNotice Aggregate Root
 *
 * <p>상품별 고시 데이터를 나타내는 도메인 엔티티입니다. 템플릿을 기반으로 실제 상품의 고시 정보를 저장합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드 + 방어적 복사
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 *   <li>Long FK 전략 - productGroupId는 Long 타입
 * </ul>
 */
@SuppressWarnings("PMD.DomainLayerDemeterStrict")
public class ProductNotice {

    private final ProductNoticeId id;
    private final Long productGroupId;
    private final NoticeTemplateId templateId;
    private final List<NoticeItem> items;
    private final Instant createdAt;
    private final Instant updatedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private ProductNotice(
            ProductNoticeId id,
            Long productGroupId,
            NoticeTemplateId templateId,
            List<NoticeItem> items,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.productGroupId = productGroupId;
        this.templateId = templateId;
        this.items = List.copyOf(items);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 신규 상품고시 생성용 Static Factory Method
     *
     * <p>ID 없이 신규 생성
     *
     * @param productGroupId 상품그룹 ID
     * @param templateId 템플릿 ID
     * @param items 고시 항목 목록
     * @param createdAt 생성일시
     * @return ProductNotice 인스턴스
     */
    public static ProductNotice create(
            Long productGroupId,
            NoticeTemplateId templateId,
            List<NoticeItem> items,
            Instant createdAt) {
        validateCreate(productGroupId, templateId, items);
        return new ProductNotice(
                ProductNoticeId.forNew(),
                productGroupId,
                templateId,
                items != null ? items : List.of(),
                createdAt,
                createdAt);
    }

    /**
     * 신규 상품고시 생성 + 필수 필드 검증
     *
     * <p>템플릿의 필수 필드가 모두 입력되었는지 검증합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @param templateId 템플릿 ID
     * @param items 고시 항목 목록
     * @param requiredFieldKeys 필수 필드 키 목록 (템플릿에서 가져옴)
     * @param createdAt 생성일시
     * @return ProductNotice 인스턴스
     * @throws RequiredNoticeFieldMissingException 필수 필드가 누락된 경우
     */
    public static ProductNotice createWithValidation(
            Long productGroupId,
            NoticeTemplateId templateId,
            List<NoticeItem> items,
            Set<String> requiredFieldKeys,
            Instant createdAt) {
        validateCreate(productGroupId, templateId, items);
        validateRequiredFields(items, requiredFieldKeys);
        return new ProductNotice(
                ProductNoticeId.forNew(),
                productGroupId,
                templateId,
                items != null ? items : List.of(),
                createdAt,
                createdAt);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 상품고시 ID
     * @param productGroupId 상품그룹 ID
     * @param templateId 템플릿 ID
     * @param items 고시 항목 목록
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return ProductNotice 인스턴스
     */
    public static ProductNotice reconstitute(
            ProductNoticeId id,
            Long productGroupId,
            NoticeTemplateId templateId,
            List<NoticeItem> items,
            Instant createdAt,
            Instant updatedAt) {
        return new ProductNotice(
                id,
                productGroupId,
                templateId,
                items != null ? items : List.of(),
                createdAt,
                updatedAt);
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 고시 항목 전체 교체
     *
     * @param newItems 새로운 고시 항목 목록
     * @param updatedAt 수정일시
     * @return 항목이 교체된 ProductNotice 인스턴스
     */
    public ProductNotice replaceItems(List<NoticeItem> newItems, Instant updatedAt) {
        return new ProductNotice(
                this.id,
                this.productGroupId,
                this.templateId,
                newItems != null ? newItems : List.of(),
                this.createdAt,
                updatedAt);
    }

    /**
     * 고시 항목 전체 교체 + 필수 필드 검증
     *
     * @param newItems 새로운 고시 항목 목록
     * @param requiredFieldKeys 필수 필드 키 목록
     * @param updatedAt 수정일시
     * @return 항목이 교체된 ProductNotice 인스턴스
     * @throws RequiredNoticeFieldMissingException 필수 필드가 누락된 경우
     */
    public ProductNotice replaceItemsWithValidation(
            List<NoticeItem> newItems, Set<String> requiredFieldKeys, Instant updatedAt) {
        validateRequiredFields(newItems, requiredFieldKeys);
        return replaceItems(newItems, updatedAt);
    }

    /**
     * 특정 필드 값 업데이트
     *
     * @param fieldKey 업데이트할 필드 키
     * @param newValue 새로운 값
     * @param updatedAt 수정일시
     * @return 값이 업데이트된 ProductNotice 인스턴스
     */
    public ProductNotice updateItemValue(String fieldKey, String newValue, Instant updatedAt) {
        if (fieldKey == null || fieldKey.isBlank()) {
            throw new IllegalArgumentException("필드 키는 필수입니다");
        }

        List<NoticeItem> updatedItems = new ArrayList<>();
        boolean found = false;

        for (NoticeItem item : this.items) {
            if (item.fieldKey().equals(fieldKey)) {
                updatedItems.add(item.updateValue(newValue));
                found = true;
            } else {
                updatedItems.add(item);
            }
        }

        if (!found) {
            throw new IllegalArgumentException("존재하지 않는 필드 키입니다: " + fieldKey);
        }

        return new ProductNotice(
                this.id,
                this.productGroupId,
                this.templateId,
                updatedItems,
                this.createdAt,
                updatedAt);
    }

    /**
     * 고시 항목 추가
     *
     * @param item 추가할 항목
     * @param updatedAt 수정일시
     * @return 항목이 추가된 ProductNotice 인스턴스
     */
    public ProductNotice addItem(NoticeItem item, Instant updatedAt) {
        if (item == null) {
            throw new IllegalArgumentException("추가할 항목은 필수입니다");
        }

        boolean exists = items.stream().anyMatch(i -> i.fieldKey().equals(item.fieldKey()));
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 필드 키입니다: " + item.fieldKey());
        }

        List<NoticeItem> newItems = new ArrayList<>(this.items);
        newItems.add(item);

        return new ProductNotice(
                this.id, this.productGroupId, this.templateId, newItems, this.createdAt, updatedAt);
    }

    /**
     * 고시 항목 제거
     *
     * @param fieldKey 제거할 필드 키
     * @param updatedAt 수정일시
     * @return 항목이 제거된 ProductNotice 인스턴스
     */
    public ProductNotice removeItem(String fieldKey, Instant updatedAt) {
        if (fieldKey == null || fieldKey.isBlank()) {
            throw new IllegalArgumentException("필드 키는 필수입니다");
        }

        List<NoticeItem> newItems =
                this.items.stream().filter(i -> !i.fieldKey().equals(fieldKey)).toList();

        return new ProductNotice(
                this.id, this.productGroupId, this.templateId, newItems, this.createdAt, updatedAt);
    }

    // ========== 상태 확인 메서드 ==========

    /**
     * 특정 필드 키의 항목 조회
     *
     * @param fieldKey 조회할 필드 키
     * @return NoticeItem Optional
     */
    public Optional<NoticeItem> findItemByKey(String fieldKey) {
        return items.stream().filter(i -> i.fieldKey().equals(fieldKey)).findFirst();
    }

    /**
     * 특정 필드 키의 값 조회
     *
     * @param fieldKey 조회할 필드 키
     * @return 필드 값 Optional
     */
    public Optional<String> getItemValue(String fieldKey) {
        return findItemByKey(fieldKey).map(NoticeItem::fieldValue);
    }

    /**
     * 특정 필드 키가 존재하는지 확인
     *
     * @param fieldKey 확인할 필드 키
     * @return 존재하면 true
     */
    public boolean containsItem(String fieldKey) {
        return items.stream().anyMatch(i -> i.fieldKey().equals(fieldKey));
    }

    /**
     * 고시 항목이 있는지 확인
     *
     * @return 항목이 1개 이상이면 true
     */
    public boolean hasItems() {
        return !items.isEmpty();
    }

    /**
     * 고시 항목 개수 반환
     *
     * @return 항목 개수
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * 모든 필드 키 목록 반환
     *
     * @return 필드 키 Set
     */
    public Set<String> getItemKeys() {
        return items.stream().map(NoticeItem::fieldKey).collect(Collectors.toSet());
    }

    /**
     * 값이 입력된 항목만 반환
     *
     * @return 값이 있는 항목 목록
     */
    public List<NoticeItem> getItemsWithValue() {
        return items.stream().filter(NoticeItem::hasValue).toList();
    }

    /**
     * 필수 필드 누락 여부 확인
     *
     * @param requiredFieldKeys 필수 필드 키 목록
     * @return 누락된 필수 필드 키 목록
     */
    public List<String> getMissingRequiredFields(Set<String> requiredFieldKeys) {
        Set<String> existingKeys = getItemKeys();
        return requiredFieldKeys.stream()
                .filter(key -> !existingKeys.contains(key) || !hasValueForKey(key))
                .toList();
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
     * 상품고시 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 상품고시 ID Long 값, ID가 없으면 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 템플릿 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 템플릿 ID Long 값
     */
    public Long getTemplateIdValue() {
        return templateId != null ? templateId.value() : null;
    }

    // ========== 내부 유틸리티 메서드 ==========

    private boolean hasValueForKey(String fieldKey) {
        return findItemByKey(fieldKey).map(NoticeItem::hasValue).orElse(false);
    }

    // ========== 검증 메서드 ==========

    private static void validateCreate(
            Long productGroupId, NoticeTemplateId templateId, List<NoticeItem> items) {
        if (productGroupId == null || productGroupId <= 0) {
            throw new IllegalArgumentException("ProductGroupId is required and must be positive");
        }
        if (templateId == null) {
            throw new IllegalArgumentException("TemplateId is required");
        }

        // 중복 필드 키 검증
        if (items != null && !items.isEmpty()) {
            Set<String> keys = items.stream().map(NoticeItem::fieldKey).collect(Collectors.toSet());
            if (keys.size() != items.size()) {
                throw new IllegalArgumentException("중복된 필드 키가 존재합니다");
            }
        }
    }

    private static void validateRequiredFields(
            List<NoticeItem> items, Set<String> requiredFieldKeys) {
        if (requiredFieldKeys == null || requiredFieldKeys.isEmpty()) {
            return;
        }

        Set<String> providedKeys =
                items != null
                        ? items.stream()
                                .filter(NoticeItem::hasValue)
                                .map(NoticeItem::fieldKey)
                                .collect(Collectors.toSet())
                        : Set.of();

        List<String> missingKeys =
                requiredFieldKeys.stream().filter(key -> !providedKeys.contains(key)).toList();

        if (!missingKeys.isEmpty()) {
            throw new RequiredNoticeFieldMissingException(missingKeys);
        }
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public ProductNoticeId getId() {
        return id;
    }

    public Long getProductGroupId() {
        return productGroupId;
    }

    public NoticeTemplateId getTemplateId() {
        return templateId;
    }

    public List<NoticeItem> getItems() {
        return items;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
