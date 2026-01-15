#!/bin/bash

# ===============================================
# 로컬 개발 환경 종료 스크립트
# ===============================================
# Redis 컨테이너 종료 + Stage RDS 포트포워딩 종료
# ===============================================

set -e

# 색상 정의
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}SetOf Commerce 로컬 개발 환경 종료${NC}"
echo -e "${YELLOW}========================================${NC}"
echo ""

# 옵션 확인
REMOVE_CONTAINER=false
STOP_MOCK=false
for arg in "$@"; do
    case $arg in
        --clean|-c)
            REMOVE_CONTAINER=true
            echo -e "${RED}⚠️  Redis 컨테이너도 완전히 삭제됩니다!${NC}"
            echo ""
            ;;
        --mock|-m)
            STOP_MOCK=true
            ;;
        --help|-h)
            echo "Usage: $0 [OPTIONS]"
            echo ""
            echo "Options:"
            echo "  --clean, -c   Redis 컨테이너 완전 삭제"
            echo "  --mock, -m    OMS Mock Server도 함께 종료"
            echo "  --help, -h    도움말 표시"
            exit 0
            ;;
    esac
done

# -----------------------------------------------
# 1. Stage RDS 포트포워딩 종료
# -----------------------------------------------
echo -e "${BLUE}[1/2] Stage RDS 포트포워딩 종료...${NC}"

PID_FILE="/tmp/fileflow-port-forward-stage/rds.pid"
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ps -p "$PID" > /dev/null 2>&1; then
        echo "포트포워딩 프로세스 종료 중 (PID: ${PID})..."
        kill "$PID" 2>/dev/null || true
        sleep 1
        # 강제 종료 필요 시
        if ps -p "$PID" > /dev/null 2>&1; then
            kill -9 "$PID" 2>/dev/null || true
        fi
        echo -e "${GREEN}✅ Stage RDS 포트포워딩 종료 완료${NC}"
    else
        echo -e "${YELLOW}⚠️  포트포워딩 프로세스가 이미 종료되었습니다.${NC}"
    fi
    rm -f "$PID_FILE"
else
    echo -e "${YELLOW}⚠️  포트포워딩 PID 파일이 없습니다.${NC}"
fi

# 관련 SSM 세션도 정리
SSM_PIDS=$(pgrep -f "aws ssm start-session.*staging-shared-mysql" 2>/dev/null || true)
if [ -n "$SSM_PIDS" ]; then
    echo "SSM 세션 정리 중..."
    echo "$SSM_PIDS" | xargs kill 2>/dev/null || true
fi

echo ""

# -----------------------------------------------
# 2. Redis 컨테이너 종료
# -----------------------------------------------
echo -e "${BLUE}[2/2] Redis 컨테이너 종료...${NC}"

if docker ps --format '{{.Names}}' | grep -q '^setof-redis-dev$'; then
    if [ "$REMOVE_CONTAINER" = true ]; then
        echo "Redis 컨테이너 삭제 중..."
        docker stop setof-redis-dev
        docker rm setof-redis-dev
        echo -e "${GREEN}✅ Redis 컨테이너 삭제 완료${NC}"
    else
        echo "Redis 컨테이너 중지 중..."
        docker stop setof-redis-dev
        echo -e "${GREEN}✅ Redis 컨테이너 중지 완료 (데이터 유지)${NC}"
    fi
elif docker ps -a --format '{{.Names}}' | grep -q '^setof-redis-dev$'; then
    if [ "$REMOVE_CONTAINER" = true ]; then
        echo "중지된 Redis 컨테이너 삭제 중..."
        docker rm setof-redis-dev
        echo -e "${GREEN}✅ Redis 컨테이너 삭제 완료${NC}"
    else
        echo -e "${YELLOW}⚠️  Redis 컨테이너가 이미 중지되어 있습니다.${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  Redis 컨테이너가 없습니다.${NC}"
fi

echo ""

# -----------------------------------------------
# 3. OMS Mock Server 종료 (선택적)
# -----------------------------------------------
if [ "$STOP_MOCK" = true ]; then
    echo -e "${BLUE}[3/3] OMS Mock Server 종료...${NC}"

    if docker ps --format '{{.Names}}' | grep -q '^oms-mock-server$'; then
        if [ "$REMOVE_CONTAINER" = true ]; then
            echo "Mock Server 컨테이너 삭제 중..."
            docker stop oms-mock-server
            docker rm oms-mock-server
            echo -e "${GREEN}✅ OMS Mock Server 삭제 완료${NC}"
        else
            echo "Mock Server 컨테이너 중지 중..."
            docker stop oms-mock-server
            echo -e "${GREEN}✅ OMS Mock Server 중지 완료${NC}"
        fi
    elif docker ps -a --format '{{.Names}}' | grep -q '^oms-mock-server$'; then
        if [ "$REMOVE_CONTAINER" = true ]; then
            echo "중지된 Mock Server 컨테이너 삭제 중..."
            docker rm oms-mock-server
            echo -e "${GREEN}✅ OMS Mock Server 삭제 완료${NC}"
        else
            echo -e "${YELLOW}⚠️  Mock Server가 이미 중지되어 있습니다.${NC}"
        fi
    else
        echo -e "${YELLOW}⚠️  Mock Server 컨테이너가 없습니다.${NC}"
    fi

    echo ""
fi

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}로컬 개발 환경 종료 완료${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

if [ "$REMOVE_CONTAINER" = false ]; then
    echo -e "${BLUE}💡 컨테이너는 중지만 되었습니다.${NC}"
    echo "   다시 시작: ./scripts/local-start.sh"
    echo "   완전 삭제: ./scripts/local-stop.sh --clean"
    echo ""
fi
