---
name: legacy-domain-bridge
description: 새 Domain 설계 + Legacy DB 브릿지. 새 스키마 기준 Domain Aggregate 설계 후 Legacy Entity에서 데이터 조회/저장.
context: fork
agent: legacy-domain-bridge-generator
allowed-tools: Read, Write, Edit, Glob, Grep, Bash
---

# /legacy-domain-bridge

**새 도메인 객체를 설계**하고, 레거시 DB에서 데이터를 조회/저장하는 **브릿지 Persistence Layer**를 생성합니다.

기존 `/legacy-query`와의 차이:
- `/legacy-query`: 레거시 구조를 **그대로** 사용하여 Persistence 생성
- `/legacy-domain-bridge`: **새 스키마 기준 Domain 설계** + 레거시 데이터 브릿지 (Mapper로 변환)

## 사용법

```bash
/legacy-domain-bridge web:shipping-address          # 전체 브릿지 생성
/legacy-domain-bridge web:shipping-address --step 2  # 특정 단계부터
/legacy-domain-bridge web:refund-account --ref shipping-address  # 레퍼런스
```

## 입력

- `web:shipping-address`: 대상 (prefix:domain)
- `--step N`: (선택) N단계부터 재개
- `--ref {domain}`: (선택) 레퍼런스 도메인 (이미 완성된 브릿지 패턴 참고)

---

## 전제조건

1. `/legacy-flow` 분석 문서가 존재해야 함 (`claudedocs/legacy-flows/`)
2. Domain Layer의 Aggregate가 이미 설계되어 있어야 함 (또는 Step 1에서 신규 설계)
3. 레거시 테이블 구조를 알아야 함 (Entity 또는 DB 스키마)

---

## 워크플로우 (4단계)

```
Step 1: Domain Aggregate 확인/설계
   │   - 이미 있으면 확인만, 없으면 새 스키마 기준으로 설계
   │   - VO, ID, Exception 포함
   │
Step 2: Legacy Entity + Domain Mapper 생성
   │   - Legacy Entity (레거시 테이블 매핑)
   │   - LegacyXxxMapper (Legacy Entity ↔ Domain Aggregate 변환)
   │   - 타입 변환 로직 (Yn → boolean, String enum → Domain enum 등)
   │
Step 3: Repository + Adapter 생성
   │   - ConditionBuilder (동적 쿼리 조건)
   │   - QueryDslRepository (조회 로직)
   │   - JpaRepository (Command용, 필요시)
   │   - QueryAdapter (Query Port 구현)
   │   - CommandAdapter (Command Port 구현, 필요시)
   │
Step 4: Application Layer 생성
       - Query/Command Port 인터페이스
       - Service (UseCase 구현)
       - Manager (도메인 서비스)
       - Assembler (Domain → Result 변환)
```

---

## 핵심 패턴: Domain ↔ Legacy 브릿지

### 타입 변환 규칙

| Legacy (DB) | Domain (새 설계) | 변환 방향 |
|-------------|-----------------|----------|
| `Yn.Y` / `Yn.N` | `boolean` | `"Y".equals(val)` ↔ `val ? Yn.Y : Yn.N` |
| `String` enum | Domain Enum | `Enum.valueOf(val)` ↔ `enum.name()` |
| `String` column | Value Object | `VO.of(val)` ↔ `vo.value()` |
| `Long userId` | `MemberId` | `MemberId.of(val)` ↔ `memberId.value()` |
| `Embedded` | `record VO` | 필드별 매핑 |
| `null` column | Optional VO | null-safe 변환 |
| `insertDate` / `updateDate` | `Instant` | `LocalDateTime` ↔ `Instant` 변환 |

### Mapper 구조

```java
@Component
public class LegacyShippingAddressMapper {

    // Legacy Entity → Domain Aggregate (조회용)
    public ShippingAddress toDomain(LegacyShippingAddressEntity entity) {
        return ShippingAddress.reconstitute(
            ShippingAddressId.of(entity.getId()),
            MemberId.of(String.valueOf(entity.getUserId())),
            ReceiverName.of(entity.getReceiverName()),
            // ... VO 변환
            "Y".equals(entity.getDefaultYn()),  // Yn → boolean
            // ...
        );
    }

    // Domain Aggregate → Legacy Entity (저장용)
    public LegacyShippingAddressEntity toEntity(ShippingAddress domain) {
        return LegacyShippingAddressEntity.of(
            domain.idValue(),
            Long.parseLong(domain.memberIdValue()),
            domain.receiverNameValue(),
            // ... 역변환
            domain.isDefault() ? Yn.Y : Yn.N,  // boolean → Yn
            // ...
        );
    }
}
```

