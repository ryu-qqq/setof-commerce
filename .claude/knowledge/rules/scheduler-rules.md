# SCHEDULER Layer 코딩 규칙 (8개)

## 개요

- **총 규칙 수**: 8개
- **Zero-Tolerance**: 3개
- **일반 규칙**: 5개

## 요약 테이블

| Code | Name | Severity | Category | Zero-Tolerance |
|------|------|----------|----------|----------------|
| TSCH-002 | Thin Scheduler는 UseCase 인터페이스만 의존 | BLOCKER | DEPENDENCY | 🚨 |
| TSCH-003 | Thin Scheduler의 @Scheduled 메서드는 UseCase.execute() ... | BLOCKER | BEHAVIOR | 🚨 |
| TSCH-004 | Thin Scheduler에 비즈니스 로직 금지 | BLOCKER | BEHAVIOR | 🚨 |
| TSCH-001 | Thin Scheduler는 @Component + @Scheduled 어노테이션 사용 | MAJOR | ANNOTATION |  |
| TSCH-005 | @Scheduled는 fixedDelay 권장 (작업 중복 방지) | MAJOR | ANNOTATION |  |
| TSCH-006 | Thin Scheduler는 {Domain}Scheduler 또는 {Domain}Outbo... | MAJOR | NAMING |  |
| TSCH-007 | @Scheduled 메서드는 void 반환 | MAJOR | STRUCTURE |  |
| TSCH-008 | Thin Scheduler는 예외 처리를 UseCase에 위임 | MAJOR | BEHAVIOR |  |

---

## 상세 규칙


### BLOCKER 규칙

#### TSCH-002: Thin Scheduler는 UseCase 인터페이스만 의존 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: DEPENDENCY
- **Description**: Thin Scheduler는 Application Layer의 Scheduler UseCase 인터페이스만 주입받습니다. Manager, Port, Repository 등 다른 컴포넌트를 직접 주입받으면 안 됩니다. 모든 비즈니스 로직은 UseCase 구현체에 위임합니다.
- **Rationale**: Thin Layer 원칙. Controller처럼 Scheduler도 호출만 담당하고 로직을 포함하지 않습니다.

#### TSCH-003: Thin Scheduler의 @Scheduled 메서드는 UseCase.execute() 단일 호출 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: @Scheduled 메서드는 useCase.execute() 한 줄만 포함합니다. 조건 분기, 예외 처리, 로깅 외 로직을 추가하면 안 됩니다. 분산락 획득, 배치 처리, 상태 업데이트는 모두 UseCase 구현체에서 처리합니다.
- **Rationale**: Thin Layer 원칙. 스케줄러는 트리거 역할만 수행하고, 모든 로직은 Application Layer에 위임합니다.

#### TSCH-004: Thin Scheduler에 비즈니스 로직 금지 🚨 **[Zero-Tolerance]**
- **Severity**: BLOCKER
- **Category**: BEHAVIOR
- **Description**: Thin Scheduler에 분산락 획득/해제, Outbox 조회, 상태 업데이트, 외부 API 호출 로직을 구현하지 않습니다. 이 모든 로직은 Application Layer의 Scheduler Service에서 처리합니다.
- **Rationale**: 관심사 분리. 스케줄링 인프라와 비즈니스 로직을 명확히 분리합니다.


### MAJOR 규칙

#### TSCH-001: Thin Scheduler는 @Component + @Scheduled 어노테이션 사용
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: Thin Scheduler 클래스는 @Component 어노테이션으로 빈 등록하고, 실행 메서드에 @Scheduled를 사용합니다. fixedDelay 권장 (작업 완료 후 지연). cron은 복잡한 스케줄 필요 시에만 사용합니다.
- **Rationale**: Spring 통합. Spring의 스케줄링 기능을 활용하여 주기적 실행을 트리거합니다.

#### TSCH-005: @Scheduled는 fixedDelay 권장 (작업 중복 방지)
- **Severity**: MAJOR
- **Category**: ANNOTATION
- **Description**: Outbox 처리 등 배치 작업에는 fixedDelay를 권장합니다. fixedDelay는 이전 작업 완료 후 지연을 적용하여 작업이 중첩되지 않습니다. fixedRate는 작업 시간이 길어지면 중첩될 수 있어 주의가 필요합니다.
- **Rationale**: 작업 안전성. 처리 시간이 가변적인 배치 작업에서 중첩 실행을 방지합니다.

#### TSCH-006: Thin Scheduler는 {Domain}Scheduler 또는 {Domain}OutboxScheduler 네이밍
- **Severity**: MAJOR
- **Category**: NAMING
- **Description**: Thin Scheduler 클래스는 {Domain}Scheduler 또는 {Domain}OutboxScheduler로 네이밍합니다. 예: OrderOutboxScheduler, PaymentRetryScheduler, NotificationScheduler. 담당 도메인과 역할을 명확히 표현합니다.
- **Rationale**: 네이밍 일관성. 클래스명만으로 스케줄러의 담당 영역을 파악할 수 있습니다.

#### TSCH-007: @Scheduled 메서드는 void 반환
- **Severity**: MAJOR
- **Category**: STRUCTURE
- **Description**: @Scheduled 어노테이션이 적용된 메서드는 void를 반환합니다. 반환값이 있어도 Spring이 무시하므로 의미가 없습니다. 실행 결과는 UseCase 내부에서 로깅하거나 상태를 업데이트합니다.
- **Rationale**: Spring 스펙 준수. @Scheduled 메서드의 반환값은 사용되지 않습니다.

#### TSCH-008: Thin Scheduler는 예외 처리를 UseCase에 위임
- **Severity**: MAJOR
- **Category**: BEHAVIOR
- **Description**: Thin Scheduler의 @Scheduled 메서드에서 try-catch를 사용하지 않습니다. 모든 예외 처리는 UseCase 구현체 내부에서 처리합니다. 필요시 최상위 예외 로깅만 허용합니다.
- **Rationale**: 책임 분리. 예외 처리 로직도 비즈니스 로직의 일부이므로 Application Layer에서 처리합니다.

