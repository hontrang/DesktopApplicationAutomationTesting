#!/bin/bash
process_ids=$(pgrep -f "ByPass2FA")
if [ -n "$process_ids" ]; then
  echo "$process_ids" | xargs kill -9
  echo "Killed process ByPass2FA"
else
  echo "No process ByPass2FA found"
fi
cd ~/sfcc_authen
java -jar ByPass2FA.jar -u "Hon Trang" -n code.txt -c icauto_asics.json &