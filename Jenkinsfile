pipeline{
    environment {
        registry = "vaibhavjain11/timetable"
        registryCredential = "dockerhub"
    }
    agent any
    stages {
        stage('Checkout code') {
            steps {
                git branch: 'master',
                    credentialsId: '2e2df098-0749-4c28-a2db-4c5361d86d47',
                    url: 'https://github.com/vaibhavjain11/timetable'
            }
        }
        stage('Gradle Build') {
            steps {
                   sh './gradlew clean build'
            }
        }
        stage('Building image') {
            steps {
                script {
                    dockerImage = docker.build registry + ":$BUILD_NUMBER"
                }
            }
        }
        stage('Publish Image') {
            steps {
                script {
                    docker.withRegistry('', registryCredential) {
                             dockerImage.push()
                   }
                }
            }
        }
        stage('Remove unused Image') {
            steps {
                     sh "docker rmi $registry:$BUILD_NUMBER"
            }
        }

        stage('Deploy Image on container') {
            steps {
                script {
                    sh "./change.sh $BUILD_NUMBER"
                    sh "kubectl apply -f ."
                }
            }
        }
    }
}