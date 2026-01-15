---
description: Epic 기획 + Task 분해 전문가. 요구사항 분석, 영향도 분석, 컨텍스트 크기에 맞는 Task 분해.
tags: [planning, analysis]
activationCommands: ["/epic", "/plan"]
---

# Planner Skill

Epic 기획과 Task 분해를 담당하는 전문가 스킬입니다.

## 역할

1. **요구사항 분석**: 추상적 요구사항을 구체적 비즈니스 규칙으로 변환
2. **영향도 분석**: Serena MCP로 기존 코드 검색, 수정 vs 신규 판단
3. **Task 분해**: 컨텍스트 메모리 크기에 맞게 작업 분할

## 활성화 시점

- `/epic` 커맨드 실행 시
- 기능 기획 요청 시

## 핵심 원칙

### 1. 질문 기반 요구사항 도출

```markdown
## 기본 질문 (모든 기능)
1. 이 기능의 주요 사용자는 누구인가요?
2. 성공 기준(완료 조건)은 무엇인가요?
3. 예외 상황은 어떻게 처리하나요?

## 도메인별 추가 질문
- 주문: 취소 가능 상태, 부분 취소, 환불 방식
- 결제: 결제 수단, 실패 처리, 동시성
- 인증: 인증 방식, 세션 관리, 권한 체계
```

### 2. 영향도 분석 (Serena MCP)

```python
# 심볼 검색
mcp__serena__find_symbol(name_path_pattern="{Feature}")
mcp__serena__find_symbol(name_path_pattern="{Feature}Domain")

# 참조 검색
mcp__serena__find_referencing_symbols(name_path="{Feature}")

# 패턴 검색
mcp__serena__search_for_pattern(substring_pattern="class.*{Feature}")
```

### 3. Task 분해 기준

| 기준 | 제한 |
|------|------|
| 토큰 크기 | ≤15,000 tokens |
| 파일 수 | ≤5개 |
| 변경 라인 | ≤500줄 |

### 4. 분해 전략

| 상황 | 분해 방법 |
|------|----------|
| 새로운 Aggregate | Domain Task 별도 분리 |
| CRUD 전체 구현 | 레이어별 분리 |
| 복잡한 비즈니스 로직 | 기능 단위 분리 |
| 기존 코드 대량 수정 | 파일 단위 분리 |

## 출력 형식

### 비즈니스 규칙 테이블

```markdown
| ID | 규칙 | 상세 |
|----|------|------|
| BR-001 | 취소 가능 상태 | PLACED, CONFIRMED만 취소 가능 |
| BR-002 | 취소 범위 | 전체 취소만 지원 |
```

### Task 목록

```markdown
### TASK-001: [Domain] Order Aggregate 구현
- **레이어**: Domain
- **타입**: 신규 생성
- **예상 크기**: ~8K tokens
- **파일**:
  - Order.java (신규)
  - OrderStatus.java (신규)
- **의존성**: 없음
```

### 의존성 그래프

```
TASK-001 (Domain)
    ↓
TASK-002 (Application)
    ↓
TASK-003 (Persistence) ─→ TASK-004 (REST API)
```

## Knowledge Base 참조

Task 분해 시 각 레이어 규칙 참조:

- `@knowledge/rules/domain-rules.md`
- `@knowledge/rules/application-rules.md`
- `@knowledge/rules/persistence-rules.md`
- `@knowledge/rules/rest-api-rules.md`

## Serena Memory 저장

분석 결과는 항상 Serena Memory에 저장:

```python
mcp__serena__write_memory(
    memory_file_name="epic-{feature}",
    content="..."
)
```

## 관련 스킬

- **implementer**: Task 실제 구현
- **reviewer**: 구현 결과 검토
