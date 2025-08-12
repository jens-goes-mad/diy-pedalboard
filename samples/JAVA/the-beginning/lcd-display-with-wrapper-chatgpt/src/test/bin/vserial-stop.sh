#!/usr/bin/env bash
set -euo pipefail
PAIR_NAME="${1:-vserial0}"
LEFT="/tmp/${PAIR_NAME}A"
RIGHT="/tmp/${PAIR_NAME}B"
PID_FILE="/tmp/${PAIR_NAME}.pid"

if [[ -f "$PID_FILE" ]]; then
  PID="$(cat "$PID_FILE" || true)"
  if [[ -n "${PID:-}" ]] && ps -p "$PID" >/dev/null 2>&1; then
    kill "$PID" || true
    sleep 0.2
  fi
  rm -f "$PID_FILE"
fi

rm -f "$LEFT" "$RIGHT"
echo "Stopped $PAIR_NAME."
