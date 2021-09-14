#!/usr/bin/env bash

version=$1
language=$2
generatorname=$3

docker build --build-arg VERSION=$version \
  -t openapi-generator-cli-custom \
  -f ${GITHUB_WORKSPACE}/generator/openapi-generator/Dockerfile \
  ${GITHUB_WORKSPACE}/generator/openapi-generator

docker run --rm -v ${GITHUB_WORKSPACE}/generator:/generator \
  openapi-generator-cli-custom generate \
  --generator-name $generatorname \
  --input-spec /generator/openapi-schema.json \
  --output /generator/languages/$language/sdk \
  --config /generator/languages/$language/openapi-generator-config.json \
  --template-dir /generator/languages/$language/templates \
  --skip-validate-spec
  --verbose
