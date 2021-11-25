#Compile the app
FROM maven:3.6.3 AS compilation

WORKDIR /usr/src/app
COPY /spring-boot-sample-data-jpa-standalone/ /usr/src/app

RUN mvn clean package -DskipTests=true

FROM jolokia/alpine-jre-8 as launch

ARG JAR_FILE=spring-boot-sample-data-jpa-2.3.3.RELEASE-spring-boot.jar

WORKDIR /opt/app

COPY --from=compilation /usr/src/app/target/${JAR_FILE} /opt/app

ENTRYPOINT ["java", "-jar", "spring-boot-sample-data-jpa-2.3.3.RELEASE-spring-boot.jar"]
