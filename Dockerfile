FROM openjdk
EXPOSE 8080

ADD target/trackerAPIService.jar trackerAPIService.jar


ENTRYPOINT [ "java","-jar","/trackerAPIService.jar" ]

