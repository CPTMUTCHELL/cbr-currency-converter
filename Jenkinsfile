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
        parameters {

            booleanParam(‘b1’, false)
            booleanParam(‘b2’, false)
            booleanParam(‘b3’, false)
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
                    archiveArtifacts artifacts: "${auth}/target/*.jar, ${entity}/target/*.jar, ${history}/target/*.jar, ${convert}/target/*.jar"
                }
            }

        }
//         stage ("Deploy branches") {
//             when{
//                 anyof{
//                     changeset "${auth}/**"
//                 }
//             }
//             steps {
//                 script {
//               //do stuff
//                 }
//             }
//
//         }
    }
}

