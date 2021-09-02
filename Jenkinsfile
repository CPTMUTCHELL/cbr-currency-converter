pipeline{
    options {
        buildDiscarder logRotator(numToKeepStr: '3')
    }
    agent any
    tools {
        maven 'mvn-3.8.1'
        jdk 'jdk15'
        // available jdk8, jdk15
    }
    environment {
        auth = 'auth-service'
        entity = "entity"
        history =  'history-service'
        convert = 'convert-service'
    }
    stages{
        stage('Build application'){
            steps {
                sh '''
                    mvn clean package
                '''
            }
            post{
                success{
                    archiveArtifacts artifacts: '**/*.jar'
                }
            }

        }

    }
}