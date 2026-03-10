# Notification System 구현 계획

> 레거시 알림톡(AOP + 배치) 시스템을 새 서버에 아웃박스 패턴으로 재구축

---

## 전체 흐름

```
비즈니스 Service
  → Domain Aggregate 상태 변경
  → Port.persist()

Coordinator (@Transactional, 같은 트랜잭션)
  → Service.execute()
  → {Domain}NotificationManager.onXxx()
       → NotificationOutbox.forNew() 생성
       → NotificationOutboxCommandManager.persist()

Scheduler (5초 폴링)
  → PENDING 조회
  → SQS 발행
  → PUBLISHED 상태 변경

SQS Consumer
  → NotificationReferenceType별 DataResolver (전략 패턴)
  → 부족한 데이터 추가 조회
  → NHN Cloud 알림톡 API 호출
  → COMPLETED / FAILED 처리
```

---

## 1단계: Domain Layer (완료)

### 생성 완료

| 패키지 | 클래스 | 설명 |
|--------|--------|------|
| `notification.aggregate` | `NotificationOutbox` | Aggregate Root (상태 머신 + 재시도) |
| `notification.id` | `NotificationOutboxId` | ID VO |
| `notification.vo` | `NotificationChannel` | ALIMTALK, SMS |
| `notification.vo` | `NotificationStatus` | PENDING → PUBLISHED → COMPLETED/FAILED |
| `notification.vo` | `NotificationEventType` | 18개 트리거 이벤트 |
| `notification.vo` | `NotificationReference` | referenceType + referenceId + payload(JSON) |
| `notification.vo` | `NotificationReferenceType` | ORDER, CANCEL, REFUND, QNA, MEMBER, MILEAGE |
| `notification.vo` | `NotificationRecipient` | phoneNumber + memberId |
| `notification.query` | `NotificationOutboxSearchCriteria` | 모니터링용 검색 조건 |
| `notification.query` | `NotificationOutboxSortKey` | 정렬 키 |

---

## 2단계: Application Layer

### 2-1. notification 모듈 (공통 인프라)

| 패키지 | 클래스 | 역할 |
|--------|--------|------|
| `notification.manager` | `NotificationOutboxCommandManager` | 아웃박스 persist / persistInNewTransaction |
| `notification.manager` | `NotificationOutboxReadManager` | findPending, findStuckPublished, findById |
| `notification.port.out.command` | `NotificationOutboxCommandPort` | 저장 포트 |
| `notification.port.out.query` | `NotificationOutboxQueryPort` | 조회 포트 |
| `notification.port.out.client` | `NotificationOutboxMessageClient` | SQS 발행 포트 |
| `notification.service.command` | `PublishNotificationOutboxService` | 스케줄러 → SQS 발행 UseCase |
| `notification.service.command` | `RecoverStuckNotificationOutboxService` | Stuck 복구 UseCase |
| `notification.port.in.command` | `PublishNotificationOutboxUseCase` | 발행 인바운드 포트 |
| `notification.port.in.command` | `RecoverStuckNotificationOutboxUseCase` | 복구 인바운드 포트 |

### 2-2. 각 도메인별 NotificationManager

> 각 도메인의 manager/ 패키지에 위치. NotificationOutbox + NotificationOutboxCommandManager만 의존.

#### order/manager/OrderNotificationManager

```java
void onAccepted(Order order)           // → ORDER_ACCEPTED (셀러), ORDER_COMPLETED (고객)
void onDeliveryStarted(Order order)    // → DELIVERY_STARTED (고객)
```

**payload에 담을 데이터** (이미 Order에 있는 것만):
- orderId, orderAmount, productGroupId, memberId

**Consumer가 추가 조회할 데이터**:
- productGroupName, sellerPhone(csPhoneNumber)

#### cancel/manager/CancelNotificationManager

```java
void onRequested(Cancel cancel)        // → CANCEL_REQUESTED (고객), CANCEL_REQUESTED_TO_SELLER (셀러)
void onCompleted(Cancel cancel)        // → CANCEL_COMPLETED (고객)
void onAutoCancel(Cancel cancel)       // → CANCEL_AUTO (고객) — 배치용
```

