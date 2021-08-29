FROM openjdk:8

ARG VERSION=5.1.1

ENV GEN_DIR /opt/openapi-generator

ADD https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/${VERSION}/openapi-generator-cli-${VERSION}.jar ${GEN_DIR}/modules/openapi-generator-cli/target/openapi-generator-cli.jar

WORKDIR ${GEN_DIR}/modules/openapi-generator-cli/target

COPY ./codegen/* ${GEN_DIR}/modules/openapi-generator-cli/target/

COPY docker-entrypoint.sh /usr/local/bin/

RUN chmod +x /usr/local/bin/docker-entrypoint.sh

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]

CMD ["help"] 
