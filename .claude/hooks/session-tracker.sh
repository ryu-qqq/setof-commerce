#!/bin/bash
# Session Tracker Hook
# 진행 중인 작업 상태 표시 (Serena Memory 기반)

set -e

# Serena memory 디렉토리 확인
MEMORY_DIR=".claude/memory"

if [[ ! -d "$MEMORY_DIR" ]]; then
    exit 0
fi

# plan-* 파일 검색
ACTIVE_PLANS=$(find "$MEMORY_DIR" -name "plan-*.md" -type f 2>/dev/null | head -5)

if [[ -z "$ACTIVE_PLANS" ]]; then
    exit 0
fi

echo ""
echo "🔄 진행 중인 작업:"

for PLAN_FILE in $ACTIVE_PLANS; do
    PLAN_NAME=$(basename "$PLAN_FILE" .md | sed 's/plan-//')

    # 상태 확인 (파일 내용에서 Status 추출)
    STATUS=$(grep -m1 "Status:" "$PLAN_FILE" 2>/dev/null | sed 's/.*Status:[[:space:]]*//' || echo "진행 중")

    echo "  📝 $PLAN_NAME ($STATUS)"
done

echo ""
echo "💡 이어서 작업하려면: \"{feature} 작업 이어서 해줘\""
