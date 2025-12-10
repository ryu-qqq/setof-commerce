# Domain Layer - Exception Rules

## ERRORCODE_ENUM (8 rules)

ErrorCode Enum 규칙

```json
{
  "category": "ERRORCODE_ENUM",
  "rules": [
    {
      "ruleId": "EXC-001",
      "name": "ErrorCode 인터페이스 구현 필수",
      "severity": "ERROR",
      "description": "ErrorCode Enum은 ErrorCode 인터페이스를 구현해야 함",
      "pattern": "enum\\s+\\w+ErrorCode\\s+implements\\s+ErrorCode",
      "archUnitTest": "ExceptionArchTest.errorCodeEnum_MustImplementInterface"
    },
    {
      "ruleId": "EXC-002",
      "name": "domain.[bc].exception 패키지 위치",
      "severity": "ERROR",
      "description": "ErrorCode Enum은 domain.[bc].exception 패키지에 위치",
      "pattern": "domain\\.\\w+\\.exception\\.\\w+ErrorCode",
      "archUnitTest": "ExceptionArchTest.errorCodeEnum_MustBeInCorrectPackage"
    },
    {
      "ruleId": "EXC-003",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "ErrorCode Enum에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(Getter|Setter|Data|RequiredArgsConstructor)",
      "archUnitTest": "ExceptionArchTest.errorCodeEnum_MustNotUseLombok"
    },
    {
      "ruleId": "EXC-004",
      "name": "public 접근 제어자 필수",
      "severity": "ERROR",
      "description": "ErrorCode Enum은 public 접근 제어자 필수",
      "pattern": "public\\s+enum\\s+\\w+ErrorCode",
      "archUnitTest": "ExceptionArchTest.errorCodeEnum_MustBePublic"
    },
    {
      "ruleId": "EXC-005",
      "name": "getCode() 메서드 필수",
      "severity": "ERROR",
      "description": "ErrorCode Enum은 getCode() 메서드 필수",
      "pattern": "String\\s+getCode\\(\\)",
      "archUnitTest": "ExceptionArchTest.errorCodeEnum_MustHaveGetCodeMethod"
    },
    {
      "ruleId": "EXC-006",
      "name": "getHttpStatus() 메서드 필수 (int 반환)",
      "severity": "ERROR",
      "description": "ErrorCode Enum은 getHttpStatus() 메서드 필수 (int 타입 반환)",
      "pattern": "int\\s+getHttpStatus\\(\\)",
      "archUnitTest": "ExceptionArchTest.errorCodeEnum_MustHaveGetHttpStatusMethod"
    },
    {
      "ruleId": "EXC-007",
      "name": "getMessage() 메서드 필수",
      "severity": "ERROR",
      "description": "ErrorCode Enum은 getMessage() 메서드 필수",
      "pattern": "String\\s+getMessage\\(\\)",
      "archUnitTest": "ExceptionArchTest.errorCodeEnum_MustHaveGetMessageMethod"
    },
    {
      "ruleId": "EXC-008",
      "name": "Spring HttpStatus 사용 금지",
      "severity": "ERROR",
      "description": "ErrorCode Enum에서 Spring HttpStatus 사용 금지, int 타입 사용",
      "antiPattern": "import\\s+org\\.springframework\\.http\\.HttpStatus|HttpStatus\\.",
      "archUnitTest": "ExceptionArchTest.errorCodeEnum_MustNotUseSpringHttpStatus"
    }
  ]
}
```

---

## CONCRETE_EXCEPTION (12 rules)

구체 Exception 클래스 규칙

