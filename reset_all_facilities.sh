#!/bin/bash
process_ids=$(pgrep -f "ByPass2FA")
process_emulator=$(pgrep -f "emulator")
if [ -n "$process_ids" ]; then
  echo "$process_ids" | xargs kill -9
  echo "Killed process ByPass2FA"
else
  echo "No process ByPass2FA found"
fi

if [ -n "$process_emulator" ]; then
  echo "$process_emulator" | xargs kill -9
  echo "Killed process emulator"
else
  echo "No process emulator found"
fi
process_appium=$(pgrep -f "appium")
if [ -n "$process_appium" ]; then
  echo "Appium is running"
else
  echo "Start appium"
  appium &
fi

emulator -avd SFCC_AUTHENTICATION &

SCRIPT="check_authen_start.sh"
while true; do
    ./$SCRIPT
    STATUS=$?
    if [ $STATUS -eq 0 ]; then
        break
    fi
    echo "Script error with code $STATUS. Restarting the script ..."
    sleep 5
done

java -jar ~/sfcc_authen/ByPass2FA.jar -c "Hon Trang" -n code.txt -c icauto_asics.json &
