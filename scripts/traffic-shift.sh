#!/bin/bash
# ============================================================================
# SetOf Commerce Traffic Shift Script
#
# Strangler Fig Pattern: Legacy(v1) → New(v2) 점진적 트래픽 전환
#
# 사용법:
#   ./scripts/traffic-shift.sh --service legacy-api --target-weight 10 --env prod
#   ./scripts/traffic-shift.sh --service legacy-api --canary 10 --env stage
#   ./scripts/traffic-shift.sh --service legacy-api --full-cutover --env prod
#   ./scripts/traffic-shift.sh --service legacy-api --rollback --env prod
#
# 트래픽 전환 단계 (권장):
#   1. 0%  → Canary 테스트 준비
#   2. 10% → Canary 배포 (에러율, 지연시간 모니터링)
#   3. 30% → 점진적 확장 (30분 관찰)
#   4. 50% → 중간 단계 (1시간 관찰)
#   5. 80% → 대부분 전환 (2시간 관찰)
#   6. 100% → 완전 전환 (Legacy 제거 준비)
# ============================================================================

set -euo pipefail

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default values
AWS_REGION="ap-northeast-2"
DRY_RUN=false
SKIP_HEALTH_CHECK=false
VERBOSE=false

# Service configurations
declare -A SERVICE_CONFIG=(
    # [service]="listener_arn|legacy_tg_arn|new_tg_arn|health_endpoint"
    # These will be populated from AWS or environment variables
)

# Logging functions
log_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
log_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1" >&2; }

# Usage
usage() {
    cat << EOF
Usage: $(basename "$0") [OPTIONS]

옵션:
    --service <name>        서비스 이름 (legacy-api, legacy-api-admin)
    --env <environment>     환경 (stage, prod)
    --target-weight <0-100> New 서버로 전환할 트래픽 비율 (%)
    --canary <percentage>   Canary 배포 시작 (권장: 10%)
    --full-cutover          100% 트래픽 전환
    --rollback              Legacy로 즉시 롤백 (100% → 0%)
    --status                현재 트래픽 분배 상태 확인
    --dry-run               실제 실행 없이 시뮬레이션
    --skip-health-check     헬스체크 스킵 (긴급 상황용)
    --verbose               상세 로그 출력
    -h, --help              이 도움말 표시

예제:
    # Stage에서 10% Canary 시작
    $(basename "$0") --service legacy-api --canary 10 --env stage

    # Prod에서 30%로 확장
    $(basename "$0") --service legacy-api --target-weight 30 --env prod

    # 100% 완전 전환
    $(basename "$0") --service legacy-api --full-cutover --env prod

    # 긴급 롤백
    $(basename "$0") --service legacy-api --rollback --env prod

EOF
    exit 1
}

# AWS 리소스 검색
discover_resources() {
    local service=$1
    local env=$2

    log_info "AWS 리소스 검색 중 (service: $service, env: $env)..."

    # ALB 이름 패턴
    local alb_name="setof-commerce-${service/legacy-/legacy-}-alb-${env}"
    if [[ "$service" == "legacy-api-admin" ]]; then
        alb_name="setof-commerce-legacy-admin-alb-${env}"
    elif [[ "$service" == "legacy-api" ]]; then
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
        log_error "ALB를 찾을 수 없습니다: $alb_name"
        return 1
    fi

    # Listener ARN 조회 (HTTPS:443)
    local listener_arn
    listener_arn=$(aws elbv2 describe-listeners \
        --region "$AWS_REGION" \
        --load-balancer-arn "$alb_arn" \
        --query "Listeners[?Port==\`443\`].ListenerArn | [0]" \
        --output text 2>/dev/null || echo "")

    if [[ -z "$listener_arn" || "$listener_arn" == "None" ]]; then
        # HTTP:80 fallback
        listener_arn=$(aws elbv2 describe-listeners \
            --region "$AWS_REGION" \
            --load-balancer-arn "$alb_arn" \
            --query "Listeners[?Port==\`80\`].ListenerArn | [0]" \
            --output text 2>/dev/null || echo "")
    fi

    if [[ -z "$listener_arn" || "$listener_arn" == "None" ]]; then
        log_error "Listener를 찾을 수 없습니다"
        return 1
    fi

    # Target Group ARN 조회
    local legacy_tg_name="setof-commerce-${service/legacy-api/legacy}-tg-${env}"
    local new_tg_name="setof-commerce-${service/legacy-/}-tg-${env}"

    if [[ "$service" == "legacy-api" ]]; then
        legacy_tg_name="setof-commerce-legacy-tg-${env}"
        new_tg_name="setof-commerce-api-tg-${env}"
    elif [[ "$service" == "legacy-api-admin" ]]; then
        legacy_tg_name="setof-commerce-legacy-admin-tg-${env}"
        new_tg_name="setof-commerce-api-admin-tg-${env}"
    fi

    local legacy_tg_arn
    legacy_tg_arn=$(aws elbv2 describe-target-groups \
        --region "$AWS_REGION" \
        --names "$legacy_tg_name" \
        --query 'TargetGroups[0].TargetGroupArn' \
        --output text 2>/dev/null || echo "")

    local new_tg_arn
    new_tg_arn=$(aws elbv2 describe-target-groups \
        --region "$AWS_REGION" \
        --names "$new_tg_name" \
        --query 'TargetGroups[0].TargetGroupArn' \
        --output text 2>/dev/null || echo "")

    # 결과 저장
    SERVICE_LISTENER_ARN="$listener_arn"
    SERVICE_LEGACY_TG_ARN="$legacy_tg_arn"
    SERVICE_NEW_TG_ARN="$new_tg_arn"
    SERVICE_ALB_ARN="$alb_arn"

    if $VERBOSE; then
        log_info "ALB ARN: $alb_arn"
        log_info "Listener ARN: $listener_arn"
        log_info "Legacy TG ARN: $legacy_tg_arn"
        log_info "New TG ARN: $new_tg_arn"
    fi

    return 0
}

