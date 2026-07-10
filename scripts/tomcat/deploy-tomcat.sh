#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
: "${JAVA_HOME:?Debe definir JAVA_HOME}"
: "${CATALINA_HOME:?Debe definir CATALINA_HOME (Tomcat 9)}"
command -v mvn >/dev/null || { echo "Maven no esta disponible" >&2; exit 1; }
test -x "$CATALINA_HOME/bin/catalina.sh" || { echo "CATALINA_HOME no contiene Tomcat" >&2; exit 1; }
cd "$ROOT"
mvn clean package -DskipTests -Djacoco.skip=true
test -f target/minimarket_segu.war
cp target/minimarket_segu.war "$CATALINA_HOME/webapps/minimarket_segu.war"
echo "WAR copiado a $CATALINA_HOME/webapps/minimarket_segu.war"
