// sudo chmod 777 /var/run/docker.sock
pipeline {
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
        history = 'history-service'
        convert = 'convert-service'
        set = 'helm upgrade --install cbr ./cbr-converter-chart --set '
        pg_user = credentials('pg_user')
        pg_pass = credentials('pg_pass')
    }
    parameters {
        booleanParam(name: 'AUTH_IMAGE', defaultValue: false, description: 'Build auth service docker image')
        booleanParam(name: 'CONVERT_IMAGE', defaultValue: false, description: 'Build convert service docker image')
        booleanParam(name: 'HISTORY_IMAGE', defaultValue: false, description: 'Build history service docker image')
//         booleanParam(name: 'ALL', defaultValue: false, description: 'Run all stages')
    }
    stages {
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

        stage("Cert-manager") {
            steps {
                script {
                    sh """
                    cd k8s/helm
                    helm repo add jetstack https://charts.jetstack.io
                    helm repo update
                    helm upgrade cert-manager jetstack/cert-manager --install -n cert-manager --create-namespace \
                     --version v1.6.1 \
                     --set installCRDs=true

                    """
                }
            }
        }

        stage("Create db") {
            steps {
                sh '''
                   kubectl delete secret postgres-secret --ignore-not-found
                   kubectl create secret generic postgres-secret --from-literal=POSTGRES_PASSWORD=${pg_pass} --from-literal=POSTGRES_USER=${pg_user}
                   '''
                withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                    sh """
                         kubectl delete job postgres-createdb-job --ignore-not-found=true
                        bash ./docker.sh postgres-createdb v1
                     """

                }

            }
        }
         stage("Deploy migrations") {
            parallel {
                stage("Auth db migration") {

                    steps {
                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """
                           bash ./docker.sh flyway-authdb v1 ${auth}/flyway

                           """
                        }

                    }
                }
                stage("Convert db migration") {

                    steps {
                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """
                             bash ./docker.sh flyway-convertdb v1 ${convert}/flyway
                            """

                        }

                    }
                }
                stage("History db migration") {

                    steps {
                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """
                               bash ./docker.sh flyway-historydb v1 ${history}/flyway

                               """
                        }
                    }
                }
            }
        }

        stage("Build images") {
            stages {
                stage("Auth image build") {
                    when {
                        anyOf {
                            changeset "${auth}/**"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${auth}/.*"') == 0
                            }
                            expression { return params.AUTH_IMAGE }
                        }
                    }
                    steps {

                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """
                             bash ./docker.sh ${auth} v${BUILD_NUMBER}
                             """
                        }
                        script {
                            set = set + 'auth.tag=v${BUILD_NUMBER},'
                        }
                    }
                }
                stage("Convert image build") {
                    when {
                        anyOf {
                            changeset "${convert}/**"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${convert}/.*"') == 0
                            }
                            expression { return params.CONVERT_IMAGE }

                        }
                    }
                    steps {

                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """


                             bash ./docker.sh ${convert} v${BUILD_NUMBER}
                             """
                        }
                        script {
                            set = set + 'convert.tag=v${BUILD_NUMBER},'
                        }
                    }
                }
                stage("History image build") {
                    when {
                        anyOf {
                            changeset "${history}/**"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${history}/.*"') == 0
                            }
                            expression { return params.HISTORY_IMAGE }
                        }
                    }
                    steps {

                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """

                             bash ./docker.sh ${history} v${BUILD_NUMBER}
                             """
                        }
                        script {
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
                        sh """
                        cd k8s/helm
                        eval ${set}
                        """
                    } else {
                        sh """
                            cd k8s/helm
                            helm upgrade --install  cbr ./cbr-converter-chart
                        """
                    }
//                     sh"""
//                     ansible-playbook create_db.yml --extra-vars "new_db=convert_db"
//
//                      ansible-playbook create_db.yml --extra-vars "new_db=auth_db"
//                     ansible-playbook create_db.yml --extra-vars "new_db=history_db"
//                     """
                }
            }
        }
    }
}

