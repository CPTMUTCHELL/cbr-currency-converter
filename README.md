# cbr-currency-converter
Recommendations:
1. Create a postgres database with name "converterDb"
2. Set your username and password for your postgres server in application.properties (postgres, postgres by default)
3. Default port is 8080. In order to select another add server.port="desired port" in application.properties
4. Go to downloaded project, open cmd, run:
```
mvn clean package
``` 
5.After packaging run:
```
6.java -jar target/converter-0.0.1-SNAPSHOT.jar^C
```
7. Register a new user and sign in to convert currencies.
