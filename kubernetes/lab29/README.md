# Lab 29: Security and RBAC

## Objective
This lab focuses on Kubernetes Role-Based Access Control (RBAC). By the end of this lab, you will:
- Create a Service Account with a token.
- Define a Role named `pod-reader` that allows read-only access to Pods in the namespace.
- Bind the `pod-reader` Role to the Service Account.
- Compare Service Account, Role & Role Binding vs. Cluster Role & Cluster Role Binding.

---

## **Step 1: Create a Service Account**
Run the following command to create a Service Account named `pod-reader-sa`:
```sh
kubectl create serviceaccount pod-reader-sa
```
Verify the Service Account creation:
```sh
kubectl get serviceaccounts
```
Output:

<img width="349" alt="image" src="https://github.com/user-attachments/assets/6d54ed98-6b67-404f-8300-e253c260a2da" />

---

## **Step 2: Manually Create a Secret for the Service Account Token**
By default, Kubernetes 1.24+ does not automatically create Secrets for Service Accounts. We need to manually create one.

Create a new YAML file:
```sh
nano pod-reader-sa-secret.yaml
```
Add the following content:
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: pod-reader-sa-token
  annotations:
    kubernetes.io/service-account.name: "pod-reader-sa"
type: kubernetes.io/service-account-token
```
Save and exit (`CTRL + X`, then `Y`, then `ENTER`).

Apply the Secret:
```sh
kubectl apply -f pod-reader-sa-secret.yaml
```
Verify the Secret:
```sh
kubectl get secrets
```
Output:

<img width="357" alt="image" src="https://github.com/user-attachments/assets/59445f28-6ca0-40f4-b6b6-da617cef5481" />


Retrieve the token:
```sh
kubectl get secret pod-reader-sa-token -o jsonpath='{.data.token}' | base64 --decode
```
Use this token to authenticate with Kubernetes.

---

## **Step 3: Define a Role for Read-Only Pod Access**
Create a new YAML file:
```sh
nano pod-reader-role.yaml
```
Add the following content:
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: pod-reader
rules:
- apiGroups: [""]
  resources: ["pods"]
  verbs: ["get", "list"]
```
Save and exit (`CTRL + X`, then `Y`, then `ENTER`).

Apply the Role:
```sh
kubectl apply -f pod-reader-role.yaml
```

Verify the Role:
```sh
kubectl get roles
```
Output:

<img width="353" alt="image" src="https://github.com/user-attachments/assets/abb19174-b5aa-41de-9c02-5579ae75529d" />

---

## **Step 4: Bind the Role to the Service Account**
Create a new YAML file:
```sh
nano pod-reader-binding.yaml
```
Add the following content:
```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: pod-reader-binding
subjects:
- kind: ServiceAccount
  name: pod-reader-sa
  namespace: default
roleRef:
  kind: Role
  name: pod-reader
  apiGroup: rbac.authorization.k8s.io
```
Save and exit (`CTRL + X`, then `Y`, then `ENTER`).

Apply the RoleBinding:
```sh
kubectl apply -f pod-reader-binding.yaml
```

Verify the RoleBinding:
```sh
kubectl get rolebindings
```
Output:

<img width="334" alt="image" src="https://github.com/user-attachments/assets/f0554a70-826e-4e05-a412-0b08888a5ec1" />

---

## Verification

### 1. Verify Role Creation
```bash
kubectl describe role pod-reader
```
Output:

<img width="375" alt="image" src="https://github.com/user-attachments/assets/3cc5331b-5ed5-4ae1-9fde-375cad898a0d" />

### 2. Verify Role Binding

Check details:
```bash
kubectl describe rolebinding pod-reader-binding
```

Output:

<img width="452" alt="image" src="https://github.com/user-attachments/assets/07d3b66f-fccf-471a-91ca-a799fdcffd56" />


## Cleanup
To remove all created resources, run:
```bash
kubectl delete serviceaccount pod-reader-sa
kubectl delete role pod-reader
kubectl delete rolebinding pod-reader-binding
```
This ensures the cluster is clean after completing the lab.


## **Comparison: Role vs. ClusterRole**
| Feature          | Role                                      | ClusterRole                               |
|-----------------|-----------------------------------------|------------------------------------------|
| Scope          | Namespace-specific                      | Cluster-wide                             |
| Resources      | Can only grant permissions within a namespace | Can grant permissions across all namespaces |
| Use Case      | Restrict access within a namespace       | Grant global permissions                |

| Feature           | RoleBinding                             | ClusterRoleBinding                       |
|------------------|----------------------------------------|-----------------------------------------|
| Scope           | Binds a Role to a Service Account within a namespace | Binds a ClusterRole to a Service Account across the cluster |
| Use Case       | Assigns permissions in a namespace       | Assigns cluster-wide permissions       |

---

## **Conclusion**
You have successfully:
- Created a Service Account.
- Defined a Role with read-only access to Pods.
- Bound the Role to the Service Account.
- Manually created a Secret for the Service Account Token.
- Compared Role, RoleBinding, ClusterRole, and ClusterRoleBinding.

This setup ensures restricted access, following Kubernetes best practices for security and RBAC.

---


