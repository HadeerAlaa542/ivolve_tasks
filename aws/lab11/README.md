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
   - **developer** group: Assign **AmazonEC2FullAccess** policy.

2. **Create IAM Users:**
   - **admin-l** (Console access only, MFA enabled, member of admin group).
     ![image](https://github.com/user-attachments/assets/a4c401be-4bad-4ceb-af1c-39465ae6bc2f)

   - **admin-2** (CLI access only, member of admin group, uses access key and secret key).
   - ![image](https://github.com/user-attachments/assets/5ef9acb8-301a-44ff-9c71-6b3fd8f39337)

   - **dev-l** (Console and programmatic access, member of developer group).
     ![image](https://github.com/user-attachments/assets/2f18ac37-059f-4aa8-9b3b-f89d0313d9db)

![image](https://github.com/user-attachments/assets/1de2807c-fd24-4976-910b-e79011e2ef21)


3. **Enable MFA for admin-l:**
   - Go to **IAM > Users > admin-l > Security Credentials**.
   - Enable MFA using a virtual device (Google Authenticator/Authy).

### **3. Configure AWS CLI for admin-2 and Create an EC2 Instance**
1. Install AWS CLI (if not installed):
   ```bash
   aws --version
   ```
   If not installed, download from [AWS CLI](https://aws.amazon.com/cli/).
2. Configure AWS CLI for **admin-2**:
   ```bash
   aws configure
   ```
   - Enter **Access Key ID** and **Secret Access Key**.
   - Set region (e.g., `us-east-1`).
   - Choose output format (`json`).
3. Launch an EC2 instance:
   ```bash
   aws ec2 run-instances --image-id ami-12345678 --count 1 --instance-type t2.micro --key-name MyKeyPair --security-groups default
   ```
   - Replace `ami-12345678` with a valid AMI ID.
   - Replace `MyKeyPair` with a valid key pair.
4. Verify EC2 instance:
   ```bash
   aws ec2 describe-instances
   ```

### **4. Verify dev-l Access to EC2 and S3**
1. **Log in as dev-l via AWS Console** and ensure:
   - EC2 access is available.
   - S3 access is available.

2. **Verify CLI Access for dev-l**:
   ```bash
   aws configure
   ```
   - Enter **dev-l’s** credentials.
3. **Check EC2 Instances and S3 Buckets**:
   ```bash
   aws ec2 describe-instances
   aws s3 ls
   ```
4. **Take Screenshots**:
   - EC2 dashboard.
   - S3 bucket list.

---

## **Conclusion**
By completing this lab, you have successfully:
- Implemented IAM security best practices.
- Configured AWS CLI for secure access.
- Used AWS CLI to create and manage AWS resources.
- Enabled billing alerts to track AWS usage and costs.

✅ **Your AWS environment is now more secure and well-managed!** 🚀

