pipeline {
    agent any

    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t care-app .'
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    sh 'docker stop care-app || true'
                    sh 'docker rm care-app || true'
                    sh 'docker run -d -p 8081:8080 --name care-app care-app'
                }
            }
        }
    }
}