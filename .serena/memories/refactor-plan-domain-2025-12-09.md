# Refactoring Plan: Domain Layer

## 메타 정보
- **생성일**: 2025-12-09
- **대상 레이어**: Domain
- **Serena Memory 규칙 수**: 107개 (8개 파일)
- **ArchUnit 테스트 수**: 9개 테스트 클래스

---

## 이중 검증 결과

### Serena Memory 규칙 검증
- **총 규칙 수**: 107개
- **통과**: 101개
- **위반**: 6개 (WARNING 4개, INFO 2개)

### ArchUnit 테스트 검증
- **총 테스트 클래스**: 9개
- **결과**: ✅ **ALL PASSED** (BUILD SUCCESSFUL)

---

## ✅ 통과 항목 (Zero-Tolerance 규칙)

| 카테고리 | 규칙 | 상태 |
|----------|------|------|
| CMN-001 | Lombok 전면 금지 | ✅ 통과 |
| CMN-002 | JPA 전면 금지 | ✅ 통과 |
| CMN-003 | Spring 전면 금지 | ✅ 통과 |
| CMN-004 | Instant.now() 직접 호출 금지 | ✅ 통과 |
| CMN-005 | LocalDateTime 사용 금지 | ✅ 통과 (주석만 존재) |
| AGG-004 | Setter 금지 | ✅ 통과 |
| AGG-005 | private 생성자 필수 | ✅ 통과 |
| AGG-006 | forNew() 팩토리 메서드 필수 | ✅ 통과 |
| AGG-007 | of() 팩토리 메서드 필수 | ✅ 통과 |
| AGG-008 | reconstitute() 팩토리 메서드 필수 | ✅ 통과 |
| VO-001 | Record 타입 필수 | ✅ 통과 |
| VO-002 | of() 정적 팩토리 메서드 필수 | ✅ 통과 |
| EXC-009 | DomainException 상속 필수 | ✅ 통과 (17개 Exception) |
| EVT-001 | DomainEvent 인터페이스 구현 필수 | ✅ 통과 |
| EVT-002 | Record 타입 필수 | ✅ 통과 |
| EVT-003 | occurredAt 필드 필수 | ✅ 통과 |

---

## 🟡 Important 위반: 4개 (권장 사항)

### 1. EVT-004: Domain Event `from()` 메서드 누락
| 파일 | 현재 상태 | 권장 사항 |
|------|----------|----------|
| `MemberRegisteredEvent.java` | `of()` 사용 | `from(Member, Instant)` 추가 권장 |

**설명**: 규칙에서는 `from()` 팩토리 메서드를 권장하지만, 현재 `of()` 메서드 사용 중. 기능적으로 동일하나 Aggregate로부터 Event 생성 시 `from()` 패턴이 더 명확함.

### 2. VO-005: Enum VO `displayName()` 메서드 누락
| 파일 | 위치 | 영향 |
|------|------|------|
| `AuthProvider.java` | `member/vo/` | 한글 표시명 없음 |
| `Gender.java` | `member/vo/` | 한글 표시명 없음 |
| `MemberStatus.java` | `member/vo/` | 한글 표시명 없음 |
| `WithdrawalReason.java` | `member/vo/` | 한글 표시명 없음 |

**설명**: Enum VO에 `displayName()` 메서드가 없어 UI 표시 시 하드코딩 필요.

### 3. AGG-xxx: RefreshToken Aggregate `forNew()` 대신 `create()` 사용
| 파일 | 현재 메서드 | 권장 메서드 |
|------|------------|------------|
| `RefreshToken.java` | `create()` | `forNew()` |

**설명**: 규칙에서 신규 생성은 `forNew()`를 권장하지만 현재 `create()` 사용 중.

### 4. SortDirection `displayName()` 누락
| 파일 | 위치 | 영향 |
|------|------|------|
| `SortDirection.java` | `common/vo/` | 한글 표시명 없음 |

---

## 🟢 Recommended 위반: 2개 (참고 사항)

### 1. AGG-019/020: Domain Events 관리 권장
- **현재**: `Member` Aggregate만 Domain Events 관리
- **권장**: `RefreshToken`도 이벤트 발행 가능하도록 확장 고려

### 2. ClockHolder 주석 개선
- **위치**: `domain/common/util/ClockHolder.java`
- **내용**: 주석에 `LocalDateTime` 예시가 있음 (규칙 위반 아님, 주석일 뿐)
- **권장**: 주석을 `Instant` 예시로 업데이트

---

## 위반 상세 (Serena 검증)

### 🟡 Enum displayName() 누락 상세

```java
// ❌ 현재 상태
public enum AuthProvider {
    LOCAL,
    KAKAO
}

// ✅ 권장 구조
public enum AuthProvider {
    LOCAL("자체 가입"),
    KAKAO("카카오");

    private final String displayName;
    
    AuthProvider(String displayName) {
        this.displayName = displayName;
    }
    
    public String displayName() {
        return displayName;
    }
}
```

### 🟡 Domain Event from() 메서드 누락 상세

```java
// ❌ 현재 상태
public static MemberRegisteredEvent of(
    String memberId, String phoneNumber, String email, 
    String name, String authProvider, Instant registeredAt) {
    return new MemberRegisteredEvent(...);
}

// ✅ 권장 추가
public static MemberRegisteredEvent from(Member member, Instant occurredAt) {
    return new MemberRegisteredEvent(
        member.getIdValue(),
        member.getPhoneNumberValue(),
        member.getEmailValue(),
        member.getNameValue(),
        member.getProvider().name(),
        occurredAt
    );
}
```

---

## 리팩토링 우선순위

| 순위 | 항목 | 영향 파일 수 | Severity | 검증 방식 |
|------|------|-------------|----------|----------|
| 1 | Enum displayName() 추가 | 5개 | WARNING | Serena only |
| 2 | Domain Event from() 추가 | 1개 | WARNING | Serena only |
| 3 | RefreshToken forNew() 명명 | 1개 | WARNING | Serena only |
| 4 | ClockHolder 주석 개선 | 1개 | INFO | Serena only |

---

## 권장 수정 순서

1. **Enum VO displayName() 추가** (5개 파일)
   - `AuthProvider.java`
   - `Gender.java`
   - `MemberStatus.java`
   - `WithdrawalReason.java`
   - `SortDirection.java`

2. **MemberRegisteredEvent from() 메서드 추가** (1개 파일)
   - 기존 `of()` 유지하면서 `from(Member, Instant)` 추가

3. **RefreshToken 메서드명 변경** (1개 파일)
   - `create()` → `forNew()` 변경
   - 호출부 수정 필요

4. **ClockHolder 주석 개선** (선택사항)
   - 주석의 `LocalDateTime` 예시를 `Instant`로 변경

---

## 결론

### 🎉 Domain Layer 상태: **매우 양호**

- **Zero-Tolerance 규칙**: 전부 통과 ✅
- **ArchUnit 테스트**: 전부 통과 ✅
- **개선 필요 항목**: 6개 (모두 WARNING/INFO 레벨)

### 즉시 조치 필요 사항
없음 (모든 Critical 규칙 통과)

### 권장 개선 사항
1. Enum VO에 `displayName()` 메서드 추가 (UI 표시 목적)
2. Domain Event에 `from()` 팩토리 메서드 추가 (패턴 일관성)

---

**버전**: 1.0.0
**작성자**: Claude Code
**검증일**: 2025-12-09
