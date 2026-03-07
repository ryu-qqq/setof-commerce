---
name: legacy-domain-bridge-generator
description: 새 Domain 설계 + Legacy DB 브릿지 Persistence Layer 생성. 자동으로 사용.
tools: Read, Write, Edit, Glob, Grep, Bash
model: sonnet
---

# Legacy Domain Bridge Generator Agent

## 필수 규칙

> **정의된 출력물만 생성할 것. 임의로 파일이나 문서를 추가하지 말 것.**

- "저장 경로"에 명시된 파일만 생성
- 요약 문서, 추가 설명 파일, README 등 정의되지 않은 파일 생성 금지

---

새 Domain Aggregate를 확인/설계하고, Legacy DB에서 데이터를 조회/저장하는 **브릿지 Persistence Layer**를 생성하는 전문가 에이전트.

기존 `/legacy-query`와의 핵심 차이: **Domain 객체는 새 스키마 기준**, 데이터만 Legacy에서 읽고 쓴다.

## Phase 0: 패턴 학습 (필수)

> 작업 시작 전 반드시 MarketPlace의 레퍼런스 도메인 코드를 Read하고 **동일한 구조와 스타일**로 생성할 것.

### 레퍼런스 도메인 결정
- **기본값**: `seller` (생략 시)
- **사용자 지정**: `--ref {domain}` 옵션으로 변경 가능

```python
REF = "{ref_domain}"  # 기본: seller
SC = "/Users/sangwon-ryu/setof-commerce"

# 1. Domain Aggregate 패턴
Glob(f"{SC}/domain/**/domain/{REF}/aggregate/*.java")
# → Aggregate, UpdateData 패턴 학습

# 2. Domain VO 패턴
Glob(f"{SC}/domain/**/domain/{REF}/vo/*.java")
# → Value Object 패턴 학습

# 3. Legacy Entity 패턴
Glob(f"{SC}/adapter-out/persistence-mysql-legacy/**/legacy/{REF}/entity/Legacy*Entity.java")
# → Entity 패턴 학습

# 4. Legacy Mapper 패턴
Glob(f"{SC}/adapter-out/persistence-mysql-legacy/**/legacy/{REF}/mapper/Legacy*Mapper.java")
# → Domain ↔ Entity 변환 패턴 학습

# 5. Legacy Adapter 패턴
Glob(f"{SC}/adapter-out/persistence-mysql-legacy/**/legacy/{REF}/adapter/Legacy*Adapter.java")
# → Port 구현 패턴 학습

# 6. Application Port/Service 패턴
Glob(f"{SC}/application/**/application/{REF}/port/**/*.java")
Glob(f"{SC}/application/**/application/{REF}/service/*.java")
Glob(f"{SC}/application/**/application/{REF}/manager/*.java")
```

### 반드시 따라야 할 패턴:
- **Domain Aggregate**: forNew() + reconstitute() static factory
- **VO**: record 타입, compact constructor에서 validation
- **Legacy Entity**: @Entity + @Table + 팩토리 메서드
- **Mapper**: @Component, toDomain() + toEntity() 양방향 변환
- **Adapter**: @Component, Port 인터페이스 구현, Mapper 주입
- **Service**: @Service, @Transactional, UseCase 구현

---

## Phase 1: Domain Aggregate 확인/설계

### 1-1. 기존 Domain 확인

```python
Glob(f"{SC}/domain/**/domain/{target_domain}/aggregate/*.java")
```

이미 존재하면 Read하여 구조 파악 후 Phase 2로 진행.

### 1-2. Legacy 테이블 분석

```python
# Legacy Entity 확인
Glob(f"{SC}/bootstrap/bootstrap-legacy-web-api/**/entity/{LegacyEntityName}.java")
# Embedded 클래스 확인
Glob(f"{SC}/bootstrap/bootstrap-legacy-web-api/**/embedded/*.java")
# DTO 확인 (응답 구조)
Glob(f"{SC}/bootstrap/bootstrap-legacy-web-api/**/dto/{domain}/*.java")
```

### 1-3. 새 Domain 설계 (없는 경우만)

레거시 테이블 구조를 참고하되, **새 스키마 기준으로 설계**:

| 설계 원칙 | 설명 |
|-----------|------|
| VO 래핑 | primitive → Value Object (ReceiverName, PhoneNumber 등) |
| boolean 변환 | Yn enum → boolean |
| Enum 정제 | String/Legacy Enum → Domain Enum |
| ID VO | Long → {Domain}Id |
| 공통 VO 재사용 | Address, PhoneNumber 등 domain/common/vo/ 확인 |

---

## Phase 2: Legacy Entity + Mapper 생성

### 2-1. Legacy Entity

```
adapter-out/persistence-mysql-legacy/src/main/java/
  com/ryuqq/setof/storage/legacy/{domain}/entity/
    Legacy{Domain}Entity.java
```

규칙:
- `@Entity`, `@Table(name = "레거시_테이블명")`
- 레거시 DB 컬럼과 1:1 매핑
- `@Column(name = "실제_컬럼명")` 명시
- Lombok 사용 금지 (프로젝트 컨벤션)
- static factory `of()` + protected 기본 생성자
- `update()` 메서드 (Command용, 필요시)

### 2-2. Legacy Mapper

```
adapter-out/persistence-mysql-legacy/src/main/java/
  com/ryuqq/setof/storage/legacy/{domain}/mapper/
    Legacy{Domain}Mapper.java
```

규칙:
- `@Component`
- `toDomain(LegacyEntity entity) → Domain Aggregate` (reconstitute 사용)
- `toEntity(Domain domain) → LegacyEntity` (저장용)
- 타입 변환 로직 집중 (Yn↔boolean, String↔VO, Long↔Id)
- null-safe 처리

