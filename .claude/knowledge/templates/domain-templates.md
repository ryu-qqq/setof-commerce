# DOMAIN Layer 클래스 템플릿 (16개)

## 개요

이 문서는 DOMAIN Layer에서 사용하는 클래스 템플릿을 정의합니다.

## 템플릿 목록

| Class Type | Naming Pattern | Description |
|------------|----------------|-------------|
| AGGREGATE_ROOT | `{Entity}` | Aggregate Root 템플릿. forNew()/reconstitute() 팩토리 메서... |
| COMPOSITE_VO | `{Name} (Composite)` | 복합 값 Value Object 템플릿. 여러 필드를 가진 불변 객체. |
| CONCRETE_EXCEPTION | `{Entity}NotFoundException` | Concrete Exception 템플릿. DomainException 상속, 특정 Err... |
| DOMAIN_EVENT | `{Entity}CreatedEvent` | Domain Event 구체 클래스 템플릿. from(Aggregate, Instant) ... |
| DOMAIN_EVENT_INTERFACE | `DomainEvent` | Domain Event 공통 인터페이스. 모든 도메인 이벤트가 구현. |
| DOMAIN_EXCEPTION_BASE | `DomainException` | Domain Exception 기본 클래스. RuntimeException 상속, Erro... |
| ENUM_VO | `{Name}Status` | Enum Value Object 템플릿. displayName() 메서드 필수. |
| ERROR_CODE_ENUM | `{Entity}ErrorCode` | Bounded Context별 ErrorCode Enum. int 타입 HTTP 상태 코드... |
| ERROR_CODE_INTERFACE | `ErrorCode` | ErrorCode 공통 인터페이스. 모든 ErrorCode Enum이 구현. |
| GET_CRITERIA | `{Entity}GetCriteria` | 단건 조회 Criteria 템플릿. ID 기반 조회. |
| LONG_ID_VO | `{Entity}Id` | Long 기반 ID Value Object. Auto Increment 대응. forNew... |
| MONEY_VO | `Money` | Money Value Object 템플릿. 금액 계산 로직 포함. |
| SEARCH_CRITERIA | `{Entity}SearchCriteria` | 검색 조건 Criteria 템플릿. 페이징, 정렬, 필터 조건 포함. |
| SINGLE_VALUE_VO | `{Name}` | 단일 값 Value Object 템플릿. Compact Constructor에서 검증 수행... |
| STRING_ID_VO | `{Entity}Id (String)` | String 기반 ID Value Object. UUIDv7/Snowflake 등 외부 생... |
| UPDATE_DATA | `{Entity}UpdateData` | Aggregate 부분 업데이트를 위한 UpdateData Record. null이면 변경... |

---

## 상세 템플릿

### AGGREGATE_ROOT

**설명**: Aggregate Root 템플릿. forNew()/reconstitute() 팩토리 메서드, 도메인 이벤트 관리, 불변식 검증 포함.

**네이밍 패턴**: `{Entity}`

**템플릿 코드**:
```java
package com.ryuqq.domain.{bc}.aggregate;

import com.ryuqq.domain.{bc}.id.{Entity}Id;
import com.ryuqq.domain.{bc}.event.{Entity}CreatedEvent;
import com.ryuqq.domain.common.event.DomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * {Entity} Aggregate Root.
 *
 * <p>비즈니스 불변식:
 * <ul>
 *   <li>생성 시 필수 필드 검증</li>
 *   <li>상태 전이 규칙 준수</li>
 * </ul>
 */
public class {Entity} {

    private final {Entity}Id id;
    private String name;
    // TODO: 비즈니스 필드 추가
    private final Instant createdAt;
    private Instant updatedAt;

    private final List<DomainEvent> events = new ArrayList<>();

    /**
     * 새로운 {Entity} 생성을 위한 팩토리 메서드.
     *
     * @param name 이름
     * @param now 생성 시점
     * @return 새로운 {Entity} 인스턴스
     */
    public static {Entity} forNew(String name, Instant now) {
        validateForNew(name);
        {Entity} entity = new {Entity}(
            {Entity}Id.forNew(),
            name,
            now,
            now
        );
        entity.registerEvent({Entity}CreatedEvent.from(entity, now));
        return entity;
    }

    /**
     * DB에서 복원하기 위한 팩토리 메서드.
     *
     * @param id ID
     * @param name 이름
     * @param createdAt 생성 시점
     * @param updatedAt 수정 시점
     * @return 복원된 {Entity} 인스턴스
     */
    public static {Entity} reconstitute(
            {Entity}Id id,
            String name,
            Instant createdAt,
            Instant updatedAt) {
        return new {Entity}(id, name, createdAt, updatedAt);
    }

    private {Entity}(
            {Entity}Id id,
            String name,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    protected {Entity}() {
        this.id = null;
        this.name = null;
        this.createdAt = null;
        this.updatedAt = null;
    }

    private static void validateForNew(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }

    // ============================================================
    // Command Methods (상태 변경)
    // ============================================================

    /**
     * 이름을 변경합니다.
     *
     * @param newName 새로운 이름
     * @param now 변경 시점
     */
    public void updateName(String newName, Instant now) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("newName must not be blank");
        }
        this.name = newName;
        this.updatedAt = now;
    }

    // ============================================================
    // Query Methods (상태 조회)
    // ============================================================

    public {Entity}Id id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public boolean isNew() {
        return id.isNew();
    }

    // ============================================================
    // Domain Event
    // ============================================================

    protected void registerEvent(DomainEvent event) {
        this.events.add(event);
    }

    public List<DomainEvent> pollEvents() {
        List<DomainEvent> polled = new ArrayList<>(this.events);
        this.events.clear();
        return polled;
    }

    // ============================================================
    // equals/hashCode (ID 기반)
    // ============================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        {Entity} that = ({Entity}) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
```

