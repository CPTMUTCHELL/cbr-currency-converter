FROM jenkins/jenkins:lts
USER root
RUN echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] http://packages.cloud.google.com/apt cloud-sdk main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list && \
    curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key --keyring /usr/share/keyrings/cloud.google.gpg  add - && \
    apt-get update -y && apt-get install google-cloud-sdk -y
RUN apt-get update && apt-get install kubectl && apt-get install -y maven && apt-get clean
COPY jenkins-sa.json /usr/jenkins-sa.json
COPY start.sh .
RUN chmod 604 /usr/jenkins-sa.json && chmod 605 start.sh
RUN apt-get -y update && \
   apt-get -y install \
   apt-transport-https \
   ca-certificates \
   curl \
   gnupg \
   lsb-release \
   software-properties-common

RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/debian \
   $(lsb_release -cs) \
   stable"


RUN apt-get -y update && \
 apt-get -y install \
   docker-ce \
   docker-ce-cli \
   containerd.io
# 998 is docker's gid on the machine
RUN groupmod -g 998 docker && usermod -aG docker jenkins
RUN curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 && \
    chmod 700 get_helm.sh && ./get_helm.sh
RUN apt-get install -y python3.9 && apt install -y ansible && apt install -y nano && \
    apt -y install python3-pip && apt install libpq-dev python3-dev && pip3 install kubernetes && \
    ansible-galaxy collection install kubernetes.core

USER jenkins


ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/plugins.txt
COPY jenkins-casc.yaml /usr/local/jenkins-casc.yaml
ENV CASC_JENKINS_CONFIG /usr/local/jenkins-casc.yaml
#gcloud commands are inside start.sh because kube/config could be erased by VOLUME

ENTRYPOINT ["./start.sh"]