**payload**: cancelId, orderId, reason, detailReason, memberId
**Consumer 추가 조회**: productGroupName, sellerPhone, buyerPhone

#### refund/manager/RefundNotificationManager

```java
void onRequested(RefundClaim refund)   // → REFUND_REQUESTED (고객), REFUND_REQUESTED_TO_SELLER (셀러)
void onRejected(RefundClaim refund)    // → REFUND_REJECTED (고객)
void onAccepted(RefundClaim refund)    // → REFUND_ACCEPTED (고객)
```

**payload**: refundId, orderId, reason, detailReason, memberId
**Consumer 추가 조회**: productGroupName, sellerPhone, buyerPhone

#### qna/manager/QnaNotificationManager

```java
void onCreated(Qna qna)               // → QNA_PRODUCT_CREATED / QNA_ORDER_CREATED (+ _TO_SELLER)
void onReplied(Qna qna)               // → 답변 알림 (고객에게)
```

**payload**: qnaId, qnaType, targetId(orderId or productGroupId), questionType
**Consumer 추가 조회**: productGroupName, sellerPhone

#### member/manager/MemberNotificationManager

```java
void onJoined(Member member)           // → MEMBER_JOINED (고객)
```

**payload**: memberName
**Consumer 추가 조회**: 없음 (recipient에 전화번호 이미 있음)

### 2-3. Consumer 측 (SQS 수신 후 처리)

| 패키지 | 클래스 | 역할 |
|--------|--------|------|
| `notification.internal` | `NotificationDispatcher` | SQS 메시지 수신 → DataResolver 라우팅 |
| `notification.internal` | `NotificationDataResolver` | 전략 인터페이스 (supportType + resolve) |
| `notification.internal` | `OrderNotificationDataResolver` | ORDER용 — 상품명/셀러 전화번호 조회 → 메시지 조립 |
| `notification.internal` | `CancelNotificationDataResolver` | CANCEL용 |
| `notification.internal` | `RefundNotificationDataResolver` | REFUND용 |
| `notification.internal` | `QnaNotificationDataResolver` | QNA용 |
| `notification.internal` | `MemberNotificationDataResolver` | MEMBER용 — 추가 조회 없음 |
| `notification.internal` | `MileageNotificationDataResolver` | MILEAGE용 — 추가 조회 없음 |
| `notification.port.out.client` | `AlimTalkClient` | NHN Cloud API 호출 포트 |
| `notification.service.command` | `SendNotificationService` | Consumer UseCase — resolve → send → complete |

### 2-4. Consumer DataResolver가 조회할 포트

| 포트 | 용도 | 사용하는 Resolver |
|------|------|------------------|
| `NotificationProductQueryPort` | productGroupName 조회 | Order, Cancel, Refund, QNA |
| `NotificationSellerQueryPort` | sellerPhone(csPhoneNumber) 조회 | Order, Cancel, Refund, QNA |
| `NotificationMemberQueryPort` | memberPhone 조회 | Cancel, Refund (수신자가 payload에 없을 때) |

> 기존 도메인 포트를 재사용하지 않고 notification 전용 조회 포트를 만듦
> → notification 모듈이 다른 도메인 포트에 의존하지 않도록

---

## 3단계: Adapter-Out Layer (Persistence)

| 패키지 | 클래스 | 역할 |
|--------|--------|------|
| `persistence-mysql` | `NotificationOutboxEntity` | JPA Entity |
| `persistence-mysql` | `NotificationOutboxJpaRepository` | Spring Data JPA |
| `persistence-mysql` | `NotificationOutboxQueryDslRepository` | QueryDSL (findPending, findStuck) |
| `persistence-mysql` | `NotificationOutboxCommandAdapter` | CommandPort 구현 |
| `persistence-mysql` | `NotificationOutboxQueryAdapter` | QueryPort 구현 |
| `persistence-mysql` | `NotificationOutboxMapper` | Entity ↔ Domain 변환 |

### DDL