# 현재 트래픽 분배 상태 확인
get_current_status() {
    local listener_arn=$1

    log_info "현재 트래픽 분배 상태 조회 중..."

    local rules
    rules=$(aws elbv2 describe-rules \
        --region "$AWS_REGION" \
        --listener-arn "$listener_arn" \
        --query 'Rules[?!IsDefault].{Priority:Priority,Actions:Actions}' \
        --output json 2>/dev/null)

    if [[ -z "$rules" || "$rules" == "[]" ]]; then
        # Default rule 확인
        rules=$(aws elbv2 describe-rules \
            --region "$AWS_REGION" \
            --listener-arn "$listener_arn" \
            --query 'Rules[?IsDefault].{Priority:Priority,Actions:Actions}' \
            --output json 2>/dev/null)
    fi

    echo "$rules" | jq -r '
        .[0].Actions[] |
        select(.Type == "forward") |
        if .ForwardConfig then
            .ForwardConfig.TargetGroups[] |
            "  - \(.TargetGroupArn | split("/")[1]): \(.Weight // 100)%"
        else
            "  - \(.TargetGroupArn | split("/")[1]): 100%"
        end
    ' 2>/dev/null || echo "  - Unable to parse rules"
}

# 헬스체크 수행
check_target_health() {
    local tg_arn=$1
    local tg_name

    tg_name=$(echo "$tg_arn" | awk -F'/' '{print $2}')
    log_info "Target Group 헬스체크: $tg_name"

    local health_status
    health_status=$(aws elbv2 describe-target-health \
        --region "$AWS_REGION" \
        --target-group-arn "$tg_arn" \
        --query 'TargetHealthDescriptions[].TargetHealth.State' \
        --output text 2>/dev/null)

    local healthy_count=0
    local total_count=0

    for status in $health_status; do
        ((total_count++))
        if [[ "$status" == "healthy" ]]; then
            ((healthy_count++))
        fi
    done

    if [[ $total_count -eq 0 ]]; then
        log_warn "$tg_name: 등록된 타겟 없음"
        return 1
    elif [[ $healthy_count -eq 0 ]]; then
        log_error "$tg_name: 모든 타겟이 unhealthy ($healthy_count/$total_count)"
        return 1
    elif [[ $healthy_count -lt $total_count ]]; then
        log_warn "$tg_name: 일부 타겟이 unhealthy ($healthy_count/$total_count healthy)"
        return 0
    else
        log_success "$tg_name: 모든 타겟 healthy ($healthy_count/$total_count)"
        return 0
    fi
}

