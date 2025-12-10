# Component Options (선택적 컴포넌트)

> 상황에 따라 추가되는 컴포넌트를 정의합니다.
> 애매한 경우 사용자에게 질문으로 확인합니다.

---

## 1. 트랜잭션 복잡도 옵션

### Q1. 몇 개의 Aggregate를 변경하나요?

```yaml
option_id: transaction_complexity
question: "몇 개의 Aggregate를 변경하나요?"
choices:
  - id: single
    label: "1개 (단일 Aggregate)"
    description: "Order만 변경, Product만 변경 등"
    adds: 
      - component: "TransactionManager"
        count: 1
    removes:
      - component: "Facade"
      
  - id: multiple_same_bc
    label: "2개+ (같은 BC 내)"
    description: "Order + OrderLine, Product + ProductOption 등"
    adds:
      - component: "Facade"
      - component: "TransactionManager"
        count: 2
      - component: "PersistBundle"
        
  - id: multiple_cross_bc
    label: "2개+ (다른 BC 간)"
    description: "Order + Payment, Order + Inventory 등"
    adds:
      - component: "Facade"
      - component: "TransactionManager"
        count: 2
      - component: "PersistBundle"
    warns:
      - "⚠️ BC 간 트랜잭션은 신중히! 이벤트 기반 분리 고려"
```

---

## 2. 이벤트 발행 옵션

### Q2. 도메인 이벤트를 발행하나요?

```yaml
option_id: event_publishing
question: "도메인 이벤트를 발행하나요?"
triggers: ["이벤트", "알림", "동기화", "후속 작업", "비동기"]
choices:
  - id: no_event
    label: "아니오"
    description: "단순 CRUD, 후속 작업 없음"
    adds: []
    
  - id: sync_event
    label: "예 - 트랜잭션 내 처리"
    description: "같은 트랜잭션에서 즉시 처리"
    adds:
      - component: "DomainEvent"
        location: "domain/{bc}/event"
      - component: "EventListener"
        location: "application/{bc}/listener"
        annotation: "@EventListener"
    warns:
      - "⚠️ 트랜잭션 내 이벤트 처리는 롤백 시 함께 롤백됨"
      
  - id: async_event
    label: "예 - 트랜잭션 커밋 후 처리"
    description: "커밋 후 비동기로 처리 (권장)"
    adds:
      - component: "DomainEvent"
        location: "domain/{bc}/event"
      - component: "TransactionEventRegistry"
        location: "application/common"
        inject_to: "Service"
      - component: "EventListener"
        location: "application/{bc}/listener"
        annotation: "@TransactionalEventListener(phase = AFTER_COMMIT)"
```

---

## 3. 동시성 제어 옵션

### Q3. 동시성 제어가 필요하나요?

```yaml
option_id: concurrency_control
question: "동시성 제어가 필요하나요?"
triggers: ["재고", "선착순", "동시", "차감", "예약", "수량", "잔액"]
choices:
  - id: no_lock
    label: "아니오"
    description: "동시 접근 가능성 낮음"
    adds: []
    
  - id: optimistic_lock
    label: "낙관적 락 (Optimistic)"
    description: "충돌 적음, 버전 체크로 감지"
    adds:
      - component: "Version 필드"
        location: "domain/{bc}/aggregate"
        field: "@Version private Long version"
      - component: "LockQueryPort"
        location: "application/{bc}/port/out/query"
        naming: "{Bc}LockQueryPort"
        method: "findByIdWithOptimisticLock"
    warns:
      - "⚠️ OptimisticLockException 처리 필요"
      
  - id: pessimistic_lock
    label: "비관적 락 (Pessimistic)"
    description: "충돌 많음, DB 레벨 락"
    adds:
      - component: "LockQueryPort"
        location: "application/{bc}/port/out/query"
        naming: "{Bc}LockQueryPort"
        method: "findByIdForUpdate"
      - component: "LockQueryAdapter"
        location: "adapter-out/persistence/{bc}/adapter/query"
    warns:
      - "⚠️ 데드락 주의, 락 순서 일관성 유지"
      
  - id: distributed_lock
    label: "분산 락 (Redis)"
    description: "멀티 인스턴스 환경"
    adds:
      - component: "DistributedLockPort"
        location: "application/common/port/out"
        naming: "DistributedLockPort"
      - component: "LockKey VO"
        location: "application/{bc}/vo"
        naming: "{Bc}LockKey"
      - component: "DistributedLockAdapter"
        location: "adapter-out/redis/lock"
    rules_memory: "app-rules-03-port"
```

