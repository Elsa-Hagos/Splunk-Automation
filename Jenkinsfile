pipeline {
  agent {
    kubernetes {
      cloud 'kubernetes_mdc1' // MDC-1 kubernetes cluster
      label 'k8-assurance-portal-agent' // all your pods will be named with this prefix,
      idleMinutes 5 // how long the pod will live after no jobs have run on it
      yamlFile '../k8-pod-agents_mdc1.yml' // for MDC-1
      defaultContainer 'jnlp'
    }
  }
  options {
    gitLabConnection('gitlab')
  }

  environment {
    ARTIFACT_NAME = readMavenPom().getArtifactId()
    VERSION = "${BUILD_NUMBER}"
    HARBOR_REGISTRY = 'mdc1-sfcr.safaricomet.net'
    NAME_SPACE = 'dev-tools'
    DOCKER_IMAGE = "${HARBOR_REGISTRY}/${NAME_SPACE}/${ARTIFACT_NAME}:${VERSION}"
    CREDENTIALS_ID = credentials('harbor_credential')
    MANIFEST_GIT_URL = 'https://gitlab.safaricomet.net/devops-intern/splunk-deployment/k8-splunk-manifest.git'
    MANIFEST_GIT_BRANCH = 'main'
    MANIFEST_FILE_PATH = 'k8-splunk-manifest/deployment.yaml'
  }
  stages {
    stage('Notify GitLab') {
      steps {
        echo 'Notifying GitLab'
        updateGitlabCommitStatus name: 'build', state: 'pending'
      }
    }
stage('SonarQube Analysis') {
  steps{
    container('maven'){
    withSonarQubeEnv('sonarqube') {
      sh "mvn clean verify sonar:sonar -Dsonar.projectKey=splunk-automation -Dsonar.projectName='splunk-automation'"
    }
    }
  }
      post {
        success {
            updateGitlabCommitStatus name: 'sonarqub code quality analysis', state: 'success'
        }
        failure {
          updateGitlabCommitStatus name: 'sonarqub code quality analysis', state: 'failed'
        }
      }
  }
    stage('build and push with jib') {
      steps {
        container('maven') {
          withCredentials([usernamePassword(credentialsId: 'harbor_credential', usernameVariable: 'Username', passwordVariable: 'Password')]) {
            sh '''
             mvn clean package -DskipTests jib:build \
             -Djib.to.registry=${HARBOR_REGISTRY_URL} \
             -Djib.to.auth.username=${Username} \
             -Djib.to.auth.password=${Password} \
             -Djib.to.image=${DOCKER_IMAGE} \
             -Djib.allowInsecureRegistries=true
             '''
          }
        }
      }
      post {
        success {
            updateGitlabCommitStatus name: 'build and push to registry', state: 'success'
        }
        failure {
          updateGitlabCommitStatus name: 'build and push to registry', state: 'failed'
        }
      }
    }

        stage('Update deployment manifest') {
      steps {
        script {
          sh 'rm -rf k8-splunk-manifest'
          sh 'git clone -b main https://dawit.yitagesu:aQhXC97KeDEYBqU4GE2h@gitlab.safaricomet.net/devops-intern/splunk-deployment/k8-splunk-manifest.git'

          sh "git config --global user.name 'dawit yitagesu' "
          sh "git config --global user.email 'dawit.yitagesu@safaricom.et'"

          dir('k8-splunk-manifest') {
            sh 'cat deployment.yaml'
            sh "sed -i 's#image:.*#image: ${DOCKER_IMAGE}#g' deployment.yaml"
            sh 'cat deployment.yaml'
            sh 'git stage ./deployment.yaml'
            sh "git commit -m 'Updated Docker image   [skip ci]'"
            sh 'git push origin main'
          }

        }
      }
      post {
        success {
            updateGitlabCommitStatus name: 'update deployment manifest yaml file', state: 'success'
        }
        failure {
          updateGitlabCommitStatus name: 'update deployment manifest yaml file', state: 'failed'
        }
      }
        }
  }
}

