#!/usr/bin/env bash
set -euo pipefail
: "${CATALINA_HOME:?Debe definir CATALINA_HOME}"
test -x "$CATALINA_HOME/bin/shutdown.sh"
"$CATALINA_HOME/bin/shutdown.sh"
