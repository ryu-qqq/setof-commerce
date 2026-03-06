"""Response comparison engine using deepdiff."""

from __future__ import annotations

import json
from dataclasses import dataclass, field
from enum import Enum
from typing import Any

from deepdiff import DeepDiff


class CompareMode(str, Enum):
    FULL = "full"
    STRUCTURE_ONLY = "structure_only"
    STATUS_ONLY = "status_only"


DEFAULT_IGNORE_FIELDS = frozenset(
    {
        "root['timestamp']",
        "root['insertDate']",
        "root['updateDate']",
    }
)

TIMESTAMP_PATTERNS = frozenset(
    {"createdAt", "updatedAt", "insertDate", "updateDate", "timestamp"}
)


@dataclass
class DiffResult:
    test_name: str
    passed: bool
    status_match: bool
    legacy_status: int
    new_status: int
    legacy_latency_ms: float
    new_latency_ms: float
    body_diff: dict[str, Any] = field(default_factory=dict)
    error: str | None = None

    def summary(self) -> str:
        if self.passed:
            return f"PASS  {self.test_name} (legacy={self.legacy_latency_ms:.0f}ms, new={self.new_latency_ms:.0f}ms)"
        parts = [f"FAIL  {self.test_name}"]
        if not self.status_match:
            parts.append(
                f"status: legacy={self.legacy_status} new={self.new_status}"
            )
        if self.body_diff:
            parts.append(f"diff_keys: {list(self.body_diff.keys())}")
        if self.error:
            parts.append(f"error: {self.error}")
        return " | ".join(parts)


def _should_ignore(path: str, ignore_fields: set[str]) -> bool:
    for pattern in ignore_fields:
        if pattern in path:
            return True
    for ts_field in TIMESTAMP_PATTERNS:
        if ts_field in path:
            return True
    return False


def _filter_diff(diff_dict: dict, ignore_fields: set[str]) -> dict:
    filtered = {}
    for change_type, changes in diff_dict.items():
        if isinstance(changes, dict):
            kept = {
                k: v
                for k, v in changes.items()
                if not _should_ignore(k, ignore_fields)
            }
            if kept:
                filtered[change_type] = kept
        else:
            filtered[change_type] = changes
    return filtered


def compare_responses(
    test_name: str,
    legacy_status: int,
    new_status: int,
    legacy_body: Any,
    new_body: Any,
    legacy_latency_ms: float,
    new_latency_ms: float,
    compare_mode: CompareMode = CompareMode.FULL,
    ignore_fields: set[str] | None = None,
) -> DiffResult:
    effective_ignore = set(DEFAULT_IGNORE_FIELDS)
    if ignore_fields:
        effective_ignore.update(ignore_fields)

    status_match = legacy_status == new_status

    if compare_mode == CompareMode.STATUS_ONLY:
        return DiffResult(
            test_name=test_name,
            passed=status_match,
            status_match=status_match,
            legacy_status=legacy_status,
            new_status=new_status,
            legacy_latency_ms=legacy_latency_ms,
            new_latency_ms=new_latency_ms,
        )

    if not status_match:
        return DiffResult(
            test_name=test_name,
            passed=False,
            status_match=False,
            legacy_status=legacy_status,
            new_status=new_status,
            legacy_latency_ms=legacy_latency_ms,
            new_latency_ms=new_latency_ms,
        )

    if compare_mode == CompareMode.STRUCTURE_ONLY:
        diff = DeepDiff(
            legacy_body,
            new_body,
            ignore_order=True,
            ignore_numeric_type_changes=True,
            ignore_string_type_changes=True,
            include_paths=None,
            verbose_level=0,
        )
        # structure_only: only care about type_changes and dictionary_item_added/removed
        structure_diff = {}
        for key in ("type_changes", "dictionary_item_added", "dictionary_item_removed"):
            if key in diff:
                structure_diff[key] = diff[key]
        body_diff = _filter_diff(structure_diff, effective_ignore)
    else:
        diff = DeepDiff(
            legacy_body,
            new_body,
            ignore_order=True,
            ignore_numeric_type_changes=True,
            verbose_level=2,
        )
        body_diff = _filter_diff(dict(diff), effective_ignore)

    return DiffResult(
        test_name=test_name,
        passed=len(body_diff) == 0,
        status_match=True,
        legacy_status=legacy_status,
        new_status=new_status,
        legacy_latency_ms=legacy_latency_ms,
        new_latency_ms=new_latency_ms,
        body_diff=body_diff,
    )
