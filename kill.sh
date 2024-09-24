#!/bin/bash
kill_process() {
  local process_name=$1
  emulator_process=$(pgrep -f "$process_name")
  echo "Tìm process $process_name"
  if [ -n "$emulator_process" ]; then
    kill -9 "$emulator_process"
    echo "Đã kết thúc tiến trình $process_name"
  else
    echo "Không tìm thấy tiến trình $process_name"
  fi
}

kill_process "node"
kill_process "sleep"
kill_process "check_authen_start"
kill_process "java -jar $HOME/sfcc_authen/ByPass2FA.jar"
kill_process "$HOME/Android/Sdk/emulator/qemu/linux-x86_64/qemu-system-x86_64"

