pipeline{
    agent any
    tools {
        maven 'mvn-3.8.1'
        jdk 'jdk-15'
        // available jdk8, jdk15
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
                    archiveArtifacts artifacts: '/*.jar'
                }
            }

        }

    }
}