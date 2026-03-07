"""Test suite runner - orchestrates shadow traffic tests."""

from __future__ import annotations

import json
import logging
from pathlib import Path
from typing import Any

import httpx
import yaml

from auth import AuthConfig, AuthProvider
from client import CallResult, call_endpoint
from diff_engine import CompareMode, DiffResult, compare_responses

logger = logging.getLogger(__name__)

MAX_BODY_LOG_SIZE = 3000


def _truncate(obj: Any, max_size: int = MAX_BODY_LOG_SIZE) -> Any:
    """Truncate large objects for logging."""
    try:
        s = json.dumps(obj, ensure_ascii=False, default=str)
    except (TypeError, ValueError):
        s = str(obj)
    if len(s) <= max_size:
        return obj
    return s[:max_size] + f"... (truncated, total {len(s)} chars)"


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
) -> tuple[DiffResult, Any, Any]:
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
        ), legacy_result.body, new_result.body

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
        ), legacy_result.body, new_result.body

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
    ), legacy_result.body, new_result.body


async def run_suite(
    http_client,
    suite: dict,
    legacy_url: str,
    new_url: str,
    auth_headers: dict[str, str] | None = None,
) -> tuple[list[DiffResult], dict, dict]:
    domain = suite["domain"]
    results = []
    legacy_bodies: dict[str, object] = {}
    new_bodies: dict[str, object] = {}

    # Auth: acquire token if suite has auth_config
    suite_auth_headers = dict(auth_headers) if auth_headers else {}
    auth_config = AuthConfig.from_dict(suite.get("auth_config"))
    if auth_config.strategy != "none":
        auth_provider = AuthProvider(auth_config)
        token = await auth_provider.acquire_token(http_client, legacy_url)
        if token:
            suite_auth_headers.update(auth_provider.get_headers())
            logger.info(f"[{domain}] Auth token acquired (strategy={auth_config.strategy})")
        else:
            logger.warning(f"[{domain}] Failed to acquire auth token, authenticated tests may fail")

    for tc in suite.get("test_cases", []):
        # Pass suite-level auth headers only for test cases that need auth
        needs_auth = tc.get("auth") and tc["auth"] != "none"
        tc_auth = suite_auth_headers if needs_auth else None

        # Use a fresh client for unauthenticated tests to avoid cookie leakage
        # (httpx shares cookies from login across all requests in the same client)
        if needs_auth:
            result, legacy_body, new_body = await run_test_case(
                http_client, legacy_url, new_url, tc, tc_auth
            )
        else:
            async with httpx.AsyncClient(follow_redirects=False) as clean_client:
                result, legacy_body, new_body = await run_test_case(
                    clean_client, legacy_url, new_url, tc, tc_auth
                )
        log_record = logger.makeRecord(
            logger.name, logging.INFO, "", 0,
            result.summary(), (), None,
        )
        log_record.extra_data = {
            "event": "test_result",
            "domain": domain,
            "test_name": result.test_name,
            "passed": result.passed,
            "status_match": result.status_match,
            "legacy_status": result.legacy_status,
            "new_status": result.new_status,
            "legacy_latency_ms": round(result.legacy_latency_ms, 1),
            "new_latency_ms": round(result.new_latency_ms, 1),
        }
        if result.body_diff:
            log_record.extra_data["diff_keys"] = list(result.body_diff.keys())
            log_record.extra_data["diff_detail"] = _truncate(result.body_diff)
        if not result.passed:
            log_record.extra_data["legacy_body"] = _truncate(legacy_body)
            log_record.extra_data["new_body"] = _truncate(new_body)
        if result.error:
            log_record.extra_data["error"] = result.error
        logger.handle(log_record)
        results.append(result)

        if not result.passed:
            legacy_bodies[result.test_name] = legacy_body
            new_bodies[result.test_name] = new_body

    return results, legacy_bodies, new_bodies