# 트래픽 전환 실행
shift_traffic() {
    local listener_arn=$1
    local legacy_tg_arn=$2
    local new_tg_arn=$3
    local new_weight=$4

    local legacy_weight=$((100 - new_weight))

    log_info "트래픽 전환 중: Legacy ${legacy_weight}% → New ${new_weight}%"

    # New Target Group이 없거나 0% 전환인 경우
    if [[ -z "$new_tg_arn" || "$new_tg_arn" == "None" ]]; then
        if [[ $new_weight -gt 0 ]]; then
            log_error "New Target Group이 존재하지 않습니다. 먼저 New 서비스를 배포하세요."
            return 1
        fi
        # 0% 전환 (Legacy only)
        new_weight=0
        legacy_weight=100
    fi

    # Weighted Forward Config 구성
    local forward_config
    if [[ $new_weight -eq 0 ]]; then
        # Legacy only
        forward_config=$(cat << EOF
{
    "TargetGroups": [
        {
            "TargetGroupArn": "$legacy_tg_arn",
            "Weight": 100
        }
    ]
}
EOF
)
    elif [[ $new_weight -eq 100 ]]; then
        # New only
        forward_config=$(cat << EOF
{
    "TargetGroups": [
        {
            "TargetGroupArn": "$new_tg_arn",
            "Weight": 100
        }
    ]
}
EOF
)
    else
        # Weighted split
        forward_config=$(cat << EOF
{
    "TargetGroups": [
        {
            "TargetGroupArn": "$legacy_tg_arn",
            "Weight": $legacy_weight
        },
        {
            "TargetGroupArn": "$new_tg_arn",
            "Weight": $new_weight
        }
    ]
}
EOF
)
    fi

    if $DRY_RUN; then
        log_info "[DRY-RUN] 다음 설정이 적용됩니다:"
        echo "$forward_config" | jq .
        return 0
    fi

    # Default rule 수정
    local rule_arn
    rule_arn=$(aws elbv2 describe-rules \
        --region "$AWS_REGION" \
        --listener-arn "$listener_arn" \
        --query 'Rules[?IsDefault].RuleArn | [0]' \
        --output text 2>/dev/null)

    if [[ -z "$rule_arn" || "$rule_arn" == "None" ]]; then
        log_error "Default rule을 찾을 수 없습니다"
        return 1
    fi

    # Rule 수정 실행
    aws elbv2 modify-rule \
        --region "$AWS_REGION" \
        --rule-arn "$rule_arn" \
        --actions "Type=forward,ForwardConfig=$forward_config" \
        --output text > /dev/null

    log_success "트래픽 전환 완료: Legacy ${legacy_weight}% ↔ New ${new_weight}%"

    # 전환 로그 기록
    local log_file="/tmp/traffic-shift-$(date +%Y%m%d).log"
    echo "[$(date -Iseconds)] $SERVICE:$ENV Legacy=${legacy_weight}% New=${new_weight}% by=$(whoami)" >> "$log_file"

    return 0
}