---

### COMPOSITE_VO

**설명**: 복합 값 Value Object 템플릿. 여러 필드를 가진 불변 객체.

**네이밍 패턴**: `{Name} (Composite)`

**템플릿 코드**:
```java
package com.ryuqq.domain.{bc}.vo;

/**
 * {Name} Value Object.
 *
 * <p>복합 값 불변 객체. 관련 값들을 하나로 묶습니다.
 *
 * @param field1 필드1
 * @param field2 필드2
 */
public record {Name}(
    String field1,
    String field2
) {

    public {Name} {
        if (field1 == null || field1.isBlank()) {
            throw new IllegalArgumentException("field1 must not be blank");
        }
        if (field2 == null || field2.isBlank()) {
            throw new IllegalArgumentException("field2 must not be blank");
        }
    }

    /**
     * {Name} 생성 팩토리 메서드.
     *
     * @param field1 필드1
     * @param field2 필드2
     * @return {Name} 인스턴스
     */
    public static {Name} of(String field1, String field2) {
        return new {Name}(field1, field2);
    }
}
```

---

### CONCRETE_EXCEPTION

**설명**: Concrete Exception 템플릿. DomainException 상속, 특정 ErrorCode와 연결.

**네이밍 패턴**: `{Entity}NotFoundException`

**템플릿 코드**:
```java
package com.ryuqq.domain.{bc}.exception;

import com.ryuqq.domain.common.exception.DomainException;
import com.ryuqq.domain.{bc}.id.{Entity}Id;

/**
 * {Entity}를 찾을 수 없을 때 발생하는 예외.
 */
public class {Entity}NotFoundException extends DomainException {

    private final {Entity}Id {entity}Id;

    public {Entity}NotFoundException({Entity}Id {entity}Id) {
        super({Entity}ErrorCode.{ENTITY}_NOT_FOUND,
            String.format("{Entity} not found: %s", {entity}Id.value()));
        this.{entity}Id = {entity}Id;
    }

    public {Entity}Id get{Entity}Id() {
        return {entity}Id;
    }
}
```

---

### DOMAIN_EVENT

**설명**: Domain Event 구체 클래스 템플릿. from(Aggregate, Instant) 팩토리 메서드 필수.

**네이밍 패턴**: `{Entity}CreatedEvent`

**템플릿 코드**:
```java
package com.ryuqq.domain.{bc}.event;

import com.ryuqq.domain.{bc}.aggregate.{Entity};
import com.ryuqq.domain.{bc}.id.{Entity}Id;
import com.ryuqq.domain.common.event.DomainEvent;

import java.time.Instant;

/**
 * {Entity} 생성 이벤트.
 *
 * <p>새로운 {Entity}가 생성되었을 때 발행됩니다.
 *
 * @param {entity}Id 생성된 {Entity} ID
 * @param name 이름
 * @param occurredAt 이벤트 발생 시점
 */
public record {Entity}CreatedEvent(
    {Entity}Id {entity}Id,
    String name,
    Instant occurredAt
) implements DomainEvent {

    /**
     * Aggregate로부터 이벤트 생성.
     *
     * @param entity 생성된 Aggregate
     * @param now 현재 시점
     * @return 생성 이벤트
     */
    public static {Entity}CreatedEvent from({Entity} entity, Instant now) {
        return new {Entity}CreatedEvent(
            entity.id(),
            entity.name(),
            now
        );
    }
}
```

