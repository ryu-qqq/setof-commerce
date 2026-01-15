#!/bin/bash
# Rule Checker Hook
# Java 파일 수정 후 백그라운드에서 규칙 검증 실행

set -e

# 입력 파싱 (tool_input JSON)
TOOL_INPUT="$1"

# file_path 추출
FILE_PATH=$(echo "$TOOL_INPUT" | grep -o '"file_path"[[:space:]]*:[[:space:]]*"[^"]*"' | sed 's/.*"file_path"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/' 2>/dev/null || echo "")

# Java 파일이 아니면 종료
if [[ ! "$FILE_PATH" =~ \.java$ ]]; then
    exit 0
fi

# 파일이 존재하지 않으면 종료
if [[ ! -f "$FILE_PATH" ]]; then
    exit 0
fi

# 레이어 식별
LAYER=""
if [[ "$FILE_PATH" =~ /domain/ ]]; then
    LAYER="domain"
elif [[ "$FILE_PATH" =~ /application/ ]]; then
    LAYER="application"
elif [[ "$FILE_PATH" =~ /persistence/ ]] || [[ "$FILE_PATH" =~ /adapter/out/ ]]; then
    LAYER="persistence"
elif [[ "$FILE_PATH" =~ /rest/ ]] || [[ "$FILE_PATH" =~ /adapter/in/ ]]; then
    LAYER="rest-api"
fi

if [[ -z "$LAYER" ]]; then
    exit 0
fi

# Zero-Tolerance 패턴 검사
VIOLATIONS=""

# Domain Layer 검사
if [[ "$LAYER" == "domain" ]]; then
    # AGG-001: Lombok 금지
    if grep -qE '@(Data|Getter|Setter|Builder|Value|AllArgsConstructor|NoArgsConstructor)' "$FILE_PATH" 2>/dev/null; then
        VIOLATIONS="${VIOLATIONS}AGG-001:Lombok 사용 금지\n"
    fi

    # AGG-014: Law of Demeter (getter 체이닝)
    if grep -qE '\.[a-z]+[A-Z][a-zA-Z]*\(\)\.[a-z]+[A-Z][a-zA-Z]*\(\)' "$FILE_PATH" 2>/dev/null; then
        VIOLATIONS="${VIOLATIONS}AGG-014:Law of Demeter 위반 (getter 체이닝)\n"
    fi
fi

# Application Layer 검사
if [[ "$LAYER" == "application" ]]; then
    # C-001: Lombok 금지
    if grep -qE '@(Data|Getter|Setter|Builder)' "$FILE_PATH" 2>/dev/null; then
        VIOLATIONS="${VIOLATIONS}C-001:Lombok 사용 금지\n"
    fi
fi

# Persistence Layer 검사
if [[ "$LAYER" == "persistence" ]]; then
    # ENT-002: Long FK 전략 (JPA 관계 어노테이션 금지)
    if grep -qE '@(ManyToOne|OneToMany|OneToOne|ManyToMany)' "$FILE_PATH" 2>/dev/null; then
        VIOLATIONS="${VIOLATIONS}ENT-002:Long FK 전략 위반 (JPA 관계 어노테이션 금지)\n"
    fi
fi

# REST API Layer 검사
if [[ "$LAYER" == "rest-api" ]]; then
    # CTR-005: Controller에 @Transactional 금지
    if grep -qE 'Controller' "$FILE_PATH" 2>/dev/null && grep -qE '@Transactional' "$FILE_PATH" 2>/dev/null; then
        VIOLATIONS="${VIOLATIONS}CTR-005:Controller에 @Transactional 금지\n"
    fi
fi

# 위반 발견 시 출력
if [[ -n "$VIOLATIONS" ]]; then
    echo ""
    echo "🚨 Rule Violations in: $FILE_PATH"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo -e "$VIOLATIONS" | while read -r line; do
        if [[ -n "$line" ]]; then
            CODE=$(echo "$line" | cut -d: -f1)
            DESC=$(echo "$line" | cut -d: -f2)
            echo "  ❌ [$CODE] $DESC"
        fi
    done
    echo ""
    echo "📖 참조: .claude/knowledge/rules/${LAYER}-rules.md"
    echo "💡 예제: .claude/knowledge/examples/${LAYER}-examples.md"
fi

exit 0
