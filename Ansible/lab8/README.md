# Lab 8: Setting Up MySQL with Ansible Vault

## Objective
This lab demonstrates how to use Ansible to install MySQL, create a database (`ivolvedatabase`), and set up a user with privileges using Ansible Vault to securely store sensitive information such as database credentials.

---
## **Prerequisites**
- A control node with Ansible installed.
- A target node (`node02`) running Ubuntu.
- SSH access to the target node.

---
## **Step 1: Configure the Ansible Inventory**
Edit the `inventory` file to define the target node:

```ini
node02 ansible_host=44.202.60.121 ansible_user=ubuntu
```

---
## **Step 2: Create the Ansible Playbook**
Create `mysql_playbook.yml` with the following content:

```yaml
- name: Setup MySQL with Ansible Vault
  hosts: node02
  become: yes
  vars_files:
    - vault.yml

  tasks:
    - name: Install MySQL Server
      ansible.builtin.apt:
        name:
          - mysql-server
          - python3-pymysql  # Required Python library for MySQL
        state: present
        update_cache: yes

    - name: Ensure MySQL service is running
      ansible.builtin.service:
        name: mysql
        state: started
        enabled: yes

    - name: Create MySQL database
      community.mysql.mysql_db:
        name: ivolvedatabase
        state: present
        login_unix_socket: /var/run/mysqld/mysqld.sock

    - name: Create MySQL user with privileges
      community.mysql.mysql_user:
        name: "{{ db_user }}"
        password: "{{ db_password }}"
        priv: "ivolvedatabase.*:ALL"
        host: "%"
        state: present
        login_unix_socket: /var/run/mysqld/mysqld.sock
```

---
## **Step 3: Create an Ansible Vault File for Credentials**
Create `vault.yml` and define the database credentials:

```yaml
db_user: ivolve_user
db_password: 123456
```

Then, encrypt the file using:

```bash
ansible-vault encrypt vault.yml
```
Enter and confirm a Vault password when prompted.

---
## **Step 4: Verify Playbook Syntax**
Check the playbook syntax with:

```bash
ansible-playbook -i inventory mysql_playbook.yml --syntax-check --ask-vault-pass
```

---
## **Step 5: Run the Ansible Playbook**
Execute the playbook using:

```bash
ansible-playbook -i inventory mysql_playbook.yml --private-key ~/.ssh/node01key.pem --ask-vault-pass
```

Enter the Vault password when prompted.

---
## **Step 6: Verify the Database Creation**
### **Option 1: Using Ansible**
Run:
```bash
ansible node02 -i inventory -m shell -a "mysql -u root -e 'SHOW DATABASES;'" --become
```

### **Option 2: Manually via SSH**
1. SSH into the target node:
   ```bash
   ssh node02
   ```
2. Access MySQL:
   ```bash
   sudo mysql
   ```
3. Check the database list:
   ```sql
   SHOW DATABASES;
   ```
Expected output:
```
+--------------------+
| Database          |
+--------------------+
| information_schema |
| ivolvedatabase    |
| mysql             |
| performance_schema|
| sys              |
+--------------------+
```
output:
![image](https://github.com/user-attachments/assets/42a611d5-c265-42e3-bca3-c9072a9c08f3)


