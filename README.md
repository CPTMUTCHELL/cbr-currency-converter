# cbr-currency-converter

This is microservice implementation of currency converter. 
It has the following components:

[entity](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/entity)
contains exception handler, entities for the services and jwt authorization and authentication filters.

[auth-service](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/auth-service) /backend/auth
for providing sign up and sign in, token refresh and administration features.

[convert-service](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/convert-service) /backend/convert
for converting currencies based on the rates of ["Bank of Russia"](http://www.cbr.ru/scripts/XML_daily.asp)

[history-service](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/history-service) /backend/history
for showing the history of all converts.

Each service has it's own database which are pre-filled by flyway after the first start.
The pre-created user is admin:admin. 

Swagger is available for each service at ${domain}/backend/${service}/swagger-ui.html for example: http://myconverter/backend/auth/swagger-ui.html#/ or http://localhost:8082/backend/convert/swagger-ui.html#/

You have the next options to start it:

## Locally
Start locally from source code. Before the start you have to create the following databases: auth_db, convert_db, history_db. These are default names.
After that at root dir `mvn clean install` to install the dependencies and `cd ${service-name} && java -jar ${service-name}-0.0.1-SNAPSHOT.jar` to start a service. When you start all the services proceed to [cbr-currency-converter-ui-local](https://github.com/CPTMUTCHELL/cbr-currency-converter-ui/blob/master/README.md#local) 

## Docker-compose
Start with docker-compose. It'll do everything instead of you, just `docker-compose up --build` from root dir and procced to [cbr-currency-converter-ui-compose](https://github.com/CPTMUTCHELL/cbr-currency-converter-ui/blob/master/README.md#compose)


## Local k8s cluster. 
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

## Cloud provider
I decided not to use a provided cluster, but build it on a bare-metal.
My recommendations are 4GB RAM and 1 or 2 CPU for the cluster and 2GB RAM and 1 CPU for jenkins

### Ansible
For quick infrastructure build I created ansible roles. Tested on ubuntu 20.04 LTS. You have to specify your own VM's public IPs in [hosts](https://github.com/CPTMUTCHELL/cbr-currency-converter/blob/k8s/ansible/hosts) and send a public ssh key to each VM.
Of course, ansible won't work if it's not installed on your host machine, so first attempts will be unsuccessful, just read the errors and install dependencies. :)


#### k8s_setup role
Is used to install helm and k3s. Openssl crt is generated to create a user to use it in a kubeconfig for jenkins. It is needed to restrict jenkins in a certain kubernetes namespace due to security. The generated kubeconfig goes to **jenkins_setup** role

At [vars.yml](https://github.com/CPTMUTCHELL/cbr-currency-converter/blob/k8s/ansible/roles/k8s_setup/vars/main.yml)
you can specify the variables, like home dir or namespace. 

#### manifest_handler role

At manifest_handler role we apply the namespace for our application, role and roleBinding for jenkins's separate namespace. Also we install metallb (address is fetched from inventory).

To install traefik without tls you have to change the variable at [vars.yml](https://github.com/CPTMUTCHELL/cbr-currency-converter/blob/k8s/ansible/roles/manifest_handler/vars/main.yml#L5)

To install traefik with tls you have to register a domain name ( mine costs 2$/year ) and bind it to your VM's public ip. Traefik will fetch let's encrypt cert. To propagate it to your services take a look at (IngressRoute)[https://github.com/CPTMUTCHELL/cbr-currency-converter/blob/k8s/ansible/roles/manifest_handler/templates/ingRoute.yml.j2]

**Note**: I had an issue with getting cert sometimes because of two domain names, you'd better scale down frontend when traefik fetches certs.
Link: (https://github.com/traefik/traefik/issues/3414)

#### jenkins_setup role

At jenkins_setup we install jenkins on a separate VM in docker. Take a look at [files](https://github.com/CPTMUTCHELL/cbr-currency-converter/tree/k8s/ansible/roles/jenkins_setup/files). You can configure your own variables and pipelines. All envs are retrieved from host's envs. To create a webhook, generate github token, encrypt it `ansible-vault encrypt_string [OPTIONS]` with password and paste to vars. Don't forget to write your own repos in a [task](https://github.com/CPTMUTCHELL/cbr-currency-converter/blob/k8s/ansible/roles/jenkins_setup/tasks/github-webhook.yml#L19)

Run playbooks in the next order:

`ansible-playbook k3s-helm-playbook.yml -i hosts`
`ansible-playbook jenkins-playbook.yml -i hosts --ask-vault-pass <your vault pass>`


To check if everything works you can open traefik dashboard. For example (http://cbr.cptmutchell.xyz/dashboard/). Also check if jenkins is running.

When running the app don't forget to change [ingress host[(https://github.com/CPTMUTCHELL/cbr-currency-converter/blob/k8s/k8s/helm/cbr-converter-chart/templates/ing.yml#L9)

The app is delivered to the cluster by Jenkins, try to run the pipeline. The first start by hand, inside jenkins, the next by push events.

I recommend to monitor your cluster with [Lens](https://k8slens.dev/) . Don't forget to copy cluster's kubeconfig to your host to be able to connect to it with Lens.
