# cbr-currency-converter

This is microservice implementation of currency converter. 
It has the following components:

[entity](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/entity)
contains exception handler, entities for the services and jwt authorization and authentication filters.

[auth-service](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/auth-service) /api/auth
for providing sign up and sign in, token refresh and administration features.

[convert-service](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/convert-service) /api/convert
for converting currencies based on the rates of "Bank of Russia"

[history-service](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/history-service) /api/history
for showing the histrory of all convertations.

Each service has it's own database which are pre-filled by flyway after the first start.
The pre-created user is admin:admin. 

You have the next options to start it:

1) Start locally from source code. Before the start you have to create the following databases: auth_db, convert_db, history_db. These are default names.
After that at root dir `mvn clean install` to install the dependencies and `cd ${service-name} && java -jar ${service-name}-0.0.1-SNAPSHOT.jar` to start a service. When you start all the services proceed to [cbr-currency-converter-ui-local](https://github.com/CPTMUTCHELL/cbr-currency-converter-ui/blob/master/README.md#local)

2) Start with docker-compose. It'll do everything instead of you, just `docker-compose up --build` from root dir and procced to [cbr-currency-converter-ui-compose](https://github.com/CPTMUTCHELL/cbr-currency-converter-ui/blob/master/README.md#compose)