```json
{
  "category": "CONCRETE_EXCEPTION",
  "rules": [
    {
      "ruleId": "EXC-009",
      "name": "DomainException 상속 필수",
      "severity": "ERROR",
      "description": "Concrete Exception은 DomainException을 상속해야 함",
      "pattern": "class\\s+\\w+Exception\\s+extends\\s+DomainException",
      "archUnitTest": "ExceptionArchTest.exception_MustExtendDomainException"
    },
    {
      "ruleId": "EXC-010",
      "name": "domain.[bc].exception 패키지 위치",
      "severity": "ERROR",
      "description": "Concrete Exception은 domain.[bc].exception 패키지에 위치",
      "pattern": "domain\\.\\w+\\.exception\\.\\w+Exception",
      "archUnitTest": "ExceptionArchTest.exception_MustBeInCorrectPackage"
    },
    {
      "ruleId": "EXC-011",
      "name": "Lombok 금지",
      "severity": "ERROR",
      "description": "Exception에서 Lombok 어노테이션 사용 금지",
      "antiPattern": "@(Getter|Setter|Data|Builder|RequiredArgsConstructor)",
      "archUnitTest": "ExceptionArchTest.exception_MustNotUseLombok"
    },
    {
      "ruleId": "EXC-012",
      "name": "JPA 금지",
      "severity": "ERROR",
      "description": "Exception에서 JPA 어노테이션 사용 금지",
      "antiPattern": "@(Entity|Table|Id|Column)",
      "archUnitTest": "ExceptionArchTest.exception_MustNotUseJpa"
    },
    {
      "ruleId": "EXC-013",
      "name": "Spring 금지",
      "severity": "ERROR",
      "description": "Exception에서 Spring 어노테이션 사용 금지",
      "antiPattern": "@(Component|Service|Repository|Transactional)",
      "archUnitTest": "ExceptionArchTest.exception_MustNotUseSpring"
    },
    {
      "ruleId": "EXC-014",
      "name": "public 클래스 필수",
      "severity": "ERROR",
      "description": "Exception은 public 클래스 필수",
      "pattern": "public\\s+class\\s+\\w+Exception",
      "archUnitTest": "ExceptionArchTest.exception_MustBePublic"
    },
    {
      "ruleId": "EXC-015",
      "name": "RuntimeException 계층",
      "severity": "ERROR",
      "description": "Exception은 RuntimeException 계층 (Unchecked Exception)",
      "pattern": "extends\\s+(DomainException|RuntimeException)",
      "antiPattern": "extends\\s+Exception(?!\\s)",
      "archUnitTest": "ExceptionArchTest.exception_MustBeUnchecked"
    },
    {
      "ruleId": "EXC-016",
      "name": "외부 레이어 의존 금지",
      "severity": "ERROR",
      "description": "Exception에서 Application/Adapter/Bootstrap 레이어 의존 금지",
      "antiPattern": "import\\s+.*\\.(application|adapter|bootstrap)\\.",
      "archUnitTest": "ExceptionArchTest.exception_MustNotDependOnOuterLayer"
    },
    {
      "ruleId": "EXC-017",
      "name": "Spring Framework 의존 금지",
      "severity": "ERROR",
      "description": "Exception에서 Spring Framework 의존 금지",
      "antiPattern": "import\\s+org\\.springframework\\.",
      "archUnitTest": "ExceptionArchTest.exception_MustNotDependOnSpring"
    },
    {
      "ruleId": "EXC-018",
      "name": "명확한 의미의 네이밍",
      "severity": "WARNING",
      "description": "Exception은 명확한 비즈니스 의미를 담은 네이밍 사용",
      "pattern": "(NotFound|Invalid|AlreadyExists|Cannot|Failed|Duplicate|Conflict|Forbidden|Unauthorized|Expired|Denied|Mismatch|Insufficient|Violation|State|NotEditable|Timeout|Unavailable|Missing|Empty)Exception"
    },
    {
      "ruleId": "EXC-019",
      "name": "DomainException은 common 패키지",
      "severity": "ERROR",
      "description": "DomainException 기본 클래스는 domain.common.exception 패키지에 위치",
      "pattern": "domain\\.common\\.exception\\.DomainException",
      "archUnitTest": "ExceptionArchTest.domainException_MustBeInCommonPackage"
    },
    {
      "ruleId": "EXC-020",
      "name": "ErrorCode 인터페이스는 common 패키지",
      "severity": "ERROR",
      "description": "ErrorCode 인터페이스는 domain.common.exception 패키지에 위치",
      "pattern": "domain\\.common\\.exception\\.ErrorCode",
      "archUnitTest": "ExceptionArchTest.errorCodeInterface_MustBeInCommonPackage"
    }
  ]
}
```

