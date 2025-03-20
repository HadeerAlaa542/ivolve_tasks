
# Lab 27: Deployment vs. StatefulSet

## Objective
This lab demonstrates the differences between Kubernetes **Deployment** and **StatefulSet** by deploying a **MySQL database** using a StatefulSet with 3 replicas. Additionally, we create a **headless service** to allow stable DNS resolution among MySQL pods.

## Prerequisites
- Kubernetes cluster (Minikube or any other Kubernetes setup)
- kubectl installed and configured

---

## Step 1: Understanding Deployment vs. StatefulSet

### Deployment (Used for Stateless Applications)
- Pods are **randomly assigned names**.
- Scaling up/down does not guarantee the same pod name.
- No **stable** persistent storage per pod.
- Ideal for **web applications** or **microservices**.

### StatefulSet (Used for Stateful Applications)
- Each pod gets a **stable, unique name** (e.g., `mysql-0`, `mysql-1`, `mysql-2`).
- Uses **Persistent Volume Claims (PVCs)**, so data remains even if the pod restarts.
- Pods are created **sequentially** (`mysql-0` first, then `mysql-1`, etc.).
- Ideal for **databases** like MySQL, PostgreSQL, and Redis.

---

## Step 2: Setting Up Kubernetes Environment

Ensure Kubernetes is running:

```sh
kubectl get nodes
```

If not running, start Minikube:

```sh
minikube start
```

---

## Step 3: Creating the MySQL StatefulSet

### Create a YAML file for the StatefulSet (`mysql-statefulset.yaml`):

```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: mysql
spec:
  serviceName: "mysql"
  replicas: 3
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - name: mysql
        image: mysql:5.7
        ports:
        - containerPort: 3306
        env:
        - name: MYSQL_ROOT_PASSWORD
          value: "mypassword"
        volumeMounts:
        - name: mysql-storage
          mountPath: /var/lib/mysql
  volumeClaimTemplates:
  - metadata:
      name: mysql-storage
    spec:
      accessModes: [ "ReadWriteOnce" ]
      resources:
        requests:
          storage: 1Gi
```

Apply the StatefulSet:

```sh
kubectl apply -f mysql-statefulset.yaml
```
![image](https://github.com/user-attachments/assets/0064d074-0b36-4051-be43-ad9e8ea39e7c)

Check the pods:

```sh
kubectl get pods
```

Output:

<img width="269" alt="image" src="https://github.com/user-attachments/assets/77e8b433-11bf-480b-93a4-c7d6bc65c9d3" />

---

## Step 4: Creating the MySQL Headless Service

### Create a YAML file for the service (`mysql-service.yaml`):

```yaml
apiVersion: v1
kind: Service
metadata:
  name: mysql
spec:
  ports:
  - port: 3306
    targetPort: 3306
  selector:
    app: mysql
  clusterIP: None  # Headless service for StatefulSet
```

Apply the service:

```sh
kubectl apply -f mysql-service.yaml
```
![image](https://github.com/user-attachments/assets/efc3b59d-dd1f-4eb0-a614-7b398fa16ec5)

Check the service:

```sh
kubectl get svc
```

Output:

![image](https://github.com/user-attachments/assets/273ca524-d9f6-4319-8f64-f89309fbafca)

Since `ClusterIP` is **None**, this confirms a headless service.

---

## Step 5: Verifying Pod Storage

Check persistent storage:

```sh
kubectl get pvc
```

Output:

![image](https://github.com/user-attachments/assets/94e5f4d4-a2f1-44f8-afe0-b2ef6d370991)

Each **MySQL pod has its own persistent storage**.

---

## Step 6: Test the database is running.
Run:

```bash
kubectl exec -it mysql-0 -- mysql -u root -p
```
When prompted, enter your MySQL **root password** (this is defined in your StatefulSet YAML).

If successful, you will see:
```
Welcome to the MySQL monitor...
mysql>
```

Now, try running:

```sql
SHOW DATABASES;
```

Output:
![image](https://github.com/user-attachments/assets/f7e99118-f584-4d06-950c-d851f4a83fc6)

## Step 7: Cleaning Up

To delete the StatefulSet and service:

```sh
kubectl delete -f mysql-statefulset.yaml
kubectl delete -f mysql-service.yaml
```

To delete all Persistent Volume Claims (PVCs):

```sh
kubectl delete pvc --all
```

---

## Conclusion
**You have successfully deployed MySQL using StatefulSet!**


