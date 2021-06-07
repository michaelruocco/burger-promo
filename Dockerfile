FROM adoptopenjdk/openjdk15:alpine-jre

ARG VERSION

ENV SERVER_PORT=80 \
    SPRING_PROFILES_ACTIVE=default

COPY build/libs/burger-promo-${VERSION}.jar /opt/app.jar

CMD java \
  -Djava.security.egd=file:/dev/./urandom \
  -Dserver.port=${SERVER_PORT} \
  -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
  -Dspring.data.mongodb.uri=${MONGO_CONNECTION_STRING} \
  -jar /opt/app.jar