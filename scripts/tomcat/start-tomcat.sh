#!/usr/bin/env bash
set -euo pipefail
: "${JAVA_HOME:?Debe definir JAVA_HOME}"
: "${CATALINA_HOME:?Debe definir CATALINA_HOME}"
test -x "$CATALINA_HOME/bin/startup.sh"
"$CATALINA_HOME/bin/startup.sh"
