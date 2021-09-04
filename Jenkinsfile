pipeline{
    options {
        buildDiscarder logRotator(numToKeepStr: '3')
    }
    agent any
    tools {
        maven 'mvn-3.8.1'
        jdk 'jdk15'
    }
    environment {
        dockerImage = ''
        registryCredential = 'dockerhub_id'
        me = 'cptmutchell'
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
        stage ("Build images") {
            stages{
                stage("Auth image build"){
                    when{
                        anyOf{
                            changeset "${auth}/**"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${auth}/.*"') == 0
                            }
                            expression {return params.AUTH_IMAGE}
                        }
                    }
                    steps {
                        dir('${auth}/'){
//                         withDockerRegistry(credentialsId: '', url: '')

                            script {

                                dockerImage = docker.build me + "/$auth" + ":$BUILD_NUMBER"
                                docker.withRegistry('',registryCredential){
                                    dockerImage.push()
                                }
                            }
                       }
                    }
                }
                stage("Convert image build"){
                    when{
                        anyOf{
                            changeset "${convert}/**"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${convert}/.*"') == 0
                            }
                            expression {return params.CONVERT_IMAGE}

                        }
                    }
                    steps {
                            sh '''
                                echo ${CONVERT_IMAGE}

                            '''
                    }
                }
                stage("History image build"){
                    when{
                        anyOf{
                            changeset "${history}/**"
                            expression {
                                sh(returnStatus: true, script: 'git diff  origin/k8s --name-only | grep --quiet "^${history}/.*"') == 0
                            }
                            expression {return params.HISTORY_IMAGE}

                        }
                    }
                    steps {
                            sh '''
                                e

                            '''
                    }
                }
            }
        }
    }
}

