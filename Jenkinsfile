// sudo chmod 777 /var/run/docker.sock
pipeline{
    agent any


     options {
            buildDiscarder logRotator(numToKeepStr: '3')
            durabilityHint('PERFORMANCE_OPTIMIZED')

     }
    environment {
        dockerImage = ''
        registryCredential = 'dockerhub_id'
        me = 'cptmutchell'
        auth = 'auth-service'
        entity = "entity"
        history =  'history-service'
        convert = 'convert-service'
        set='helm upgrade --install cbr ./cbr-converter-chart --set '
    }
    parameters {
    booleanParam(name: 'AUTH_IMAGE', defaultValue: false, description: 'Build auth service docker image')
    booleanParam(name: 'CONVERT_IMAGE', defaultValue: false, description: 'Build convert service docker image')
    booleanParam(name: 'HISTORY_IMAGE', defaultValue: false, description: 'Build history service docker image')
    }
    stages{

         stage("Traefik") {
            steps {
             script {

                    sh """
                    cd k8s/helm
                    helm repo add traefik https://helm.traefik.io/traefik
                    helm repo update
                    helm upgrade traefik traefik/traefik --install --create-namespace -n traefik --values traefik.yml

                    """
             }
            }
         }
        stage("Custom postgres") {
           steps {
                sh """
                docker build -t ${me}/postgres-multidb:v1 postgres/
                """
                withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                   sh """
                    docker push ${me}/postgres-multidb:v1
                    docker rmi ${me}/postgres-multidb:v1
                   """
                }
           }
        }
        stage("Deploy migrations") {
            parallel{
                stage("Auth db migration"){
                    when{
                        anyOf{
                            changeset "${auth}/src/resources/userdb/*.sql"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${auth}/src/resources/userdb/.*"') == 0
                            }
                            expression {return params.AUTH_IMAGE}
                        }
                    }
                    steps {

                         sh """
                            docker build -t ${me}/flyway-userdb:v1 -f ${auth}/flyway/Dockerfile .
                            """

                         withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                           sh """
                            docker push ${me}/flyway-userdb:v1
                            docker rmi ${me}/flyway-userdb:v1
                            kubectl delete job flyway-userdb-job --ignore-not-found=true
                           """

                         }

                    }
                }
                stage("Convert db migration"){
                    when{
                        anyOf{
                            changeset "${convert}/src/resources/converterdb/*.sql"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${convert}/src/resources/converterdb/.*"') == 0
                            }
                            expression {return params.CONVERT_IMAGE}

                        }
                    }
                    steps {
                          sh """
                          docker build -t ${me}/flyway-converterdb:v1 -f ${convert}/flyway/Dockerfile .
                          """
                          withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                             sh """
                              docker push ${me}/flyway-converterdb:v1
                              docker rmi ${me}/flyway-converterdb:v1
                              kubectl delete job flyway-converterdb-job --ignore-not-found=true

                             """
                          }

                    }
                }
                stage("History db migration"){
                    when{
                        anyOf{
                            changeset "${history}/src/resources/historydb/*.sql"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${history}/src/resources/historydb/.*"') == 0
                            }
                            expression {return params.HISTORY_IMAGE}
                        }
                    }
                    steps {
                          sh """
                          docker build -t ${me}/flyway-historydb:v1 -f ${history}/flyway/Dockerfile .
                          """
                          withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                             sh """
                              docker push ${me}/flyway-historydb:v1
                              docker rmi ${me}/flyway-historydb:v1
                              kubectl delete job flyway-historydb-job --ignore-not-found=true
                             """
                          }

                    }
                }
            }
        }

        stage ("Build images") {
            stages{
                stage("Auth image build"){
                    when{
                        anyOf{
                            changeset "${auth}/**"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${auth}/.*"') == 0
                            }
                            expression {return params.AUTH_IMAGE}
                        }
                    }
                    steps {
                          sh """
                         DOCKER_BUILDKIT=1 docker build -t ${me}/${auth}:v${BUILD_NUMBER} -f ${auth}/Dockerfile .
                          """
                          withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                             sh """
                              docker push ${me}/${auth}:v${BUILD_NUMBER}
                              docker rmi ${me}/${auth}:v${BUILD_NUMBER}
                             """
                          }
                          script{
                            set = set + 'auth.tag=v${BUILD_NUMBER},'
                          }
                    }
                }
                stage("Convert image build"){
                    when{
                        anyOf{
                            changeset "${convert}/**"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${convert}/.*"') == 0
                            }
                            expression {return params.CONVERT_IMAGE}

                        }
                    }
                    steps {
                          sh """
                         DOCKER_BUILDKIT=1 docker build -t ${me}/${convert}:v${BUILD_NUMBER} -f ${convert}/Dockerfile .
                          """
                          withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                             sh """
                              docker push ${me}/${convert}:v${BUILD_NUMBER}
                              docker rmi ${me}/${convert}:v${BUILD_NUMBER}
                             """
                          }
                          script{
                            set = set + 'convert.tag=v${BUILD_NUMBER},'
                          }
                    }
                }
                stage("History image build"){
                    when{
                        anyOf{
                            changeset "${history}/**"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${history}/.*"') == 0
                            }
                            expression {return params.HISTORY_IMAGE}
                        }
                    }
                    steps {
                          sh """
                          DOCKER_BUILDKIT=1 docker build -t ${me}/${history}:v${BUILD_NUMBER} -f ${history}/Dockerfile .
                          """
                          withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                             sh """
                              docker push ${me}/${history}:v${BUILD_NUMBER}
                              docker rmi ${me}/${history}:v${BUILD_NUMBER}
                             """
                          }
                          script{
                              set = set + 'history.tag=v${BUILD_NUMBER},'

                          }
                    }
                }
            }
        }
         stage("Helm") {
            steps {
                script {
                    if (set =~ '--set [A-Za-z]') {
                        set = set.substring(0, set.length() - 1);
                        sh"""
                        bash -c ${set}
                        """

                    }
                }
            }
         }
    }
}

