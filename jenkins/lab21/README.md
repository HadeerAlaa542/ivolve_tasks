# Lab 21: Role-based Authorization

## Objective
The goal of this lab is to set up role-based authorization by creating two users and assigning them different roles:
- `user1`: Admin role
- `user2`: Read-only role

## Prerequisites
- A system with the necessary software installed (e.g., Jenkins, AWS IAM, or any other system where role-based authorization is being implemented).
- Administrative access to configure user roles.

## Steps to Implement Role-based Authorization

### Step 1: Install the Role Plugin (Jenkins)
1. Log in to Jenkins as an administrator.
2. Navigate to **Manage Jenkins** > **Manage Plugins**.
3. Click on the **Available plugins** tab and search for "Role-based Authorization Strategy".
4. Select the plugin and click **Install without restart**.
5. Wait for the installation to complete and restart Jenkins.
6. Once installed, go to **Manage Jenkins** > **Configure Global Security**.
7. Select **Role-Based Strategy** under the Authorization section and save the changes.

![image](https://github.com/user-attachments/assets/01c6e71c-852a-4740-bb7c-33abe6e52da3)

![image](https://github.com/user-attachments/assets/8474d0cd-8237-4adf-ad30-7f8b0301e84f)

### Step 2: Create Users
#### Creating `user1`
1. Navigate to the user management section.
2. Click on "Create User."
3. Enter `user1` as the username.
4. Assign the necessary credentials (password or key-based authentication).
5. Save and proceed.

#### Creating `user2`
1. Repeat the above steps and create `user2`.
   
![image](https://github.com/user-attachments/assets/71429e60-ea9b-459b-a225-f739421d3e8f)
   
### Step 3: Configure Role-Based Strategy
1. Navigate to **Manage Jenkins** > **Manage and Assign Roles**.
2. Click on **Assign Roles**.
3. You will see a section to assign roles to users or groups.


![image](https://github.com/user-attachments/assets/409edf36-c7e4-44c1-bf87-560cadb6fb70)

### Step 4: Assign Roles
#### Assign Admin Role to `user1`
1. Go to **Manage Jenkins** > **Manage and Assign Roles** > **Assign Roles**.
2. Locate `user1` in the user list.
3. Check the **admin** role checkbox.
4. Save the changes.

#### Assign Read-Only Role to `user2`
1. Go to **Manage Jenkins** > **Manage and Assign Roles** > **Assign Roles**.
2. Locate `user2` in the user list.
3. Check the **view role** checkbox to grant read-only access.
4. Save the changes.

![image](https://github.com/user-attachments/assets/39406768-26de-4a9d-baa9-4cb973d9b292)

### Step 5: Verify Role-based Access
1. Log in as `user1` and confirm administrative privileges.

<img width="827" alt="image" src="https://github.com/user-attachments/assets/c62268f7-6847-4470-8b62-d28fc7753c30" />

2. Log in as `user2` and ensure only read access is available.

![image](https://github.com/user-attachments/assets/b07594a2-78c0-42ec-8170-f2e535668bd9)

## Conclusion
This lab successfully implements role-based authorization by creating users with distinct permissions. This enhances security by ensuring users only have access to the resources necessary for their role.

