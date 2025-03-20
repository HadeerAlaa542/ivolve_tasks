
# Lab26: Updating Applications and Rolling Back Changes

## Overview
This lab demonstrates how to deploy an NGINX application in Kubernetes, update it to Apache, and roll back changes if needed.

## Prerequisites
- Minikube installed and running
- kubectl installed and configured
  
---

## Step 1: Start Minikube
Ensure Minikube is running:
```bash
minikube start
```

Verify Minikube is ready:
```bash
kubectl get nodes
```

---

## Step 2: Deploy NGINX
Create a file `nginx-deployment.yaml` with the following content:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:latest
        ports:
        - containerPort: 80
```
Apply the deployment:
```bash
kubectl apply -f nginx-deployment.yaml
```
Check the running pods:
```bash
kubectl get pods
```
<img width="410" alt="image" src="https://github.com/user-attachments/assets/edf1afb3-9c2e-46f5-80fd-8910ee094403" />

---

## Step 3: Expose NGINX as a Service
Create a file `nginx-service.yaml`:
```yaml
apiVersion: v1
kind: Service
metadata:
  name: nginx-service
spec:
  selector:
    app: nginx
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
      nodePort: 30008
  type: NodePort
```
Apply the service:
```bash
kubectl apply -f nginx-service.yaml
```
Get the Minikube IP:
```bash
minikube ip
```
Access NGINX in the browser:
```
http://<minikube-ip>:30008
```
![image](https://github.com/user-attachments/assets/1f23a869-1983-4aa1-aeec-7548448ae1ad)

---

## Step 4: Edit the Deployment 
Modify the existing deployment to use **Apache (`httpd`) instead of NGINX**.
```bash
vim deployment nginx-deployment
```

Change the Image:
Find this section:
```yaml
containers:
- name: nginx
  image: nginx:latest
```
Change it to:
```yaml
containers:
- name: apache
  image: httpd:latest
```
- Save and exit the editor.
  
---

Apply the update:
```bash
kubectl apply -f nginx-deployment.yaml
```
Check the rollout status:
```bash
kubectl rollout status deployment/nginx-deployment
```
<img width="409" alt="image" src="https://github.com/user-attachments/assets/296871fb-f7e8-4847-ac61-db87e03db307" />

Refresh the browser to check if Apache is running.

![image](https://github.com/user-attachments/assets/f87655dd-8923-4845-93ba-c4a4612ce20d)

---

## Step 5: Roll Back to NGINX
If you need to roll back to the previous version:
```bash
kubectl rollout undo deployment/nginx-deployment
```
Check the status:
```bash
kubectl rollout status deployment/nginx-deployment
```
<img width="409" alt="image" src="https://github.com/user-attachments/assets/63ce5f94-2908-4eb0-bf58-d5bdf5a2a04c" />

Refresh the browser to confirm NGINX is restored.

![image](https://github.com/user-attachments/assets/3d4f5a52-e837-4e04-a8c3-f3acee54cf71)

---


## Step 6: Cleanup
To delete all resources:
```bash
kubectl delete deployment nginx-deployment
kubectl delete service nginx-service
kubectl delete pods --all
```
To reset Minikube:
```bash
minikube delete
minikube start
```

---

## Conclousion 
This lab demonstrates Kubernetes application updates and rollbacks effectively. 

