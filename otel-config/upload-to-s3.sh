#!/bin/bash
# ========================================
# OTEL Config S3 Upload Script
# ========================================
# 이 스크립트는 ADOT Collector 설정 파일을 S3에 업로드합니다.
# KMS 암호화 사용 (prod-connectly 버킷 정책)
# ========================================

set -e

BUCKET="prod-connectly"
REGION="ap-northeast-2"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "=========================================="
echo "OTEL Config S3 Upload"
echo "=========================================="
echo "Bucket: s3://${BUCKET}"
echo "Region: ${REGION}"
echo ""

# Legacy API
echo "[1/5] Uploading setof-commerce-legacy-api config..."
aws s3 cp "${SCRIPT_DIR}/setof-commerce-legacy-api/otel-config.yaml" \
    "s3://${BUCKET}/otel-config/setof-commerce-legacy-api/otel-config.yaml" \
    --sse aws:kms \
    --region "${REGION}"
echo "  -> Done"

# Legacy Admin
echo "[2/5] Uploading setof-commerce-legacy-admin config..."
aws s3 cp "${SCRIPT_DIR}/setof-commerce-legacy-admin/otel-config.yaml" \
    "s3://${BUCKET}/otel-config/setof-commerce-legacy-admin/otel-config.yaml" \
    --sse aws:kms \
    --region "${REGION}"
echo "  -> Done"

# Web API
echo "[3/5] Uploading setof-commerce-web-api config..."
aws s3 cp "${SCRIPT_DIR}/setof-commerce-web-api/otel-config.yaml" \
    "s3://${BUCKET}/otel-config/setof-commerce-web-api/otel-config.yaml" \
    --sse aws:kms \
    --region "${REGION}"
echo "  -> Done"

# Web API Admin
echo "[4/5] Uploading setof-commerce-web-api-admin config..."
aws s3 cp "${SCRIPT_DIR}/setof-commerce-web-api-admin/otel-config.yaml" \
    "s3://${BUCKET}/otel-config/setof-commerce-web-api-admin/otel-config.yaml" \
    --sse aws:kms \
    --region "${REGION}"
echo "  -> Done"

# Legacy Batch
echo "[5/5] Uploading setof-commerce-legacy-batch config..."
aws s3 cp "${SCRIPT_DIR}/setof-commerce-legacy-batch/otel-config.yaml" \
    "s3://${BUCKET}/otel-config/setof-commerce-legacy-batch/otel-config.yaml" \
    --sse aws:kms \
    --region "${REGION}"
echo "  -> Done"

echo ""
echo "=========================================="
echo "All configs uploaded successfully!"
echo "=========================================="
echo ""
echo "S3 Paths:"
echo "  - s3://${BUCKET}/otel-config/setof-commerce-legacy-api/otel-config.yaml"
echo "  - s3://${BUCKET}/otel-config/setof-commerce-legacy-admin/otel-config.yaml"
echo "  - s3://${BUCKET}/otel-config/setof-commerce-web-api/otel-config.yaml"
echo "  - s3://${BUCKET}/otel-config/setof-commerce-web-api-admin/otel-config.yaml"
echo "  - s3://${BUCKET}/otel-config/setof-commerce-legacy-batch/otel-config.yaml"
echo ""
echo "To apply changes, redeploy ECS services or update config_version in Terraform."
