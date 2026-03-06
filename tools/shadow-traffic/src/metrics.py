"""CloudWatch metrics publisher."""

from __future__ import annotations

import logging
from typing import TYPE_CHECKING

import boto3

if TYPE_CHECKING:
    from diff_engine import DiffResult

logger = logging.getLogger(__name__)

NAMESPACE = "ShadowTraffic"


class MetricsPublisher:
    def __init__(self, enabled: bool = True, region: str = "ap-northeast-2"):
        self.enabled = enabled
        if enabled:
            self.client = boto3.client("cloudwatch", region_name=region)
        else:
            self.client = None

    def publish(self, domain: str, results: list[DiffResult]) -> None:
        if not self.enabled or not self.client:
            logger.info("[metrics] disabled, skipping publish")
            return

        total = len(results)
        passed = sum(1 for r in results if r.passed)
        failed = total - passed
        diff_rate = (failed / total * 100) if total > 0 else 0

        legacy_latencies = [r.legacy_latency_ms for r in results]
        new_latencies = [r.new_latency_ms for r in results]

        dimensions = [{"Name": "Domain", "Value": domain}]

        metric_data = [
            {
                "MetricName": "DiffRate",
                "Dimensions": dimensions,
                "Value": diff_rate,
                "Unit": "Percent",
            },
            {
                "MetricName": "DiffCount",
                "Dimensions": dimensions,
                "Value": failed,
                "Unit": "Count",
            },
            {
                "MetricName": "TestCount",
                "Dimensions": dimensions,
                "Value": total,
                "Unit": "Count",
            },
            {
                "MetricName": "LegacyLatencyP99",
                "Dimensions": dimensions,
                "Value": sorted(legacy_latencies)[int(len(legacy_latencies) * 0.99)] if legacy_latencies else 0,
                "Unit": "Milliseconds",
            },
            {
                "MetricName": "NewLatencyP99",
                "Dimensions": dimensions,
                "Value": sorted(new_latencies)[int(len(new_latencies) * 0.99)] if new_latencies else 0,
                "Unit": "Milliseconds",
            },
        ]

        try:
            self.client.put_metric_data(
                Namespace=NAMESPACE, MetricData=metric_data
            )
            logger.info(
                f"[metrics] published {domain}: diff_rate={diff_rate:.1f}% ({failed}/{total})"
            )
        except Exception as e:
            logger.error(f"[metrics] failed to publish: {e}")
