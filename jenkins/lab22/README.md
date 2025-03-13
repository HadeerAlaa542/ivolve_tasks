# Lab 22: Jenkins Pipeline for Application Deployment

## Overview
This lab automates the deployment of an application using Jenkins, Docker, and Kubernetes. The pipeline follows a series of steps to build a Docker image, push it to Docker Hub, update the Kubernetes deployment file, and deploy the application to a Kubernetes cluster running on an EC2 instance.

## Environment Setup
### 1. Infrastructure Setup
- **Jenkins Installed on Local Host**: Jenkins was installed and configured on the local machine.
- **Kubernetes Installed on EC2 Instance**: Kubernetes was set up on an AWS EC2 instance.
- **SSH Access to EC2**: The EC2 instance was accessed from the local host using SSH.

### 2. Jenkins Configuration
- **Created Credentials in Jenkins**:
  - Docker Hub credentials (ID: `dockerhub`) for authentication.
  - SSH private key credentials (ID: `k8s-ssh`) to connect to the remote EC2 instance.
     
  ![image](https://github.com/user-attachments/assets/830e1a91-2dac-4ce2-b83f-461300f9ca55)
  
- **Installed Required Plugins**:
  - Docker pipeline 
  - kubernetes CLI plugin
    
  ![image](https://github.com/user-attachments/assets/8693c0ea-076a-43f3-a283-fdded8872bcd)
  ![image](https://github.com/user-attachments/assets/8c2ee27b-aa5a-4b5c-9ba9-0006e0266f6e)

## Pipeline Workflow

### 1. Clone the GitHub Repository
The pipeline pulls the source code from GitHub.

### 2. Build and Push Docker Image
- Builds a Docker image using the `Dockerfile` from the repository.
- Logs into Docker Hub and pushes the image.
  
<img width="960" alt="image" src="https://github.com/user-attachments/assets/fb1230f7-73b1-49d8-8500-2837e006a5f4" />

- Removes the local Docker image to save space.

### 3. Update Kubernetes Deployment File
- create the deployment.yaml file 
- The `deployment.yaml` file was placed in the Jenkins workspace.
![image](https://github.com/user-attachments/assets/9c660f5a-18d2-47a5-8d5b-43869ade8553)

### 4. Deploy to Kubernetes
- Copies the updated `deployment.yaml` file to the remote Kubernetes server.
- Runs `kubectl apply -f deployment.yaml` on the EC2 instance to deploy the application.

## Jenkins Pipeline Code
```groovy
pipeline {
    agent any
    environment {
        DEPLOYMENT_FILE = "deployment.yaml"
        K8S_REMOTE_SERVER = "13.216.214.97"
        K8S_USER = "ec2-user"
        DOCKER_IMAGE = "hadeeralaa542/lab22-image:v1.0"
    }

    stages {
        stage('Clone Repository') {
            steps {
                git url: 'https://github.com/IbrahimAdell/App1.git', branch: 'main'
            }
        }

        stage('Docker Image Build & Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh """
                    docker build . -t ${USERNAME}/lab22-image:v1.0
                    docker login -u ${USERNAME} -p ${PASSWORD}
                    docker push ${USERNAME}/lab22-image:v1.0
                    docker rmi ${USERNAME}/lab22-image:v1.0
                    """
                }
            }
        }
        
        stage('Update Deployment File') {
            steps {
                sh """
                sed -i 's|image: .*|image: ${DOCKER_IMAGE}|' ${DEPLOYMENT_FILE}
                """
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'k8s-ssh', keyFileVariable: 'SSH_KEY')]) {
                    sh """
                    chmod 600 $SSH_KEY
                    scp -o StrictHostKeyChecking=no -i $SSH_KEY ${DEPLOYMENT_FILE} ${K8S_USER}@${K8S_REMOTE_SERVER}:/home/${K8S_USER}/
                    ssh -o StrictHostKeyChecking=no -i $SSH_KEY ${K8S_USER}@${K8S_REMOTE_SERVER} "kubectl apply -f /home/${K8S_USER}/${DEPLOYMENT_FILE}"
                    """
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed!'
        }
        success {
            echo 'Deployment was successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}
```

## Verification
- The deployment was verified on the EC2 instance.
![image](https://github.com/user-attachments/assets/235e58ea-8839-433e-a61c-4dd0f2fb31fb)
![image](https://github.com/user-attachments/assets/2058fe1c-f7a6-47cc-ae8c-096091e81fee)
![image](https://github.com/user-attachments/assets/4b747f72-b0aa-48f5-98c3-20657a5cff93)

- The Jenkins pipeline executed successfully, deploying the application to Kubernetes.
![image](https://github.com/user-attachments/assets/cb618a3f-2d2a-48ad-bc72-e94511b5ae77)

## Output 
![image](https://github.com/user-attachments/assets/4b6f569f-b9b5-482f-b47f-fc6dd1496485)

## Conclusion
This lab demonstrated how to automate application deployment using a Jenkins pipeline integrated with Docker and Kubernetes. The pipeline ensures efficient, repeatable deployments with post-action notifications for success or failure.



