#!/usr/bin/env bash

set -euo pipefail

GEN_DIR=${GEN_DIR:-/opt/openapi-generator}
JAVA_OPTS=${JAVA_OPTS:-"-Xmx1024M -DloggerPath=conf/log4j.properties -DmodelTests=false -DapiTests=false"}

cli="${GEN_DIR}/modules/openapi-generator-cli"
target="${cli}/target"

javac -classpath ""${target}"/*" "${target}"/*.java 
exec java ${JAVA_OPTS} -classpath "${target}"/:"${target}"/* org.openapitools.codegen.OpenAPIGenerator "$@"
