#!/bin/bash

process_appium=$(pgrep -f "appium")
if [ -n "$process_appium" ]; then
  echo "Appium is running"
fi
appium