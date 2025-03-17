#!/usr/bin/env groovy
def call(String k8sCredentialsID, String repoName, String imageName, String deploymentFile, String namespace) {
    def tag = env.BRANCH_NAME   // Use branch name as tag
    // Update the deployment YAML file with the correct image
    sh "sed -i 's|image:.*|image: ${repoName}/${imageName}:${tag}|g' ${deploymentFile}"    withCredentials([file(credentialsId: "${k8sCredentialsID}", variable: 'KUBECONFIG_FILE')]) {
    sh "export KUBECONFIG=${KUBECONFIG_FILE} && kubectl apply -f ${deploymentFile} -n ${namespace}"
    }
}

 
