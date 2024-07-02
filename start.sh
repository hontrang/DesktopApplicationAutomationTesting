#!/bin/bash

SCRIPT="reset_all_facilities.sh"

while true; do
    ./$SCRIPT
    STATUS=$?
    if [ $STATUS -eq 0 ]; then
        break
    fi
    echo "Script error with code $STATUS. Restarting the script ..."
    sleep 5
done