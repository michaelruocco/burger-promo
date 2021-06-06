FROM adoptopenjdk/openjdk15:alpine-jre

ARG VERSION

ENV SERVER_PORT=80

COPY build/libs/burger-promo-${VERSION}.jar /opt/app.jar

CMD java \
  -Djava.security.egd=file:/dev/./urandom \
  -Dserver.port=${SERVER_PORT} \
  -Dspring.data.mongodb.uri=${MONGO_CONNECTION_STRING} \
  -jar /opt/app.jar