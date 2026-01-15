#!/bin/bash
# Work Mode Manager
# /work 모드 시작/종료 관리

set -e

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
STATE_DIR="$PROJECT_ROOT/.claude/state"
WORK_MODE_FILE="$STATE_DIR/work-mode"

ACTION="$1"
JIRA_KEY="$2"
TASK_DESC="$3"

mkdir -p "$STATE_DIR"

case "$ACTION" in
    start)
        if [[ -z "$JIRA_KEY" ]]; then
            echo "❌ Usage: work-mode-manager.sh start <JIRA_KEY> [TASK_DESC]"
            exit 1
        fi

        # 기존 브랜치 확인
        CURRENT_BRANCH=$(git branch --show-current)

        # feature 브랜치 생성 (없으면)
        FEATURE_BRANCH="feature/${JIRA_KEY}"
        if [[ "$CURRENT_BRANCH" != "$FEATURE_BRANCH"* ]]; then
            # 기존 feature 브랜치가 있으면 checkout, 없으면 생성
            if git show-ref --verify --quiet "refs/heads/$FEATURE_BRANCH" 2>/dev/null; then
                git checkout "$FEATURE_BRANCH"
            else
                git checkout -b "$FEATURE_BRANCH"
            fi
        fi

        # Work 모드 활성화
        echo "$JIRA_KEY" > "$WORK_MODE_FILE"
        echo "${TASK_DESC:-$JIRA_KEY 작업}" >> "$WORK_MODE_FILE"

        echo ""
        echo "🚀 Work Mode Started"
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        echo "  📋 Jira: $JIRA_KEY"
        echo "  🔀 Branch: $(git branch --show-current)"
        echo "  📝 Auto-commit: 활성화됨 (30초 간격)"
        echo ""
        ;;

    stop)
        if [[ ! -f "$WORK_MODE_FILE" ]]; then
            echo "ℹ️ Work 모드가 활성화되어 있지 않습니다."
            exit 0
        fi

        JIRA_KEY=$(cat "$WORK_MODE_FILE" | head -1)

        # WIP 커밋들 카운트
        WIP_COUNT=$(git log --oneline | grep -c "^[a-f0-9]* WIP: $JIRA_KEY" 2>/dev/null || echo "0")

        rm -f "$WORK_MODE_FILE"
        rm -f "$STATE_DIR/last-auto-commit"

        echo ""
        echo "⏹️ Work Mode Stopped"
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        echo "  📋 Jira: $JIRA_KEY"
        echo "  📝 WIP Commits: $WIP_COUNT"
        echo "  💡 Tip: /ship으로 WIP 커밋을 정리하세요"
        echo ""
        ;;

    status)
        if [[ -f "$WORK_MODE_FILE" ]]; then
            JIRA_KEY=$(cat "$WORK_MODE_FILE" | head -1)
            TASK_DESC=$(cat "$WORK_MODE_FILE" | tail -1)
            WIP_COUNT=$(git log --oneline | grep -c "^[a-f0-9]* WIP: $JIRA_KEY" 2>/dev/null || echo "0")

            echo ""
            echo "🔄 Work Mode Active"
            echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
            echo "  📋 Jira: $JIRA_KEY"
            echo "  📝 Task: $TASK_DESC"
            echo "  🔀 Branch: $(git branch --show-current)"
            echo "  💾 WIP Commits: $WIP_COUNT"
            echo ""
        else
            echo "ℹ️ Work 모드 비활성화 상태"
        fi
        ;;

    *)
        echo "Usage: work-mode-manager.sh <start|stop|status> [JIRA_KEY] [TASK_DESC]"
        exit 1
        ;;
esac

exit 0
