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
        BUILD_VERSION = "${BRANCH_NAME}"-"${GIT_COMMIT[0..7]}"-sh(script: "echo `date +%F.%H%M%S`", returnStdout: true).trim()

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

    }
}

