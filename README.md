# cbr-currency-converter

Old CI/CD setup and IAC can be seen in [old-ci/cd branch](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/old-ci/cd)

New setup is available [here](https://github.com/CPTMUTCHELL/cbr-devops)


This is microservice implementation of currency converter. 
It has the following components:

1. entity. Contains exception handler, Rabbit config, entities for the services and jwt authorization filter.
2. auth-service. Provides sign up and sign in, token refresh and administration features.
3. convert-service. Converts currencies based on the rates of ["Bank of Russia"](http://www.cbr.ru/scripts/XML_daily.asp)
4. history-service. Shows the history of all converts.

Each service has it's own database which are pre-filled by flyway after the first start.
The pre-created user is admin:admin. 

Swagger is available for each service at ${domain}/backend/${service}/swagger-ui.html for example: http://myconverter/backend/auth/swagger-ui.html#/ or http://localhost:8082/backend/convert/swagger-ui.html#/

### RabbitMQ.

#### Docker compose and local:
To enable rabbit, in application.yml set rabbitmq.enable=true in both convert and history services.

To run rabbit run `cd rabbitmq && docker-compose up --build` You can configure init.sh or envs there.

If rabbit is enabled the convert dto data will be transfered via queue, otherwise via rest template.

Management console will be enabled at localhost:15672


### You have the next options to start it:

## Locally
Start locally from source code. Before the start you have to create the following databases: auth_db, convert_db, history_db. These are default names.
After that at root dir `mvn clean install` to install the dependencies and `cd ${service-name} && java -jar ${service-name}.jar` to start a service. When you start all the services proceed to [cbr-currency-converter-ui-local](https://github.com/CPTMUTCHELL/cbr-currency-converter-ui/blob/master/README.md#local) 


## Docker-compose
Start with docker-compose. It'll do everything instead of you, just `docker-compose up --build` from root dir and procced to [cbr-currency-converter-ui-compose](https://github.com/CPTMUTCHELL/cbr-currency-converter-ui/blob/master/README.md#compose)






