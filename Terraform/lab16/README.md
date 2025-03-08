# Terraform Lab 16: Multi-Tier Application Deployment

## Overview

This lab implements a multi-tier infrastructure using Terraform on AWS Cloud9. The setup includes a manually created VPC imported into Terraform, public and private subnets, an EC2 instance in the public subnet, and an RDS instance in the private subnet. An internet gateway enables external access for the EC2 instance, and a local provisioner is used to store the EC2 public IP in a file.
               ![Uploading WhatsApp Image 2025-03-06 at 22.07.45_765fe40e.jpg…]()

## Prerequisites

- AWS Cloud9 environment set up
- AWS CLI configured with appropriate permissions
- Terraform installed
- Manually created VPC imported into Terraform

## Steps

### 1. Set Up AWS Cloud9 Environment

1. Log in to AWS Management Console.
2. Navigate to **Cloud9** and create a new environment.
3. Choose an EC2 instance (t2.micro recommended).
### 2. Install Terraform

```sh
sudo apt update && sudo apt install terraform -y
```
### 3. create the vpc manually 
<img width="639" alt="image" src="https://github.com/user-attachments/assets/dbadd221-72aa-4a20-8165-85c2ed2781de" />
![image](https://github.com/user-attachments/assets/b78d1566-e1fe-4554-91c2-e36cf78692c3)

### 3. Write Terraform Configuration

#### Importing a Manually Created VPC into Terraform for Management

```hcl
provider "aws" {
  region = "us-east-1"
}
resource "aws_vpc" "main-vpc" {
  cidr_block = "10.0.0.0/16"
}
```
#### Initialize Terraform

```sh
terraform init
```

#### Import the Existing VPC

```sh
terraform import aws_vpc.main-vpc vpc-03e2f63b6dc5a7eff
```

### Output:
![image](https://github.com/user-attachments/assets/9f7307c2-40c5-4d5f-a175-2cdfbec6f45d)

![image](https://github.com/user-attachments/assets/b75b7657-cbb7-4818-aee3-c1efa98ed25c)

#### Public and Private Subnets

```hcl
resource "aws_subnet" "public_subnet" {
  vpc_id                  = aws_vpc.main-vpc.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-1a"
}
resource "aws_subnet" "private_subnet_1" {
  vpc_id            = aws_vpc.main-vpc.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "us-east-1a"
}
resource "aws_subnet" "private_subnet_2" {
  vpc_id            = aws_vpc.main-vpc.id
  cidr_block        = "10.0.3.0/24"
  availability_zone = "us-east-1b"
}
```

#### Internet Gateway and Route Table

```hcl
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.main-vpc.id
}
resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.main-vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
}
resource "aws_route_table_association" "public_assoc" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_rt.id
}
```

#### Security Groups

```hcl
resource "aws_security_group" "web_sg" {
  vpc_id = aws_vpc.main-vpc.id
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
```

#### EC2 Instance with Local Provisioner

```hcl
resource "aws_instance" "web_server" {
  ami             = "ami-05b10e08d247fb927"
  instance_type   = "t2.micro"
  subnet_id       = aws_subnet.public_subnet.id
  vpc_security_group_ids = [aws_security_group.web_sg.id]
  associate_public_ip_address = true

  provisioner "local-exec" {
    command = "echo ${self.public_ip} > ec2-ip.txt"
  }
}
```

#### RDS Security Group and Instance

```hcl
resource "aws_security_group" "rds_sg" {
  vpc_id = aws_vpc.main-vpc.id
  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    security_groups = [aws_security_group.web_sg.id]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "rds-subnet-group"
  subnet_ids = [aws_subnet.private_subnet_1.id, aws_subnet.private_subnet_2.id]
}

resource "aws_db_instance" "rds" {
  allocated_storage    = 20
  engine              = "mysql"
  engine_version      = "8.0"
  instance_class      = "db.t3.micro"
  username           = "admin"
  password           = "password123"
  db_subnet_group_name = aws_db_subnet_group.rds_subnet_group.name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  publicly_accessible  = false
  skip_final_snapshot = true
}
```

### 4. Deploy Infrastructure

```sh
terraform init
terraform plan
terraform apply -auto-approve
```
### Output:
<img width="627" alt="image" src="https://github.com/user-attachments/assets/689c4667-eab9-4b64-a901-058a9914582a" />

### 5. Verify Deployment

- Check the **EC2 Instance** in AWS Console.
<img width="752" alt="image" src="https://github.com/user-attachments/assets/d8aac252-a421-4bd0-8422-ff2d8415b0db" />
<img width="752" alt="image" src="https://github.com/user-attachments/assets/205c5b89-7f67-4a43-9ece-20156ecf2f15" />

- Verify that **the EC2 IP is stored** in `ec2-ip.txt`.
<img width="486" alt="image" src="https://github.com/user-attachments/assets/743ca0fb-1da6-4d81-9748-e75b9ef09b6f" />

- Ensure that RDS is created in the private subnet.
<img width="751" alt="image" src="https://github.com/user-attachments/assets/39b15079-94e9-4b0c-8c8b-ee276ecb4f06" />
![Uploading image.png…]()

### 6. Destroy Resources

```sh
terraform destroy -auto-approve
```

## Conclusion

This lab demonstrates deploying a manually created VPC imported into Terraform, with an EC2 instance in a public subnet and an RDS database in a private subnet. The internet gateway enables public access, and a local provisioner stores the EC2 public IP in a file.


![image](https://github.com/user-attachments/assets/529dacd4-e7c5-4744-aab6-70d18ad8b92a)
![image](https://github.com/user-attachments/assets/82df5e00-160b-4295-88f5-5452502b6090)
![image](https://github.com/user-attachments/assets/1898386a-3a46-41f2-b811-10f6a5654ced)
![image](https://github.com/user-attachments/assets/1d547d0d-fac2-49d6-909d-c3f42f7d2275)

![image](https://github.com/user-attachments/assets/40003ac9-5d21-4ca7-b31a-bb85b61f3e50)

<img width="960" alt="image" src="https://github.com/user-attachments/assets/86aba446-284f-4e64-b2fc-8e48d1e359e4" />
<img width="960" alt="image" src="https://github.com/user-attachments/assets/a0563606-8bfd-41b3-a709-fa508f26b27e" />
<img width="960" alt="image" src="https://github.com/user-attachments/assets/8fcdfd48-324a-4a63-b51f-1b095a04f5b2" />
<img width="960" alt="image" src="https://github.com/user-attachments/assets/30583fd3-5c67-4467-a2a9-dd25f2cea482" />
<img width="960" alt="image" src="https://github.com/user-attachments/assets/29eaa298-2cbd-4f19-81ad-a6ee626c46b3" />
