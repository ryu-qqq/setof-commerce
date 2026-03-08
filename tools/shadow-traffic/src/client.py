"""HTTP client for calling legacy and new endpoints."""

from __future__ import annotations

import time
from dataclasses import dataclass
from typing import Any

import httpx


@dataclass
class CallResult:
    status_code: int
    body: Any
    latency_ms: float
    error: str | None = None


async def call_endpoint(
    http_client: httpx.AsyncClient,
    base_url: str,
    method: str,
    path: str,
    headers: dict[str, str] | None = None,
    body: dict | None = None,
    timeout: float = 10.0,
) -> CallResult:
    url = f"{base_url.rstrip('/')}{path}"
    start = time.monotonic()
    try:
        # Legacy server uses cookie-based auth, New server uses Bearer header.
        # Send token as both cookie and header so both servers authenticate.
        cookies = None
        if headers and "Authorization" in headers:
            token = headers["Authorization"].removeprefix("Bearer ")
            cookies = {"token": token}

        response = await http_client.request(
            method=method.upper(),
            url=url,
            headers=headers,
            cookies=cookies,
            json=body if method.upper() in ("POST", "PUT", "PATCH") else None,
            params=body if method.upper() in ("GET", "DELETE") and body else None,
            timeout=timeout,
        )
        latency_ms = (time.monotonic() - start) * 1000

        try:
            resp_body = response.json()
        except Exception:
            resp_body = response.text

        return CallResult(
            status_code=response.status_code,
            body=resp_body,
            latency_ms=latency_ms,
        )
    except httpx.TimeoutException:
        latency_ms = (time.monotonic() - start) * 1000
        return CallResult(
            status_code=0,
            body=None,
            latency_ms=latency_ms,
            error="TIMEOUT",
        )
    except Exception as e:
        latency_ms = (time.monotonic() - start) * 1000
        return CallResult(
            status_code=0,
            body=None,
            latency_ms=latency_ms,
            error=str(e),
        )
