#!/bin/bash
# Auto-Commit Hook
# /work 모드 활성화 시 파일 변경마다 WIP 커밋 수행
# 설정: .claude/state/work-mode 파일이 존재할 때만 동작

set -e

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
STATE_DIR="$PROJECT_ROOT/.claude/state"
WORK_MODE_FILE="$STATE_DIR/work-mode"
LAST_COMMIT_FILE="$STATE_DIR/last-auto-commit"

# /work 모드가 아니면 종료
if [[ ! -f "$WORK_MODE_FILE" ]]; then
    exit 0
fi

# 입력 파싱 (tool_input JSON)
TOOL_INPUT="$1"

# file_path 추출
FILE_PATH=$(echo "$TOOL_INPUT" | grep -o '"file_path"[[:space:]]*:[[:space:]]*"[^"]*"' | sed 's/.*"file_path"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/' 2>/dev/null || echo "")

# 파일이 없으면 종료
if [[ -z "$FILE_PATH" ]]; then
    exit 0
fi

# .claude, .git, build 디렉토리 변경은 무시
if [[ "$FILE_PATH" =~ \.claude/ ]] || [[ "$FILE_PATH" =~ \.git/ ]] || [[ "$FILE_PATH" =~ /build/ ]]; then
    exit 0
fi

# 최소 커밋 간격 체크 (30초)
MIN_INTERVAL=30
if [[ -f "$LAST_COMMIT_FILE" ]]; then
    LAST_COMMIT=$(cat "$LAST_COMMIT_FILE")
    NOW=$(date +%s)
    DIFF=$((NOW - LAST_COMMIT))
    if [[ $DIFF -lt $MIN_INTERVAL ]]; then
        exit 0
    fi
fi

# Work 모드 정보 읽기
JIRA_KEY=$(cat "$WORK_MODE_FILE" | head -1)
TASK_DESC=$(cat "$WORK_MODE_FILE" | tail -1)

# Git 상태 확인
cd "$PROJECT_ROOT"

# 변경 사항이 있는지 확인
if git diff --quiet && git diff --staged --quiet; then
    exit 0
fi

# WIP 커밋 수행
git add -A

# 커밋 메시지 생성
COMMIT_MSG="WIP: $JIRA_KEY - $(basename "$FILE_PATH" | sed 's/\.[^.]*$//')"

# 커밋 실행 (quiet mode)
git commit -m "$COMMIT_MSG" --no-verify --quiet 2>/dev/null || true

# 마지막 커밋 시간 기록
mkdir -p "$STATE_DIR"
date +%s > "$LAST_COMMIT_FILE"

# 상태 출력 (짧게)
echo "📝 WIP: $JIRA_KEY"

exit 0
