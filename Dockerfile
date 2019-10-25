FROM openjdk:11
ADD target/martian-robots.jar /app/martian-robots.jar
ENTRYPOINT ["java", "-jar", "/app/martian-robots.jar"]