```sql
CREATE TABLE notification_outbox (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    channel            VARCHAR(20)  NOT NULL,          -- ALIMTALK, SMS
    event_type         VARCHAR(50)  NOT NULL,          -- ORDER_ACCEPTED 등
    reference_type     VARCHAR(20)  NOT NULL,          -- ORDER, CANCEL 등
    reference_id       BIGINT       NOT NULL,
    payload            TEXT,                            -- JSON 스냅샷
    recipient_phone    VARCHAR(20)  NOT NULL,
    recipient_member_id BIGINT,
    status             VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    retry_count        INT          NOT NULL DEFAULT 0,
    fail_reason        VARCHAR(500),
    created_at         DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6)  NOT NULL,

    INDEX idx_status_created (status, created_at),
    INDEX idx_reference (reference_type, reference_id),
    INDEX idx_event_type (event_type)
);
```

---

## 4단계: Adapter-Out Layer (Client)

| 패키지 | 클래스 | 역할 |
|--------|--------|------|
| `sqs-client` | `NotificationSqsClient` | NotificationOutboxMessageClient 구현 → SQS 발행 |
| `nhn-client` | `NhnAlimTalkClient` | AlimTalkClient 구현 → NHN Cloud API 호출 |
| `nhn-client` | `NhnAlimTalkTemplateMapper` | NotificationEventType → NHN 템플릿 코드 매핑 |

### NHN 템플릿 매핑

| NotificationEventType | NHN Template Code |
|-----------------------|-------------------|
| ORDER_ACCEPTED | ORDER_ACCEPT |
| ORDER_COMPLETED | ORDER_COMPLETE |
| DELIVERY_STARTED | DELIVERY_START |
| CANCEL_REQUESTED | CANCEL_REQUEST |
| CANCEL_REQUESTED_TO_SELLER | CANCEL_ORDER_S |
| CANCEL_AUTO | CANCEL_ORDER_AUTO |
| CANCEL_COMPLETED | CANCEL_SALE |
| REFUND_REQUESTED | RETURN_REQUEST |
| REFUND_REQUESTED_TO_SELLER | RETURN_REQUEST_S |
| REFUND_REJECTED | RETURN_REJECT |
| REFUND_ACCEPTED | RETURN_ACCEPT |
| QNA_PRODUCT_CREATED | CS_PRODUCT |
| QNA_PRODUCT_CREATED_TO_SELLER | CS_PRODUCT_S |
| QNA_ORDER_CREATED | CS_ORDER |
| QNA_ORDER_CREATED_TO_SELLER | CS_ORDER_S |
| MEMBER_JOINED | MEMBER_JOIN |
| MILEAGE_EXPIRING_SOON | MILEAGE_SOON_EXPIRE |

---

## 5단계: Adapter-In Layer (Scheduler + Consumer)

| 패키지 | 클래스 | 역할 |
|--------|--------|------|
| `bootstrap-web-api` | `NotificationOutboxScheduler` | @Scheduled(5초) → PublishNotificationOutboxUseCase |
| `bootstrap-web-api` | `NotificationStuckRecoveryScheduler` | @Scheduled(1분) → RecoverStuckUseCase |
| `bootstrap-web-api` | `NotificationSqsConsumer` | SQS Listener → SendNotificationUseCase |

---

## 구현 순서

```
1단계: Domain Layer ✅ 완료

2단계: Application Layer
  2-1. notification 공통 (Manager, Port, Service)
  2-2. 도메인별 NotificationManager (order, cancel, refund, qna, member)
  2-3. Consumer 측 (Dispatcher, DataResolver 전략)

3단계: Adapter-Out Persistence
  3-1. DDL + Entity + Repository
  3-2. Adapter (Command + Query)

4단계: Adapter-Out Client
  4-1. SQS Client
  4-2. NHN AlimTalk Client

5단계: Adapter-In (Scheduler + Consumer)

6단계: 기존 Coordinator에 NotificationManager 연결
```

---

## 미결 사항

| 항목 | 상태 | 비고 |
|------|------|------|
| SQS Queue Terraform | 미착수 | discount outbox용 SQS도 함께 |
| NHN Cloud API 키 관리 | 미착수 | AWS Secrets Manager |
| Consumer 실패 시 DLQ | 미정 | SQS Dead Letter Queue 설정 |
| 배치 알림 (자동취소, 가상계좌) | 미착수 | 별도 스케줄러에서 아웃박스 생성 |
| 알림 발송 모니터링 대시보드 | 미착수 | Grafana 연동 |
