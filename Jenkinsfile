// sudo chmod 777 /var/run/docker.sock
pipeline {
    agent any
    options {
        buildDiscarder logRotator(numToKeepStr: '3')
        durabilityHint('PERFORMANCE_OPTIMIZED')

    }
    environment {
        registryCredential = 'dockerhub_id'
        auth = 'auth-service'
        history = 'history-service'
        convert = 'convert-service'
        BUILD_VERSION = "${GIT_BRANCH.split("/")[1]}"+"-"+"${GIT_COMMIT[0..6]}"+"-"
        BUILD_VERSION1 = sh(script: "echo \$(date +%Y%m%d%H%M%S)", returnStdout: true).trim()
    }
    parameters {
        booleanParam(name: 'BUILD_ALL', defaultValue: false, description: 'Build all services')
    }
    stages {

        stage("Deploy migrations") {
            parallel {
                stage("Auth db migration") {
                    when {changeset "${auth}/src/main/resources/authdb/**" }
                    steps {
                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """
                           bash ./docker.sh flyway-authdb v1 ${auth}/flyway
                           """
                        }

                    }
                }
                stage("Convert db migration") {
                    when {changeset "${convert}/src/main/resources/convertdb/**" }

                    steps {
                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """
                             bash ./docker.sh flyway-convertdb v1 ${convert}/flyway
                            """

                        }

                    }
                }
                stage("History db migration") {
                    when {changeset "${history}/src/main/resources/historydb/**" }
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
                            expression { return params.BUILD_ALL }
                        }
                    }
                    steps {

                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """
                             bash ./docker.sh ${auth} ${BUILD_VERSION}${BUILD_VERSION1}
                             """
                        }

                    }
                }
                stage("Convert image build") {
                    when {
                        anyOf {
                            changeset "${convert}/**"
                            changeset "pom.xml"
                            expression { return params.BUILD_ALL }

                        }
                    }
                    steps {

                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """
                             bash ./docker.sh ${convert} ${BUILD_VERSION}${BUILD_VERSION1}
                             """
                        }

                    }
                }
                stage("History image build") {
                    when {
                        anyOf {
                            changeset "${history}/**"
                            changeset "pom.xml"

                            expression { return params.BUILD_ALL }
                        }
                    }
                    steps {

                        withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                            sh """

                             bash ./docker.sh ${history} ${BUILD_VERSION}${BUILD_VERSION1}
                             """
                        }
                    }
                }
            }
        }
    }
}

