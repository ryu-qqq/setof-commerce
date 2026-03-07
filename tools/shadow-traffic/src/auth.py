"""Authentication provider for shadow traffic tests.

Supports JWT token acquisition from Legacy server login API.
Both Legacy and New servers share the same JWT_SECRET,
so a token from Legacy is valid on New server too.

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

    The token is obtained from the Legacy server and reused for both
    Legacy and New server requests (they share the same JWT_SECRET).
    """

    def __init__(self, config: AuthConfig):
        self._config = config
        self._token: str | None = None

    @property
    def strategy(self) -> str:
        return self._config.strategy

    def needs_auth(self) -> bool:
        return self._config.strategy != "none"

    async def acquire_token(self, http_client: httpx.AsyncClient, legacy_url: str) -> str | None:
        """Acquire JWT token based on configured strategy."""
        if self._token:
            return self._token

        if self._config.strategy == "static":
            self._token = self._config.token
            if not self._token:
                logger.warning("Static token strategy but no token provided")
            return self._token

        if self._config.strategy == "login":
            return await self._login(http_client, legacy_url)

        return None

    async def _login(self, http_client: httpx.AsyncClient, legacy_url: str) -> str | None:
        """Login to Legacy server and extract JWT token."""
        url = f"{legacy_url.rstrip('/')}{self._config.login_endpoint}"
        logger.info(f"Acquiring JWT token from {url}")

        try:
            response = await http_client.post(
                url,
                json={
                    "phoneNumber": self._config.phone_number,
                    "passwordHash": self._config.password,
                },
                timeout=10.0,
            )

            if response.status_code != 200:
                logger.error(
                    f"Login failed: status={response.status_code}, body={response.text[:500]}"
                )
                return None

            # Legacy login sets token in cookie, not response body
            # Check Set-Cookie header first
            cookies = response.cookies
            token = cookies.get("accessToken") or cookies.get("token")

            if not token:
                # Fallback: try response body
                try:
                    body = response.json()
                    data = body.get("data", {})
                    if isinstance(data, dict):
                        token = data.get("accessToken") or data.get("token")
                except Exception:
                    pass

            if not token:
                # Fallback: check Authorization header in response
                auth_header = response.headers.get("Authorization", "")
                if auth_header.startswith("Bearer "):
                    token = auth_header[7:]

            if token:
                self._token = token
                logger.info("JWT token acquired successfully")
            else:
                logger.error("Could not extract token from login response")
                logger.debug(f"Response headers: {dict(response.headers)}")
                logger.debug(f"Response cookies: {dict(response.cookies)}")

            return self._token

        except Exception as e:
            logger.error(f"Login request failed: {e}")
            return None

    def get_headers(self) -> dict[str, str]:
        """Return auth headers with cached token."""
        if not self._token:
            return {}
        return {"Authorization": f"Bearer {self._token}"}

    def clear(self) -> None:
        """Clear cached token (for re-authentication)."""
        self._token = None
