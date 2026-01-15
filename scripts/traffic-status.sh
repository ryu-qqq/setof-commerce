#!/bin/bash
# ============================================================================
# SetOf Commerce Traffic Status Script
#
# 모든 서비스의 현재 트래픽 분배 상태를 확인합니다.
#
# 사용법:
#   ./scripts/traffic-status.sh --env prod
#   ./scripts/traffic-status.sh --env stage
#   ./scripts/traffic-status.sh --env all
# ============================================================================

set -euo pipefail

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

AWS_REGION="ap-northeast-2"

# Services to check
SERVICES=("legacy-api" "legacy-api-admin")

log_header() { echo -e "\n${CYAN}═══════════════════════════════════════════════════════════${NC}"; }
log_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
log_success() { echo -e "${GREEN}✅${NC} $1"; }
log_warn() { echo -e "${YELLOW}⚠️${NC} $1"; }
log_error() { echo -e "${RED}❌${NC} $1"; }

get_service_status() {
    local service=$1
    local env=$2

    # ALB 이름 결정
    local alb_name
    if [[ "$service" == "legacy-api-admin" ]]; then
        alb_name="setof-commerce-legacy-admin-alb-${env}"
    else
        alb_name="setof-commerce-legacy-alb-${env}"
    fi

    # ALB ARN 조회
    local alb_arn
    alb_arn=$(aws elbv2 describe-load-balancers \
        --region "$AWS_REGION" \
        --names "$alb_name" \
        --query 'LoadBalancers[0].LoadBalancerArn' \
        --output text 2>/dev/null || echo "")

    if [[ -z "$alb_arn" || "$alb_arn" == "None" ]]; then
        echo "  └─ ALB not found"
        return
    fi

    # Listener ARN 조회
    local listener_arn
    listener_arn=$(aws elbv2 describe-listeners \
        --region "$AWS_REGION" \
        --load-balancer-arn "$alb_arn" \
        --query "Listeners[?Port==\`443\`].ListenerArn | [0]" \
        --output text 2>/dev/null || echo "")

    if [[ -z "$listener_arn" || "$listener_arn" == "None" ]]; then
        listener_arn=$(aws elbv2 describe-listeners \
            --region "$AWS_REGION" \
            --load-balancer-arn "$alb_arn" \
            --query "Listeners[?Port==\`80\`].ListenerArn | [0]" \
            --output text 2>/dev/null || echo "")
    fi

    if [[ -z "$listener_arn" || "$listener_arn" == "None" ]]; then
        echo "  └─ Listener not found"
        return
    fi

    # Rules에서 트래픽 분배 확인
    local rules
    rules=$(aws elbv2 describe-rules \
        --region "$AWS_REGION" \
        --listener-arn "$listener_arn" \
        --query 'Rules[?IsDefault].Actions' \
        --output json 2>/dev/null)

    # 파싱 및 출력
    echo "$rules" | jq -r '
        .[][0] |
        if .ForwardConfig then
            .ForwardConfig.TargetGroups | sort_by(.Weight) | reverse | .[] |
            "  └─ \(.TargetGroupArn | split("/")[1]): \(.Weight)%"
        else
            "  └─ \(.TargetGroupArn | split("/")[1]): 100% (single target)"
        end
    ' 2>/dev/null || echo "  └─ Unable to parse rules"
}

check_target_health() {
    local service=$1
    local env=$2

    # Target Group 이름들
    local tg_names=()
    if [[ "$service" == "legacy-api" ]]; then
        tg_names+=("setof-commerce-legacy-tg-${env}")
        tg_names+=("setof-commerce-api-tg-${env}")
    else
        tg_names+=("setof-commerce-legacy-admin-tg-${env}")
        tg_names+=("setof-commerce-api-admin-tg-${env}")
    fi

    for tg_name in "${tg_names[@]}"; do
        local tg_arn
        tg_arn=$(aws elbv2 describe-target-groups \
            --region "$AWS_REGION" \
            --names "$tg_name" \
            --query 'TargetGroups[0].TargetGroupArn' \
            --output text 2>/dev/null || echo "")

        if [[ -z "$tg_arn" || "$tg_arn" == "None" ]]; then
            continue
        fi

        local health
        health=$(aws elbv2 describe-target-health \
            --region "$AWS_REGION" \
            --target-group-arn "$tg_arn" \
            --query 'TargetHealthDescriptions[].TargetHealth.State' \
            --output text 2>/dev/null)

        local healthy=0
        local total=0
        for state in $health; do
            ((total++))
            if [[ "$state" == "healthy" ]]; then
                ((healthy++))
            fi
        done

        if [[ $total -eq 0 ]]; then
            echo "  └─ $tg_name: no targets"
        elif [[ $healthy -eq $total ]]; then
            echo -e "  └─ $tg_name: ${GREEN}$healthy/$total healthy${NC}"
        elif [[ $healthy -gt 0 ]]; then
            echo -e "  └─ $tg_name: ${YELLOW}$healthy/$total healthy${NC}"
        else
            echo -e "  └─ $tg_name: ${RED}0/$total healthy${NC}"
        fi
    done
}

show_env_status() {
    local env=$1

    echo -e "${CYAN}📊 Environment: ${env^^}${NC}"
    echo "───────────────────────────────────────"

    for service in "${SERVICES[@]}"; do
        echo -e "${BLUE}🔹 Service: $service${NC}"

        echo "  Traffic Distribution:"
        get_service_status "$service" "$env"

        echo "  Target Health:"
        check_target_health "$service" "$env"

        echo ""
    done
}

main() {
    local env=""

    while [[ $# -gt 0 ]]; do
        case $1 in
            --env)
                env="$2"
                shift 2
                ;;
            -h|--help)
                echo "Usage: $(basename "$0") --env <stage|prod|all>"
                exit 0
                ;;
            *)
                echo "Unknown option: $1"
                exit 1
                ;;
        esac
    done

    if [[ -z "$env" ]]; then
        env="all"
    fi

    log_header
    echo -e "${CYAN}   SetOf Commerce Traffic Status Report${NC}"
    echo -e "${CYAN}   $(date '+%Y-%m-%d %H:%M:%S')${NC}"
    log_header

    if [[ "$env" == "all" ]]; then
        show_env_status "stage"
        log_header
        show_env_status "prod"
    else
        show_env_status "$env"
    fi

    log_header
    echo ""
    echo "💡 트래픽 전환 명령어:"
    echo "   ./scripts/traffic-shift.sh --service legacy-api --canary 10 --env stage"
    echo "   ./scripts/traffic-shift.sh --service legacy-api --rollback --env prod"
    echo ""
}

main "$@"
