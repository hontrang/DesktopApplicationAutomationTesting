#!/bin/bash

# Kiểm tra và giết tiến trình Java chạy file ByPass2FA.jar
java_process=$(pgrep -f "java -jar $HOME/sfcc_authen/ByPass2FA.jar")
if [ -n "$java_process" ]; then
  kill -9 "$java_process"
  echo "Đã kết thúc tiến trình Java chạy file ByPass2FA.jar"
else
  echo "Không tìm thấy tiến trình Java chạy file ByPass2FA.jar"
fi

# Kiểm tra và giết tiến trình giả lập Android
emulator_process=$(pgrep -f "$HOME/Android/Sdk/emulator/qemu/linux-x86_64/qemu-system-x86_64")
if [ -n "$emulator_process" ]; then
  kill -9 "$emulator_process"
  echo "Đã kết thúc tiến trình giả lập Android"
else
  echo "Không tìm thấy tiến trình giả lập Android"
fi