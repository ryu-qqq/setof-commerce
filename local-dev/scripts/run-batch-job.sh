#!/bin/bash

# ===============================================
# ECS Batch Job 수동 실행 스크립트
# ===============================================
# VPC 외부에서 AWS API를 통해 배치 Job을 트리거합니다.
# ===============================================

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# 환경 변수 기본값
ENVIRONMENT=${ENVIRONMENT:-prod}
AWS_REGION=${AWS_REGION:-ap-northeast-2}
PROJECT_NAME="setof-commerce"

# Job 이름 매핑 함수
get_job_name() {
    case "$1" in
        sellic)           echo "syncSellicOrderJob" ;;
        shipment)         echo "trackingShipmentJob" ;;
        alimtalk)         echo "alimTalkNotifyJob" ;;
        auto-cancel)      echo "scheduleAutoCancelJob" ;;
        vbank-cancel)     echo "scheduleVBankCancelJob" ;;
        order-completed)  echo "updateCompletedOrdersJob" ;;
        order-settlement) echo "updateSettlementOrdersJob" ;;
        order-rejected)   echo "updateRejectedOrdersJob" ;;
        cancel-request)   echo "updateCancelRequestOrdersJob" ;;
        payment-fail)     echo "updatePaymentFailOrdersJob" ;;
        vbank-fail)       echo "updateVBankFailOrdersJob" ;;
        *)                echo "" ;;
    esac
}

# 도움말 출력
show_help() {
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}ECS Batch Job 수동 실행${NC}"
    echo -e "${BLUE}========================================${NC}"
    echo ""
    echo -e "${CYAN}사용법:${NC}"
    echo "  $0 <job-name> [environment]"
    echo ""
    echo -e "${CYAN}사용 가능한 Job:${NC}"
    echo "  sellic            → syncSellicOrderJob"
    echo "  shipment          → trackingShipmentJob"
    echo "  alimtalk          → alimTalkNotifyJob"
    echo "  auto-cancel       → scheduleAutoCancelJob"
    echo "  vbank-cancel      → scheduleVBankCancelJob"
    echo "  order-completed   → updateCompletedOrdersJob"
    echo "  order-settlement  → updateSettlementOrdersJob"
    echo "  order-rejected    → updateRejectedOrdersJob"
    echo "  cancel-request    → updateCancelRequestOrdersJob"
    echo "  payment-fail      → updatePaymentFailOrdersJob"
    echo "  vbank-fail        → updateVBankFailOrdersJob"
    echo ""
    echo -e "${CYAN}예시:${NC}"
    echo "  $0 sellic              # prod 환경에서 Sellic 동기화 실행"
    echo "  $0 sellic stage        # stage 환경에서 실행"
    echo "  $0 shipment            # 배송 추적 Job 실행"
    echo "  $0 alimtalk            # 알림톡 발송 Job 실행"
    echo ""
    echo -e "${CYAN}환경 변수:${NC}"
    echo "  ENVIRONMENT   - 실행 환경 (기본값: prod)"
    echo "  AWS_REGION    - AWS 리전 (기본값: ap-northeast-2)"
    echo ""
}

# 인자 확인
if [ -z "$1" ] || [ "$1" == "-h" ] || [ "$1" == "--help" ]; then
    show_help
    exit 0
fi

JOB_KEY=$1
ENVIRONMENT=${2:-$ENVIRONMENT}

# Job 이름 유효성 검사
JOB_NAME=$(get_job_name "$JOB_KEY")
if [ -z "$JOB_NAME" ]; then
    echo -e "${RED}❌ 알 수 없는 Job: ${JOB_KEY}${NC}"
    echo ""
    show_help
    exit 1
