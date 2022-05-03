FROM openjdk:17-alpine3.14

RUN apk add --no-cache curl unzip graphviz fontconfig

WORKDIR /usr/share/fonts/open-sans
COPY open-sans.zip .
RUN unzip open-sans.zip && \
    rm -f open-sans.zip && \
    fc-cache -fv

WORKDIR /usr/local/lib/schemaspy
RUN curl -Lo schemaspy.jar https://github.com/schemaspy/schemaspy/releases/download/v6.1.0/schemaspy-6.1.0.jar

WORKDIR /drivers
RUN curl -JLO https://jdbc.postgresql.org/download/postgresql-42.3.4.jar

WORKDIR /output

WORKDIR /
