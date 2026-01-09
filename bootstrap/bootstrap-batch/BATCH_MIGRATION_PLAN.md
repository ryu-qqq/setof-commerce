# Bootstrap-Batch 개발 계획서

## 개요
- **목적**: 에어플로우 배치 작업을 Spring Batch로 마이그레이션
- **모듈**: bootstrap-batch
- **DataSource**: legacyDataSource (레거시 DB), setofDataSource (신규 DB)
- **에어플로우 소스**: `~/PycharmProjects/SetofAirflow/`

---

## 배치 작업 목록 (총 15개)

### 1. Shipment (배송) - 1개
| 상태 | DAG | 스케줄 | 분석 | 구현 |
|------|-----|--------|------|------|
| ✅ | tracking_shipment_dag | 하루 2회 (10:30, 17:30) | ✅ | ✅ |

### 2. Order (주문) - 6개
| 상태 | DAG | 스케줄 | 분석 | 구현 |
|------|-----|--------|------|------|
| ✅ | update_rejected_orders_dag | 매분 | ✅ | ✅ |
| ✅ | update_completed_orders_dag | 매분 | ✅ | ✅ |
| ✅ | update_settlement_orders_dag | 매시 | ✅ | ✅ |
| ✅ | update_cancel_request_orders_dag | 매시 | ✅ | ✅ |
| ✅ | update_fail_orders_dag | 15분마다 | ✅ | ✅ |
| ✅ | update_fail_vbank_orders_dag | 15분마다 | ✅ | ✅ |

### 3. Notification (알림톡) - 3개
| 상태 | DAG | 스케줄 | 분석 | 구현 |
|------|-----|--------|------|------|
| ✅ | alim_talk_notify_dag | 5분마다 | ✅ | ✅ |
| ✅ | schedule_auto_order_cancel | 매시 | ✅ | ✅ |
| ✅ | schedule_vbank_cancel | 15분마다 | ✅ | ✅ |

### 4. External (외부연동) - 2개
| 상태 | DAG | 스케줄 | 분석 | 구현 |
|------|-----|--------|------|------|
| ⏳ | sync_sellic_order_dag | 07,10,13,16,19,22시 05분 | ❌ | ❌ |
| ⏳ | sync_sellic_claim_order_dag | 07,10,13,16,19,22시 10분 | ❌ | ❌ |

### 5. Naver/SEO - 3개
| 상태 | DAG | 스케줄 | 분석 | 구현 |
|------|-----|--------|------|------|
| ⏳ | process_naver_ep_update_dag | 00,09,13,17시 10분 | ❌ | ❌ |
| ⏳ | process_naver_ep_seller_dag | 23,08,12,16시 10분 | ❌ | ❌ |
| ⏳ | process_seo_update_dag | 00,09,13,17시 13분 | ❌ | ❌ |

---

## 구현 순서

1. **Phase 1: Shipment** - 패턴 확립 (1개)
2. **Phase 2: Order** - Strategy 패턴 (6개)
3. **Phase 3: Notification** - 알림톡 (3개)
4. **Phase 4: External** - SELLIC 연동 (2개)
5. **Phase 5: Naver/SEO** - 파일 업로드 (3개)

---

## 현재 진행 상황

### Phase 1: Shipment 분석
- [ ] 에어플로우 서비스 코드 분석
- [ ] DB 스키마/쿼리 파악
- [ ] Spring Batch Job 설계
- [ ] 구현
- [ ] 테스트

---

## 상태 범례
- ⏳ 대기
- 🔍 분석중
- 🔨 구현중
- ✅ 완료

---

## 분석 노트

### [Phase 1] tracking_shipment_dag ✅ 분석완료

**에어플로우 경로**:
- DAG: `dags/shipment_update_dag.py`
- Service: `module/shipment/services/shipment_update_service.py`

**호출 메서드**: `shipment_update_service.update_delivery_processing_orders()`

**분석 내용**:

1. **조회 (fetch_shipments)**:
   ```sql
   SELECT s.invoice_no, s.company_code, s.order_id
   FROM shipment s
   JOIN orders o ON s.order_id = o.order_id
   WHERE o.order_status = 'DELIVERY_PROCESSING'
     AND s.shipment_type = 'PARCEL_SERVICE'
     AND s.delivery_status = 'DELIVERY_PROCESSING'
   ```