---

### DOMAIN_EVENT_INTERFACE

**설명**: Domain Event 공통 인터페이스. 모든 도메인 이벤트가 구현.

**네이밍 패턴**: `DomainEvent`

**템플릿 코드**:
```java
package com.ryuqq.domain.common.event;

import java.time.Instant;

/**
 * Domain Event 공통 인터페이스.
 *
 * <p>모든 도메인 이벤트는 이 인터페이스를 구현해야 합니다.
 */
public interface DomainEvent {

    /**
     * 이벤트 발생 시점.
     *
     * @return 발생 시점
     */
    Instant occurredAt();
}
```

---

### DOMAIN_EXCEPTION_BASE

**설명**: Domain Exception 기본 클래스. RuntimeException 상속, ErrorCode 연동.

**네이밍 패턴**: `DomainException`

**템플릿 코드**:
```java
package com.ryuqq.domain.common.exception;

/**
 * Domain Exception 기본 클래스.
 *
 * <p>모든 도메인 예외는 이 클래스를 상속해야 합니다.
 */
public class DomainException extends RuntimeException {

    private final ErrorCode errorCode;

    public DomainException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DomainException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DomainException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getCode() {
        return errorCode.getCode();
    }

    public int getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
```

---

### ENUM_VO

**설명**: Enum Value Object 템플릿. displayName() 메서드 필수.

**네이밍 패턴**: `{Name}Status`

**템플릿 코드**:
```java
package com.ryuqq.domain.{bc}.vo;

/**
 * {Name} 상태 Enum.
 *
 * <p>상태 전이 규칙을 포함합니다.
 */
public enum {Name}Status {

    PENDING("대기중"),
    ACTIVE("활성"),
    COMPLETED("완료"),
    CANCELLED("취소됨");

    private final String displayName;

    {Name}Status(String displayName) {
        this.displayName = displayName;
    }

    /**
     * UI 표시용 이름.
     *
     * @return 한글 표시명
     */
    public String displayName() {
        return displayName;
    }

    /**
     * 활성 상태 여부.
     */
    public boolean isActive() {
        return this == ACTIVE;
    }

    /**
     * 종료 상태 여부.
     */
    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }
}
```

---

### ERROR_CODE_ENUM

**설명**: Bounded Context별 ErrorCode Enum. int 타입 HTTP 상태 코드 사용 (Spring HttpStatus 금지).

**네이밍 패턴**: `{Entity}ErrorCode`

**템플릿 코드**:
```java
package com.ryuqq.domain.{bc}.exception;

import com.ryuqq.domain.common.exception.ErrorCode;

/**
 * {Entity} 관련 에러 코드.
 */
public enum {Entity}ErrorCode implements ErrorCode {

    // 400 Bad Request
    INVALID_{ENTITY}_NAME("{ENTITY}_INVALID_NAME", 400, "잘못된 {Entity} 이름입니다"),

    // 404 Not Found
    {ENTITY}_NOT_FOUND("{ENTITY}_NOT_FOUND", 404, "{Entity}를 찾을 수 없습니다"),

    // 409 Conflict
    DUPLICATE_{ENTITY}("{ENTITY}_DUPLICATE", 409, "이미 존재하는 {Entity}입니다"),
    {ENTITY}_ALREADY_CANCELLED("{ENTITY}_ALREADY_CANCELLED", 409, "이미 취소된 {Entity}입니다"),

    // 422 Unprocessable Entity
    CANNOT_CANCEL_{ENTITY}("{ENTITY}_CANNOT_CANCEL", 422, "취소할 수 없는 상태입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    {Entity}ErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
```

---

### ERROR_CODE_INTERFACE

**설명**: ErrorCode 공통 인터페이스. 모든 ErrorCode Enum이 구현.

**네이밍 패턴**: `ErrorCode`

