#!/bin/bash

APP_NAME="ByPass2FA.jar"
KILL="kill.sh"
START="start.sh"
PASSWORD="123456"

while true; do
    if pgrep -f "$APP_NAME" >/dev/null; then
        echo "$APP_NAME đang chạy."
    else
        echo "$PASSWORD" | sudo -S ./$KILL &
        sleep 5
        ./$START
        echo "==================== completed =================="
    fi
    sleep 5
done