# 메인 로직
main() {
    local service=""
    local env=""
    local target_weight=""
    local canary_weight=""
    local full_cutover=false
    local rollback=false
    local status_only=false

    # 인자 파싱
    while [[ $# -gt 0 ]]; do
        case $1 in
            --service)
                service="$2"
                shift 2
                ;;
            --env)
                env="$2"
                shift 2
                ;;
            --target-weight)
                target_weight="$2"
                shift 2
                ;;
            --canary)
                canary_weight="$2"
                shift 2
                ;;
            --full-cutover)
                full_cutover=true
                shift
                ;;
            --rollback)
                rollback=true
                shift
                ;;
            --status)
                status_only=true
                shift
                ;;
            --dry-run)
                DRY_RUN=true
                shift
                ;;
            --skip-health-check)
                SKIP_HEALTH_CHECK=true
                shift
                ;;
            --verbose)
                VERBOSE=true
                shift
                ;;
            -h|--help)
                usage
                ;;
            *)
                log_error "Unknown option: $1"
                usage
                ;;
        esac
    done

    # 필수 인자 검증
    if [[ -z "$service" ]]; then
        log_error "--service 옵션이 필요합니다"
        usage
    fi

    if [[ -z "$env" ]]; then
        log_error "--env 옵션이 필요합니다"
        usage
    fi

    if [[ "$env" != "stage" && "$env" != "prod" ]]; then
        log_error "환경은 'stage' 또는 'prod'만 가능합니다"
        exit 1
    fi

    # 전역 변수 설정
    SERVICE="$service"
    ENV="$env"

    # AWS 리소스 검색
    if ! discover_resources "$service" "$env"; then
        exit 1
    fi

    # 상태 확인 모드
    if $status_only; then
        echo ""
        echo "=== 트래픽 분배 상태 ==="
        echo "서비스: $service"
        echo "환경: $env"
        echo "현재 분배:"
        get_current_status "$SERVICE_LISTENER_ARN"
        echo ""
        exit 0
    fi

    # 대상 가중치 결정
    local new_weight=0

    if $rollback; then
        new_weight=0
        log_warn "🚨 ROLLBACK 모드: New 서버에서 Legacy로 완전 롤백합니다"
    elif $full_cutover; then
        new_weight=100
        log_warn "⚠️  FULL CUTOVER 모드: Legacy → New 100% 전환합니다"
    elif [[ -n "$canary_weight" ]]; then
        new_weight="$canary_weight"
        log_info "🐤 CANARY 모드: ${new_weight}% 트래픽을 New 서버로 전환합니다"
    elif [[ -n "$target_weight" ]]; then
        new_weight="$target_weight"
    else
        log_error "트래픽 비율을 지정하세요 (--target-weight, --canary, --full-cutover, --rollback)"
        usage
    fi

    # 가중치 검증
    if ! [[ "$new_weight" =~ ^[0-9]+$ ]] || [[ $new_weight -lt 0 ]] || [[ $new_weight -gt 100 ]]; then
        log_error "가중치는 0-100 사이여야 합니다: $new_weight"
        exit 1
    fi

    # 프로덕션 경고
    if [[ "$env" == "prod" && $new_weight -ge 50 && ! $DRY_RUN ]]; then
        echo ""
        log_warn "⚠️  프로덕션 환경에서 ${new_weight}% 이상 트래픽 전환을 시도합니다!"
        log_warn "이 작업은 실제 사용자에게 영향을 미칩니다."
        echo ""
        read -p "계속하시겠습니까? (yes/no): " confirm
        if [[ "$confirm" != "yes" ]]; then
            log_info "작업이 취소되었습니다"
            exit 0
        fi
    fi

    # 헬스체크
    if ! $SKIP_HEALTH_CHECK; then
        log_info "헬스체크 수행 중..."

        # Legacy TG 체크
        if [[ -n "$SERVICE_LEGACY_TG_ARN" && "$SERVICE_LEGACY_TG_ARN" != "None" ]]; then
            if ! check_target_health "$SERVICE_LEGACY_TG_ARN"; then
                if [[ $new_weight -lt 100 ]]; then
                    log_error "Legacy Target Group이 healthy하지 않습니다"
                    exit 1
                fi
            fi
        fi

        # New TG 체크 (가중치가 0보다 큰 경우)
        if [[ $new_weight -gt 0 ]]; then
            if [[ -z "$SERVICE_NEW_TG_ARN" || "$SERVICE_NEW_TG_ARN" == "None" ]]; then
                log_error "New Target Group이 존재하지 않습니다. 먼저 New 서비스를 배포하세요."
                exit 1
            fi
            if ! check_target_health "$SERVICE_NEW_TG_ARN"; then
                log_error "New Target Group이 healthy하지 않습니다"
                exit 1
            fi
        fi
    else
        log_warn "헬스체크를 스킵합니다 (--skip-health-check)"
    fi

    # 트래픽 전환 실행
    echo ""
    if $DRY_RUN; then
        log_info "=== DRY-RUN 모드 ==="
    fi

    shift_traffic "$SERVICE_LISTENER_ARN" "$SERVICE_LEGACY_TG_ARN" "$SERVICE_NEW_TG_ARN" "$new_weight"

    # 최종 상태 출력
    echo ""
    echo "=== 전환 후 상태 ==="
    get_current_status "$SERVICE_LISTENER_ARN"
    echo ""

    if [[ $new_weight -gt 0 && $new_weight -lt 100 ]]; then
        log_info "💡 다음 단계:"
        echo "   - CloudWatch 메트릭 모니터링 (에러율, 지연시간)"
        echo "   - 문제 발생 시: ./scripts/traffic-shift.sh --service $service --rollback --env $env"
        if [[ $new_weight -lt 50 ]]; then
            echo "   - 안정적이면 확장: ./scripts/traffic-shift.sh --service $service --target-weight 50 --env $env"
        fi
    elif [[ $new_weight -eq 100 ]]; then
        log_success "🎉 트래픽 100% 전환 완료!"
        log_info "Legacy 서비스는 롤백용으로 일정 기간 유지하세요."
    elif [[ $new_weight -eq 0 ]]; then
        log_success "✅ Legacy 서버로 완전 롤백 완료"
    fi
}

# 실행
main "$@"
