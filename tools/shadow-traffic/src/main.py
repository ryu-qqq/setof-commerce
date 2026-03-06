"""Shadow Traffic Service - Entry Point.

ECS Scheduled Task or manual execution.

Usage:
    python main.py                           # run all domains
    python main.py --domains brand,category  # run specific domains
    python main.py --dry-run                 # no metrics/alerts
"""

from __future__ import annotations

import argparse
import asyncio
import logging
import os
import sys
from pathlib import Path

import httpx

from metrics import MetricsPublisher
from notifier import SlackNotifier
from runner import load_all_suites, run_suite

class JsonFormatter(logging.Formatter):
    """Structured JSON log format for OpenSearch/CloudWatch Insights."""

    def format(self, record: logging.LogRecord) -> str:
        import json as _json

        log_entry = {
            "timestamp": self.formatTime(record, "%Y-%m-%dT%H:%M:%S"),
            "level": record.levelname,
            "logger": record.name,
            "message": record.getMessage(),
            "service": "shadow-traffic",
        }
        if hasattr(record, "extra_data"):
            log_entry.update(record.extra_data)
        return _json.dumps(log_entry, ensure_ascii=False, default=str)


handler = logging.StreamHandler()
handler.setFormatter(JsonFormatter())
logging.basicConfig(level=logging.INFO, handlers=[handler])
logger = logging.getLogger(__name__)


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Shadow Traffic Service")
    parser.add_argument(
        "--legacy-url",
        default=os.getenv("LEGACY_URL", "http://localhost:8080"),
        help="Legacy server base URL",
    )
    parser.add_argument(
        "--new-url",
        default=os.getenv("NEW_URL", "http://localhost:8081"),
        help="New server base URL",
    )
    parser.add_argument(
        "--domains",
        default=os.getenv("DOMAINS", ""),
        help="Comma-separated domain filter (empty = all)",
    )
    parser.add_argument(
        "--test-cases-dir",
        default=os.getenv(
            "TEST_CASES_DIR",
            str(Path(__file__).parent.parent / "test-cases"),
        ),
        help="Path to test-cases directory",
    )
    parser.add_argument(
        "--slack-webhook",
        default=os.getenv("SLACK_WEBHOOK_URL", ""),
        help="Slack webhook URL for alerts",
    )
    parser.add_argument(
        "--dashboard-url",
        default=os.getenv("DASHBOARD_URL", ""),
        help="Grafana dashboard URL for alert links",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        default=os.getenv("DRY_RUN", "false").lower() == "true",
        help="Disable metrics and alerts",
    )
    parser.add_argument(
        "--region",
        default=os.getenv("AWS_REGION", "ap-northeast-2"),
        help="AWS region for CloudWatch",
    )
    return parser.parse_args()


async def main() -> int:
    args = parse_args()

    domains = [d.strip() for d in args.domains.split(",") if d.strip()] or None
    test_cases_dir = Path(args.test_cases_dir)

    if not test_cases_dir.exists():
        logger.error(f"test-cases directory not found: {test_cases_dir}")
        return 1

    suites = load_all_suites(test_cases_dir, domains)
    if not suites:
        logger.warning("No test suites found")
        return 0

    logger.info(
        f"Loaded {len(suites)} suite(s): {[s['domain'] for s in suites]}"
    )
    logger.info(f"Legacy: {args.legacy_url}")
    logger.info(f"New:    {args.new_url}")

    metrics = MetricsPublisher(enabled=not args.dry_run, region=args.region)
    notifier = SlackNotifier(
        webhook_url=args.slack_webhook if not args.dry_run else None
    )

    total_passed = 0
    total_failed = 0

    async with httpx.AsyncClient(follow_redirects=False) as http_client:
        for suite in suites:
            domain = suite["domain"]
            logger.info(f"--- Running suite: {domain} ---")

            results = await run_suite(
                http_client,
                suite,
                legacy_url=args.legacy_url,
                new_url=args.new_url,
            )

            passed = sum(1 for r in results if r.passed)
            failed = len(results) - passed
            total_passed += passed
            total_failed += failed

            diff_rate = (failed / len(results) * 100) if results else 0
            logger.info(
                f"[{domain}] {passed}/{len(results)} passed, diff_rate={diff_rate:.1f}%"
            )

            metrics.publish(domain, results)
            await notifier.notify_failures(domain, results, args.dashboard_url)

    total = total_passed + total_failed
    logger.info(f"=== TOTAL: {total_passed}/{total} passed, {total_failed} failed ===")

    return 1 if total_failed > 0 else 0


if __name__ == "__main__":
    exit_code = asyncio.run(main())
    sys.exit(exit_code)
