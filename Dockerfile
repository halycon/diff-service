FROM openjdk:8-jre-alpine

MAINTAINER vncetin@gmail.com

ENV VERTICLE_FILE diff-service-1.0.jar
ENV VERTICLE_HOME /opt/

EXPOSE 8080:8080

# Copy your fat jar to the container
COPY target/$VERTICLE_FILE $VERTICLE_HOME/

# Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar -Djava.security.egd=file:/dev/./urandom $VERTICLE_FILE "]
