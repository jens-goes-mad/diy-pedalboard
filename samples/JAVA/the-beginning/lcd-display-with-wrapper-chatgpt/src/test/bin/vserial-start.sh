#!/usr/bin/env bash
set -euo pipefail

# Usage: ./vserial-start.sh [pair-name]
# Creates /tmp/<pair-name>A  and /tmp/<pair-name>B  pointing to two linked PTYs.
PAIR_NAME="${1:-vserial0}"
LEFT="/tmp/${PAIR_NAME}A"
RIGHT="/tmp/${PAIR_NAME}B"
PID_FILE="/tmp/${PAIR_NAME}.pid"

# Stop any previous instance for this pair (if running)
if [[ -f "$PID_FILE" ]]; then
  OLD_PID="$(cat "$PID_FILE" || true)"
  if [[ -n "${OLD_PID:-}" ]] && ps -p "$OLD_PID" >/dev/null 2>&1; then
    echo "Killing previous socat (PID $OLD_PID) for $PAIR_NAME..."
    kill "$OLD_PID" || true
    sleep 0.2
  fi
  rm -f "$PID_FILE"
fi

# Clean old links if they exist
rm -f "$LEFT" "$RIGHT"

# Create two linked PTYs with stable, user-writable symlinks under /tmp
# NOTE: macOS will not allow creating links under /dev; /tmp is fine.
socat -d -d pty,raw,echo=0,link=/tmp/vserial0A,mode=666 \
            pty,raw,echo=0,link=/tmp/vserial0B,mode=666 \
            &

SOCAT_PID=$!
echo "$SOCAT_PID" > "$PID_FILE"

# Wait for symlinks to appear (up to ~2s)
for i in {1..20}; do
  if [[ -L "$LEFT" && -L "$RIGHT" ]]; then
    break
  fi
  sleep 0.1
done

if [[ ! -L "$LEFT" || ! -L "$RIGHT" ]]; then
  echo "Error: virtual links were not created. See /tmp/${PAIR_NAME}.socat.out"
  exit 1
fi

L_REAL="$(readlink "$LEFT")"
R_REAL="$(readlink "$RIGHT")"

echo "Virtual serial pair is up:"
echo "  $LEFT  ->  $L_REAL"
echo "  $RIGHT ->  $R_REAL"
echo "socat PID: $SOCAT_PID (stored in $PID_FILE)"
echo
echo "Tips:"
echo "  1) Open the peer first (PTYs behave better):"
echo "       screen $RIGHT 115200"
echo "     then open $LEFT in your Java app."
echo "  2) If Java struggles with symlinks, open the real device instead:"
echo "       $L_REAL"
echo "  3) Logs: /tmp/${PAIR_NAME}.socat.out"
