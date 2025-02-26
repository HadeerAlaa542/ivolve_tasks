# **Lab6: Ansible Installation Lab**

## **Objective**
Install and configure Ansible on a control node, create an inventory of managed hosts, and perform ad-hoc commands to verify functionality.

---

## **Steps**

### **1. Check SSH Connection Between Control and Managed Nodes**
Ensure that the control node can connect to the managed node via SSH.
```bash
ssh node01
```
If successful, you should see a welcome message from the managed node.

---

### **2. Install Ansible on the Control Node**
Update the package lists and install Ansible:
```bash
sudo apt update
sudo apt install ansible -y
```
Verify the installation:
```bash
ansible --version
```

---

### **3. Create the Ansible Inventory File**
Create an inventory file that defines the managed hosts:
```bash
vim inventory
```
Add the following content:
```
[nodes]
node01.example.com ansible_host=3.87.52.183 ansible_user=ubuntu
```
Save and exit (`ESC`, then `:wq`).

---

### **4. Verify Ansible Can See the Managed Host**
Check if Ansible recognizes the managed node:
```bash
ansible -i ~/inventory node01.example.com --list-hosts
```
Expected output:
```
hosts (1):
    node01.example.com
```

---

### **5. Run an Ad-Hoc Command to Test Connectivity**
Run a simple `ping` test:
```bash
ansible -i ~/inventory node01.example.com -m ping
```
Expected output:
```
node01.example.com | SUCCESS => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python3.12"
    },
    "changed": false,
    "ping": "pong"
}
```
