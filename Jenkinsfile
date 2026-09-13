pipeline {
    agent any
    
    environment {
        WORKSPACE_DIR = "${WORKSPACE}"
        SELENIUM_GRID_ENV = "docker"
        DOCKER_NETWORK = "jenkins-selenium"
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo "Checking out code from GitHub..."
                checkout scm
            }
        }
        
        stage('Setup Docker Network & Selenium Grid') {
            steps {
                script {
                    echo "Creating Docker network for Selenium Grid..."
                    sh '''
                        docker network create ${DOCKER_NETWORK} || true
                    '''
                    
                    echo "Starting Selenium Grid with Docker Compose..."
                    sh '''
                        cd ${WORKSPACE_DIR}
                        docker-compose -p jenkins-selenium up -d
                        echo "Waiting for Selenium Grid to be ready..."
                        sleep 10
                        docker-compose -p jenkins-selenium logs
                    '''
                }
            }
        }
        
        stage('Build & Test') {
            steps {
                script {
                    echo "Running Maven build and tests..."
                    sh '''
                        cd ${WORKSPACE_DIR}
                        mvn clean install -Dselenium.grid.env=docker -Dtest.docker=true
                    '''
                }
            }
        }
        
        stage('Generate TestNG Report') {
            steps {
                echo "TestNG reports generated"
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'test-output',
                    reportFiles: 'emailable-report.html',
                    reportName: 'TestNG Report'
                ])
            }
        }
    }
    
    post {
        always {
            echo "Cleaning up Docker resources..."
            sh '''
                cd ${WORKSPACE_DIR}
                docker-compose -p jenkins-selenium down || true
            '''
        }
        
        success {
            echo "✅ Build and Tests Passed!"
        }
        
        failure {
            echo "❌ Build or Tests Failed!"
            script {
                sh '''
                    echo "Collecting Docker logs..."
                    docker-compose -p jenkins-selenium logs > docker-logs.txt || true
                    echo "Collecting test output..."
                    if [ -d "test-output" ]; then
                        tar -czf test-output-${BUILD_NUMBER}.tar.gz test-output/
                    fi
                '''
            }
        }
    }
}
