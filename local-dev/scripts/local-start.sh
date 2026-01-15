#!/bin/bash

# ===============================================
# 로컬 개발 환경 시작 스크립트
# ===============================================
# Stage RDS 포트포워딩 + 로컬 Redis로 개발 환경 구성
# 애플리케이션은 IDE에서 spring.profiles.active=local로 실행
# ===============================================

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 옵션 파싱
START_MOCK=false
for arg in "$@"; do
    case $arg in
        --mock|-m)
            START_MOCK=true
            shift
            ;;
        --help|-h)
            echo "Usage: $0 [OPTIONS]"
            echo ""
            echo "Options:"
            echo "  --mock, -m    OMS Mock Server도 함께 시작"
            echo "  --help, -h    도움말 표시"
            exit 0
            ;;
    esac
done

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}SetOf Commerce 로컬 개발 환경 시작${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 스크립트 디렉토리로 이동
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR/.."

# -----------------------------------------------
# 1. Redis 컨테이너 시작 (로컬)
# -----------------------------------------------
echo -e "${BLUE}[1/2] Redis 컨테이너 시작...${NC}"

# 기존 Redis 컨테이너 확인 및 시작
if docker ps -a --format '{{.Names}}' | grep -q '^setof-redis-dev$'; then
    if docker ps --format '{{.Names}}' | grep -q '^setof-redis-dev$'; then
        echo -e "${GREEN}✅ Redis 컨테이너가 이미 실행 중입니다.${NC}"
    else
        echo "기존 Redis 컨테이너 시작..."
        docker start setof-redis-dev
        echo -e "${GREEN}✅ Redis 컨테이너 시작 완료${NC}"
    fi
else
    echo "Redis 컨테이너 생성 및 시작..."
    docker run -d \
        --name setof-redis-dev \
        -p 46379:6379 \
        --restart unless-stopped \
        redis:7.2-alpine \
        redis-server --appendonly yes --maxmemory 256mb --maxmemory-policy allkeys-lru
    echo -e "${GREEN}✅ Redis 컨테이너 생성 및 시작 완료${NC}"
fi

# Redis 헬스체크
echo "Redis 연결 확인..."
for i in {1..10}; do
    if docker exec setof-redis-dev redis-cli ping 2>/dev/null | grep -q PONG; then
        echo -e "${GREEN}✅ Redis 연결 성공${NC}"
        break
    fi
    if [ $i -eq 10 ]; then
        echo -e "${RED}❌ Redis 연결 실패${NC}"
        exit 1
    fi
    sleep 1
done

echo ""

# -----------------------------------------------
# 2. Stage RDS 포트포워딩 시작 (백그라운드)
# -----------------------------------------------
echo -e "${BLUE}[2/2] Stage RDS 포트포워딩 시작...${NC}"

# 기존 포트포워딩 프로세스 확인
PID_FILE="/tmp/fileflow-port-forward-stage/rds.pid"
if [ -f "$PID_FILE" ]; then
    OLD_PID=$(cat "$PID_FILE")
    if ps -p "$OLD_PID" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Stage RDS 포트포워딩이 이미 실행 중입니다. (PID: ${OLD_PID})${NC}"
    else
        echo "기존 PID 파일 제거..."
        rm -f "$PID_FILE"
    fi
fi

# 포트포워딩 시작 (PID 파일이 없는 경우)
if [ ! -f "$PID_FILE" ]; then
    # AWS 자격 증명 확인
    if ! aws sts get-caller-identity &> /dev/null; then
        echo -e "${YELLOW}⚠️  AWS 자격 증명이 설정되어 있지 않습니다.${NC}"
        echo -e "${YELLOW}   aws sso login 또는 aws configure를 먼저 실행하세요.${NC}"
        echo ""
        echo -e "${YELLOW}   포트포워딩 없이 진행합니다. (로컬 MySQL 사용 필요)${NC}"
    else
        echo "Stage RDS 포트포워딩 시작 (백그라운드)..."

        # 백그라운드로 포트포워딩 스크립트 실행
        nohup "$SCRIPT_DIR/aws-port-forward-stage.sh" > /tmp/port-forward-stage.log 2>&1 &
        FORWARD_PID=$!

        # 포트포워딩 연결 대기
        echo "포트포워딩 연결 대기 중..."
        for i in {1..30}; do
            if nc -z 127.0.0.1 13308 2>/dev/null; then
                echo -e "${GREEN}✅ Stage RDS 포트포워딩 연결 성공 (PID: ${FORWARD_PID})${NC}"
                break
            fi
            if [ $i -eq 30 ]; then
                echo -e "${YELLOW}⚠️  Stage RDS 포트포워딩 연결 대기 시간 초과${NC}"
                echo -e "${YELLOW}   수동으로 확인하세요: ./scripts/aws-port-forward-stage.sh${NC}"
            fi
            sleep 1
        done
    fi
fi

echo ""

# -----------------------------------------------
# 3. OMS Mock Server 시작 (선택적)
# -----------------------------------------------
if [ "$START_MOCK" = true ]; then
    echo -e "${BLUE}[3/3] OMS Mock Server 시작...${NC}"

    MOCK_SERVER_DIR="$SCRIPT_DIR/../../mock-server"

    if [ -d "$MOCK_SERVER_DIR" ]; then
        # 기존 컨테이너 확인 및 시작
        if docker ps --format '{{.Names}}' | grep -q '^oms-mock-server$'; then
            echo -e "${GREEN}✅ OMS Mock Server가 이미 실행 중입니다.${NC}"
        elif docker ps -a --format '{{.Names}}' | grep -q '^oms-mock-server$'; then
            echo "기존 Mock Server 컨테이너 시작..."
            docker start oms-mock-server
            echo -e "${GREEN}✅ OMS Mock Server 시작 완료${NC}"
        else
            echo "Mock Server 컨테이너 생성 및 시작..."
            docker-compose -f "$MOCK_SERVER_DIR/docker-compose.yml" up -d
            echo -e "${GREEN}✅ OMS Mock Server 생성 및 시작 완료${NC}"
        fi

        # Mock Server 헬스체크
        echo "Mock Server 연결 확인..."
        for i in {1..10}; do
            if curl -s http://localhost:48089/__admin/mappings > /dev/null 2>&1; then
                echo -e "${GREEN}✅ OMS Mock Server 연결 성공${NC}"
                break
            fi
            if [ $i -eq 10 ]; then
                echo -e "${YELLOW}⚠️  Mock Server 연결 대기 시간 초과${NC}"
            fi
            sleep 1
        done
    else
        echo -e "${YELLOW}⚠️  Mock Server 디렉토리를 찾을 수 없습니다: ${MOCK_SERVER_DIR}${NC}"
    fi

    echo ""
fi

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}로컬 개발 환경 시작 완료${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${BLUE}📌 연결 정보:${NC}"
echo "  - Redis:     localhost:46379"
echo "  - Stage RDS: localhost:13308 (admin 계정)"
if [ "$START_MOCK" = true ]; then
    echo "  - Mock API:  http://localhost:48089/api/oms"
fi
echo ""
echo -e "${BLUE}📌 애플리케이션 실행 (IDE):${NC}"
echo "  VM Options: -Dspring.profiles.active=local"
echo "  환경변수:"
echo "    DB_HOST=127.0.0.1"
echo "    DB_PORT=13308"
echo "    DB_NAME=common"
echo "    DB_USER=admin"
echo "    DB_PASSWORD=<Secrets Manager에서 조회>"
echo ""
echo -e "${BLUE}📌 Stage DB 비밀번호 조회:${NC}"
echo "  aws secretsmanager get-secret-value \\"
echo "    --secret-id \"stage-shared-mysql-master-password\" \\"
echo "    --query \"SecretString\" --output text | jq -r '.password'"
echo ""
echo -e "${BLUE}📌 종료:${NC}"
echo "  ./scripts/local-stop.sh"
if [ "$START_MOCK" = true ]; then
    echo "  (Mock Server 포함: --mock 옵션 사용 시 자동 종료)"
fi
echo ""
