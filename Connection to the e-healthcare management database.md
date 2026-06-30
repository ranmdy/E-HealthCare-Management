How to connect to the MySQL database I created using SSH connections as collaborators

Setting up SSH key

🔧 Step 1: Collaborator Sets Up SSH Tunnel

1. Each collaborator installs Tailscale on their laptop.

2. The host invites them invite them via the Tailscale admin console.

- The invite link → https://login.tailscale.com/admin/invite/Zw2UeXFpBgiq2xLb9xsS11  

Now they’re on the same private network.

🔧 Step 2: Each collaborator generates their own key pair

1. On their own laptop, they run on powershell:

ssh-keygen -t rsa -b 4096 -C "your_email@egmail.com"

2. When prompted for a file location, press Enter (default: C:\Users\<YourWindowsUsername>\.ssh\id_rsa).

3. Optionally set a passphrase (adds extra security but not compulsory) or just press enter when asked for a passphrase.

- your_email@gmail.com → Use your email

This creates:

Private key → ~/.ssh/id_rsa (keep secret).

Public key → ~/.ssh/id_rsa.pub (send to you, the host).

🔧 Step 3: Enable OpenSSH Server on Windows 11

1. Press Win + I → open Settings.

2. Go to Apps → Optional Features.

3. Click Add a feature.

4. Search for OpenSSH Client → Install.

👉 Now your Windows machine can initiate SSH connections.

🔧 Step 4: Confirm SSH is Running

1. Open PowerShell and run:

Get-Service sshd

It should show Running.

🔧 Step 5: Collect public keys
Each collaborator sends the host(Abdussalam) their public key file (id_rsa.pub).
Example contents look like(e.g):
ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQ... your_email@gmail.com

🔧 Step 6: The hosts(Abdussalam) adds the Public Keys to Authorized Keys

🔧 Step 7: Distribute Private Keys
Each collaborator keeps their private key (id_rsa) on their own machine.
 load it into an SSH agent by running this on powershell:
 
ssh-add ~/.ssh/id_rsa

🔧 Step 8: Collaborators connect with their own key
Each collaborator uses their private key to connect buy running this on powershell:

ssh -i ~/.ssh/id_rsa -L 8080:localhost:80 -L 3307:localhost:3306 lukman@100.127.75.93

- 8080 → local port on collaborator’s machine. This forwards local port 8080 to the remote machine’s web server (port 80)).

  8080 is the local port we will all use for our connection, but if the port is running for another process use another free one and also change it your jdbc connection (jdbc:mysql://localhost:yourlocalport/e-healthcare management database) and if the local port you choose is below 1024 then you will need to run this (ssh -L 8080:localhost:80 -L 3307:localhost:3306 yourWindowsUsername@102.88.55.233) as an administrator

- 80 → Apache port on your laptop

- 3307 → local port on collaborator’s machine. This forwards local port 3307 to the remote machine’s MySQL server (port 3306).

  3307 is the local port we will all use for our connection, but if the port is running for another process use another free one and also change it your jdbc connection (jdbc:mysql://localhost:yourlocalport/e-healthcare management database) and if the local port you choose is below 1024 then you will need to run this (ssh -L 8080:localhost:80 -L 3307:localhost:3306 yourWindowsUsername@102.88.55.233) as an administrator

- 3306 → MySQL port on your laptop.

- lukman → hosts Windows account name.

- 100.127.75.93 → hosts tailscale IP

🔧 Step 5: Connect via JDBC in IntelliJ

In their Java code (your dbconfig.properties) input :

db.url=jdbc:mysql://localhost:3307/e-healthcare management database

db.user=yourusername

db.password=your password

This connects through the tunnel securely.

Then run main to check if your connection was successful

Then visit this on your browser:

http://localhost:8080/phpmyadmin

Usernames (db.user) and passwords (db.password) for the jdbc connection in dbconfig.properties on IntelliJ IDEA:

1. Rotimi :

user : Rotimi

password : Rotimi123

2. Ose :

user : Ose

password : Ose123

3. Samuel :

user : Samuel

password : Samuel123

4. Harrietta :

user : Harrietta

password : Harrietta123

5. Baliqees :

user : Baliqees

password : Baliqees123

N:B : 

But before collaborators can successfullt visit http://localhost:8080/phpmyadmin, the host (Abdussalam) will have to open XAMPP Control panel and run Apache to serve phpMyAdmin web interface and MySQL to power the database the collaborators are connecting to.

This-ssh -i ~/.ssh/id_rsa -L 8080:localhost:80 -L 3307:localhost:3306 lukman@100.127.75.93 will have to be run on powershell and left open on powershell before they run their IntelliJ IDEA code everytime the collaborators want to connect.

---
