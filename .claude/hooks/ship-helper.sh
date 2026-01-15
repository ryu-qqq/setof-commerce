#!/bin/bash
# Ship Helper
# WIP 커밋 정리 및 최종 커밋/푸시 지원

set -e

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
STATE_DIR="$PROJECT_ROOT/.claude/state"
WORK_MODE_FILE="$STATE_DIR/work-mode"

ACTION="$1"
COMMIT_MSG="$2"

cd "$PROJECT_ROOT"

case "$ACTION" in
    squash)
        # Jira 키 확인
        if [[ -f "$WORK_MODE_FILE" ]]; then
            JIRA_KEY=$(cat "$WORK_MODE_FILE" | head -1)
        else
            # 브랜치 이름에서 추출 시도
            CURRENT_BRANCH=$(git branch --show-current)
            JIRA_KEY=$(echo "$CURRENT_BRANCH" | grep -oE '[A-Z]+-[0-9]+' | head -1 || echo "")
        fi

        if [[ -z "$JIRA_KEY" ]]; then
            echo "❌ Jira 키를 찾을 수 없습니다."
            exit 1
        fi

        # WIP 커밋 수 확인
        WIP_COUNT=$(git log --oneline origin/main..HEAD | grep -c "WIP: $JIRA_KEY" 2>/dev/null || echo "0")

        if [[ "$WIP_COUNT" -eq 0 ]]; then
            echo "ℹ️ 정리할 WIP 커밋이 없습니다."
            exit 0
        fi

        echo ""
        echo "📦 WIP Commits to Squash: $WIP_COUNT"
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        git log --oneline origin/main..HEAD | grep "WIP: $JIRA_KEY" | head -10
        echo ""

        # main 브랜치 기준으로 rebase
        TOTAL_COMMITS=$(git rev-list --count origin/main..HEAD)

        if [[ "$TOTAL_COMMITS" -gt 0 ]]; then
            echo "💡 Interactive rebase 시작..."
            echo "   모든 커밋을 'squash' 또는 'fixup'으로 변경하세요."
            echo ""
        fi
        ;;

    commit)
        if [[ -z "$COMMIT_MSG" ]]; then
            echo "❌ Usage: ship-helper.sh commit \"커밋 메시지\""
            exit 1
        fi

        # Jira 키 확인
        if [[ -f "$WORK_MODE_FILE" ]]; then
            JIRA_KEY=$(cat "$WORK_MODE_FILE" | head -1)
        else
            CURRENT_BRANCH=$(git branch --show-current)
            JIRA_KEY=$(echo "$CURRENT_BRANCH" | grep -oE '[A-Z]+-[0-9]+' | head -1 || echo "")
        fi

        # 변경 사항 스테이징
        git add -A

        # 커밋 메시지 형식화
        FORMATTED_MSG="$COMMIT_MSG"

        # Co-Authored-By 추가
        FULL_MSG="$FORMATTED_MSG

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>"

        # 커밋 실행
        git commit -m "$FULL_MSG"

        echo ""
        echo "✅ Committed: $FORMATTED_MSG"
        echo ""
        ;;

    push)
        CURRENT_BRANCH=$(git branch --show-current)

        # main/master 브랜치 푸시 방지
        if [[ "$CURRENT_BRANCH" == "main" ]] || [[ "$CURRENT_BRANCH" == "master" ]]; then
            echo "❌ main/master 브랜치에 직접 푸시할 수 없습니다."
            exit 1
        fi

        # 푸시 실행
        git push -u origin "$CURRENT_BRANCH"

        echo ""
        echo "🚀 Pushed to: origin/$CURRENT_BRANCH"
        echo ""
        ;;

    pr)
        # PR 제목과 본문
        PR_TITLE="$2"
        PR_BODY="$3"

        if [[ -z "$PR_TITLE" ]]; then
            # 마지막 커밋 메시지 사용
            PR_TITLE=$(git log -1 --pretty=%s)
        fi

        CURRENT_BRANCH=$(git branch --show-current)

        # Jira 링크 추출
        JIRA_KEY=$(echo "$CURRENT_BRANCH" | grep -oE '[A-Z]+-[0-9]+' | head -1 || echo "")

        echo ""
        echo "🔗 PR Creation Info"
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        echo "  Title: $PR_TITLE"
        echo "  Branch: $CURRENT_BRANCH → main"
        if [[ -n "$JIRA_KEY" ]]; then
            echo "  Jira: $JIRA_KEY"
        fi
        echo ""
        echo "💡 gh pr create 명령어로 PR을 생성하세요."
        echo ""
        ;;

    status)
        echo ""
        echo "📊 Git Status Summary"
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
        echo "  Branch: $(git branch --show-current)"
        echo "  Commits ahead: $(git rev-list --count origin/main..HEAD 2>/dev/null || echo "N/A")"
        echo "  Staged files: $(git diff --staged --name-only | wc -l | tr -d ' ')"
        echo "  Modified files: $(git diff --name-only | wc -l | tr -d ' ')"
        echo "  Untracked files: $(git ls-files --others --exclude-standard | wc -l | tr -d ' ')"
        echo ""
        ;;

    *)
        echo "Usage: ship-helper.sh <squash|commit|push|pr|status> [args...]"
        exit 1
        ;;
esac

exit 0
