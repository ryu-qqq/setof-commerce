# Application Layer Validation Rules Index

> 필요한 카테고리만 선택적으로 읽어서 토큰을 절약하세요.

## 개요

- **Layer**: Application
- **총 카테고리**: 24개
- **총 룰**: 165개
- **버전**: 1.0.0

---

## 카테고리 인덱스

### 📦 Service (UseCase 구현체)
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `app-rules-01-service.md` | COMMAND_SERVICE | 9 | CUD 연산 UseCase 구현 |
| `app-rules-01-service.md` | QUERY_SERVICE | 8 | Read 연산 UseCase 구현 |

### 📋 DTO (데이터 전송 객체)
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `app-rules-02-dto.md` | COMMAND_DTO | 8 | CUD 요청 데이터 |
| `app-rules-02-dto.md` | QUERY_DTO | 6 | 조회 요청 데이터 |
| `app-rules-02-dto.md` | RESPONSE_DTO | 6 | 응답 데이터 |
| `app-rules-02-dto.md` | PERSIST_BUNDLE | 8 | 영속화 객체 묶음 |
| `app-rules-02-dto.md` | QUERY_BUNDLE | 8 | 조회 결과 묶음 |

### 🔌 Port (인터페이스)
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `app-rules-03-port.md` | PORT_IN_COMMAND | 5 | Command UseCase 인터페이스 |
| `app-rules-03-port.md` | PORT_IN_QUERY | 5 | Query UseCase 인터페이스 |
| `app-rules-03-port.md` | PORT_OUT_COMMAND | 5 | PersistencePort |
| `app-rules-03-port.md` | PORT_OUT_QUERY | 5 | QueryPort |
| `app-rules-03-port.md` | CACHE_QUERY_PORT | 7 | 도메인 특화 캐시 |
| `app-rules-03-port.md` | DISTRIBUTED_LOCK_PORT | 6 | 분산락 (Cross-cutting) |
| `app-rules-03-port.md` | LOCK_KEY | 6 | 분산락 키 VO |

### 🏗️ Manager & Facade (트랜잭션 관리)
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `app-rules-04-manager-facade.md` | TRANSACTION_MANAGER | 7 | 단일 PersistencePort 트랜잭션 |
| `app-rules-04-manager-facade.md` | READ_MANAGER | 7 | 단일 QueryPort 읽기 |
| `app-rules-04-manager-facade.md` | COMMAND_FACADE | 8 | 2+ Manager 조율 |
| `app-rules-04-manager-facade.md` | QUERY_FACADE | 7 | 2+ ReadManager 조율 |

### 🏭 Factory & Assembler (변환)
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `app-rules-05-factory-assembler.md` | COMMAND_FACTORY | 8 | Command → Domain 변환 |
| `app-rules-05-factory-assembler.md` | QUERY_FACTORY | 5 | Query → Criteria 변환 |
| `app-rules-05-factory-assembler.md` | ASSEMBLER | 7 | Domain → Response 변환 |

### 📡 Event & Scheduler (비동기)
| 파일 | 카테고리 | 룰 수 | 용도 |
|-----|---------|------|------|
| `app-rules-06-event-scheduler.md` | EVENT_REGISTRY | 6 | 트랜잭션 커밋 후 이벤트 |
| `app-rules-06-event-scheduler.md` | EVENT_LISTENER | 7 | 이벤트 처리 |
| `app-rules-06-event-scheduler.md` | SCHEDULER | 11 | 배치 작업 오케스트레이션 |

---

## Zero-Tolerance 규칙 요약

1. **Lombok 금지** - 모든 Application Layer 컴포넌트
2. **@Transactional 위치** - Service 금지, Manager/Facade만 허용
3. **Port 직접 호출 금지** - Service에서 Manager/Facade 통해서만
4. **toDomain 금지** - Assembler는 Domain → Response만
5. **비즈니스 로직 금지** - Domain 책임
6. **ThreadLocal 금지** - TransactionSynchronizationManager 사용
7. **Scheduler Port 호출 금지** - UseCase만 호출
8. **String 락 키 금지** - LockKey VO 필수
9. **String 캐시 키 금지** - 도메인 ID 필수

---

## 사용법

```
# 인덱스 확인
read_memory("convention-application-layer-validation-rules.md")

# 필요한 룰만 선택적으로 읽기
read_memory("app-rules-01-service.md")      # Service 룰
read_memory("app-rules-02-dto.md")          # DTO 룰
read_memory("app-rules-03-port.md")         # Port 룰
read_memory("app-rules-04-manager-facade.md") # Manager/Facade 룰
read_memory("app-rules-05-factory-assembler.md") # Factory/Assembler 룰
read_memory("app-rules-06-event-scheduler.md")   # Event/Scheduler 룰
```
