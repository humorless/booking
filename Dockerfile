FROM openjdk:8-alpine

COPY target/uberjar/booking.jar /booking/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/booking/app.jar"]
