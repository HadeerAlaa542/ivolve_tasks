```md
# Lab 3: Shell Scripting Basics - Network Ping Scanner  

## Objective  
Create a shell script that scans all IP addresses in the `172.16.17.x` subnet (where `x` ranges from `0` to `255`).  
- If a **ping** is successful, display: `"Server 172.16.17.x is up and running"`.  
- If a **ping** fails, display: `"Server 172.16.17.x is unreachable"`.  

## Prerequisites  
- A **Linux** system with a Bash shell.  
- Basic knowledge of **shell scripting**.  
- **Network access** to the subnet.  

## Steps  

### 1️⃣ Create the Script  
Open a terminal and create a new script file:  
```bash
sudo nano ping_script.sh
```

### 2️⃣ Add the Following Code  
```bash
#!/bin/bash

# Loop through all possible IPs in the 172.16.17.x subnet (0-255)
for x in {0..255}; do
    IP="172.16.17.$x"
    
    # Ping each IP with a timeout of 1 second and check the response
    if ping -c 1 -W 1 $IP > /dev/null 2>&1; then
        echo "Server $IP is up and running"
    else
        echo "Server $IP is unreachable"
    fi
done
```
Save and exit (`CTRL + X`, then `Y`, then `Enter`).  

### 3️⃣ Make the Script Executable  
Give execution permission:  
```bash
sudo chmod +x ping_script.sh
```

### 4️⃣ Run the Script  
Execute the script to start scanning the network:  
```bash
./ping_script.sh
```

### 5️⃣ (Optional) Save Output to a Log File  
If you want to store the results in a log file:  
```bash
/usr/local/bin/ping_scan.sh > /var/log/ping_scan.log
```

## Expected Output  
```
Server 172.16.17.1 is up and running
Server 172.16.17.2 is unreachable
Server 172.16.17.3 is up and running
...
```

