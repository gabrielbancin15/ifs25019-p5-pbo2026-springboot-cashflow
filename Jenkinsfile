pipeline {
  agent any
  options { timestamps(); disableConcurrentBuilds() }
  parameters {
    booleanParam(name: 'PUBLISH_IMAGE', defaultValue: false, description: 'Push image ke Docker Registry')
    booleanParam(name: 'DEPLOY_RENDER', defaultValue: false, description: 'Trigger deployment Render melalui Deploy Hook')
  }
  environment {
    IMAGE_NAME = 'ifs25019-p5-cashflow'
  }
  stages {
    stage('Build & Test') {
      steps { sh 'mvn -B clean verify' }
    }
    stage('Security Scan') {
      steps { sh 'trivy fs --exit-code 1 --severity HIGH,CRITICAL --ignore-unfixed .' }
    }
    stage('Build Docker Image') {
      steps { sh 'docker build -t $IMAGE_NAME:$BUILD_NUMBER .' }
    }
    stage('Publish Image') {
      when { expression { params.PUBLISH_IMAGE } }
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
          sh 'docker tag $IMAGE_NAME:$BUILD_NUMBER $DOCKER_USER/$IMAGE_NAME:latest'
          sh 'docker push $DOCKER_USER/$IMAGE_NAME:latest'
        }
      }
    }
    stage('Deploy Render') {
      when { expression { params.DEPLOY_RENDER } }
      steps {
        withCredentials([string(credentialsId: 'render-deploy-hook', variable: 'RENDER_DEPLOY_HOOK')]) {
          sh 'curl --fail --silent --show-error -X POST "$RENDER_DEPLOY_HOOK"'
        }
      }
    }
  }
  post {
    always {
      junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
      archiveArtifacts allowEmptyArchive: true, artifacts: 'target/site/jacoco/**'
    }
  }
}
