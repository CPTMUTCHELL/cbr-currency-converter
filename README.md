# cbr-currency-converter

This is microservice implementation of currency converter. 
It has the following components:

[entity](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/entity)
contains exception handler, entities for the services and jwt authorization and authentication filters.

[auth-service](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/auth-service) /api/auth
for providing sign up and sign in, token refresh and administration features.

[convert-service](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/convert-service) /api/convert
for converting currencies based on the rates of ["Bank of Russia"](http://www.cbr.ru/scripts/XML_daily.asp)

[history-service](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/history-service) /api/history
for showing the history of all converts.

Each service has it's own database which are pre-filled by flyway after the first start.
The pre-created user is admin:admin. 

Swagger is available for each service at ${domain}/backend/${service}/swagger-ui.html for example: http://myconverter/backend/auth/swagger-ui.html#/ or http://localhost:8082/backend/convert/swagger-ui.html#/

You have the next options to start it:

1) Start locally from source code. Before the start you have to create the following databases: auth_db, convert_db, history_db. These are default names.
After that at root dir `mvn clean install` to install the dependencies and `cd ${service-name} && java -jar ${service-name}-0.0.1-SNAPSHOT.jar` to start a service. When you start all the services proceed to [cbr-currency-converter-ui-local](https://github.com/CPTMUTCHELL/cbr-currency-converter-ui/blob/master/README.md#local) 

2) Start with docker-compose. It'll do everything instead of you, just `docker-compose up --build` from root dir and procced to [cbr-currency-converter-ui-compose](https://github.com/CPTMUTCHELL/cbr-currency-converter-ui/blob/master/README.md#compose)

3) Local k8s cluster. 
At first, you need to install a k8s cluster, kubectl and helm on your machine. For example, I use k3s:
   `curl -sfL https://get.k3s.io | sh -`
   
`cd k8s/helm`

At first, we need to install [metallb](https://metallb.universe.tf/) to get our cluster a Loadbalancer.

Add to helm: `helm repo add metallb https://metallb.github.io/metallb` and install `helm install -n metallb-system metallb metallb/metallb -f metallb-values.yml`

**Note**: in `metallb-values.yml` you can specify the address range. Those addresses will be applied as external ip to LoadBalancer and Ingress kinds. 

I use traefik as IngressController so we need to install it to the cluster.
These commands will add helm chart to our helm:
`helm repo add traefik https://helm.traefik.io/traefik
helm repo update`

To install `helm upgrade traefik traefik/traefik --install --create-namespace --namespace traefik --values traefik.yml`

Run `kubectl get svc -n traefik`, you'll see external-ip set by metallb. Copy the address and paste to /etc/hosts along with desired domain name. For example:
10.128.54.230 myconverter. If traefik didn't receive an address, fix it with google. :)

If you want to enable traefik dashboard, run `kubectl apply -f traefik-dashboard.yaml`.

**Note**: in `traefik-dashboard.yaml` you can specify hostname and login:password in Secret. To generate your own login and password run `htpasswd -nb login password | base64` and replace mine in Secret. 
Dashboard will be available at http://myconverter/dashboard/ when the ingress starts.

After that, you can finally install cbr: `helm upgrade --install --create-namespace -n cbr cbr ./cbr-converter-chart`

**Note**: pay attention at [Ingress](https://github.com/CPTMUTCHELL/cbr-currency-converter/blob/k8s/k8s/helm/cbr-converter-chart/templates/ing.yml#L9)

To check whether everything works proceed to [swagger](http://myconverter/backend/auth/swagger-ui.html#/)

If the backend works, proceed to [ui deployment](https://github.com/CPTMUTCHELL/cbr-currency-converter-ui#local-k8s)


