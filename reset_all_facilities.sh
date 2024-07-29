#!/bin/bash
process_emulator=$(pgrep -f "emulator")

if [ -n "$process_emulator" ]; then
  echo "$process_emulator" | xargs kill -9
  echo "Killed process emulator"
else
  echo "No process emulator found"
fi


emulator -avd SFCC_AUTHENTICATION &

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

echo "=== completed ==="

