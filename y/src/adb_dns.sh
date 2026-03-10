cat << 'EOF' > /sdcard/dns_boot.sh
#!/system/bin/sh
DNS1=94.140.14.14
DNS2=94.140.15.15

iptables -t nat -D OUTPUT -p udp --dport 53 \
-j DNAT --to-destination $DNS1:53 2>/dev/null
iptables -t nat -D OUTPUT -p tcp --dport 53 \
-j DNAT --to-destination $DNS1:53 2>/dev/null
iptables -t nat -D OUTPUT -p udp --dport 53 \
-j DNAT --to-destination $DNS2:53 2>/dev/null
iptables -t nat -D OUTPUT -p tcp --dport 53 \
-j DNAT --to-destination $DNS2:53 2>/dev/null

iptables -t nat -A OUTPUT -p udp --dport 53 \
-m statistic --mode nth --every 2 --packet 0 \
-j DNAT --to-destination $DNS1:53
iptables -t nat -A OUTPUT -p udp --dport 53 \
-j DNAT --to-destination $DNS2:53
iptables -t nat -A OUTPUT -p tcp --dport 53 \
-m statistic --mode nth --every 2 --packet 0 \
-j DNAT --to-destination $DNS1:53
iptables -t nat -A OUTPUT -p tcp --dport 53 \
-j DNAT --to-destination $DNS2:53

setprop net.dns1 $DNS1
setprop net.dns2 $DNS2

echo "[OK] DNS: $DNS1 / $DNS2"
EOF

chmod 755 /sdcard/dns_boot.sh

sh /sdcard/dns_boot.sh