---

## 4. 외부 API 연동 옵션

### Q4. 외부 시스템 연동이 필요하나요?

```yaml
option_id: external_integration
question: "외부 시스템 연동이 필요하나요?"
triggers: ["외부", "API", "연동", "결제", "알림", "SMS", "이메일", "PG"]
choices:
  - id: no_external
    label: "아니오"
    description: "내부 시스템만 사용"
    adds: []
    
  - id: sync_external
    label: "예 - 동기 호출"
    description: "결과 기다림 (응답 필요)"
    adds:
      - component: "ExternalApiPort"
        location: "application/{bc}/port/out/external"
        naming: "{External}ApiPort"
      - component: "ExternalApiAdapter"
        location: "adapter-out/external/{external}"
        naming: "{External}ApiAdapter"
    warns:
      - "⚠️ @Transactional 내에서 호출 금지!"
      - "⚠️ 타임아웃 설정 필수"
    followup:
      question: "API 실패 시 재시도가 필요하나요?"
      choices:
        - id: no_retry
          label: "아니오 - 즉시 실패"
        - id: with_retry
          label: "예 - 재시도 필요"
          adds:
            - component: "RetryTemplate"
            - component: "CircuitBreaker"
              optional: true
              
  - id: async_external
    label: "예 - 비동기 호출"
    description: "이벤트 리스너에서 호출"
    adds:
      - component: "ExternalApiPort"
      - component: "ExternalApiAdapter"
      - component: "EventListener"
        annotation: "@TransactionalEventListener(phase = AFTER_COMMIT)"
    warns:
      - "⚠️ 실패 시 재시도/보상 로직 필요"
```

---

## 5. 검증 복잡도 옵션

### Q5. 복잡한 검증이 필요하나요?

```yaml
option_id: validation_complexity
question: "복잡한 검증이 필요하나요?"
triggers: ["검증", "유효성", "중복", "존재", "권한"]
choices:
  - id: simple_validation
    label: "단순 검증"
    description: "null 체크, 포맷 검증 등"
    adds: []
    note: "Domain 객체 생성 시 자체 검증"
    
  - id: domain_validation
    label: "도메인 규칙 검증"
    description: "비즈니스 규칙 (ex: 최소 주문 금액)"
    adds: []
    note: "Aggregate 메서드 내에서 검증"
    
  - id: cross_aggregate_validation
    label: "다른 Aggregate 조회 필요"
    description: "존재 확인, 상태 확인 등"
    adds:
      - component: "ReadManager"
        note: "다른 BC의 ReadManager 주입"
        location: "application/{other_bc}/manager/query"
    warns:
      - "⚠️ Service에서 조회 후 Domain에 전달"
      
  - id: external_validation
    label: "외부 시스템 검증 필요"
    description: "외부 API로 유효성 확인"
    adds:
      - component: "ValidationPort"
        location: "application/{bc}/port/out/external"
      - component: "ValidationAdapter"
```

---

## 6. 캐시 옵션

### Q6. 캐시가 필요하나요?

```yaml
option_id: caching
question: "캐시가 필요하나요?"
triggers: ["캐시", "성능", "빈번한 조회", "자주 조회"]
choices:
  - id: no_cache
    label: "아니오"
    adds: []
    
  - id: local_cache
    label: "로컬 캐시 (Caffeine)"
    description: "단일 인스턴스, 빠른 접근"
    adds:
      - component: "CacheQueryPort"
        location: "application/{bc}/port/out/cache"
        naming: "{Bc}CacheQueryPort"
      - component: "LocalCacheAdapter"
        location: "adapter-out/cache/{bc}"
    
  - id: distributed_cache
    label: "분산 캐시 (Redis)"
    description: "멀티 인스턴스, 공유 캐시"
    adds:
      - component: "CacheQueryPort"
        location: "application/{bc}/port/out/cache"
        naming: "{Bc}CacheQueryPort"
      - component: "RedisCacheAdapter"
        location: "adapter-out/redis/cache/{bc}"
```

