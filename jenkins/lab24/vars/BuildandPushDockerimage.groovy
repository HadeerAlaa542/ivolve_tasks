#!usr/bin/env groovy
def call(String dockerHubCredentialsID, String repoName, String imageName) {
	withCredentials([usernamePassword(credentialsId: "${dockerHubCredentialsID}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
		sh "docker login -u ${USERNAME} -p ${PASSWORD}"
        	sh "docker build -t ${repoName}/${imageName}:${BUILD_NUMBER} ."
        	sh "docker push ${repoName}/${imageName}:${BUILD_NUMBER}"	 
        	sh "docker rmi ${repoName}/${imageName}:${BUILD_NUMBER}"
  }
}
