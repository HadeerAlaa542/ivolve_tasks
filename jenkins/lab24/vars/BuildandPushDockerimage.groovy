#!/usr/bin/env groovy
def call(String dockerHubCredentialsID, String repoName, String imageName) {
    withCredentials([usernamePassword(credentialsId: "${dockerHubCredentialsID}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        sh "docker login -u ${USERNAME} -p ${PASSWORD}"
        sh "docker build -t ${repoName}/${imageName}:${env.BRANCH_NAME} ."  // Use branch name for consistency
        sh "docker push ${repoName}/${imageName}:${env.BRANCH_NAME}"	 
        sh "docker rmi ${repoName}/${imageName}:${env.BRANCH_NAME}"
    }
}