2. **처리 (track_shipment)**:
   - 외부 택배 조회 API 호출 (스마트 택배 API)
   - URL: `{base_url}?t_code={택배사코드}&t_invoice={송장번호}&t_key={API키}`
   - 응답에서 `completeYN == 'Y'` 이면 배송완료

3. **업데이트**:
   - `shipment` 테이블: delivery_status = 'DELIVERY_COMPLETED', delivery_date 설정
   - `orders` 테이블: order_status = 'DELIVERY_COMPLETED'
   - `order_history` 테이블: 이력 추가

**테이블 구조**:
- `shipment`: shipment_id, order_id, shipment_type, invoice_no, company_code, delivery_status, delivery_date
- `orders`: order_id, order_status, ...
- `order_history`: (이력 테이블)

**Spring Batch 설계**:

```
[TrackingShipmentJob]
└─ [TrackingShipmentStep]
     ├─ ItemReader: 배송중 shipment 조회 (JdbcPagingItemReader)
     ├─ ItemProcessor: 외부 API로 배송완료 여부 확인
     └─ ItemWriter: shipment, orders, order_history 업데이트
```

**구현 파일**:
- `legacy/shipment/TrackingShipmentJobConfig.java`
- `legacy/shipment/dto/ShipmentInfo.java`
- `legacy/shipment/ShipmentItemReader.java`
- `legacy/shipment/ShipmentItemProcessor.java`
- `legacy/shipment/ShipmentItemWriter.java`
- `legacy/shipment/ShipmentTrackerClient.java` (외부 API 호출)

---

### [Phase 2] Order Update ✅ 분석 및 구현완료

**에어플로우 경로**:
- DAG: `dags/order_update_dag.py`
- Context: `module/order/core/order_update_context.py`
- Strategy들: `module/order/services/strategy/`

**Strategy 패턴 분석**:

| Strategy | 대상 상태 | 전이 상태 | 처리 방식 |
|----------|----------|----------|----------|
| RejectedOrder | CANCEL_REQUEST_REJECTED, RETURN_REQUEST_REJECTED | DELIVERY_PROCESSING, DELIVERY_COMPLETED | DB 직접 업데이트 |
| CompletedOrder | CANCEL_REQUEST_CONFIRMED, SALE_CANCELLED, RETURN_REQUEST_CONFIRMED | *_COMPLETED | DB 직접 업데이트 |
| SettlementProcessing | DELIVERY_COMPLETED | SETTLEMENT_PROCESSING | DB + Settlement 테이블 업데이트 |
| CancelRequest | CANCEL_REQUEST | - | Spring API 호출 |
| PaymentFail | ORDER_PROCESSING (결제실패) | - | Spring API 호출 |
| VBankFail | ORDER_PROCESSING (가상계좌 기한만료) | - | Spring API 호출 |

**DB 직접 업데이트 Job (3개)**:
1. 조회: 기간 내 대상 상태 주문 조회
2. 처리: 상태 매핑 테이블 기반 신규 상태 결정
3. 업데이트: orders, order_history 테이블 업데이트

**API 호출 Job (3개)**:
1. 조회: 기간 내 대상 주문 ID 조회
2. 처리: Spring API `/api/v1/order/cancel/batch` 또는 `/api/v1/order/payment-fail/batch` 호출

**구현 파일**:
- `legacy/order/enums/OrderStatus.java` - 상태 Enum + 매핑
- `legacy/order/dto/UpdateOrder.java` - 조회 DTO
- `legacy/order/dto/OrderUpdateResult.java` - 처리 결과 DTO
- `legacy/order/strategy/OrderUpdateStrategy.java` - Strategy 인터페이스
- `legacy/order/strategy/*Strategy.java` - 6개 Strategy 구현체
- `legacy/order/client/OrderApiClient.java` - Spring API 클라이언트
- `legacy/order/OrderUpdateJobConfig.java` - DB 직접 업데이트 Job (3개)
- `legacy/order/OrderApiCallJobConfig.java` - API 호출 Job (3개)
- `legacy/order/OrderUpdateJobController.java` - REST API 컨트롤러

**API 엔드포인트**:
| HTTP Method | Path | 설명 |
|-------------|------|------|
| POST | /api/batch/order/rejected/run | 거부된 주문 처리 |
| POST | /api/batch/order/completed/run | 완료 처리 주문 |
| POST | /api/batch/order/settlement/run | 정산 처리 주문 |
| POST | /api/batch/order/cancel-request/run | 취소 요청 처리 |
| POST | /api/batch/order/payment-fail/run | 결제 실패 처리 |
| POST | /api/batch/order/vbank-fail/run | 가상계좌 실패 처리 |
| GET | /api/batch/order/{jobType}/status | Job 상태 조회 |