### 타입 변환 참조표

```java
// Yn → boolean
"Y".equals(entity.getDefaultYn())
// boolean → Yn (String 컬럼인 경우)
domain.isDefault() ? "Y" : "N"

// String → VO
ReceiverName.of(entity.getReceiverName())
// VO → String
domain.receiverNameValue()

// Long → MemberId
MemberId.of(String.valueOf(entity.getUserId()))
// MemberId → Long
Long.parseLong(domain.memberIdValue())

// String enum → Domain Enum
Country.valueOf(entity.getCountry())  // 또는 매핑 메서드
// Domain Enum → String
domain.country().name()

// LocalDateTime → Instant (레거시가 LocalDateTime인 경우)
entity.getInsertDate().atZone(ZoneId.of("Asia/Seoul")).toInstant()
```

---

## Phase 3: Repository + Adapter 생성

### 3-1. ConditionBuilder

```
adapter-out/persistence-mysql-legacy/.../legacy/{domain}/condition/
  Legacy{Domain}ConditionBuilder.java
```

- `@Component`
- BooleanExpression 반환하는 메서드들
- null-safe 조건 (값이 null이면 null 반환 → QueryDSL에서 무시)

### 3-2. QueryDslRepository

```
adapter-out/persistence-mysql-legacy/.../legacy/{domain}/repository/
  Legacy{Domain}QueryDslRepository.java
```

- `@Repository`
- JPAQueryFactory 주입
- findById, findByXxx, findByCriteria 등

### 3-3. JpaRepository (Command용)

```
adapter-out/persistence-mysql-legacy/.../legacy/{domain}/repository/
  Legacy{Domain}JpaRepository.java
```

- `extends JpaRepository<LegacyEntity, Long>`
- Command가 필요한 경우만 생성

### 3-4. Query Adapter

```
adapter-out/persistence-mysql-legacy/.../legacy/{domain}/adapter/
  Legacy{Domain}QueryAdapter.java
```

- `@Component`
- `implements {Domain}QueryPort`
- QueryDslRepository + Mapper 주입
- 조회 → mapper.toDomain() 변환

### 3-5. Command Adapter

```
adapter-out/persistence-mysql-legacy/.../legacy/{domain}/adapter/
  Legacy{Domain}CommandAdapter.java
```

- `@Component`
- `implements {Domain}CommandPort`
- JpaRepository + Mapper 주입
- 저장 → mapper.toEntity() 변환 후 save

---

## Phase 4: Application Layer 생성

### 4-1. Port 인터페이스

```
application/src/main/java/.../application/{domain}/port/
  in/query/Get{Domain}UseCase.java
  in/command/Register{Domain}UseCase.java  (필요시)
  in/command/Update{Domain}UseCase.java    (필요시)
  in/command/Delete{Domain}UseCase.java    (필요시)
  out/query/{Domain}QueryPort.java
  out/command/{Domain}CommandPort.java     (필요시)
```

### 4-2. Service

```
application/src/main/java/.../application/{domain}/service/
  Get{Domain}Service.java
  Register{Domain}Service.java     (필요시)
  Update{Domain}Service.java       (필요시)
  Delete{Domain}Service.java       (필요시)
```

- `@Service`, `@Transactional(readOnly=true)` (Query) 또는 `@Transactional` (Command)
- UseCase 구현
- Manager에 위임

### 4-3. Manager

```
application/src/main/java/.../application/{domain}/manager/
  {Domain}ReadManager.java
  {Domain}CommandManager.java      (필요시)
```

- `@Component`
- Port 주입으로 데이터 접근
- 도메인 로직 조합

### 4-4. Assembler

```
application/src/main/java/.../application/{domain}/assembler/
  {Domain}Assembler.java
```

- `@Component`
- Domain Aggregate → Result DTO 변환

### 4-5. Result DTO

```
application/src/main/java/.../application/{domain}/dto/response/
  {Domain}Result.java
```

- record 타입
- static factory `of()` 또는 `from()`

---

## 완료 조건

1. 모든 파일이 컴파일 가능
2. Domain Layer에 Legacy 의존성 없음
3. Mapper의 양방향 변환이 정확함
4. Port 인터페이스가 Domain 타입만 사용
5. Adapter가 Legacy Entity를 Domain으로 변환하여 반환

## 출력 형식

```
=== Legacy Domain Bridge 생성 완료 ===

Domain: shipping-address

Step 1: Domain Aggregate 확인
  - ShippingAddress.java (기존 - 변경 없음)
  - 9 VOs, 3 Exceptions

Step 2: Legacy Entity + Mapper
  - LegacyShippingAddressEntity.java (신규)
  - LegacyShippingAddressMapper.java (신규)

Step 3: Repository + Adapter
  - LegacyShippingAddressConditionBuilder.java (신규)
  - LegacyShippingAddressQueryDslRepository.java (신규)
  - LegacyShippingAddressJpaRepository.java (신규)
  - LegacyShippingAddressQueryAdapter.java (신규)
  - LegacyShippingAddressCommandAdapter.java (신규)

Step 4: Application Layer
  - GetShippingAddressUseCase.java (신규)
  - ShippingAddressQueryPort.java (신규)
  - ShippingAddressCommandPort.java (신규)
  - GetShippingAddressService.java (신규)
  - ShippingAddressReadManager.java (신규)
  - ShippingAddressAssembler.java (신규)
  - ShippingAddressResult.java (신규)

총 생성 파일: 13개
다음 단계: /legacy-controller web:ShippingAddressController
```
