# Lab 7: Ansible Playbooks for Web Server Configuration

## Objective

The objective of this lab is to write an Ansible playbook to automate the configuration of a web server using NGINX on an Ubuntu machine.

## Prerequisites

- Ansible is installed on the control node.
- A managed node (Ubuntu) accessible via SSH.
- Ansible inventory file configured with the managed node.

## Steps

### 1. Verify SSH Connection

Ensure that the control node can connect to the managed node via SSH:

```bash
ssh node01
```

### 2. Create an Ansible Inventory

Create an inventory file and define the managed node:

```bash
vim inventory
```

Add the following content:

```
[nodes]
node01.example.com ansible_host=3.87.52.183 ansible_user=ubuntu
```

### 3. Write the Ansible Playbook

Create the `webserver.yml` playbook file:

```bash
vim webserver.yml
```

Add the following content:

```yaml
---
- name: Install and Configure NGINX on Ubuntu
  hosts: node01.example.com
  become: yes
  tasks:
    - name: Update package list
      apt:
        update_cache: yes

    - name: Install NGINX
      apt:
        name: nginx
        state: present

    - name: Start and enable NGINX service
      service:
        name: nginx
        state: started
        enabled: yes

    - name: Allow HTTP traffic on port 80
      ufw:
        rule: allow
        port: "80"
        proto: tcp

    - name: Deploy a custom index.html
      copy:
        content: "<h1>Welcome to the NGINX Server</h1>"
        dest: /var/www/html/index.html
        owner: www-data
        group: www-data
        mode: '0644'

    - name: Restart NGINX service
      service:
        name: nginx
        state: restarted

```

### 4. Run Syntax Check

Before executing the playbook, check for syntax errors:

```bash
ansible-playbook -i inventory webserver.yml --syntax-check
```

### 5. Execute the Playbook

Run the playbook to install and configure NGINX:

```bash
ansible-playbook -i inventory webserver.yml --private-key ~/.ssh/node01key.pem 
```

### 6. Verify NGINX Installation

Check if the NGINX service is running on the managed node:

```bash
systemctl status nginx
```

Alternatively, access the web server from a browser using the managed node's IP address: 

```bash
http://<managed_node_IP>
```

You should see a page displaying:

```
Welcome to the NGINX Server
```
