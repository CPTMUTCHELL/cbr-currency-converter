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
        ns = 'cbr'
        def BUILDVERSION = sh(script: "echo `date +%Y%m%d%H%M%S`", returnStdout: true).trim()

    }
    parameters {
        booleanParam(name: 'AUTH_IMAGE', defaultValue: false, description: 'Build auth service docker image')
        booleanParam(name: 'CONVERT_IMAGE', defaultValue: false, description: 'Build convert service docker image')
        booleanParam(name: 'HISTORY_IMAGE', defaultValue: false, description: 'Build history service docker image')
//         booleanParam(name: 'ALL', defaultValue: false, description: 'Run all stages')
    }
    stages {
        stage("Create db") {
            steps {
                sh '''
                   kubectl delete secret -n ${ns} postgres-secret --ignore-not-found
                   kubectl create secret -n ${ns} generic postgres-secret --from-literal=POSTGRES_PASSWORD=${pg_pass} --from-literal=POSTGRES_USER=${pg_user}
                   '''
                withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                    sh """
                         kubectl delete job -n ${ns} postgres-createdb-job --ignore-not-found=true
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
                             changeset "pom.xml"
                             expression { return params.AUTH_IMAGE }
                         }
                     }
                    steps {

                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """
                             bash ./docker.sh ${auth} v${BUILDVERSION}
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
                             changeset "pom.xml"
                             expression { return params.CONVERT_IMAGE }

                         }
                     }
                    steps {

                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """


                             bash ./docker.sh ${convert} v${BUILDVERSION}
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
                             changeset "pom.xml"

                             expression { return params.HISTORY_IMAGE }
                         }
                     }
                    steps {

                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """

                             bash ./docker.sh ${history} v${BUILDVERSION}
                             """
                        }
                        script {
                            set = set + 'history.tag=v${BUILD_NUMBER},'
                        }
                    }
                }
            }
        }



    }
}

