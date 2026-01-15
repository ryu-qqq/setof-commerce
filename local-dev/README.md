# SetOf Commerce 로컬 개발 환경

로컬에서 Stage AWS 리소스에 연결하여 개발하기 위한 환경입니다.

## 구조

```
local-dev/
├── README.md                    # 이 파일
├── docker-compose.aws.yml       # 애플리케이션 서버 (Stage RDS 연결)
├── .env.aws.example             # 환경변수 예시
├── .env.aws.stage               # 실제 환경변수 (git 제외)
└── scripts/
    ├── local-start.sh           # 인프라 시작 (Redis + Stage RDS 포트포워딩)
    ├── local-stop.sh            # 인프라 종료
    └── aws-port-forward-stage.sh # SSM 포트포워딩 스크립트
```

## 사전 준비

```bash
# 1. AWS SSO 로그인
aws sso login

# 2. 자격 증명 확인
aws sts get-caller-identity

# 3. 환경변수 파일 생성
cd local-dev
cp .env.aws.example .env.aws.stage

# 4. Stage DB 비밀번호 조회 후 .env.aws.stage에 입력
aws secretsmanager get-secret-value \
  --secret-id "stage-shared-mysql-master-password" \
  --query "SecretString" --output text | jq -r '.password'
```

## 빠른 시작

### 1. 인프라만 시작 (IDE에서 직접 실행 시)

```bash
cd local-dev

# Redis + Stage RDS 포트포워딩 시작
./scripts/local-start.sh

# IDE에서 애플리케이션 실행
# VM Options: -Dspring.profiles.active=local
# 환경변수: DB_HOST=127.0.0.1, DB_PORT=13308, REDIS_HOST=127.0.0.1, REDIS_PORT=46379
```

### 2. 애플리케이션 서버 띄우기 (프론트 개발자용)

```bash
cd local-dev

# 인프라 시작
./scripts/local-start.sh

# 원하는 서비스 실행
docker-compose -f docker-compose.aws.yml --env-file .env.aws.stage --profile web-api up -d

# 종료
docker-compose -f docker-compose.aws.yml down
./scripts/local-stop.sh
```

### 3. Mock Server 실행 (개발 완료 전 프론트 개발자용)

```bash
cd local-dev

# Mock Server 포함 시작
./scripts/local-start.sh --mock

# 종료
./scripts/local-stop.sh --mock
```

## 명령어 상세

### local-start.sh

```bash
./scripts/local-start.sh [OPTIONS]

Options:
  --mock, -m    OMS Mock Server도 함께 시작
  --help, -h    도움말 표시
```

**실행 내용:**
1. Redis 컨테이너 시작 (포트 46379)
2. Stage RDS SSM 포트포워딩 (포트 13308)
3. (선택) Mock Server 시작 (포트 48089)

### local-stop.sh

```bash
./scripts/local-stop.sh [OPTIONS]

Options:
  --mock, -m    Mock Server도 함께 종료
  --clean, -c   컨테이너 완전 삭제 (데이터 포함)
  --help, -h    도움말 표시
```

### docker-compose.aws.yml

```bash
# 개별 서비스
docker-compose -f docker-compose.aws.yml --env-file .env.aws.stage --profile web-api up -d
docker-compose -f docker-compose.aws.yml --env-file .env.aws.stage --profile web-api-admin up -d
docker-compose -f docker-compose.aws.yml --env-file .env.aws.stage --profile legacy-web-api up -d
docker-compose -f docker-compose.aws.yml --env-file .env.aws.stage --profile batch up -d
docker-compose -f docker-compose.aws.yml --env-file .env.aws.stage --profile migration up -d

# 그룹
docker-compose -f docker-compose.aws.yml --env-file .env.aws.stage --profile api up -d      # web-api + web-api-admin
docker-compose -f docker-compose.aws.yml --env-file .env.aws.stage --profile legacy up -d   # legacy-web-api + legacy-web-api-admin

# 전체
docker-compose -f docker-compose.aws.yml --env-file .env.aws.stage --profile all up -d
```

## 포트 정보

| 서비스 | 포트 | 설명 |
|--------|------|------|
| Redis | 46379 | 로컬 캐시 |
| Stage RDS | 13308 | SSM 포트포워딩 |
| Mock Server | 48089 | OMS Mock API |
| web-api | 48080 | 신규 API |
| web-api-admin | 48081 | 신규 Admin API |
| legacy-web-api | 48082 | 레거시 API |
| legacy-web-api-admin | 48083 | 레거시 Admin API |
| batch | 48084 | 배치 서버 |
| migration | 48085 | 마이그레이션 서버 |

## 환경변수

`.env.aws.stage` 파일에 설정 (`.env.aws.example` 참고):

| 변수 | 설명 | 필수 |
|------|------|------|
| DB_PASSWORD | Stage RDS 비밀번호 | ✅ |
| DB_NAME | 데이터베이스명 (기본: common) | |
| DB_USERNAME | DB 사용자 (기본: admin) | |
| KAKAO_CLIENT_ID | 카카오 OAuth | |
| JWT_SECRET | JWT 시크릿 | |

## 트러블슈팅

### AWS SSO 로그인 필요

```bash
aws sso login
aws sts get-caller-identity  # 확인
```

### 포트포워딩 실패

```bash
# 포트 확인
lsof -i :13308
lsof -i :46379

# 재시작
./scripts/local-stop.sh
./scripts/local-start.sh
```

### Docker 빌드 실패

```bash
# 캐시 없이 빌드
docker-compose -f docker-compose.aws.yml --env-file .env.aws.stage --profile web-api build --no-cache
```

### 컨테이너 로그 확인

```bash
docker logs setof-web-api
docker logs setof-redis-dev
```

## 보안 주의

- `.env.aws.stage`는 절대 커밋하지 마세요 (.gitignore에 포함됨)
- Stage DB는 실제 테스트 데이터 - 수정 시 주의
- AWS 자격 증명은 SSO로 관리 (키 하드코딩 금지)
