FROM openjdk:11
COPY target/orders-api.jar orders-api.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/orders-api.jar"]