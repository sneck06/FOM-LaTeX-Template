FROM dockerhub.devops.telekom.de/eclipse-temurin:11-jre-alpine

COPY /build/libs/*.jar /home/service/bonienrichmentprototype/app.jar

COPY /versions /home/service/bonienrichmentprototype/versions

COPY /src/main/resources/keystore/*.jks /home/service/bonienrichmentprototype/src/main/resources/keystore/

COPY /logs/*.xml /home/service/bonienrichmentprototype/logs/

#bash shell requested by ITT
RUN apk add --update bash && rm -rf /var/cache/apk/*
#less command requested by ITT
RUN apk add --update less && rm -rf /var/cache/apk/*
#zgrep command requested by ITT
RUN apk add --update gzip && rm -rf /var/cache/apk/*

EXPOSE 47466

WORKDIR /home/service

CMD java -jar bonienrichmentprototype/app.jar
