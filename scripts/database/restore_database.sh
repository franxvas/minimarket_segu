#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
SOURCE="${1:?Uso: restore_database.sh RUTA_RESPALDO}"
if lsof -nP -iTCP:1666 -sTCP:LISTEN >/dev/null 2>&1; then
  echo "Detenga la aplicacion/HSQLDB antes de restaurar." >&2
  exit 1
fi
test -f "$SOURCE/minimarket_segu-db.properties"
for extension in properties script data backup log lck; do
  rm -f "$ROOT/data/minimarket_segu-db.$extension"
done
rmdir "$ROOT/data/minimarket_segu-db.tmp" 2>/dev/null || true
for extension in properties script data backup log; do
  file="$SOURCE/minimarket_segu-db.$extension"
  [[ -f "$file" ]] && cp "$file" "$ROOT/data/"
done
echo "Base restaurada desde $SOURCE"
