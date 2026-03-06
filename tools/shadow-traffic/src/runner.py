"""Test suite runner - orchestrates shadow traffic tests."""

from __future__ import annotations

import logging
from pathlib import Path
from typing import Any

import yaml

from client import CallResult, call_endpoint
from diff_engine import CompareMode, DiffResult, compare_responses

logger = logging.getLogger(__name__)


def load_test_suite(path: Path) -> dict[str, Any]:
    with open(path) as f:
        return yaml.safe_load(f)


def load_all_suites(test_cases_dir: Path, domains: list[str] | None = None) -> list[dict]:
    suites = []
    for yml_file in sorted(test_cases_dir.glob("*.yml")):
        suite = load_test_suite(yml_file)
        if domains and suite.get("domain") not in domains:
            continue
        suites.append(suite)
    return suites


async def run_test_case(
    http_client,
    legacy_url: str,
    new_url: str,
    test_case: dict,
    auth_headers: dict[str, str] | None = None,
) -> DiffResult:
    name = test_case["name"]
    method = test_case.get("method", "GET")
    path = test_case["path"]
    body = test_case.get("body")
    compare_mode = CompareMode(test_case.get("compare_mode", "full"))
    ignore_fields = set(test_case.get("ignore_fields", []))

    headers = {}
    if test_case.get("auth") and test_case["auth"] != "none":
        if auth_headers:
            headers.update(auth_headers)

    legacy_result: CallResult = await call_endpoint(
        http_client, legacy_url, method, path, headers=headers, body=body
    )
    new_result: CallResult = await call_endpoint(
        http_client, new_url, method, path, headers=headers, body=body
    )

    if legacy_result.error:
        return DiffResult(
            test_name=name,
            passed=False,
            status_match=False,
            legacy_status=legacy_result.status_code,
            new_status=new_result.status_code,
            legacy_latency_ms=legacy_result.latency_ms,
            new_latency_ms=new_result.latency_ms,
            error=f"legacy error: {legacy_result.error}",
        )

    if new_result.error:
        return DiffResult(
            test_name=name,
            passed=False,
            status_match=False,
            legacy_status=legacy_result.status_code,
            new_status=new_result.status_code,
            legacy_latency_ms=legacy_result.latency_ms,
            new_latency_ms=new_result.latency_ms,
            error=f"new error: {new_result.error}",
        )

    return compare_responses(
        test_name=name,
        legacy_status=legacy_result.status_code,
        new_status=new_result.status_code,
        legacy_body=legacy_result.body,
        new_body=new_result.body,
        legacy_latency_ms=legacy_result.latency_ms,
        new_latency_ms=new_result.latency_ms,
        compare_mode=compare_mode,
        ignore_fields=ignore_fields,
    )


async def run_suite(
    http_client,
    suite: dict,
    legacy_url: str,
    new_url: str,
    auth_headers: dict[str, str] | None = None,
) -> list[DiffResult]:
    domain = suite["domain"]
    results = []

    for tc in suite.get("test_cases", []):
        result = await run_test_case(
            http_client, legacy_url, new_url, tc, auth_headers
        )
        status = "PASS" if result.passed else "FAIL"
        logger.info(f"[{domain}] {status} {result.summary()}")
        results.append(result)

    return results
