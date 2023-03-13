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
    }

    stages {
        stage("migrations") {
            steps {
                withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                    sh """
                       bash ./docker.sh flyway-authdb v1 ${auth}/flyway
                       bash ./docker.sh flyway-convertdb v1 ${convert}/flyway
                       bash ./docker.sh flyway-historydb v1 ${history}/flyway

                       """
                }
            }
        }

        stage("app") {
            steps {
                withDockerRegistry(credentialsId: registryCredential, url: 'https://index.docker.io/v1/') {
                    sh """
                             bash ./docker.sh ${auth} v${BUILD_NUMBER}
                             bash ./docker.sh ${convert} v${BUILD_NUMBER}
                             bash ./docker.sh ${history} v${BUILD_NUMBER}
                             """
                }
            }
        }
    }
}


