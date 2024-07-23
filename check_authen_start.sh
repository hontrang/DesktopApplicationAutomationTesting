#!/bin/bash

while true; do
  is_active=$(adb shell dumpsys activity activities | grep mResumedActivity | grep -c "com.salesforce.authenticator")

  if [ "$is_active" -eq 0 ]; then
    echo "Salesforce authentication is not running. Restarting..."
    adb shell monkey -p com.salesforce.authenticator -c android.intent.category.LAUNCHER 1
  else
    echo "Salesforce authentication is running"
  fi

  sleep 5
done
