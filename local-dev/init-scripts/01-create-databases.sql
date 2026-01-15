-- ===============================================
-- SetOf Commerce - 로컬 개발용 데이터베이스 초기화
-- ===============================================
-- 이 스크립트는 Docker MySQL 컨테이너 최초 실행 시 자동 실행됩니다.
-- ===============================================

-- 기본 데이터베이스는 MYSQL_DATABASE 환경변수로 생성됨 (common)

-- 추가 데이터베이스 생성 (필요 시)
-- CREATE DATABASE IF NOT EXISTS setof_test CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 사용자 권한 부여
GRANT ALL PRIVILEGES ON *.* TO 'setof'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- 테이블 생성은 Flyway가 담당합니다.
-- src/main/resources/db/migration/ 폴더의 마이그레이션 파일 참조
