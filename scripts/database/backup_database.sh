#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
DEST="${1:-$ROOT/backups/$(date +%Y%m%d-%H%M%S)}"
if lsof -nP -iTCP:1666 -sTCP:LISTEN >/dev/null 2>&1; then
  echo "Detenga la aplicacion/HSQLDB antes de copiar la base de datos." >&2
  exit 1
fi
mkdir -p "$DEST"
for extension in properties script data backup log; do
  file="$ROOT/data/minimarket_segu-db.$extension"
  [[ -f "$file" ]] && cp "$file" "$DEST/"
done
echo "Respaldo creado en $DEST"
