FROM alpine:3.19.0 AS build
LABEL maintainer="Ozren Dabić (dabico@usi.ch)"

ENV IMAGE_NAME="seart-group/java-tree-sitter" \
    IMAGE_REPO_URL="https://github.com/${IMAGE_NAME}/" \
    JAVA_HOME="/usr/lib/jvm/java-11-openjdk"

RUN apk update --quiet && \
    apk add --quiet \
            --no-cache \
            alpine-sdk \
            make \
            openjdk11 \
            python3 \
            py3-distutils-extra

WORKDIR /java-tree-sitter
COPY . ./

RUN python3 build.py

FROM scratch AS export
COPY --from=build /java-tree-sitter/libjava-tree-sitter.so .
