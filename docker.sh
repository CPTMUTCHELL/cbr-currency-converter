#!/bin/bash

if [[ "$1" =~ "flyway" ]]; then
  kubectl delete job "$1""-job" --ignore-not-found=true
  DOCKER_BUILDKIT=1 docker build -t cptmutchell/"$1":"$2" -f "$3"/Dockerfile .

else
DOCKER_BUILDKIT=1 docker build -t cptmutchell/"$1":"$2" -f "$1"/Dockerfile .
fi
docker push cptmutchell/"$1":"$2"
docker rmi cptmutchell/"$1":"$2"
