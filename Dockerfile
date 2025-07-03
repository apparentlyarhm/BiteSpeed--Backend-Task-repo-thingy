FROM maven:3.9.6-eclipse-temurin-17-alpine AS thick

WORKDIR /app
COPY . .
RUN mvn clean install

FROM eclipse-temurin:17 AS thin
WORKDIR /app

ARG DB_NAME
ARG DB_INSTANCE_NAME
ARG MYSQL_USER
ARG MYSQL_PASS

COPY --from=thick /app/infra/starter.sh .
COPY --from=thick /app/target/coolname-0.0.1-SNAPSHOT.jar .

ENV DB_NAME=$DB_NAME
ENV DB_INSTANCE_NAME=$DB_INSTANCE_NAME
ENV MYSQL_USER=$MYSQL_USER
ENV MYSQL_PASS=$MYSQL_PASS

RUN chmod 755 starter.sh
CMD ["bash", "./starter.sh"]