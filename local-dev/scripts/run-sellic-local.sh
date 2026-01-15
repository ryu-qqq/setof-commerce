#!/bin/bash

# Sellic Batch Job 로컬 실행 스크립트

set -e

export DB_URL='jdbc:mysql://localhost:13307/setof?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul'
export DB_USERNAME='setof_user'
export DB_PASSWORD='0l8RgpL1clTdr06XyQ9DjfUOcF2ryBIN'
export LEGACY_DB_URL='jdbc:mysql://localhost:13307/luxurydb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul'
export LEGACY_DB_USERNAME='admin'
export LEGACY_DB_PASSWORD='E[&mUlOgA+ucv31nRmSDlbOr398VyGep'
export SELLIC_API_KEY='6790qy09-6642-ac64-zzj3-92jficnw1a50'
export SELLIC_CUSTOMER_ID='1012'
export JOB_NAME='syncSellicOrderJob'
export ADMIN_SERVER_URL='https://admin-server.set-of.net'

cd /Users/sangwon-ryu/setof-commerce

./gradlew :bootstrap:bootstrap-batch:bootRun --args='--spring.batch.job.enabled=true --spring.batch.job.name=syncSellicOrderJob'
