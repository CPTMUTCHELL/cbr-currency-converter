#!/bin/sh
gcloud auth activate-service-account jenkins-sa@aerobic-datum-327116.iam.gserviceaccount.com --key-file /usr/jenkins-sa.json --project=aerobic-datum-327116 && \
    gcloud config set project aerobic-datum-327116 && \
    gcloud container clusters get-credentials cluster-1 --zone europe-central2-a --project aerobic-datum-327116
ansible-galaxy collection install kubernetes.core
/sbin/tini -- /usr/local/bin/jenkins.sh