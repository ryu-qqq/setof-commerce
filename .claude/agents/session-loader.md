---
name: session-loader
description: 세션 시작 시 프로젝트 컨텍스트 로딩. Serena 프로젝트 활성화 + Spring Standards 규칙 인덱스 캐싱. 자동으로 사용.
tools: Read, Write, Glob, Grep, Bash
model: opus
---

# Session Loader Agent

세션 시작 시 프로젝트 컨텍스트를 로딩합니다.
Serena 프로젝트 활성화, 메모리 복원, Spring Standards 규칙 인덱스 캐싱을 수행합니다.

## 핵심 원칙

> **Serena 활성화 → 메모리 복원 → 규칙 인덱스 캐싱 → 컨텍스트 요약 반환**

---

## 실행 워크플로우

### Phase 1: Serena 프로젝트 활성화

```python
# 1. 프로젝트 활성화
activate_project(directory=current_working_directory)

# 2. 기존 메모리 목록 조회
memories = list_memories()
```

### Phase 2: 메모리 복원

```python
# 주요 메모리 키 확인 및 로드
key_memories = [
    "spring_rules_index",       # 규칙 인덱스
    "current_plan",             # 진행 중인 계획
    "epic-*",                   # Epic 관련
    "jira-*",                   # Jira 관련
    "session_summary",          # 이전 세션 요약
    "checkpoint_*",             # 체크포인트
]

for memory in memories:
    if matches_any(memory.name, key_memories):
        content = read_memory(memory.name)
        # 요약에 포함
```

### Phase 3: Spring Standards 규칙 인덱스 캐싱

```python
# 1. 캐시 확인
cached_index = read_memory("spring_rules_index")

if cached_index and not args.refresh:
    rules_index = cached_index
else:
    # 2. MCP에서 조회
    rules_index = list_rules()  # Spring Standards MCP

    # 3. Serena 메모리에 캐싱
    write_memory("spring_rules_index", rules_index)
```

### Phase 4: 프로젝트 현황 스캔

```python
# 1. Git 상태 확인
git_status = bash("git status --short")
git_branch = bash("git branch --show-current")

# 2. 최근 커밋 확인
recent_commits = bash("git log --oneline -5")

# 3. 진행 중인 작업 확인 (plans 디렉토리)
plans = Glob(".claude/plans/*.md")
```

### Phase 5: 컨텍스트 요약 반환

```python
# 메인 에이전트에게 반환할 요약
summary = f"""
✅ 프로젝트 활성화 완료

📋 브랜치: {git_branch}
📝 미커밋 변경: {change_count}개 파일
📊 최근 커밋: {recent_commits[0]}

🧠 복원된 메모리:
{memory_summary}

📚 규칙 인덱스: {rules_count}개 규칙 로드됨

📂 진행 중인 계획:
{plans_summary}
"""
```

---

## 옵션

| 옵션 | 설명 |
|------|------|
| (없음) | 기본 로딩 (캐시 우선) |
| `--refresh` | 규칙 인덱스 캐시 무시하고 새로 조회 |
| `--full` | 모든 메모리 상세 내용 포함 |
| `--status-only` | 프로젝트 현황만 (메모리/규칙 로드 안함) |

---

## 규칙 Lazy Loading 패턴

세션 로드 시 **인덱스만** 캐싱하고, 개별 규칙은 개발 중 필요할 때 조회합니다:

```python
# 개발 중 특정 규칙 필요 시 (메인 에이전트가 수행)
cached = read_memory(f"spring_rule_{code}")
if cached:
    return cached

# 캐시 미스 → MCP 조회 후 캐싱
rule_detail = get_rule(code)
write_memory(f"spring_rule_{code}", rule_detail)
return rule_detail
```

---

## 출력 형식

```
🔄 세션 로딩 시작...

✅ Serena 프로젝트 활성화
✅ 메모리 복원: {n}개 항목
✅ 규칙 인덱스: {n}개 규칙 캐싱됨

📋 현재 브랜치: feature/persistence-legacy
📝 미커밋 변경: 42개 파일
📊 최근 커밋: dd89f098 chore: trigger rebuild

🧠 복원된 컨텍스트:
   - current_plan: "Seller Admin 마이그레이션"
   - epic-seller-admin: Epic 진행 중
   - jira-AESA-123: 3/7 Task 완료

📂 진행 중인 계획:
   - seller-admin-application.md
   - admin-auth-brand-category.md

🚀 세션 준비 완료
```
