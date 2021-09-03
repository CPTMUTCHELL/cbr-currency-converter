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
     booleanParam(name: 'RUN_TESTS', defaultValue: false, description: 'Do you want to run the build with tests?'),
     booleanParam(name: 'dddd', defaultValue: false, description: 'Do you want to rudild with tests?')
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

