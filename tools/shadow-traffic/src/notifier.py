"""Slack notification for diff alerts."""

from __future__ import annotations

import json
import logging
from typing import TYPE_CHECKING

import httpx

if TYPE_CHECKING:
    from diff_engine import DiffResult

logger = logging.getLogger(__name__)


class SlackNotifier:
    def __init__(self, webhook_url: str | None = None):
        self.webhook_url = webhook_url

    async def notify_failures(
        self, domain: str, results: list[DiffResult], dashboard_url: str = ""
    ) -> None:
        failures = [r for r in results if not r.passed]
        if not failures:
            return

        if not self.webhook_url:
            logger.info(f"[slack] no webhook configured, {len(failures)} failures in {domain}")
            return

        total = len(results)
        failed = len(failures)
        diff_rate = failed / total * 100

        blocks = [
            {
                "type": "header",
                "text": {
                    "type": "plain_text",
                    "text": f"Shadow Traffic Diff Detected - {domain}",
                },
            },
            {
                "type": "section",
                "fields": [
                    {"type": "mrkdwn", "text": f"*Domain:* {domain}"},
                    {"type": "mrkdwn", "text": f"*Diff Rate:* {diff_rate:.1f}% ({failed}/{total})"},
                ],
            },
        ]

        for f in failures[:5]:
            detail = f"*{f.test_name}*\n"
            if not f.status_match:
                detail += f"Status: legacy={f.legacy_status} vs new={f.new_status}\n"
            if f.body_diff:
                diff_str = json.dumps(f.body_diff, ensure_ascii=False, default=str)
                if len(diff_str) > 500:
                    diff_str = diff_str[:500] + "..."
                detail += f"```{diff_str}```"
            if f.error:
                detail += f"Error: {f.error}"

            blocks.append({"type": "section", "text": {"type": "mrkdwn", "text": detail}})

        if dashboard_url:
            blocks.append(
                {
                    "type": "section",
                    "text": {
                        "type": "mrkdwn",
                        "text": f"<{dashboard_url}|View Dashboard>",
                    },
                }
            )

        payload = {"blocks": blocks}

        try:
            async with httpx.AsyncClient() as client:
                resp = await client.post(
                    self.webhook_url,
                    json=payload,
                    timeout=10.0,
                )
                if resp.status_code != 200:
                    logger.error(f"[slack] failed: {resp.status_code} {resp.text}")
                else:
                    logger.info(f"[slack] alert sent for {domain}")
        except Exception as e:
            logger.error(f"[slack] failed to send: {e}")
