# Lab II: AWS Security

## **Overview**
This lab focuses on securing an AWS environment by setting up IAM users, groups, permissions, MFA, and using AWS CLI for managing AWS resources. The key tasks include:

- Creating an AWS account and setting up a billing alarm.
- Configuring IAM groups and assigning appropriate permissions.
- Creating IAM users with different access levels (console, CLI, and programmatic access).
- Enabling Multi-Factor Authentication (MFA) for enhanced security.
- Using AWS CLI to create and verify AWS resources.

---

## **Steps to Complete the Lab**

### **1. Create an AWS Account and Set a Billing Alarm**
1. Sign up for an AWS Free Tier account at [AWS Sign-Up](https://aws.amazon.com/).
2. Enable billing alerts:
   - Open the **Billing Dashboard**.
   - Enable "Receive Billing Alerts" under **Billing Preferences**.
   - Create a CloudWatch billing alarm to notify when usage exceeds a threshold.

![image](https://github.com/user-attachments/assets/2e49723e-ef69-462d-9597-ae1d1be40dfc)
  
![image](https://github.com/user-attachments/assets/b16d2c0c-1ae0-4223-9931-fee29480ac62)
  
![image](https://github.com/user-attachments/assets/7a0fc365-3d5e-4662-8b4d-78bb0e62c0f4)

![image](https://github.com/user-attachments/assets/9092764c-88a8-4c50-bd35-7166a1996f0c)

![image](https://github.com/user-attachments/assets/7c57b4d0-3d11-43cc-beed-5fa3f720fc3d)

![image](https://github.com/user-attachments/assets/03a01d93-7076-4a77-ad07-b131a78f4c9d)

### **2. Configure IAM Users and Groups**
1. **Create IAM Groups:**
   - **admin** group: Assign **AdministratorAccess** policy.
   ![image](https://github.com/user-attachments/assets/4e37dcb9-c055-41aa-bb04-b18e348175f2)

   - **developer** group: Assign **AmazonEC2FullAccess** policy.
   ![image](https://github.com/user-attachments/assets/545df654-4657-4b17-8e09-0470a5ce7879)

2. **Create IAM Users:**
   - **admin-l** (Console access only, MFA enabled, member of admin group).
     ![image](https://github.com/user-attachments/assets/a4c401be-4bad-4ceb-af1c-39465ae6bc2f)

   - **admin-2** (CLI access only, member of admin group, uses access key and secret key).
   - ![image](https://github.com/user-attachments/assets/5ef9acb8-301a-44ff-9c71-6b3fd8f39337)

   - **dev-l** (Console and programmatic access, member of developer group).
     ![image](https://github.com/user-attachments/assets/2f18ac37-059f-4aa8-9b3b-f89d0313d9db)

3. **Enable MFA for admin-l:**
   - Go to **IAM > Users > admin-l > Security Credentials**.
   - Enable MFA using a virtual device (Google Authenticator/Authy).

### **3. Configure AWS CLI for admin-2 and Create an EC2 Instance**
1. Install AWS CLI.
   ```bash
   aws --version
   ```
2. Configure AWS CLI for **admin-2**:
   ```bash
   aws configure
   ```
   - Enter **Access Key ID** and **Secret Access Key**.
   - Set region (e.g., `us-east-1`).
   - Choose output format (`json`).
3. Launch an EC2 instance:
   ```bash
   aws ec2 run-instances --image-id ami-0ff8a915Ø7f77f867 --count 1 --instance-type t2.micro --key-name mykey --security-groups default
   ```
   ![image](https://github.com/user-attachments/assets/b12f7c7e-0a13-4888-955a-ecf0e562ba65)


### **4. Verify dev-l Access to EC2 and S3**
1. **Log in as dev-l via AWS Console** and ensure:
   - EC2 access is available.
   ![image](https://github.com/user-attachments/assets/88f8677c-d5a2-467b-9442-966c8ef9e41d)

   - S3 access is available.
     ![image](https://github.com/user-attachments/assets/6ac60138-6856-4331-9e8b-cbc87b7b488f)


2. **Verify CLI Access for dev-l**:
   ```bash
   aws configure
   ```
   - Enter **dev-l’s** credentials.
3. **Check EC2 Instances and S3 Buckets**:
   ```bash
   aws ec2 describe-instances
   ```
   ![image](https://github.com/user-attachments/assets/b99ef9c7-597a-4127-9fc1-d32890c78174)
  ```bash
   aws s3 ls
   ```
   ![image](https://github.com/user-attachments/assets/74c43e8b-e17e-46d8-bda9-f540ebe1e2ca)

## **Conclusion**
By completing this lab, you have successfully:
- Implemented IAM security best practices.
- Configured AWS CLI for secure access.
- Used AWS CLI to create and manage AWS resources.
- Enabled billing alerts to track AWS usage and costs.

