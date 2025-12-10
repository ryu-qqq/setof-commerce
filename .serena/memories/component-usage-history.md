# Component Usage History (사용 이력)

> 실제 생성된 패턴을 기록하여 추천에 활용합니다.
> /design 실행 후 자동으로 업데이트됩니다.

---

## 패턴 기록 형식

```yaml
patterns:
  {pattern_id}:
    name: "패턴 이름"
    description: "패턴 설명"
    flow_type: "command | query"
    
    # 컴포넌트 구성
    components:
      required:  # 필수 컴포넌트
        - "UseCase"
        - "Service"
        - "Command"
        - "Response"
      optional:  # 선택된 옵션
        - "Facade"
        - "EventRegistry"
        
    # 선택된 옵션
    options:
      transaction_complexity: "multiple_same_bc"
      event_publishing: "async_event"
      concurrency_control: "none"
      
    # 사용 통계
    usage_count: 5
    last_used: "2024-12-08"
    first_used: "2024-11-15"
    
    # 실제 사용 예시
    examples:
      - service: "PlaceOrderService"
        bc: "order"
        created: "2024-11-15"
      - service: "CancelOrderService"
        bc: "order"
        created: "2024-12-01"
        
    # 자연어 트리거 (자동 매칭용)
    triggers:
      - "주문"
      - "결제"
      - "취소"
```

---

## 현재 등록된 패턴

### 1. simple_crud

```yaml
simple_crud:
  name: "단순 CRUD"
  description: "단일 Aggregate, 이벤트 없음, 기본 패턴"
  flow_type: "command"
  
  components:
    required:
      - CommandUseCase
      - CommandService
      - Command DTO
      - Response DTO
      - CommandFactory
      - TransactionManager
      - Assembler
      - PersistencePort
    optional: []
    
  options:
    transaction_complexity: "single"
    event_publishing: "no_event"
    concurrency_control: "no_lock"
    external_integration: "no_external"
    
  usage_count: 0
  last_used: null
  first_used: null
  examples: []
  triggers:
    - "생성"
    - "등록"
    - "수정"
    - "create"
    - "update"
    - "register"
```

### 2. simple_query

```yaml
simple_query:
  name: "단순 조회"
  description: "단일 ReadManager, 단건/페이지 조회"
  flow_type: "query"
  
  components:
    required:
      - QueryUseCase
      - QueryService
      - Query DTO
      - Response DTO
      - ReadManager
      - Assembler
      - QueryPort
    optional:
      - QueryFactory
      
  options:
    pagination: "offset_pagination"
    caching: "no_cache"
    
  usage_count: 0
  last_used: null
  first_used: null
  examples: []
  triggers:
    - "조회"
    - "검색"
    - "목록"
    - "상세"
    - "get"
    - "search"
    - "find"
```

### 3. complex_with_event

```yaml
complex_with_event:
  name: "복합 트랜잭션 + 이벤트"
  description: "2+ Manager, Facade, 트랜잭션 후 이벤트 발행"
  flow_type: "command"
  
  components:
    required:
      - CommandUseCase
      - CommandService
      - Command DTO
      - Response DTO
      - CommandFactory
      - CommandFacade
      - TransactionManager (x2+)
      - Assembler
      - PersistencePort (x2+)
    optional:
      - DomainEvent
      - TransactionEventRegistry
      - EventListener
      - PersistBundle
      
  options:
    transaction_complexity: "multiple_same_bc"
    event_publishing: "async_event"
    concurrency_control: "no_lock"
    
  usage_count: 0
  last_used: null
  first_used: null
  examples: []
  triggers:
    - "주문"
    - "order"
    - "결제"
    - "payment"
```

### 4. concurrent_safe

```yaml
concurrent_safe:
  name: "동시성 제어 패턴"
  description: "비관적/분산 락 사용"
  flow_type: "command"
  
  components:
    required:
      - CommandUseCase
      - CommandService
      - Command DTO
      - CommandFactory
      - TransactionManager
    optional:
      - LockQueryPort
      - DistributedLockPort
      - LockKey VO
      
  options:
    transaction_complexity: "single"
    concurrency_control: "pessimistic_lock | distributed_lock"
    
  usage_count: 0
  last_used: null
  first_used: null
  examples: []
  triggers:
    - "재고"
    - "차감"
    - "선착순"
    - "예약"
    - "stock"
    - "inventory"
    - "deduct"
```

### 5. with_external_api

```yaml
with_external_api:
  name: "외부 API 연동 패턴"
  description: "외부 시스템 호출 포함"
  flow_type: "command"
  
  components:
    required:
      - CommandUseCase
      - CommandService
      - Command DTO
      - Response DTO
      - CommandFactory
      - TransactionManager
    optional:
      - ExternalApiPort
      - ExternalApiAdapter
      - RetryTemplate
      - EventListener (비동기 호출 시)
      
  options:
    external_integration: "sync_external | async_external"
    
  usage_count: 0
  last_used: null
  first_used: null
  examples: []
  triggers:
    - "결제"
    - "PG"
    - "알림"
    - "SMS"
    - "이메일"
    - "외부"
    - "연동"
```

### 6. complex_query

```yaml
complex_query:
  name: "복합 조회"
  description: "2+ ReadManager, QueryFacade 사용"
  flow_type: "query"
  
  components:
    required:
      - QueryUseCase
      - QueryService
      - Query DTO
      - Response DTO
      - QueryFacade
      - ReadManager (x2+)
      - Assembler
      - QueryPort (x2+)
    optional:
      - QueryFactory
      - QueryBundle
      
  options:
    pagination: "offset_pagination"
    
  usage_count: 0
  last_used: null
  first_used: null
  examples: []
  triggers:
    - "상세"
    - "detail"
    - "통합"
    - "종합"
```

---

## 패턴 매칭 알고리즘

```yaml
matching_algorithm:
  # 1단계: 자연어 트리거 매칭
  trigger_matching:
    method: "keyword_in_feature_name"
    weight: 0.4
    
  # 2단계: 옵션 조합 매칭
  option_matching:
    method: "selected_options_similarity"
    weight: 0.4
    
  # 3단계: 사용 빈도 가중치
  frequency_weight:
    method: "usage_count_normalized"
    weight: 0.2
    
  # 최소 매칭 점수
  minimum_score: 0.5
```

---

## 사용 이력 업데이트 트리거

```yaml
update_triggers:
  # /design 완료 시
  on_design_complete:
    action: "increment_usage_count"
    update_fields:
      - usage_count
      - last_used
      - examples
      
  # 새 패턴 발견 시
  on_new_pattern:
    action: "create_pattern_entry"
    auto_generate:
      - pattern_id (hash of components)
      - triggers (from feature name)
      
  # 패턴 수정 시
  on_pattern_modify:
    action: "fork_or_update"
    decision: "similarity > 0.8 ? update : fork"
```

---

## 추천 출력 형식

```markdown
## 💡 추천 패턴 발견!

### [1] complex_with_event (5회 사용, 가장 최근)
- **설명**: 복합 트랜잭션 + 이벤트
- **예시**: `PlaceOrderService`, `CancelOrderService`
- **구성**: UseCase, Command, Facade, Manager x2, Assembler, EventRegistry
- **매칭 이유**: "주문", "취소" 키워드 일치

### [2] simple_crud (12회 사용, 가장 빈번)
- **설명**: 단순 CRUD
- **예시**: `UpdateCategoryService`, `RegisterBrandService`
- **구성**: UseCase, Command, Manager, Assembler

어떤 패턴으로 시작할까요? [1/2/직접 선택]
```
