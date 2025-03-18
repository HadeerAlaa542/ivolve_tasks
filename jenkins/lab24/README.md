# Lab 24: MultiBranch Pipeline Project

## Overview
This lab focuses on setting up a MultiBranch Pipeline in Jenkins to automate deployment to a Kubernetes cluster based on GitHub branches. The pipeline will deploy applications to different Kubernetes namespaces corresponding to the branches. A Jenkins slave will be configured on an AWS EC2 instance to execute the pipeline, and a Shared Library will be used for better maintainability.

## Prerequisites
- A Kubernetes cluster running on an AWS EC2 instance
- A working Jenkins installation on the master.
- A Jenkins slave set up on an AWS EC2 instance
- A GitHub repository for storing the Dockerfile and pipeline code
- Docker installed on the Jenkins slave for building images
- Kubernetes CLI (`kubectl`) installed
- Jenkins Shared Library setup

## Steps

### 1. Clone the Repository
```sh
 git clone https://github.com/IbrahimAdell/App3.git
```

### 2. Push the Dockerfile to Your Repository
Create a new GitHub repository and push the Dockerfile:
```sh
 cd App3
 rm -rf app3/.git  # Removes the Git history of the cloned repo
 git add app3
 git commit -m "Added app3 without submodule"
 git push origin main
```

### 3. Create Branches on your Repository
```sh
git checkout -b stag
git push -u origin stag
git checkout -b dev
git push -u origin dev
```

### 4. Create Kubernetes Namespaces on the kube ec2
```sh
kubectl create namespace main
kubectl create namespace stag
kubectl create namespace dev
```
### 5. Configure credentials for DockerHub , jenkins slave , k8s cluster.

![image](https://github.com/user-attachments/assets/28c5ee67-0406-4b82-991f-d44d2729d2ed)

### 6. Create a MultiBranch Pipeline in Jenkins
- Navigate to Jenkins → New Item → MultiBranch Pipeline.
- Configure the repository URL to your GitHub repo.
- Define a `Jenkinsfile` to automate deployment.
  
  ![image](https://github.com/user-attachments/assets/57d170f1-debe-4ad1-839e-ce77887f6253)
  ![image](https://github.com/user-attachments/assets/dbd49915-4ca0-431f-8e67-2a67cca36899)

### 7. Create a Jenkins Slave on AWS EC2
- Launch an EC2 instance and configure it as a Jenkins agent.
- Install open jdk , Docker and `kubectl` on the instance.
- Connect the agent to Jenkins.
  
  ![image](https://github.com/user-attachments/assets/79089fc7-45ff-44e6-ae20-86539b975748)

### 8. Use a Shared Library
- Define a Shared Library for common deployment logic.
- Integrate the library into the `Jenkinsfile`.

  ![image](https://github.com/user-attachments/assets/7cfbe871-4c9f-41df-ba96-eda5bc17afea)

### 9. Run the Pipeline

![image](https://github.com/user-attachments/assets/731de297-601f-4f93-9fa0-efcfe57a54ac)
![image](https://github.com/user-attachments/assets/fb51c1db-3a34-4943-be7f-b2966aac5946)

### 10. Verification 
1- check the image on DockerHub

![image](https://github.com/user-attachments/assets/8d49e019-25ae-4bf7-8e4b-ad3cc5bf1ebd)
![image](https://github.com/user-attachments/assets/2e896268-1e15-45f5-a255-ea0ecda8ece0)

2-Confirm that the application is deployed to Kubernetes

![image](https://github.com/user-attachments/assets/8ae0f0a3-f520-44c3-b9a2-43fb6d54d45b)
![image](https://github.com/user-attachments/assets/f5e58608-ac0b-4842-a866-b169c35df2c1)

## Conclusion
This lab demonstrates how to set up a Jenkins MultiBranch Pipeline to automate Kubernetes deployments using GitHub branches. A Jenkins slave running on AWS EC2 is used to offload the build process, and a Shared Library enhances pipeline reusability.


