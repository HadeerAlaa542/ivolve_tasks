# Lab 28: Storage Configuration

## Objective
This lab demonstrates how storage works in Kubernetes by deploying an NGINX pod, verifying ephemeral storage loss, and implementing persistent storage using a Persistent Volume Claim (PVC).

## Steps

### 1. Deploy NGINX with 1 Replica
Create an NGINX deployment with a single replica.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  replicas: 1
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
```sh
kubectl apply -f nginx-deployment.yaml
```
![image](https://github.com/user-attachments/assets/45651bfc-94c3-4c2f-aca2-1241fd920b35)

### 2. Create a File in the Pod
Exec into the running NGINX pod and create a file:
```sh
kubectl exec -it <nginx-pod-name> -- /bin/sh
```
Inside the pod, run:
```sh
echo "hello iVolve" > /usr/share/nginx/html/hello.txt
```
![image](https://github.com/user-attachments/assets/b90aaf85-9b9d-4a4c-b045-f7cee97d23d3)

![image](https://github.com/user-attachments/assets/05060ca0-7094-4531-8e39-9cfe36dc9588)

### 3. Verify the File
Exit the pod and run:
```sh
kubectl port-forward <nginx-pod-name> 9090:80 &
curl localhost:9090/hello.txt
```
![image](https://github.com/user-attachments/assets/2efa01e6-f8b2-41f0-8df1-5d474e44e4b1)

![image](https://github.com/user-attachments/assets/8832db4a-b269-4630-ac3c-bcde30a71d32)


### 4. Delete the Pod
```sh
kubectl delete pod <nginx-pod-name>
```
Wait for a new pod to be created and verify the file is missing:
```sh
kubectl exec -it <new-nginx-pod-name> -- ls /usr/share/nginx/html/
```
![image](https://github.com/user-attachments/assets/8342ca69-def9-4119-af09-2bb237d18cd8)

![image](https://github.com/user-attachments/assets/fd5bc3f1-cd88-4d02-906d-ff40fec36652)

### 5. Create a Persistent Volume Claim (PVC)
Create a PVC YAML file:
```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: nginx-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

Apply the PVC:
```sh
kubectl apply -f pvc.yaml
```

![image](https://github.com/user-attachments/assets/6cbe06ec-d4aa-4e7a-a593-3961e6b0051a)

### 6. Modify the Deployment to Use PVC
Modify the spec section inside containers:
```yaml
    spec:
      containers:
      - name: nginx
        image: nginx
        ports:
        - containerPort: 80
        volumeMounts:
        - mountPath: /usr/share/nginx/html
          name: nginx-storage
      volumes:
      - name: nginx-storage
        persistentVolumeClaim:
          claimName: nginx-pvc

```
Apply the changes:
```sh
kubectl apply -f nginx-deployment.yaml
```
![image](https://github.com/user-attachments/assets/a22248a3-df7e-439a-84a2-1ce2e768a075)

### 7. Verify File Persistence
Repeat Step 2 to create the file, then delete the pod. Check if the file persists in the new pod.

![image](https://github.com/user-attachments/assets/00793bde-18ad-4307-9419-a94809257220)

![image](https://github.com/user-attachments/assets/157ae760-3247-4ce5-b465-66836ac24e15)

![image](https://github.com/user-attachments/assets/ab2253bc-6abc-4084-8f42-97763b9afc96)


### 8. Comparison of PV, PVC, and StorageClass
- **Persistent Volume (PV)**: A cluster-wide storage resource provisioned by admins.
- **Persistent Volume Claim (PVC)**: A request for storage by users that binds to a PV.
- **StorageClass**: Defines storage types and provisions PVs dynamically.

## Conclusion
This lab demonstrates how Kubernetes handles ephemeral and persistent storage, highlighting the importance of using PVCs for data persistence.

