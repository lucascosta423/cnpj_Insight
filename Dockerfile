# Etapa 1: Build
FROM openjdk:21-jdk-slim as builder

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x ./mvnw

# Baixa dependências
RUN ./mvnw dependency:go-offline -B --no-transfer-progress

# Copia o código-fonte
COPY src ./src

# Gera o JAR
RUN ./mvnw clean package -DskipTests -B --no-transfer-progress

# Etapa 2: Runtime
FROM openjdk:21-jdk-slim

# Instala curl para o healthcheck
RUN apt-get update && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd -r cnpjinsight && useradd -r -g cnpjinsight cnpjinsight

WORKDIR /app

# Cria e dá permissão à pasta de exportações
RUN mkdir -p /app/exports && chown -R cnpjinsight:cnpjinsight /app

# Copia o JAR gerado
COPY --from=builder /app/target/*.jar app.jar

# Usa usuário não-root
USER cnpjinsight

# Expõe porta da aplicação
EXPOSE 8080

# Healthcheck do Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Executa a aplicação diretamente
ENTRYPOINT ["java", "-jar", "app.jar"]
