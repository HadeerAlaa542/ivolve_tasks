# Lab 20: Jenkins Installation

## Overview
This lab provides step-by-step instructions to install and configure Jenkins as a Docker container.

## Prerequisites
- Docker installed on your system
- Basic understanding of Docker commands

## Step 1: Pull the Jenkins Docker Image
First, pull the official Jenkins image with the latest Long-Term Support (LTS) version:
```sh
docker pull jenkins/jenkins:lts-jdk17
```

## Step 2: Run Jenkins as a Container
Run the following command to start Jenkins as a container:
```sh
docker run -d --name jenkins \
  -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  jenkins/jenkins:lts-jdk17
```
Explanation of options:
- `-d` → Runs the container in detached mode (background process).
- `--name jenkins` → Assigns a name to the container.
- `-p 8080:8080` → Maps Jenkins UI to port 8080.
- `-p 50000:50000` → Maps the port for Jenkins agent communication.
- `-v jenkins_home:/var/jenkins_home` → Mounts the Jenkins volume for persistent data.

## Step 3: Verify Jenkins Container
Check if the Jenkins container is running:
```sh
docker container ls
```
![image](https://github.com/user-attachments/assets/1e10a761-a6c4-459d-a0e7-0174ea73103a)

Expected output:
```
CONTAINER ID   IMAGE                        COMMAND                CREATED         STATUS         PORTS                              NAMES
xxxxxxxxxxx   jenkins/jenkins:lts-jdk17   "/usr/bin/tini -- /u…"  X minutes ago  Up X minutes  0.0.0.0:8080->8080/tcp, ...   jenkins
```

## Step 4: Access Jenkins
1. Open a browser and navigate to:
   ```
   http://localhost:8080
   ```
2. Retrieve the initial admin password:
   ```sh
   docker logs jenkins 
   ```
   In the logs, you will see a line similar to the following:   

   ```  
   Jenkins initial setup is required. An admin user has been created and a password generated.
   Please use the following password to proceed to installation:

   [password]
   ```
3. Copy the password and paste it into the Jenkins setup page.
4. Follow the on-screen instructions to complete the installation.

## Output

![image](https://github.com/user-attachments/assets/f17ebfb8-449b-410e-b939-2b50cc327051)



