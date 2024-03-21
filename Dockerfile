FROM alpine:3.17.7 AS build
LABEL maintainer="Ozren Dabić (dabico@usi.ch)"

ENV JAVA_HOME="/usr/lib/jvm/java-11-openjdk"

RUN apk update && \
    apk add --no-cache \
            openjdk11~=11.0.22 \
            python3~=3.10.13 \
            py3-distutils-extra~=2.47 \
            make~=4.3 \
            g++~=12.2.1

WORKDIR /java-tree-sitter
COPY . ./

RUN python3 build.py

FROM scratch AS export

WORKDIR /

COPY --from=build /java-tree-sitter/libjava-tree-sitter.so .
