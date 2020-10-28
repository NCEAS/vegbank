# DEPLOYMENT PLAYBOOK

How to deploy a new version of vegbank.

Note: The process here could be streamlined a lot!

1. Log into VM
2. Clone or update copy of repo
3. `sudo ant install`
4. `mkdir web/vegbranch/media`
5. `sudo ant web-install`
6. `cd web`
7. `sudo ant general`
8. `sudo chown -R tomcat:vegbank /usr/vegbank`
9. `sudo systemctl restart tomcat9`
