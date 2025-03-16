# Lab 23: Jenkins Slave & Shared Libraries

## Overview
In this lab, we will set up a Jenkins slave to execute a CI/CD pipeline and use a shared library to modularize tasks across multiple pipelines. The pipeline will include the following stages:

1. **BuildApp**
2. **BuildImage**
3. **PushImage**
4. **RemoveImageLocally**
5. **DeployOnK8s**

## Prerequisites
- A running Jenkins master instance on the local host.
- A jenkins slave.
- Kubernetes cluster.

  ![image](https://github.com/user-attachments/assets/951ce1cd-c881-4282-b057-7f671f30c2c8)

- Docker installed on the slave node.
- GitHub repository containing the Dockerfile: `https://github.com/IbrahimAdell/App3.git`

## Steps

### Step 1: Configure Jenkins Slave
1. Launch a new node in Jenkins:
   - Navigate to **Manage Jenkins** > **Manage Nodes and Clouds**.
   - Click **New Node**.
   - Provide a name and select **Permanent Agent**.
   - Configure the **Remote Root Directory**, labels, and the launch method.
   - Ensure the slave has Docker and Kubernetes CLI installed.
     
     ![image](https://github.com/user-attachments/assets/b7b0fec6-43b3-4565-9ee7-8b8d2bb0fc75)
     ![image](https://github.com/user-attachments/assets/61a780a8-2b4b-4fe3-b150-2121f67b6d7e)

2. Start the slave and verify it's connected under **Manage Nodes**.

   <img width="928" alt="image" src="https://github.com/user-attachments/assets/9ea8bc6f-d20b-4cdb-ac9c-22e80f2498d8" />

### Step 2: Create a Jenkins Shared Library
1. Navigate to Jenkins and go to **Manage Jenkins** > **Configure System**.
2. Under **Global Pipeline Libraries**, add a new library with:
   - **Name**: `jenkins-shared-lib`
   - **Source Code Management**: Git
   - **Repository URL**: Your shared library repository URL.
   - Default version: `main` or a specific branch.
     
     <img width="412" alt="image" src="https://github.com/user-attachments/assets/70d21afc-ce11-4498-8ba7-c5a89d754120" />

### Step 3: Define Shared Library Functions 
- Create a new repository for the shared library and define Groovy functions for pipeline tasks:
- This is the linke of the Repo: https://github.com/HadeerAlaa542/jenkins-shared-library

### Step 4: Create the Jenkins Pipeline
```groovy
@Library('jenkins-shared-library')_
pipeline {
    agent {
        label 'slave1' 
    }
    environment {
        GITHUB_REPO_URL = 'https://github.com/IbrahimAdell/App3.git'
        GITHUB_REPO_BRANCH = 'main'
        DOCKER_REGISTRY = "hadeeralaa542"
        DOCKER_IMAGE = "app3"
        DOCKERHUB_CRED_ID = "dockerhub"
        K8S_CRED_ID = 'kube'   
        DEPLOYMENT = 'deployment.yaml' 
    }

    stages {
        stage('Clone Repository') {
            steps {
                git url: GITHUB_REPO_URL, branch: GITHUB_REPO_BRANCH    
            }
        }

        stage('Manage Docker Image') {
            steps {
                script {
                    BuildandPushDockerimage("${DOCKERHUB_CRED_ID}", "${DOCKER_REGISTRY}", "${DOCKER_IMAGE}")
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    DeployOnKubernetes("${K8S_CRED_ID}", "${DOCKER_REGISTRY}", "${DOCKER_IMAGE}", "${DEPLOYMENT}")
                }
            }
        }
        
    }

    post {
        success {
            echo "Deployment successful!"
        }
        failure {
            echo "Build or deployment failed."
        }
    }
}
```

### Step 5: Run and Verify the Pipeline
- Save the pipeline script in Jenkins.
- Run the pipeline and verify that each stage executes successfully on the Jenkins slave.

  ![image](https://github.com/user-attachments/assets/55f4973c-e15b-404c-adb9-27678a362c22)

- check the imade on DockerHub
  
  <img width="725" alt="image" src="https://github.com/user-attachments/assets/ebebff19-ceef-402c-8d17-8cd7c9de783d" />
  
- Confirm that the application is deployed to Kubernetes using:
  ```sh
  kubectl get pods
  kubectl get services
  ```

## Conclusion
This lab demonstrates how to set up a Jenkins slave, create a shared library, and use it in different pipelines for modular and reusable automation.