fi

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}ECS Batch Job 실행${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "${CYAN}Job:${NC}         ${JOB_NAME}"
echo -e "${CYAN}Environment:${NC} ${ENVIRONMENT}"
echo -e "${CYAN}Region:${NC}      ${AWS_REGION}"
echo ""

# AWS CLI 확인
if ! command -v aws &> /dev/null; then
    echo -e "${RED}❌ AWS CLI가 설치되어 있지 않습니다.${NC}"
    exit 1
fi

# AWS 자격 증명 확인
echo "AWS 자격 증명 확인 중..."
if ! aws sts get-caller-identity &> /dev/null; then
    echo -e "${RED}❌ AWS 자격 증명이 설정되어 있지 않습니다.${NC}"
    exit 1
fi
echo -e "${GREEN}✅ AWS 인증 완료${NC}"
echo ""

# 클러스터 이름
CLUSTER_NAME="${PROJECT_NAME}-cluster-${ENVIRONMENT}"

# Task Definition 조회
echo "Task Definition 조회 중..."
TASK_DEF=$(aws ecs describe-task-definition \
    --region $AWS_REGION \
    --task-definition "${PROJECT_NAME}-legacy-batch-${ENVIRONMENT}" \
    --query 'taskDefinition.taskDefinitionArn' \
    --output text 2>/dev/null || echo "")

if [ -z "$TASK_DEF" ] || [ "$TASK_DEF" == "None" ]; then
    echo -e "${RED}❌ Task Definition을 찾을 수 없습니다.${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Task Definition: ${TASK_DEF}${NC}"

# 네트워크 설정 조회 (VPC, Subnet, Security Group)
echo ""
echo "네트워크 설정 조회 중..."

# ECS 서비스가 없으므로 SSM Parameter Store에서 조회
PRIVATE_SUBNETS=$(aws ssm get-parameter \
    --region $AWS_REGION \
    --name "/shared/vpc/private-subnet-ids" \
    --query 'Parameter.Value' \
    --output text 2>/dev/null || echo "")

if [ -z "$PRIVATE_SUBNETS" ] || [ "$PRIVATE_SUBNETS" == "None" ]; then
    echo -e "${RED}❌ Private Subnet 정보를 찾을 수 없습니다.${NC}"
    echo "SSM Parameter Store에 /shared/vpc/private-subnet-ids가 필요합니다."
    exit 1
fi

# Security Group 조회 (태그로 검색)
SECURITY_GROUP=$(aws ec2 describe-security-groups \
    --region $AWS_REGION \
    --filters "Name=tag:Name,Values=${PROJECT_NAME}-legacy-batch-sg-${ENVIRONMENT}" \
    --query 'SecurityGroups[0].GroupId' \
    --output text 2>/dev/null || echo "")

if [ -z "$SECURITY_GROUP" ] || [ "$SECURITY_GROUP" == "None" ]; then
    echo -e "${YELLOW}⚠️  Security Group을 태그로 찾지 못했습니다. 기본 SG 사용...${NC}"
    # VPC 기본 Security Group 사용
    VPC_ID=$(aws ssm get-parameter \
        --region $AWS_REGION \
        --name "/shared/vpc/vpc-id" \
        --query 'Parameter.Value' \
        --output text 2>/dev/null || echo "")

    SECURITY_GROUP=$(aws ec2 describe-security-groups \
        --region $AWS_REGION \
        --filters "Name=vpc-id,Values=${VPC_ID}" "Name=group-name,Values=default" \
        --query 'SecurityGroups[0].GroupId' \
        --output text 2>/dev/null || echo "")
fi

echo -e "${GREEN}✅ Subnets: ${PRIVATE_SUBNETS}${NC}"
echo -e "${GREEN}✅ Security Group: ${SECURITY_GROUP}${NC}"

# Subnet을 JSON 배열로 변환
SUBNET_ARRAY=$(echo "$PRIVATE_SUBNETS" | tr ',' '\n' | sed 's/^/"/;s/$/"/' | tr '\n' ',' | sed 's/,$//')

echo ""
echo -e "${YELLOW}🚀 ECS Task 실행 중...${NC}"
echo ""

# ECS RunTask 실행
TASK_ARN=$(aws ecs run-task \
    --region $AWS_REGION \
    --cluster "$CLUSTER_NAME" \
    --task-definition "$TASK_DEF" \
    --launch-type FARGATE \
    --network-configuration "{
        \"awsvpcConfiguration\": {
            \"subnets\": [${SUBNET_ARRAY}],
            \"securityGroups\": [\"${SECURITY_GROUP}\"],
            \"assignPublicIp\": \"DISABLED\"
        }
    }" \
    --overrides "{
        \"containerOverrides\": [{
            \"name\": \"legacy-batch\",
            \"environment\": [
                {\"name\": \"JOB_NAME\", \"value\": \"${JOB_NAME}\"}
            ]
        }]
    }" \
    --query 'tasks[0].taskArn' \
    --output text)

if [ -z "$TASK_ARN" ] || [ "$TASK_ARN" == "None" ]; then
    echo -e "${RED}❌ Task 실행에 실패했습니다.${NC}"
    exit 1
fi

# Task ID 추출
TASK_ID=$(echo "$TASK_ARN" | awk -F'/' '{print $NF}')

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}✅ Task 실행 성공!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${CYAN}Task ARN:${NC}  ${TASK_ARN}"
echo -e "${CYAN}Task ID:${NC}   ${TASK_ID}"
echo -e "${CYAN}Job Name:${NC}  ${JOB_NAME}"
echo ""
echo -e "${CYAN}로그 확인:${NC}"
echo "  aws logs tail /aws/ecs/${PROJECT_NAME}-legacy-batch-${ENVIRONMENT}/application --follow"
echo ""
echo -e "${CYAN}Task 상태 확인:${NC}"
echo "  aws ecs describe-tasks --cluster ${CLUSTER_NAME} --tasks ${TASK_ID} --region ${AWS_REGION}"
echo ""
echo -e "${CYAN}AWS 콘솔에서 확인:${NC}"
echo "  https://${AWS_REGION}.console.aws.amazon.com/ecs/home?region=${AWS_REGION}#/clusters/${CLUSTER_NAME}/tasks/${TASK_ID}/details"
echo ""
