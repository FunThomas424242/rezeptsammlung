!#groovy

pipeline {
    agent {
        docker { image 'openjdk:13' }
    }
    stages {
        stage('Build') {
            steps {
                sh './mvnw clean site install'
            }
        }
    }
}
