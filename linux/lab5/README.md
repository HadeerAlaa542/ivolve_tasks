## **Lab 5 - SSH Key-Based Authentication & Configuration**

### **Objective**
This lab demonstrates how to configure SSH key-based authentication and set up an SSH config file to simplify remote access to an Ubuntu server.

---

### **Prerequisites**
- Two Ubuntu machines:  
  - **Control machine** (Local)  
  - **Managed node** (AWS EC2 instance or another Ubuntu server)
- SSH installed on both machines.
- A private key (`node01key.pem`) for the managed node, stored in `~/.ssh/node01key.pem` on the control machine.

---

### **Steps Performed**

#### **1. Set Hostname on Control Machine**
```bash
hostnamectl set-hostname control
```
This sets the hostname of the local machine to `control`.

---

#### **2. Configure SSH Key Permissions**
```bash
chmod 400 ~/.ssh/node01key.pem
```
This restricts the permissions of the private key to prevent unauthorized access. The key is used to authenticate with the managed node.

---

#### **3. Connect to the Managed Node Using SSH Key**
```bash
ssh -i ~/.ssh/node01key.pem ubuntu@<MANAGED_NODE_IP>
```
- The first-time connection prompts a key fingerprint verification.
- Typing `yes` adds the managed node to `known_hosts`.

---

#### **4. Create an SSH Config File for Shortcut Access**
Edit the SSH config file:
```bash
nano ~/.ssh/config
```
Add the following configuration:
```
Host node01
    HostName <MANAGED_NODE_IP>
    User ubuntu
    IdentityFile ~/.ssh/node01key.pem
    StrictHostKeyChecking no
```
This allows connecting to the managed node using just:
```bash
ssh node01
```

---

#### **5. Secure the SSH Config File**
```bash
chmod 600 ~/.ssh/config
```
This ensures only the user can read and write the file.

---

### **Outcome**
- The managed node is now accessible via `ssh node01`, simplifying the connection process.

