FROM openjdk:17
LABEL authors="Ri Arkadiy"
ADD build/libs/my-spring-boot-app_soa.jar soa1.jar
ENTRYPOINT ["java", "-jar", "soa1.jar"]