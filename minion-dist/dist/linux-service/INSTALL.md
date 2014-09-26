# Minion install

## Linux

Copy this folder content to server. Example:
```bash
scp * minion:~/app/minion/startup
```

```bash
# Copy start|stop scripts
cp minion-start.sh /usr/local/bin/minion-start.sh
cp minion-stop.sh /usr/local/bin/minion-stop.sh
# Copy service script
cp minion /etc/init.d/minion

# Allow execution
chmod +x /usr/local/bin/minion-start.sh
chmod +x /usr/local/bin/minion-stop.sh
chmod +x /etc/init.d/minion
```
