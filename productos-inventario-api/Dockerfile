# ================================
# Etapa 1: Compilar con Maven
# ================================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos pom.xml primero para cachear dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Compilamos el JAR (sin tests, se ejecutan por separado)
RUN mvn clean package -DskipTests

# ================================
# Etapa 2: Imagen liviana para ejecutar
# ================================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiamos el JAR generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Puerto del microservicio
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
