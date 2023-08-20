
# mvn clean -f "/Users/euiyoung.hwang/ES/Python_Workspace/python-search/Java/demo/pom.xml"
# mvn install -f "/Users/euiyoung.hwang/ES/Python_Workspace/python-search/Java/demo/pom.xml"

# docker run --rm --name abc fn-search-java:es

FROM --platform=linux/amd64 openjdk:11 as environment
ARG JAR_FILE=demo/target/java-indexing-1.0-SNAPSHOT.jar

WORKDIR /app

COPY ${JAR_FILE} ./indexing.jar
COPY . ES-Services


# ENTRYPOINT ["java","-jar","/indexing.jar"]
ENTRYPOINT ["/app/ES-Services/docker-run-entrypoints.sh"]


