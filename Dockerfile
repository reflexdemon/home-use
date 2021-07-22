FROM adoptopenjdk:11-jdk
EXPOSE 8080
ADD build/libs/home-use.jar /app/
WORKDIR /app
CMD java -cp . -jar home-use.jar
