#!/bin/bash
acc=cptmutchell
if [[ "$1" =~ "flyway" ]]; then
  kubectl delete job "$1""-job" --ignore-not-found=true
  DOCKER_BUILDKIT=1 docker build -t ${acc}/"$1":"$2" -f "$3"/Dockerfile .

else
DOCKER_BUILDKIT=1 docker build -t ${acc}/"$1":"$2" -f "$1"/Dockerfile .
fi
docker push ${acc}/"$1":"$2"
docker rmi ${acc}/"$1":"$2"