---

### [Phase 3] Notification ✅ 분석 및 구현완료

**에어플로우 경로**:
- `alim_talk_notify_dag`: `module/notification/services/alim_talk_notify_service.py`
- `schedule_auto_order_cancel`: `module/notification/services/strategy/auto_cancel_strategy.py`
- `schedule_vbank_cancel`: `module/notification/services/strategy/vbank_cancel_strategy.py`

**Job 분석**:

| Job | 목적 | 스케줄 | 처리 방식 |
|-----|------|--------|----------|
| alimTalkNotifyJob | PENDING 메시지 발송 | 5분마다 | NHN Cloud API 호출 |
| scheduleAutoCancelJob | 자동 취소 알림 생성 | 매시 | message_queue INSERT |
| scheduleVBankCancelJob | 가상계좌 만료 알림 생성 | 15분마다 | message_queue INSERT |

**1. alimTalkNotifyJob (메시지 발송)**:
```sql
-- Reader
SELECT message_id, order_id, phone_number, template_code, template_variables, status, created_at
FROM message_queue
WHERE status = 'PENDING'

-- Writer (발송 후)
UPDATE message_queue
SET status = 'SEND' | 'FAILED', response_code, response_message, sent_at
WHERE message_id = ?
```

**2. scheduleAutoCancelJob (자동 취소 알림 생성)**:
- 조건: CANCEL_REQUEST 상태 + 24-23시간 경과 + 중복 미발송
- 템플릿: CANCEL_ORDER_AUTO
- 변수: orderNumber, productName, cancelRequestDate

**3. scheduleVBankCancelJob (가상계좌 만료 알림 생성)**:
- 조건: ORDER_PROCESSING + VBANK + 1시간 내 만료 + 오늘 미발송
- 템플릿: CANCEL_NOTIFY
- 변수: orderNumber, bankName, accountNumber, totalAmount, expiryDate

**구현 파일**:
- `legacy/notification/enums/MessageStatus.java` - 메시지 상태 Enum
- `legacy/notification/enums/AlimTalkTemplateCode.java` - 템플릿 코드 Enum
- `legacy/notification/dto/MessageQueueItem.java` - 메시지 큐 DTO
- `legacy/notification/dto/ScheduledCancelOrder.java` - 자동 취소 대상 DTO
- `legacy/notification/dto/VBankExpiryOrder.java` - 가상계좌 만료 대상 DTO
- `legacy/notification/dto/AlimTalkSendResult.java` - 발송 결과 DTO
- `legacy/notification/client/NhnCloudAlimTalkClient.java` - NHN Cloud API 클라이언트
- `legacy/notification/AlimTalkNotifyJobConfig.java` - 메시지 발송 Job
- `legacy/notification/ScheduleAlimTalkJobConfig.java` - 메시지 생성 Job (2개)
- `legacy/notification/NotificationJobController.java` - REST API 컨트롤러

**API 엔드포인트**:
| HTTP Method | Path | 설명 |
|-------------|------|------|
| POST | /api/batch/notification/alim-talk/run | 알림톡 발송 |
| POST | /api/batch/notification/schedule-auto-cancel/run | 자동 취소 알림 생성 |
| POST | /api/batch/notification/schedule-vbank-cancel/run | 가상계좌 만료 알림 생성 |
| GET | /api/batch/notification/{jobType}/status | Job 상태 조회 |

**NHN Cloud API 설정**:
```yaml
nhn:
  alimtalk:
    url: ${NHN_ALIMTALK_URL:https://api-alimtalk.cloud.toast.com}
    app-key: ${NHN_ALIMTALK_APP_KEY:}
    secret-key: ${NHN_ALIMTALK_SECRET_KEY:}
    sender-key: ${NHN_ALIMTALK_SENDER_KEY:}
```

---

### [Phase 4] External (SELLIC)

**에어플로우 경로**:
- DAG: `dags/external_order_sync_dag.py`
- Service: `module/external/services/external_order_sync_service.py`

**분석 내용**:
> (분석 후 업데이트)

---

### [Phase 5] Naver/SEO

**에어플로우 경로**:
- Naver EP: `module/naver/services/naver_ep_upload_service.py`
- SEO: `module/seo/services/seo_update_service.py`

**분석 내용**:
> (분석 후 업데이트)
