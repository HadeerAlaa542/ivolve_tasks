#!/usr/bin/env groovy
def call(String k8sCredentialsID, String repoName, String imageName, String deploymentFile) {
    sh "sed -i 's|image:.*|image: ${repoName}/${imageName}:${BUILD_NUMBER}|g' ${deploymentFile}"
    withCredentials([file(credentialsId: "${k8sCredentialsID}", variable: 'KUBECONFIG_FILE')]) {
        sh "export KUBECONFIG=${KUBECONFIG_FILE} && kubectl apply -f ${deploymentFile}"
    }
}
