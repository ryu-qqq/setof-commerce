"""Authentication provider for shadow traffic tests.

Supports JWT token acquisition from both Legacy and New servers independently.
Legacy and New may use different JWT implementations, so each server needs
its own token acquired via its own login endpoint.

Usage in test-cases YAML:
    auth_config:
      strategy: login           # login | static | none
      login_endpoint: /api/v1/user/login
      phone_number: "01012345678"
      password: "testPassword1!"
    # or
    auth_config:
      strategy: static
      token: "${JWT_TOKEN}"     # env var
"""

from __future__ import annotations

import logging
import os
from dataclasses import dataclass

import httpx

logger = logging.getLogger(__name__)


def _resolve_env(value: str) -> str:
    """Resolve ${ENV_VAR} patterns to environment variable values."""
    if not value:
        return value
    if value.startswith("${") and value.endswith("}"):
        env_key = value[2:-1]
        return os.getenv(env_key, "")
    return value


@dataclass
class AuthConfig:
    """Authentication configuration parsed from suite YAML."""

    strategy: str = "none"  # none | login | static
    login_endpoint: str = "/api/v1/user/login"
    phone_number: str = ""
    password: str = ""
    token: str = ""

    @classmethod
    def from_dict(cls, data: dict | None) -> AuthConfig:
        if not data:
            return cls()

        strategy = data.get("strategy", "none")

        if strategy == "static":
            return cls(
                strategy="static",
                token=_resolve_env(data.get("token", "")),
            )

        if strategy == "login":
            return cls(
                strategy="login",
                login_endpoint=data.get("login_endpoint", "/api/v1/user/login"),
                phone_number=_resolve_env(data.get("phone_number", "")),
                password=_resolve_env(data.get("password", "")),
            )

        return cls(strategy="none")


class AuthProvider:
    """Acquires and caches JWT tokens for authenticated shadow traffic tests.

    Since Legacy and New servers may have different JWT implementations,
    tokens are acquired independently from each server.
    """

    def __init__(self, config: AuthConfig):
        self._config = config
        self._legacy_token: str | None = None
        self._new_token: str | None = None

    @property
    def strategy(self) -> str:
        return self._config.strategy

    def needs_auth(self) -> bool:
        return self._config.strategy != "none"

    async def acquire_tokens(
        self,
        http_client: httpx.AsyncClient,
        legacy_url: str,
        new_url: str,
    ) -> bool:
        """Acquire JWT tokens from both Legacy and New servers."""
        if self._config.strategy == "static":
            self._legacy_token = self._config.token
            self._new_token = self._config.token
            return bool(self._legacy_token)

        if self._config.strategy == "login":
            legacy_ok = await self._login(http_client, legacy_url, "legacy")
            new_ok = await self._login(http_client, new_url, "new")
            return legacy_ok and new_ok

        return False

    async def _login(
        self,
        http_client: httpx.AsyncClient,
        base_url: str,
        server_label: str,
    ) -> bool:
        """Login to a server and extract JWT token."""
        url = f"{base_url.rstrip('/')}{self._config.login_endpoint}"
        logger.info(f"Acquiring JWT token from {url} ({server_label})")

        try:
            payload = {
                "phoneNumber": self._config.phone_number,
                "passwordHash": self._config.password,
            }
            response = await http_client.post(
                url,
                json=payload,
                timeout=10.0,
            )

            if response.status_code != 200:
                logger.error(
                    f"Login failed ({server_label}): status={response.status_code}, "
                    f"body={response.text[:500]}"
                )
                return False

            # Extract token: cookie first, then response body, then header
            token = self._extract_token(response)

            if token:
                if server_label == "legacy":
                    self._legacy_token = token
                else:
                    self._new_token = token
                logger.info(f"JWT token acquired successfully ({server_label})")
                return True
            else:
                logger.error(f"Could not extract token from login response ({server_label})")
                logger.debug(f"Response headers: {dict(response.headers)}")
                logger.debug(f"Response cookies: {dict(response.cookies)}")
                return False

        except Exception as e:
            logger.error(f"Login request failed ({server_label}): {e}")
            return False

    def _extract_token(self, response: httpx.Response) -> str | None:
        """Extract JWT token from response (cookie → body → header)."""
        # 1. Check Set-Cookie header
        cookies = response.cookies
        token = cookies.get("accessToken") or cookies.get("token")
        if token:
            return token

        # 2. Try response body
        try:
            body = response.json()
            data = body.get("data", {})
            if isinstance(data, dict):
                token = data.get("accessToken") or data.get("token")
                if token:
                    return token
        except Exception:
            pass

        # 3. Check Authorization header in response
        auth_header = response.headers.get("Authorization", "")
        if auth_header.startswith("Bearer "):
            return auth_header[7:]

        return None

    def get_legacy_headers(self) -> dict[str, str]:
        """Return auth headers for Legacy server."""
        if not self._legacy_token:
            return {}
        return {"Authorization": f"Bearer {self._legacy_token}"}

    def get_new_headers(self) -> dict[str, str]:
        """Return auth headers for New server."""
        if not self._new_token:
            return {}
        return {"Authorization": f"Bearer {self._new_token}"}

    def clear(self) -> None:
        """Clear cached tokens (for re-authentication)."""
        self._legacy_token = None
        self._new_token = None
