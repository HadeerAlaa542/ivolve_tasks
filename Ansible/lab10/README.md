# Lab 10: Ansible Dynamic Inventory with AWS EC2

## Overview
This lab demonstrates how to set up an Ansible Dynamic Inventory for AWS EC2 instances and use an Ansible Galaxy role to install Apache on the instance.

## Prerequisites
- An AWS account
- Ansible installed on your local machine
- `boto3` and `botocore` Python packages installed
- Ansible Galaxy role for Apache
- SSH key pair for connecting to EC2

## Step 1: Create an IAM User
1. Log in to the AWS Management Console.
2. Navigate to **IAM (Identity and Access Management)**.
3. Click **Users** and then **Add users**.
4. Enter a **User name** (e.g., `ansible-user`).
5. Select **Access key - Programmatic access**.
6. Click **Next** and select **Attach existing policies directly**.
7. Attach the following policies:
   - `AmazonEC2FullAccess`
8. Click **Next**, review, and create the user.
9. Download the Access Key ID and Secret Access Key.
10. Configure the AWS CLI with:
    ```bash
    aws configure
    ```
    Enter the **Access Key ID**, **Secret Access Key**, region, and output format.

## Step 2: Install `boto3` and `botocore`
1. Install the required dependencies:
    ```bash
    sudo apt install python3-boto3
    ```
    
2. Verify the installation:
    ```bash
    python -c "import boto3; print('boto3 is installed successfully!')"
    ```

## Step 3: Launch an EC2 Instance
1. Open the AWS EC2 Dashboard.
2. Launch a new instance with the following:
   - **Ubuntu 24.04** as the AMI
   - Assign a security group allowing SSH (port 22) and HTTP (port 80)
   - Choose a key pair for SSH access
3. Copy the public IP address of the instance.


## Step 4: Configure `ansible.cfg`
1. Create or modify the `ansible.cfg` file in your working directory:
    ```ini
    [defaults]
    inventory = inventory_aws_ec2.yaml
    private_key_file = lab10-key.pem
    host_key_checking = False
    enable_plugins = aws_ec2
    remote_user = ubuntu
    ```
2. This configuration ensures Ansible uses the correct inventory file and SSH key.

## Step 5: Set Up Ansible Dynamic Inventory
1. Create an **inventory file** (`inventory_aws_ec2.yaml`):
    ```yaml
    plugin: amazon.aws.aws_ec2
    regions:
      - us-east-1  # Change based on your region
    ```
2. Run Ansible inventory to verify:
    ```bash
    ansible-inventory -i inventory_aws_ec2.yaml --graph
    ```
![image](https://github.com/user-attachments/assets/35688c28-676f-4630-829d-cd0194a0d872)

## Step 6: Install Apache Using Ansible Galaxy Role
1. Install the Apache role:
    ```bash
    ansible-galaxy install geerlingguy.apache
    ```
2. Create a **playbook** (`install_apache.yaml`):
    ```yaml
    - name: Install Apache using Ansible Galaxy Role
      hosts: all
      become: yes
      roles:
        - geerlingguy.apache
    ```
3. Run the playbook:
    ```bash
    ansible-playbook -i inventory_aws_ec2.yaml install_apache.yaml 
    ```

## Step 7: Verify Apache Installation
1. SSH into the instance:
    ```bash
    ssh -i lab10-key.pem ubuntu@<EC2_PUBLIC_IP>
    ```
2. Check if Apache is running:
    ```bash
    systemctl status apache2
    ```
  ![image](https://github.com/user-attachments/assets/1e3b933d-7cf9-4c1a-9c6a-f22a9e479757)
  
3. Open a web browser and visit `http://<EC2_PUBLIC_IP>` to confirm Apache is serving pages.
   
<img width="958" alt="image" src="https://github.com/user-attachments/assets/28d0096c-5f71-40ec-9f1f-564ca4c059b5" />

## Conclusion
This lab covered setting up an Ansible Dynamic Inventory with AWS EC2, creating an IAM user, installing Apache via Ansible Galaxy, and verifying the setup.

