# cbr-currency-converter
Recommendations:
1. Create a postgres database with name "converter_db"
2. Set your username and password for your postgres server in application.yml (postgres, postgres by default)
3. Default port is 8080. In order to select another add server.port="desired port" in application.yml
4. Go to downloaded project folder, open cmd, run:
```
mvn clean package
``` 
5.After packaging run:
```
java -jar target/converter-0.0.1-SNAPSHOT.jar
```
6. Register a new user and sign in to convert currencies.


Docker:
1. Go to downloaded project folder, open cmd, run:
```
docker-compose up --build
``` 
Data will be saved in root directory (./postgres-data). 

wait-for-it.sh is taken from https://github.com/vishnubob/wait-for-it
