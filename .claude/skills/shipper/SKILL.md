---
description: 배포 전문가. Git 커밋/푸시, Jira 상태 업데이트, PR 생성.
tags: [git, jira, deployment]
activationCommands: ["/ship", "/jira-create", "/jira-fetch"]
---

# Shipper Skill

Git 커밋, 푸시, Jira 상태 관리를 담당하는 전문가 스킬입니다.

## 역할

1. **Git 관리**: 브랜치 생성, 커밋, 푸시
2. **Jira 연동**: 이슈 생성, 상태 업데이트 (MCP 활용)
3. **PR 생성**: Pull Request 자동 생성
4. **WIP 관리**: 작업 중 커밋 정리

## 활성화 시점

- `/ship` 커맨드 실행 시
- `/jira-create`, `/jira-fetch` 실행 시
- Git 작업 요청 시

## Git 워크플로우

### 1. 브랜치 네이밍

```bash
# 패턴: feature/{이슈키}-{짧은설명}
feature/PROJ-456-order-aggregate
feature/PROJ-457-cancel-usecase

# 버그 수정
fix/PROJ-789-null-pointer

# 리팩토링
refactor/PROJ-321-extract-service
```

### 2. 커밋 메시지 규칙

```bash
# 형식
{type}: [{이슈키}] {제목}

{본문}

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
```

### 커밋 타입

| 타입 | 용도 | 예시 |
|------|------|------|
| `feat:` | 새 기능 | `feat: [PROJ-456] Order Aggregate 구현` |
| `fix:` | 버그 수정 | `fix: [PROJ-789] NPE 수정` |
| `refactor:` | 리팩토링 | `refactor: [PROJ-321] 서비스 분리` |
| `test:` | 테스트 | `test: [PROJ-456] 주문 취소 테스트 추가` |
| `docs:` | 문서 | `docs: README 업데이트` |
| `chore:` | 빌드/설정 | `chore: Gradle 버전 업데이트` |

### 3. WIP 커밋 관리

```bash
# 작업 중 자동 커밋 (Auto-commit Hook)
WIP: PROJ-456 - Order 기본 구조
WIP: PROJ-456 - OrderStatus 추가
WIP: PROJ-456 - 테스트 작성

# Ship 시 Squash
git rebase -i HEAD~3
# → 하나의 의미있는 커밋으로 합침

feat: [PROJ-456] Order Aggregate 구현

- Order 도메인 모델 생성
- OrderStatus Value Object 추가
- 단위 테스트 작성
```

## Jira MCP 연동

### Epic 생성

```python
# Jira MCP 호출
jira_create_issue(
    project="PROJ",
    issue_type="Epic",
    summary="결제 기능 구현",
    description="""
    ## 개요
    주문 결제 기능을 구현합니다.

    ## 범위
    - 결제 요청
    - 결제 확인
    - 환불 처리
    """,
    labels=["backend", "payment"],
    components=["payment-service"]
)
```

### Sub-task 생성

```python
jira_create_issue(
    project="PROJ",
    issue_type="Sub-task",
    parent="PROJ-123",  # Epic 키
    summary="[Domain] Order Aggregate 구현",
    description="""
    ## 작업 내용
    - Order Aggregate Root 생성
    - OrderStatus Value Object
    - 주문 생성/취소 로직

    ## 예상 크기
    ~8K tokens

    ## 파일
    - Order.java
    - OrderStatus.java
    - OrderTest.java
    """,
    story_points=3
)
```

### 상태 업데이트

```python
# 작업 시작
jira_transition_issue(
    issue_key="PROJ-456",
    transition="In Progress"
)

# 작업 완료
jira_transition_issue(
    issue_key="PROJ-456",
    transition="Done"
)

# 코멘트 추가
jira_add_comment(
    issue_key="PROJ-456",
    comment="""
    ## 완료 내용
    - Order Aggregate 구현 완료
    - 단위 테스트 95% 커버리지

    ## PR
    https://github.com/your-repo/pull/123
    """
)
```

### Task 조회

```python
# Epic 하위 Task 조회
jira_search_issues(
    jql="parent = PROJ-123 ORDER BY rank ASC"
)

# 내 할당 Task 조회
jira_search_issues(
    jql="assignee = currentUser() AND status = 'To Do' ORDER BY priority DESC"
)

# 현재 스프린트 Task
jira_search_issues(
    jql="sprint in openSprints() AND assignee = currentUser()"
)
```

## PR 생성

### GitHub CLI 활용

```bash
# PR 생성
gh pr create \
    --title "feat: [PROJ-456] Order Aggregate 구현" \
    --body "## 변경 사항
- Order 도메인 모델 생성
- OrderStatus Value Object 추가
- 단위 테스트 작성

## 테스트
- [x] 단위 테스트 통과
- [x] 통합 테스트 통과
- [x] ArchUnit 테스트 통과

## Jira
- [PROJ-456](https://your-jira.atlassian.net/browse/PROJ-456)
" \
    --base main \
    --head feature/PROJ-456-order-aggregate
```

### PR 템플릿

```markdown
## 변경 사항
{변경 내용 요약}

## 테스트
- [ ] 단위 테스트 통과
- [ ] 통합 테스트 통과
- [ ] ArchUnit 테스트 통과
- [ ] 정적 분석 통과 (Checkstyle, PMD, SpotBugs)

## 체크리스트
- [ ] Zero-Tolerance 규칙 준수
- [ ] Knowledge Base 컨벤션 준수
- [ ] 테스트 커버리지 80% 이상

## Jira
- [{이슈키}]({Jira URL})

## 스크린샷 (해당 시)
{API 변경이나 UI 변경 시 스크린샷}
```

## Serena Memory 연동

### 작업 상태 저장

```python
mcp__serena__write_memory(
    memory_file_name="task-PROJ-456",
    content="""
    # Task: PROJ-456

    ## 상태
    - 브랜치: feature/PROJ-456-order-aggregate
    - 커밋: 5개 (WIP 3, 최종 2)
    - Jira: In Progress

    ## Git Log
    - abc1234: feat: Order 기본 구조
    - def5678: feat: OrderStatus 추가
    ...
    """
)
```

### 작업 완료 시 정리

```python
# 완료된 Task Memory 삭제 또는 아카이브
mcp__serena__delete_memory(memory_file_name="task-PROJ-456")

# 또는 완료 기록 남기기
mcp__serena__write_memory(
    memory_file_name="completed-PROJ-456",
    content="완료: 2025-01-14, PR #123"
)
```

## 관련 스킬

- **planner**: 배포할 Task 정의
- **implementer**: 배포할 코드 구현
- **reviewer**: 배포 전 리뷰
