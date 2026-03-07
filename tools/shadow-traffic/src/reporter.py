"""S3 diff report writer - stores detailed diff results for analysis."""

from __future__ import annotations

import json
import logging
from datetime import datetime, timezone
from typing import TYPE_CHECKING

import os

import boto3

if TYPE_CHECKING:
    from diff_engine import DiffResult

logger = logging.getLogger(__name__)

CDN_BASE_URL = os.getenv("CDN_BASE_URL", "https://stage-cdn.set-of.com")


class S3Reporter:
    def __init__(self, bucket: str | None = None, prefix: str = "public/traffic", region: str = "ap-northeast-2"):
        self.bucket = bucket
        self.prefix = prefix
        if bucket:
            self.client = boto3.client("s3", region_name=region)
        else:
            self.client = None

    async def save_report(
        self,
        domain: str,
        results: list[DiffResult],
        legacy_bodies: dict[str, object],
        new_bodies: dict[str, object],
    ) -> str | None:
        if not self.client or not self.bucket:
            logger.debug("[s3] no bucket configured, skipping report")
            return None

        failures = [r for r in results if not r.passed]
        if not failures:
            return None

        now = datetime.now(timezone.utc)
        key = f"{self.prefix}/{now:%Y/%m/%d}/{domain}/{now:%H%M%S}.json"

        total = len(results)
        failed = len(failures)

        report = {
            "timestamp": now.isoformat(),
            "domain": domain,
            "summary": {
                "total": total,
                "passed": total - failed,
                "failed": failed,
                "diff_rate": round(failed / total * 100, 1) if total > 0 else 0,
            },
            "failures": [],
        }

        for r in failures:
            entry = {
                "test_name": r.test_name,
                "status_match": r.status_match,
                "legacy_status": r.legacy_status,
                "new_status": r.new_status,
                "legacy_latency_ms": round(r.legacy_latency_ms, 1),
                "new_latency_ms": round(r.new_latency_ms, 1),
            }
            if r.error:
                entry["error"] = r.error
            if r.body_diff:
                entry["diff"] = _serialize(r.body_diff)
            if r.test_name in legacy_bodies:
                entry["legacy_body"] = _serialize(legacy_bodies[r.test_name])
            if r.test_name in new_bodies:
                entry["new_body"] = _serialize(new_bodies[r.test_name])
            report["failures"].append(entry)

        try:
            body = json.dumps(report, ensure_ascii=False, default=str, indent=2)
            self.client.put_object(
                Bucket=self.bucket,
                Key=key,
                Body=body.encode("utf-8"),
                ContentType="application/json",
            )
            cdn_url = f"{CDN_BASE_URL}/{key}"
            logger.info(f"[s3] report saved: {cdn_url}")
            return cdn_url
        except Exception as e:
            logger.error(f"[s3] failed to save report: {e}")
            return None


def _serialize(obj: object) -> object:
    """Ensure object is JSON-serializable."""
    try:
        json.dumps(obj, default=str)
        return obj
    except (TypeError, ValueError):
        return str(obj)
