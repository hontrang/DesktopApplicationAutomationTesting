#!/bin/bash
kill_processes() {
  local process_name=$1
  processes=$(pgrep -f "$process_name")
  echo "Tìm processes $process_name"
  if [ -n "$processes" ]; then
    kill -9 "$processes"
    echo "Đã kết thúc tiến trình $process_name"
  else
    echo "Không tìm thấy tiến trình $process_name"
    return 0
  fi
}

kill_processes "node"
kill_processes "sleep"
kill_processes "check_authen_start"
kill_processes "java -jar $HOME/sfcc_authen/ByPass2FA.jar"
kill_processes "$HOME/Android/Sdk/emulator/qemu/linux-x86_64/qemu-system-x86_64"