**템플릿 코드**:
```java
package com.ryuqq.domain.common.exception;

/**
 * ErrorCode 공통 인터페이스.
 *
 * <p>모든 ErrorCode Enum은 이 인터페이스를 구현해야 합니다.
 */
public interface ErrorCode {

    /**
     * API 응답용 에러 코드.
     *
     * @return 에러 코드 (예: "ORDER_NOT_FOUND")
     */
    String getCode();

    /**
     * HTTP 상태 코드.
     *
     * @return HTTP 상태 코드 (int)
     */
    int getHttpStatus();

    /**
     * 사용자용 에러 메시지.
     *
     * @return 에러 메시지
     */
    String getMessage();
}
```

---

### GET_CRITERIA

**설명**: 단건 조회 Criteria 템플릿. ID 기반 조회.

**네이밍 패턴**: `{Entity}GetCriteria`

**템플릿 코드**:
```java
package com.ryuqq.domain.{bc}.query.criteria;

import com.ryuqq.domain.{bc}.id.{Entity}Id;

/**
 * {Entity} 단건 조회 조건.
 *
 * @param {entity}Id 조회할 {Entity} ID
 */
public record {Entity}GetCriteria(
    {Entity}Id {entity}Id
) {

    public {Entity}GetCriteria {
        if ({entity}Id == null) {
            throw new IllegalArgumentException("{entity}Id must not be null");
        }
    }

    /**
     * 조회 조건 생성.
     *
     * @param {entity}Id {Entity} ID
     * @return 조회 조건
     */
    public static {Entity}GetCriteria of({Entity}Id {entity}Id) {
        return new {Entity}GetCriteria({entity}Id);
    }
}
```

---

### LONG_ID_VO

**설명**: Long 기반 ID Value Object. Auto Increment 대응. forNew()는 null 반환, isNew()로 신규 판단.

**네이밍 패턴**: `{Entity}Id`

**템플릿 코드**:
```java
package com.ryuqq.domain.{bc}.id;

/**
 * {Entity} ID Value Object.
 *
 * <p>Long 기반 Auto Increment ID.
 * forNew()는 null을 반환하여 JPA persist 시점에 ID 할당.
 *
 * @param value ID 값 (null이면 신규)
 */
public record {Entity}Id(Long value) {

    /**
     * 새로운 ID 생성 (Auto Increment용).
     * ID는 null로, persist 시점에 DB에서 할당됩니다.
     *
     * @return 신규 ID
     */
    public static {Entity}Id forNew() {
        return new {Entity}Id(null);
    }

    /**
     * 기존 ID 복원.
     *
     * @param value ID 값
     * @return 복원된 ID
     */
    public static {Entity}Id of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("{Entity}Id value must not be null for existing entity");
        }
        return new {Entity}Id(value);
    }

    /**
     * 신규 생성 여부.
     *
     * @return ID가 null이면 true
     */
    public boolean isNew() {
        return value == null;
    }
}
```

---

### MONEY_VO

**설명**: Money Value Object 템플릿. 금액 계산 로직 포함.

**네이밍 패턴**: `Money`

**템플릿 코드**:
```java
package com.ryuqq.domain.common.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Money Value Object.
 *
 * <p>금액을 나타내는 불변 객체. 통화 연산을 제공합니다.
 *
 * @param amount 금액
 */
public record Money(BigDecimal amount) {

    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("amount must not be null");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Money 생성 팩토리 메서드.
     *
     * @param amount 금액
     * @return Money 인스턴스
     */
    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    /**
     * Money 생성 (long).
     *
     * @param amount 금액
     * @return Money 인스턴스
     */
    public static Money of(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    /**
     * 0원.
     */
    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    /**
     * 더하기.
     */
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    /**
     * 빼기.
     */
    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }

    /**
     * 곱하기.
     */
    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)));
    }

    /**
     * 0보다 큰지 확인.
     */
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 0인지 확인.
     */
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }
}
```

---

### SEARCH_CRITERIA

**설명**: 검색 조건 Criteria 템플릿. 페이징, 정렬, 필터 조건 포함.

**네이밍 패턴**: `{Entity}SearchCriteria`

