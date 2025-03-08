# Lab 14: SDK and CLI Interactions

## Objective
This lab focuses on using the AWS CLI to create and manage an S3 bucket. The tasks include:
- Creating an S3 bucket
- Configuring permissions
- Uploading and downloading files
- Enabling versioning
- Setting up server access logging

## Prerequisites
Before you begin, ensure that:
- You have AWS CLI installed and configured with appropriate IAM permissions.
- You have access to an AWS account with S3 permissions.

## Steps

### 1. Create an S3 Bucket
```sh
aws s3 mb s3://ha542-bucket
```

### 2. Verify Bucket Creation
```sh
aws s3 ls
```
<img width="725" alt="image" src="https://github.com/user-attachments/assets/80514268-ad60-4e59-a361-34b07050cf1c" />

### 3. Configure Bucket Permissions
#### 3.1 Make the Bucket Public
```sh
aws s3api put-bucket-acl --bucket ha542-bucket --acl public-read
```
#### 3.2 Verify Bucket ACL
```sh
aws s3api get-bucket-acl --bucket ha542-bucket
```
<img width="725" alt="image" src="https://github.com/user-attachments/assets/02785acc-8ab6-4668-bdfe-c4644eea7145" />

### 4. Upload a File to S3
```sh
echo "Hello AWS from Lab14!" > testfile.txt
```

### 5. Upload the File
```sh
aws s3 cp testfile.txt s3://ha542-bucket/
```
![image](https://github.com/user-attachments/assets/65d26c8d-8f1e-40aa-8be6-ab80584b5a48)

### 6. Download the File
```sh
aws s3 cp s3://ha542-bucket/testfile.txt downloaded_testfile.txt
cat downloaded_testfile.txt
```

### 7. Enable Versioning
```sh
aws s3api put-bucket-versioning --bucket ha542-bucket --versioning-configuration Status=Enabled
```

### 8. Verify Versioning
```sh
aws s3api get-bucket-versioning --bucket ha542-bucket
```

### 9. Test Versioning
#### 9.1 Upload an Updated File
```sh
echo "Updated content!" > testfile.txt
aws s3 cp testfile.txt s3://ha542-bucket/
```
![image](https://github.com/user-attachments/assets/d112a793-3c8f-4295-9f13-3eab53a111d2)

#### 9.2 List All Versions
```sh
aws s3api list-object-versions --bucket ha542-bucket
```
![image](https://github.com/user-attachments/assets/66295d6f-faf7-4dd5-ae89-340f49f1b137)
![image](https://github.com/user-attachments/assets/67b74dcd-aa82-450e-93cf-ae6a3b23e39c)
![image](https://github.com/user-attachments/assets/bd9a05ca-82b8-428c-b0ee-19b171b45c4a)

### 10. Enable Server Access Logging
#### 10.1 Create a Logging Bucket
```sh
aws s3 mb s3://ha542-bucket-logs
```
![image](https://github.com/user-attachments/assets/cedde542-8da2-42d7-a36a-531375ffb973)
#### 10.2 Enable Logging for the Main Bucket
```sh
aws s3api put-bucket-logging --bucket ha542-bucket --bucket-logging-status '{
    "LoggingEnabled": {
        "TargetBucket": "ha542-bucket-logs",
        "TargetPrefix": "logs/"
    }
}'
```

#### 10.3 Verify Logging Setup
```sh
aws s3api get-bucket-logging --bucket ha542-bucket
```


## Conclusion
This lab demonstrated the use of AWS CLI for managing S3 buckets, including configuring permissions, handling file uploads, enabling versioning, and setting up access logging. These operations are essential for effective storage management in AWS environments.

