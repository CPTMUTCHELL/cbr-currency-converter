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
        BUILD_VERSION = "${GIT_BRANCH.split("/")[1]}"+"-"+"${GIT_COMMIT[0..7]}"+"-"
        BUILD_VERSION = BUILD_VERSION+sh(script: "echo `date +%F.%H%M%S`", returnStdout: true).trim()

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
                
                    echo ${BUILD_VERSION}
                '''

            }
        }

    }
}

