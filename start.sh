#!/bin/bash
emulator -avd Pixel_XL_API_30 &

SCRIPT="check_authen_start.sh"
(
while true; do
    ./$SCRIPT
    STATUS=$?
    if [ $STATUS -eq 0 ]; then
        break
    fi
    echo "Script error with code $STATUS. Restarting the script ..."
    sleep 5
done
) &
sh appium.sh &
sleep 30
sh java.sh &
echo "=== completed ==="