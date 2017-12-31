## SBT Docker Sample App

Scala sample app that integrates docker through SBT Docker Plugin. This can help to quickly build microservices with CI/CD.

### Requirements

* git
* JDK 8
* sbt >= 0.13
* docker-compose >= 1.11

### Usage

#### Test
```
$ sbt test
```

**Start the application**

Build the application's Dockerfile:
```
$ sbt docker:stage
```
Build the docker image:
```
$ docker-compose build
```
Start container with your app:
```
$ docker-compose up
```
Run the application in _watch mode_ (`~re-start` watches changes in code and compile them before restarting the service):
```
  $ sbt 
  > ~re-start
```
Simply run the application:
```
  $ sbt run
```
With the service up, you can start sending HTTP requests:

**Status service**
```
$ curl http://127.0.0.1:8080/sample-app/status
```

**Name service**
```
$ curl -X POST \
    http://127.0.0.1:8080/sample-app/name \
    -H 'content-type: application/json' \    
    -d '{
  	"name": "Mauro"
  }'
```

**OpenAPI spec**
```
$ curl http://127.0.0.1:8080/sample-app/api-docs/swagger.yaml
```

## Notes
* Use functions, functions and more functions! (Type, compose, reuse, partial and play them =D)

* Type almost all your functions, those that are not trivial ;). This is real documentation!

* Use [Scala pattern matching](http://docs.scala-lang.org/tutorials/tour/pattern-matching.html)
( [Pattern matching](http://wiki.c2.com/?PatternMatching) )



## Author

If you have any questions regarding this project contact:  
Mauro González <jmajma8@gmail.com>
