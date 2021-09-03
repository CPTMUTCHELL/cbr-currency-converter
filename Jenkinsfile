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
    booleanParam(name: 'KIBANA_TAG', defaultValue: false, description: 'input tag for ansible command.')
    booleanParam(name: 'FLUENT_TAG', defaultValue: false, description: 'input tag for ansible command.')
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

