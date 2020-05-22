pipeline{
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
                if (isUnix()) {
                    sh './gradlew clean build'
                } else {
                    bat 'gradlew.bat clean build'
                }
            }
        }
    }
}