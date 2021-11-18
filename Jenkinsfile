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
                    helm repo add traefik https://containous.github.io/traefik-helm-chart
                    helm repo update
                    helm upgrade traefik traefik/traefik --install --create-namespace -n traefik --values traefik.yml

                    """
                 }
            }
         }
        stage("Custom postgres") {
           steps {
             withCredentials([string(credentialsId: 'pg_pass', variable: 'test')]) {
                   sh """
                   kubectl delete secret pgpass --ignore-not-found
                   kubectl create secret generic pgpass --from-literal PGPASSWORD=${test}
                   """
             }

                withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                    sh"""
                        bash ./docker.sh postgres v${BUILD_NUMBER}
                     """
                    script{
                         set = set + 'db.tag=v${BUILD_NUMBER},'
                    }
                }

           }
        }
        stage("Deploy migrations") {
            parallel{
                stage("Auth db migration"){
                    when{
                        anyOf{
                            changeset "${auth}/src/resources/userdb/.*"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${auth}/src/resources/userdb/.*"') == 0
                            }
                            expression {return params.AUTH_IMAGE}
                        }
                    }
                    steps {
                         withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                         sh"""
                           bash ./docker.sh flyway-userdb v1 ${auth}/flyway
                           """
                         }

                    }
                }
                stage("Convert db migration"){
                    when{
                        anyOf{
                            changeset "${convert}/src/resources/converterdb/.*"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${convert}/src/resources/converterdb/.*"') == 0
                            }
                            expression {return params.CONVERT_IMAGE}

                        }
                    }
                    steps {
                          withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                          sh"""
                             bash ./docker.sh flyway-converterdb v1 ${convert}/flyway
                            """
                          }

                    }
                }
                stage("History db migration"){
                    when{
                        anyOf{
                            changeset "${history}/src/resources/historydb/.*"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${history}/src/resources/historydb/.*"') == 0
                            }
                            expression {return params.HISTORY_IMAGE}
                        }
                    }
                    steps {
                          withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                                sh"""
                               bash ./docker.sh flyway-historydb v1 ${history}/flyway
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

                          withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                          sh"""
                             bash ./docker.sh ${auth} v${BUILD_NUMBER}
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

                          withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                            sh"""
                             bash ./docker.sh ${convert} v${BUILD_NUMBER}
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

                          withDockerRegistry(credentialsId: registryCredential, url:'https://index.docker.io/v1/'){
                             sh"""
                             bash ./docker.sh ${history} v${BUILD_NUMBER}
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
                        cd k8s/helm
                        eval ${set}
                        """
                    }
                    else {
                        sh"""
                            cd k8s/helm
                            helm upgrade --install cbr ./cbr-converter-chart
                        """
                    }
                }
            }
         }
    }
}

