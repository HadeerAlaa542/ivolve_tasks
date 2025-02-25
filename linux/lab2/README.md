Here's your README file formatted for GitHub preview:  

---

# Lab 2: MySQL Installation and Automated Backup with Cron  

## Objective  
The goal of this lab is to:  
- Install **MySQL** on an Ubuntu system.  
- Create a **cron job** that automatically backs up MySQL databases.  
- Schedule the backup to run **every Sunday at 5:00 PM** and save it to a specific location.  

## Prerequisites  
- A **Linux** system (Ubuntu/Debian).  
- **sudo** access.  
- **MySQL installed** and running.  

## Steps  

### 1. Install MySQL  
Run the following command to install MySQL:  
```bash
sudo apt update && sudo apt install mysql-server -y
```
Start and enable the MySQL service:  
```bash
sudo systemctl start mysql
sudo systemctl enable mysql
```

### 2. Secure MySQL Installation  
Run the security script to set a root password and remove unnecessary settings:  
```bash
sudo mysql_secure_installation
```
Follow the prompts to secure your MySQL instance.  

### 3. Create a MySQL Backup Script  
Create a script at `/usr/local/bin/mysql_backup.sh` to back up all MySQL databases:  
```bash
sudo nano /usr/local/bin/mysql_backup.sh
```
Add the following script:  
```bash
#!/bin/bash

# Define variables
BACKUP_DIR="/backup/mysql"
TIMESTAMP=$(date +%F_%H-%M-%S)
BACKUP_FILE="$BACKUP_DIR/mysql_backup_$TIMESTAMP.sql"
MYSQL_USER="root"
MYSQL_PASSWORD="your_mysql_root_password"

# Ensure backup directory exists
mkdir -p $BACKUP_DIR

# Take MySQL database backup
mysqldump -u $MYSQL_USER -p$MYSQL_PASSWORD --all-databases > $BACKUP_FILE

# Optional: Compress the backup file
gzip $BACKUP_FILE

# Print success message
echo "Backup completed successfully: $BACKUP_FILE.gz"
```
Save and exit (`CTRL + X`, then `Y`, then `Enter`).  
Give execution permission:  
```bash
sudo chmod +x /usr/local/bin/mysql_backup.sh
```

### 4. Schedule the Cron Job  
Edit the cron jobs using:  
```bash
crontab -e
```
Add the following line at the end to run the script **every Sunday at 5:00 PM**:  
```bash
0 17 * * 0 /usr/local/bin/mysql_backup.sh
```
Save and exit.  

### 5. Verify the Cron Job  
List cron jobs to confirm:  
```bash
crontab -l
```
Run the script manually to test:  
```bash
/usr/local/bin/mysql_backup.sh
```
Check if the backup file is created in `/backup/mysql/`.  
