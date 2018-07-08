## diff-service
Sample spring boot service project which provides diff related operations.

## Operations
(PUT/POST) /v1/diff/{id}/{left|right}  : operation for message store
 
(GET) /v1/diff/{id}  : operation for diff on provided messages 

## Tech/Framework used
Spring Boot, Swagger

<b>Built with</b>
- [Maven](https://maven.apache.org/)

## Installation

Maven have to be installed on local

"maven install" for build
java -Djava.security.egd=file:/dev/./urandom  -jar target/diff-service-1.0.jar

or 

docker build -t diff-service .
docker run -p 8080:8080 diff-service

## Documentation
http://localhost:8080/swagger-ui.html#

## Improvements
    - message store and diff operations should be replaced with a single operation 
    which accepts both messages and apply diff operation on them simultaneously to
    avoid concurrency restrictions and problems.      
© [Volkan Cetin]()