---

## 7. 페이지네이션 옵션 (Query 전용)

### Q7. 어떤 페이지네이션 방식을 사용하나요?

```yaml
option_id: pagination
question: "어떤 페이지네이션 방식을 사용하나요?"
applies_to: "query_flow"
choices:
  - id: no_pagination
    label: "단건 조회"
    description: "ID로 조회"
    return_type: "{Bc}Response"
    
  - id: offset_pagination
    label: "오프셋 페이징"
    description: "관리자 페이지 (총 개수 필요)"
    return_type: "PageResponse<{Bc}SummaryResponse>"
    adds:
      - component: "PageResponse"
        location: "application/common/dto"
        
  - id: cursor_pagination
    label: "커서 페이징"
    description: "무한 스크롤, 대용량"
    return_type: "CursorResponse<{Bc}SummaryResponse>"
    adds:
      - component: "CursorResponse"
        location: "application/common/dto"
      - component: "Cursor VO"
        location: "application/{bc}/vo"
        
  - id: slice_pagination
    label: "슬라이스 페이징"
    description: "다음 페이지 존재 여부만"
    return_type: "SliceResponse<{Bc}SummaryResponse>"
```

---

## 8. 스케줄러 옵션

### Q8. 배치/스케줄 작업이 필요하나요?

```yaml
option_id: scheduling
question: "배치/스케줄 작업이 필요하나요?"
triggers: ["배치", "스케줄", "주기적", "정기", "cron"]
choices:
  - id: no_scheduler
    label: "아니오"
    adds: []
    
  - id: simple_scheduler
    label: "단순 스케줄러"
    description: "주기적 실행, UseCase 호출"
    adds:
      - component: "Scheduler"
        location: "application/{bc}/scheduler"
        naming: "{Bc}Scheduler"
        annotation: "@Component @Scheduled"
    rules_memory: "app-rules-06-event-scheduler"
    warns:
      - "⚠️ Port 직접 호출 금지, UseCase만 호출"
      
  - id: batch_scheduler
    label: "대용량 배치"
    description: "Spring Batch 사용"
    adds:
      - component: "BatchJob"
        location: "application/{bc}/batch"
      - component: "ItemReader"
      - component: "ItemProcessor"
      - component: "ItemWriter"
```

---

## 9. 질문 우선순위

```yaml
question_priority:
  command_flow:
    1: transaction_complexity  # 항상 물어봄
    2: event_publishing        # triggers 매칭 시
    3: concurrency_control     # triggers 매칭 시
    4: external_integration    # triggers 매칭 시
    5: validation_complexity   # triggers 매칭 시
    
  query_flow:
    1: pagination              # 항상 물어봄
    2: caching                 # triggers 매칭 시
```

---

## 10. 자동 추론 규칙

```yaml
auto_inference:
  # 키워드 기반 자동 선택
  - keywords: ["취소", "환불", "cancel", "refund"]
    suggests:
      - option: event_publishing
        choice: async_event
        reason: "취소/환불은 보통 후속 작업(알림, 정산)이 필요합니다"
        
  - keywords: ["재고", "stock", "inventory", "차감", "deduct"]
    suggests:
      - option: concurrency_control
        choice: pessimistic_lock
        reason: "재고 차감은 동시성 이슈가 빈번합니다"
        
  - keywords: ["결제", "payment", "PG"]
    suggests:
      - option: external_integration
        choice: sync_external
        reason: "결제는 외부 PG 연동이 필요합니다"
      - option: event_publishing
        choice: async_event
        reason: "결제 완료 후 알림/정산 처리가 필요합니다"
        
  - keywords: ["선착순", "예약", "티켓", "좌석"]
    suggests:
      - option: concurrency_control
        choice: distributed_lock
        reason: "선착순 처리는 분산 환경에서 락이 필요합니다"
```
