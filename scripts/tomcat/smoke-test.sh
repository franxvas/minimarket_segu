#!/usr/bin/env bash
set -euo pipefail
URL="${1:-http://localhost:8080/minimarket_segu/}"
for intento in {1..30}; do
  codigo="$(curl -L -sS -o /tmp/minimarket-smoke.html -w '%{http_code}' "$URL" || true)"
  if [[ "$codigo" == "200" ]]; then
    grep -qi "minimarket" /tmp/minimarket-smoke.html
    echo "OK HTTP 200: $URL"
    exit 0
  fi
  sleep 2
done
echo "La aplicacion no respondio correctamente en $URL" >&2
exit 1
