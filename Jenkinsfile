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
    booleanParam(name: 'AUTH_IMAGE', defaultValue: false, description: 'Build auth service docker image')
    booleanParam(name: 'CONVERT_IMAGE', defaultValue: false, description: 'Build convert service docker image')
    booleanParam(name: 'HISTORY_IMAGE', defaultValue: false, description: 'Build history service docker image')
    }
    stages{
//         stage('Build application'){
//             steps {
//                 sh '''
//                     mvn clean package
//                 '''
//             }
//             post{
//                 success{
//                     archiveArtifacts artifacts: "${auth}/target/*.jar, ${entity}/target/*.jar, ${history}/target/*.jar, ${convert}/target/*.jar"
//                 }
//             }
//
//         }
        stage ("Deploy branches") {
        stages{
            stage("Auth service"){
            when{
                anyOf{
                    expression {return false }
                    expression {return params.AUTH_IMAGE}

                }
            }
            steps {
                    sh '''
                        echo ${AUTH_IMAGE}
                        echo AUTH
                    '''
            }
            }
            stage("Convert service"){
            when{
                anyOf{
                    expression {changeset "${convert}/**" }
                    expression {return params.CONVERT_IMAGE}

                }
            }
            steps {
                    sh '''
                        echo ${CONVERT_IMAGE}

                    '''
            }
            }
        }
        }
    }
}

