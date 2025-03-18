#!/usr/bin/env groovy
def call(String k8sCredentialsID, String repoName, String imageName, String deploymentFile, String namespace) {
    def tag = env.BRANCH_NAME  // Ensure it matches what was pushed
    withCredentials([file(credentialsId: "${k8sCredentialsID}", variable: 'KUBECONFIG_FILE')]) {
        sh """
            sed -i 's|REPLACE_IMAGE|${repoName}/${imageName}:${tag}|g' ${deploymentFile}
            sed -i 's|REPLACE_NAMESPACE|${namespace}|g' ${deploymentFile}
            export KUBECONFIG=\$KUBECONFIG_FILE
            kubectl apply -f ${deploymentFile} -n ${namespace}
        """
    }
}
