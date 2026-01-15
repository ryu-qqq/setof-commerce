---
description: 코드 리뷰 + 컨벤션 검토 전문가. CodeRabbit 결과 분석, Knowledge Base 대조.
tags: [review, quality]
activationCommands: ["/review"]
---

# Reviewer Skill

코드 리뷰와 컨벤션 검토를 담당하는 전문가 스킬입니다.

## 역할

1. **CodeRabbit 결과 분석**: Cursor 확장팩 리뷰 결과 해석
2. **컨벤션 대조**: Knowledge Base 규칙과 비교
3. **개선 제안**: 자동 수정 가능 항목 식별
4. **리팩토링 가이드**: 코드 품질 개선 방향 제시

## 활성화 시점

- `/review` 커맨드 실행 시
- 코드 리뷰 요청 시

## 리뷰 체크리스트

### 1. Zero-Tolerance 규칙 (필수)

| 규칙 | 검증 방법 |
|------|----------|
| Lombok 금지 | `import lombok` 검색 |
| Getter 체이닝 금지 | `().get` 패턴 검색 |
| JPA 관계 어노테이션 금지 | `@ManyToOne`, `@OneToMany` 검색 |
| Transaction 내 외부 API 금지 | `@Transactional` 메서드 내 외부 호출 검색 |

### 2. Domain Layer 규칙

```markdown
□ Aggregate Root 식별 가능한가?
□ Value Object는 불변인가?
□ 비즈니스 로직이 Domain에 있는가? (Tell, Don't Ask)
□ Domain Event가 필요한 곳에 있는가?
□ 외부 프레임워크 의존성이 없는가?
```

### 3. Application Layer 규칙

```markdown
□ UseCase가 단일 책임을 가지는가?
□ Command/Query가 분리되어 있는가?
□ DTO가 Record로 정의되어 있는가?
□ Transaction 경계가 적절한가?
□ Assembler로 변환이 처리되는가?
```

### 4. Persistence Layer 규칙

```markdown
□ Long FK 전략을 사용하는가?
□ Entity가 BaseAuditEntity를 상속하는가?
□ QueryDSL DTO Projection을 사용하는가?
□ Repository가 명확히 분리되어 있는가?
```

### 5. REST API Layer 규칙

```markdown
□ RESTful URL 설계인가?
□ Request/Response DTO가 분리되어 있는가?
□ @Valid 어노테이션이 있는가?
□ ApiResponse로 래핑되어 있는가?
□ 테스트가 TestRestTemplate을 사용하는가?
```

## 리뷰 결과 형식

### 요약 보고서

```markdown
## 🔍 코드 리뷰 결과

### 📊 전체 요약
- 검토 파일: 5개
- 변경 라인: +234 / -56
- 발견 이슈: 3건 (필수 1, 권장 2)

### 🔴 필수 수정 (Zero-Tolerance 위반)

#### 1. Order.java:67 - Law of Demeter 위반
```java
// 현재 코드
String city = order.getCustomer().getAddress().getCity();

// 수정 제안
String city = order.getShippingCity();
```
**사유**: Getter 체이닝은 캡슐화를 깨뜨립니다.
**자동 수정**: 불가능 (도메인 메서드 추가 필요)

### 🟡 권장 수정

#### 1. OrderService.java:45 - 메서드 크기
- 현재: 35줄
- 권장: 20줄 이하
- 제안: private 메서드로 분리

#### 2. OrderTest.java:89 - 테스트 케이스 누락
- 현재: 정상 케이스만 테스트
- 제안: 예외 케이스 추가

### 🟢 잘된 점
- CQRS 패턴 잘 적용됨
- DTO Record 사용 적절함
- Transaction 경계 명확함
```

## CodeRabbit 연동

### Cursor 확장팩 명령어

```
# 전체 리뷰 요청
@coderabbitai review

# 특정 파일 리뷰
@coderabbitai review src/main/java/.../Order.java

# 보안 중점 리뷰
@coderabbitai review --focus security

# 성능 중점 리뷰
@coderabbitai review --focus performance
```

### CodeRabbit 결과 파싱

```markdown
## CodeRabbit 피드백 분석

| 파일 | 라인 | 심각도 | 카테고리 | 내용 |
|------|------|--------|----------|------|
| Order.java | 67 | High | Bug | NPE 가능성 |
| OrderService.java | 23 | Medium | Style | 중복 코드 |
| OrderTest.java | 45 | Low | Test | 커버리지 부족 |

### 컨벤션 대조 결과
- NPE 가능성 → Zero-Tolerance (null 체크 필수)
- 중복 코드 → DRY 원칙 위반
- 커버리지 부족 → 테스트 규칙 위반
```

## 자동 수정 지원

### 수정 가능 항목

| 유형 | 자동 수정 | 방법 |
|------|----------|------|
| Import 정리 | ✅ | Spotless |
| 포맷팅 | ✅ | Spotless |
| 미사용 변수 | ⚠️ | 수동 확인 필요 |
| Getter 체이닝 | ❌ | 도메인 메서드 추가 필요 |

### 자동 수정 실행

```bash
# Spotless 자동 포맷팅
./gradlew spotlessApply

# Import 정리 + 포맷팅
./gradlew spotlessApply --quiet
```

## Knowledge Base 참조

리뷰 시 참조할 규칙 파일:

```bash
@knowledge/rules/zero-tolerance.md     # 212개 필수 규칙
@knowledge/rules/domain-rules.md       # Domain 규칙
@knowledge/rules/application-rules.md  # Application 규칙
@knowledge/rules/persistence-rules.md  # Persistence 규칙
@knowledge/rules/rest-api-rules.md     # REST API 규칙
```

## 관련 스킬

- **implementer**: 리뷰 결과 반영
- **tester**: 테스트 커버리지 검토
- **shipper**: 리뷰 완료 후 배포