---

## 코드 예시

### ErrorCode 인터페이스 (common 패키지)

```java
// domain/common/exception/ErrorCode.java
public interface ErrorCode {
    String getCode();
    int getHttpStatus();    // int 타입 (Spring 의존 없음)
    String getMessage();
}
```

### ErrorCode Enum 구현

```java
// domain/order/exception/OrderErrorCode.java
public enum OrderErrorCode implements ErrorCode {
    ORDER_NOT_FOUND("ORD-001", 404, "주문을 찾을 수 없습니다"),
    ORDER_ALREADY_CANCELLED("ORD-002", 400, "이미 취소된 주문입니다"),
    ORDER_CANNOT_CANCEL("ORD-003", 400, "취소할 수 없는 주문입니다"),
    ORDER_INVALID_AMOUNT("ORD-004", 400, "주문 금액이 유효하지 않습니다"),
    ORDER_DUPLICATE("ORD-005", 409, "중복된 주문입니다");

    private final String code;
    private final int httpStatus;   // ✅ int 타입
    private final String message;

    OrderErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() { return code; }

    @Override
    public int getHttpStatus() { return httpStatus; }  // ✅ int 반환

    @Override
    public String getMessage() { return message; }
}
```

### DomainException 기본 클래스 (common 패키지)

```java
// domain/common/exception/DomainException.java
public abstract class DomainException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detail;

    protected DomainException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = null;
    }

    protected DomainException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage() + ": " + detail);
        this.errorCode = errorCode;
        this.detail = detail;
    }

    public ErrorCode getErrorCode() { return errorCode; }
    public String getDetail() { return detail; }
}
```

### Concrete Exception 구현

```java
// domain/order/exception/OrderNotFoundException.java
public class OrderNotFoundException extends DomainException {
    
    public OrderNotFoundException(OrderId orderId) {
        super(
            OrderErrorCode.ORDER_NOT_FOUND,
            String.format("주문 ID: %s", orderId.value())
        );
    }
}

// domain/order/exception/OrderCannotCancelException.java
public class OrderCannotCancelException extends DomainException {
    
    public OrderCannotCancelException(OrderId orderId, String reason) {
        super(
            OrderErrorCode.ORDER_CANNOT_CANCEL,
            String.format("주문 ID: %s, 사유: %s", orderId.value(), reason)
        );
    }
}
```

---

## Exception 네이밍 패턴

| 패턴 | 용도 | HTTP Status |
|------|------|-------------|
| `*NotFoundException` | 리소스 찾을 수 없음 | 404 |
| `*InvalidException` | 유효하지 않은 데이터 | 400 |
| `*AlreadyExistsException` | 이미 존재함 | 409 |
| `*CannotException` | 작업 수행 불가 | 400 |
| `*FailedException` | 작업 실패 | 500 |
| `*DuplicateException` | 중복 | 409 |
| `*ConflictException` | 충돌 | 409 |
| `*ForbiddenException` | 권한 없음 | 403 |
| `*UnauthorizedException` | 인증 실패 | 401 |
| `*ExpiredException` | 만료됨 | 400/410 |

---

## Exception 사용 가이드

### Aggregate 내부에서

```java
public class Order {
    // 불변조건 위반 (개발자 버그) → IllegalArgumentException
    private Order(OrderId id, ...) {
        if (id == null) {
            throw new IllegalArgumentException("OrderId is required");
        }
    }
    
    // 비즈니스 룰 위반 (정상적인 예외) → DomainException
    public void cancel(String reason, Instant now) {
        if (this.status == OrderStatus.SHIPPED) {
            throw new OrderCannotCancelException(this.id, "이미 배송된 주문");
        }
    }
}
```

---

## 관련 문서

- [Exception Guide](docs/coding_convention/02-domain-layer/exception/exception-guide.md)
- [Exception ArchUnit](docs/coding_convention/02-domain-layer/exception/exception-archunit-guide.md)
- [Exception Test Guide](docs/coding_convention/02-domain-layer/exception/exception-test-guide.md)

---

**버전**: 2.0.0 (JSON 구조로 변환)
**작성일**: 2025-12-09
