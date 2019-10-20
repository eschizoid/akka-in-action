FROM oracle/graalvm-ce:latest

COPY build/libs/akka-in-action-1.0-SNAPSHOT-all.jar /app/akka-in-action-1.0-SNAPSHOT-all.jar

EXPOSE 5000

CMD ["java", "-jar", "/app/akka-in-action-1.0-SNAPSHOT-all.jar"]
