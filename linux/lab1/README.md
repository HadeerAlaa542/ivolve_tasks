# Lab 1: User and Group Management  

## Objective  
The goal of this lab is to create a new **group** named `ivolve`, create a new **user** assigned to this group with a **secure password**,
and configure the user’s **sudo permissions** to allow installing **Nginx without requiring a password**.  

## Prerequisites  
A **Linux** system (Ubuntu/Debian) with **sudo** access.  

## Steps  

### 1- Create a New Group and User  
Run the following commands to create the group and user:  
```bash
sudo groupadd ivolve
sudo useradd -m -g ivolve -s /bin/bash ivolveuser
sudo passwd ivolveuser  # Set a secure password for the user
```

### 2- Grant Sudo Privileges for Nginx Installation  
To allow the user to install **Nginx** without entering a password:  
1. Open the sudoers file for editing:  
   ```bash
   sudo vim  /etc/sudoers.d/ivolve
   ```
2. Add the following line:  
   ```bash
   ivolveuser ALL=(ALL) NOPASSWD: /usr/bin/apt install nginx
   ```

### 3- Switch to the New User  
```bash
su - ivolveuser
```

### 4- Install Nginx Without a Password  
```bash
sudo apt install nginx
```
**Expected result:** Nginx should install **without prompting for a password**.

## Verification  
To confirm that Nginx is installed, run:  
```bash
nginx -v
```
Or check if the service is running:  
```bash
systemctl status nginx
```
