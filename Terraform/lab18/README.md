# Lab 18: Terraform Variables and Loops

## Overview

This lab demonstrates the use of Terraform to deploy an AWS infrastructure with variables, loops, and remote provisioning. The architecture includes a VPC with two subnets (public and private), two EC2 instances (one running Nginx in the public subnet and another running Apache in the private subnet), an Internet Gateway (IGW), a NAT Gateway with an Elastic IP (EIP), and appropriate routing. The lab avoids code repetition by using loops, utilizes `variables.tf` and `terraform.tfvars` for configuration, installs Nginx via a remote provisioner, installs Apache via user data, and outputs the public and private IPs of the EC2 instances.

### Architecture Diagram

<img width="467" alt="image" src="https://github.com/user-attachments/assets/8ee11b5e-fd94-4723-a27e-e8afaa4acd6e" />

## Prerequisites

Before starting the lab, ensure you have the following:

1. **AWS Account:**
   - An active AWS account with permissions to create VPCs, subnets, EC2 instances, security groups, NAT Gateways, and route tables.
   - AWS CLI configured with credentials (run `aws configure`).

2. **Terraform:**
   - Terraform installed on your system (version 1.6.x or later recommended).
   - To install Terraform on Amazon Linux 2:
     ```bash
     sudo yum update -y
     sudo yum install wget unzip -y
     wget https://releases.hashicorp.com/terraform/1.6.6/terraform_1.6.6_linux_amd64.zip
     unzip terraform_1.6.6_linux_amd64.zip
     sudo mv terraform /usr/local/bin/
     terraform --version
     rm terraform_1.6.6_linux_amd64.zip
     ```

3. **SSH Key Pair:**
   - An SSH key pair named `lab-key` in the target AWS region (e.g., `us-east-1`).
   - Create the key pair using AWS CLI in Cloud9:
     ```bash
     aws ec2 create-key-pair --key-name lab18-key --query 'KeyMaterial' --output text > ~/.ssh/lab-key.pem
     chmod 400 ~/.ssh/lab18-key.pem
     ```
   - Alternatively, create it via the AWS Management Console under **EC2 > Key Pairs**.

4. **Environment:**
   - This lab assumes you’re working in an AWS Cloud9 environment running Amazon Linux 2, but it can be adapted for other environments.

5. **Valid AMI ID:**
   - An Amazon Linux 2 AMI ID for the target region .

## Files in This Repository

- `main.tf`: Main Terraform configuration file defining the infrastructure.
- `variables.tf`: Variable definitions for the project.
- `terraform.tfvars`: Variable values (e.g., region, CIDR blocks, AMI ID).
- `outputs.tf`: Outputs for the public and private IPs of the EC2 instances.
- `README.md`: This file.

## Setup Instructions

### Step 1: Clone or Set Up the Project Directory
1. Create a directory for the lab:
   ```bash
   mkdir ~/environment/lab18
   cd ~/environment/lab18
   ```
2. create the Terraform files (`main.tf`, `variables.tf`, `terraform.tfvars`, `outputs.tf`).

### Step 2: Define the Terraform Configuration

## Deployment Steps

1. **Initialize Terraform:**
   ```bash
   terraform init
   ```
   - This downloads the AWS provider plugin and initializes the backend.

2. **Review the Plan:**
   ```bash
   terraform plan
   ```
   - Review the proposed changes to ensure they match the lab requirements.
     ![image](https://github.com/user-attachments/assets/4c95ce68-b80c-480f-be57-542bea48356f)

3. **Apply the Configuration:**
   ```bash
   terraform apply
   ```
   - Type `yes` to confirm and deploy the resources.
   - This will create the VPC, subnets, route tables, security groups, NAT Gateway, and EC2 instances.

## Verification

1. **Check Outputs:**
   - After deployment, Terraform will display the public and private IPs of the EC2 instances:
     - `nginx_public_ip`: Public IP of the Nginx instance.
     - `nginx_private_ip`: Private IP of the Nginx instance.
        <img width="959" alt="image" src="https://github.com/user-attachments/assets/45fc004d-a505-4f52-bbd8-4773dc948a15" />

     - `apache_private_ip`: Private IP of the Apache instance (note: Apache won’t have a public IP since it’s in the private subnet).
       ![image](https://github.com/user-attachments/assets/f6be4d9f-bdb7-4118-aa16-8cdd2646946e)

2. **Test Nginx:**
   - Open a browser and navigate to `http://<nginx_public_ip>`.
     <img width="423" alt="image" src="https://github.com/user-attachments/assets/0f6c5ea6-a814-4688-a0a1-e5a6e5d90e1e" />
     
3. **Test Apache:**
   - Since Apache is in a private subnet, SSH into the Nginx instance first:
     ```bash
     ssh -i ~/.ssh/lab-key.pem ec2-user@<nginx_public_ip>
     ```
   - From the Nginx instance, test Apache:
     ```bash
     curl http://<apache_private_ip>
     ```
     <img width="379" alt="image" src="https://github.com/user-attachments/assets/29a52cfd-5602-41d3-9e29-04d5a7789123" />

## Cleanup

To avoid incurring AWS charges, destroy the resources after completing the lab:

1. **Destroy the Infrastructure:**
   ```bash
   terraform destroy
   ```
   - Type `yes` to confirm and delete all resources.

2. **Remove Local Key Files (Optional):**
   - If you created `lab-key.pem` locally, remove it:
     ```bash
     rm ~/.ssh/lab-key.pem
     ```


