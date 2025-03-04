# Lab 12: Launching an EC2 Instance

## Lab Requirements

- **Create a VPC** with both public and private subnets.
- **Launch 1 EC2 instance** in each subnet.
- **Configure the private instance’s security group** to allow inbound SSH only from the public instance’s IP.
- **SSH into the private instance** using the public (bastion) host.

## Step-by-Step Instructions

1. **Create the VPC and Subnets**
   - **VPC:** Create a VPC (e.g., with CIDR block `10.0.0.0/16`).
   - **Public Subnet:** Create a public subnet (e.g., `10.0.1.0/24`).
   - **Private Subnet:** Create a private subnet (e.g., `10.0.2.0/24`).

2. **Configure Internet Access for the Public Subnet**
   - Create and attach an **Internet Gateway (IGW)** to the VPC.
   - Create a **route table** for the public subnet with a route:  
     - Destination: `0.0.0.0/0`  
     - Target: IGW  
   - Associate the route table with the public subnet.

3. **Launch EC2 Instances**
   - **Public Instance (Bastion Host):**
     - Launch an EC2 instance in the public subnet.
     - Ensure the instance has a public IP.
     - Use a security group that allows inbound SSH (port 22) from your IP.
   - **Private Instance:**
     - Launch an EC2 instance in the private subnet.
     - Do not assign a public IP.
     - Use a security group (see next step).

4. **Configure Security Groups**
   - **Public Instance Security Group:**  
     - Allow inbound SSH (port 22) from your local IP.
   - **Private Instance Security Group:**  
     - Allow inbound SSH (port 22) **only** from the public instance’s IP.

5. **SSH into the Private Instance via the Bastion Host**
   - **Step 1:** SSH into the public (bastion) instance from your local machine:
     ```bash
     ssh -i path/to/your-key.pem ec2-user@<public-instance-IP>
     ```
   - **Step 2:** From the bastion host, SSH into the private instance using its private IP:
     ```bash
     ssh -i path/to/your-key.pem ec2-user@<private-instance-private-IP>
     ```

---

This README lists the essential steps required to meet the lab’s objectives.
### Output
<img width="402" alt="image" src="https://github.com/user-attachments/assets/d34cf19a-f10d-4616-9167-589c99cfb2c1" />
<br>
<img width="403" alt="image" src="https://github.com/user-attachments/assets/f4b84736-80af-47a7-aa18-ec7d1bd821c5" />


