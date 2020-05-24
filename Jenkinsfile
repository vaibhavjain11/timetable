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
                    docker.build registry + ":$BUILD_NUMBER"
                }
            }
        }
    }
}