---

## 출력 파일 구조

### Step 1: Domain Layer (이미 있으면 스킵)
```
domain/src/main/java/.../domain/{domain}/
├── aggregate/
│   ├── {Domain}.java              # Aggregate Root
│   └── {Domain}UpdateData.java    # 수정 데이터 record
├── id/
│   └── {Domain}Id.java            # ID VO
├── vo/
│   ├── {ValueObject1}.java
│   └── {ValueObject2}.java
├── exception/
│   ├── {Domain}Exception.java
│   ├── {Domain}ErrorCode.java
│   └── {Domain}NotFoundException.java
└── query/
    ├── {Domain}SearchCriteria.java
    └── {Domain}SortKey.java
```

### Step 2: Legacy Entity + Mapper
```
adapter-out/persistence-mysql-legacy/.../legacy/{domain}/
├── entity/
│   └── Legacy{Domain}Entity.java
└── mapper/
    └── Legacy{Domain}Mapper.java
```

### Step 3: Repository + Adapter
```
adapter-out/persistence-mysql-legacy/.../legacy/{domain}/
├── condition/
│   └── Legacy{Domain}ConditionBuilder.java
├── repository/
│   ├── Legacy{Domain}JpaRepository.java        # (Command용)
│   └── Legacy{Domain}QueryDslRepository.java
└── adapter/
    ├── Legacy{Domain}QueryAdapter.java
    └── Legacy{Domain}CommandAdapter.java        # (Command용)
```

### Step 4: Application Layer
```
application/src/main/java/.../application/{domain}/
├── port/
│   ├── in/
│   │   ├── query/
│   │   │   └── Get{Domain}UseCase.java
│   │   └── command/
│   │       ├── Register{Domain}UseCase.java
│   │       ├── Update{Domain}UseCase.java
│   │       └── Delete{Domain}UseCase.java
│   └── out/
│       ├── query/
│       │   └── {Domain}QueryPort.java
│       └── command/
│           └── {Domain}CommandPort.java
├── service/
│   ├── Get{Domain}Service.java
│   ├── Register{Domain}Service.java
│   ├── Update{Domain}Service.java
│   └── Delete{Domain}Service.java
├── manager/
│   ├── {Domain}ReadManager.java
│   └── {Domain}CommandManager.java
├── assembler/
│   └── {Domain}Assembler.java
└── dto/
    └── response/
        └── {Domain}Result.java
```

---

## Adapter에서 Mapper 사용 패턴

```java
@Component
public class LegacyShippingAddressQueryAdapter implements ShippingAddressQueryPort {

    private final LegacyShippingAddressQueryDslRepository repository;
    private final LegacyShippingAddressMapper mapper;

    // 조회: Legacy Entity → Domain Aggregate
    @Override
    public List<ShippingAddress> findByMemberId(MemberId memberId) {
        List<LegacyShippingAddressEntity> entities =
            repository.findByUserId(Long.parseLong(memberId.value()));
        return entities.stream()
            .map(mapper::toDomain)
            .toList();
    }
}

@Component
public class LegacyShippingAddressCommandAdapter implements ShippingAddressCommandPort {

    private final LegacyShippingAddressJpaRepository jpaRepository;
    private final LegacyShippingAddressMapper mapper;

    // 저장: Domain Aggregate → Legacy Entity
    @Override
    public ShippingAddress save(ShippingAddress shippingAddress) {
        LegacyShippingAddressEntity entity = mapper.toEntity(shippingAddress);
        LegacyShippingAddressEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
```

---

## 주의사항

1. **Domain은 Legacy를 모르게**: Domain Layer에 Legacy 의존성이 들어가면 안 됨
2. **Mapper는 Persistence Layer에**: 변환 로직은 `adapter-out` 모듈에만 존재
3. **VO null-safety**: 레거시 DB의 nullable 컬럼 → Domain VO에서 어떻게 처리할지 결정
4. **Soft Delete**: 레거시에 deletedAt이 없으면 물리 삭제, 있으면 soft delete
5. **ID 타입**: 레거시 `Long` ID → Domain `{Domain}Id` VO로 래핑
6. **인증 컨텍스트**: `SecurityUtils.currentUserId()` → `MemberId` 변환 방식 통일

## 다음 단계

브릿지 생성 완료 후:
```bash
/legacy-controller web:ShippingAddressController   # Controller + ApiMapper 생성
/shadow-test web:shipping-address                  # Shadow Traffic 테스트 케이스
/shadow-verify shipping-address                    # 로컬 검증
```
