FROM openjdk:11.0-jre-slim
EXPOSE 8006
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} ms-deposit.jar
ENTRYPOINT ["java","-jar","/ms-deposit.jar"]