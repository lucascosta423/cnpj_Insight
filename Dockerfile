# Etapa 1: Build
FROM openjdk:21-jdk-slim as builder

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x ./mvnw

# Cache dependências + plugins
RUN ./mvnw dependency:go-offline -B --no-transfer-progress

COPY src ./src

RUN ./mvnw clean package -DskipTests -B --no-transfer-progress

# Etapa 2: Runtime
FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd -r cnpjinsight && useradd -r -g cnpjinsight cnpjinsight

WORKDIR /app

RUN mkdir -p /app/exports && chown -R cnpjinsight:cnpjinsight /app

COPY --from=builder /app/target/*.jar app.jar

# Copia o script de start
COPY start.sh .
RUN chmod +x start.sh

USER cnpjinsight

EXPOSE 8080

ENV JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["./start.sh"]