**템플릿 코드**:
```java
package com.ryuqq.domain.{bc}.query.criteria;

import com.ryuqq.domain.common.vo.PageRequest;
import com.ryuqq.domain.common.vo.SortDirection;

/**
 * {Entity} 검색 조건.
 *
 * @param name 이름 필터 (null이면 전체)
 * @param status 상태 필터 (null이면 전체)
 * @param page 페이지 정보
 * @param sortDirection 정렬 방향
 */
public record {Entity}SearchCriteria(
    String name,
    String status,
    PageRequest page,
    SortDirection sortDirection
) {

    /**
     * 검색 조건 생성.
     *
     * @param name 이름 필터
     * @param status 상태 필터
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param sortDirection 정렬 방향
     * @return 검색 조건
     */
    public static {Entity}SearchCriteria of(
            String name,
            String status,
            int page,
            int size,
            SortDirection sortDirection) {
        return new {Entity}SearchCriteria(
            name,
            status,
            PageRequest.of(page, size),
            sortDirection != null ? sortDirection : SortDirection.DESC
        );
    }

    /**
     * 기본 검색 조건 (전체 조회, 첫 페이지).
     */
    public static {Entity}SearchCriteria defaultCriteria() {
        return of(null, null, 0, 20, SortDirection.DESC);
    }

    /**
     * 이름 필터 존재 여부.
     */
    public boolean hasNameFilter() {
        return name != null && !name.isBlank();
    }

    /**
     * 상태 필터 존재 여부.
     */
    public boolean hasStatusFilter() {
        return status != null && !status.isBlank();
    }
}
```

---

### SINGLE_VALUE_VO

**설명**: 단일 값 Value Object 템플릿. Compact Constructor에서 검증 수행.

**네이밍 패턴**: `{Name}`

**템플릿 코드**:
```java
package com.ryuqq.domain.{bc}.vo;

/**
 * {Name} Value Object.
 *
 * <p>단일 값 불변 객체. 생성 시 검증을 수행합니다.
 *
 * @param value 값
 */
public record {Name}(String value) {

    public {Name} {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("{Name} value must not be blank");
        }
        // TODO: 추가 검증 (형식, 길이 등)
    }

    /**
     * {Name} 생성 팩토리 메서드.
     *
     * @param value 값
     * @return {Name} 인스턴스
     */
    public static {Name} of(String value) {
        return new {Name}(value);
    }
}
```

---

### STRING_ID_VO

**설명**: String 기반 ID Value Object. UUIDv7/Snowflake 등 외부 생성 ID용. forNew(String)로 외부 주입.

**네이밍 패턴**: `{Entity}Id (String)`

**템플릿 코드**:
```java
package com.ryuqq.domain.{bc}.id;

/**
 * {Entity} ID Value Object.
 *
 * <p>String 기반 ID (UUIDv7, Snowflake 등).
 * ID는 Application Layer에서 생성하여 주입합니다.
 *
 * @param value ID 값
 */
public record {Entity}Id(String value) {

    public {Entity}Id {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("{Entity}Id value must not be blank");
        }
    }

    /**
     * 새로운 ID 생성 (외부 주입).
     * Application Layer의 ID Generator에서 생성된 값을 받습니다.
     *
     * @param value 외부에서 생성된 ID 값 (UUIDv7, Snowflake 등)
     * @return 신규 ID
     */
    public static {Entity}Id forNew(String value) {
        return new {Entity}Id(value);
    }

    /**
     * 기존 ID 복원.
     *
     * @param value ID 값
     * @return 복원된 ID
     */
    public static {Entity}Id of(String value) {
        return new {Entity}Id(value);
    }

    // NOTE: String ID는 isNew() 메서드를 가지면 안 됩니다.
    // String ID는 생성 시점에 이미 값이 존재하므로 신규 여부를 ID로 판단할 수 없습니다.
}
```

---

### UPDATE_DATA

**설명**: Aggregate 부분 업데이트를 위한 UpdateData Record. null이면 변경하지 않음 의미.

**네이밍 패턴**: `{Entity}UpdateData`

**템플릿 코드**:
```java
package com.ryuqq.domain.{bc}.aggregate;

import java.util.Optional;

/**
 * {Entity} 부분 업데이트를 위한 데이터 객체.
 *
 * <p>null이면 해당 필드를 변경하지 않음을 의미합니다.
 *
 * @param name 변경할 이름 (null이면 변경하지 않음)
 */
public record {Entity}UpdateData(
    String name
    // TODO: 업데이트 가능한 필드 추가
) {

    /**
     * UpdateData 생성 팩토리 메서드.
     *
     * @param name 변경할 이름
     * @return UpdateData 인스턴스
     */
    public static {Entity}UpdateData of(String name) {
        return new {Entity}UpdateData(name);
    }

    /**
     * 이름 변경 여부.
     */
    public boolean hasName() {
        return name != null;
    }

    /**
     * 이름 Optional 반환.
     */
    public Optional<String> nameOptional() {
        return Optional.ofNullable(name);
    }
}
```

---

