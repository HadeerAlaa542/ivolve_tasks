Lab9: Ansible Roles for Application Deployment

## Objective
This project organizes Ansible playbooks using roles. It includes Ansible roles for installing Jenkins, Docker, and OpenShift CLI (`oc`) on a remote Ubuntu server.

## Prerequisites
- Ansible installed on the control node
- SSH access to the target machine (`node02`)
- Ubuntu-based target machine

## Steps to Set Up the Lab

### 1. Configure Ansible Inventory
Create an inventory file `inventory` with the target machine details:
```ini
node02 ansible_host=44.211.82.164 ansible_user=ubuntu
```

### 2. Define the Playbook
Create a playbook `site.yml` to execute the roles:
```yaml
---
- name: Ansible role for installing Jenkins, Docker, and OpenShift CLI
  hosts: node02
  become: yes
  roles:
    - docker
    - jenkins
    - openshift
```

### 3. Create Role Structure
Run the following commands to create the required roles:
```bash
mkdir roles
cd roles
ansible-galaxy init docker
ansible-galaxy init jenkins
ansible-galaxy init openshift
```

### 4. Configure Docker Role
Edit `roles/docker/tasks/main.yml`:
```yaml
---
- name: Install Required Packages
  apt:
    name:
      - apt-transport-https
      - ca-certificates
      - curl
      - gnupg
      - lsb-release
    state: present

- name: Add Docker GPG Key
  apt_key:
    url: https://download.docker.com/linux/ubuntu/gpg
    state: present

- name: Add Docker Repository
  apt_repository:
    repo: "deb [arch=amd64] https://download.docker.com/linux/ubuntu {{ ansible_distribution_release }} stable"
    state: present

- name: Install Docker
  apt:
    name: docker-ce
    state: present

- name: Start Docker Service
  systemd:
    name: docker
    state: started
    enabled: yes
```

### 5. Configure Jenkins Role
Edit `roles/jenkins/tasks/main.yml`:
```yaml
---
- name: Install required dependencies
  apt:
    name:
      - wget
      - gnupg
    state: present
    update_cache: yes

- name: Add Jenkins repository key
  get_url:
    url: 'https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key'
    dest: /usr/share/keyrings/jenkins-keyring.asc
    mode: '0644'

- name: Add Jenkins repository
  apt_repository:
    repo: "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/"
    state: present
    filename: jenkins

- name: Update APT cache
  apt:
    update_cache: yes

- name: Install Jenkins
  apt:
    name: jenkins
    state: present

- name: Install Java and FontConfig
  apt:
    name:
      - fontconfig
      - openjdk-17-jre
    state: present

- name: Ensure Jenkins service is enabled and started
  systemd:
    name: jenkins
    enabled: yes
    state: started
```

### 6. Configure OpenShift CLI Role
Edit `roles/openshift/tasks/main.yml`:
```yaml
---
- name: Download OpenShift CLI
  get_url:
    url: "https://mirror.openshift.com/pub/openshift-v4/clients/oc/latest/linux/oc.tar.gz"
    dest: "/tmp/oc.tar.gz"

- name: Extract OpenShift CLI
  unarchive:
    src: "/tmp/oc.tar.gz"
    dest: "/usr/local/bin/"
    remote_src: yes

- name: Verify OpenShift CLI Installation
  command: oc version
  register: oc_version

- name: Display OpenShift CLI Version
  debug:
    msg: "{{ oc_version.stdout }}"
```

### 7. Deploy the Roles Using Ansible
Run the following command to execute the playbook:
```bash
ansible-playbook -i inventory site.yml
```

### 8. Verify Installations
- **Jenkins:**
  ```bash
  systemctl status jenkins
  ```
  Access Jenkins at `http://<server-ip>:8080`

- **Docker:**
  ```bash
  docker --version
  ```

- **OpenShift CLI:**
  ```bash
  oc version
  ```
  output 
![image](https://github.com/user-attachments/assets/d6ac2ea0-7efb-4292-a97c-a2659d67bed1)
