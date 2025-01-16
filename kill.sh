#!/bin/bash
kill_processes() {
  local process_name=$1
  echo "Tìm processes $process_name"
   if sudo pkill -f "$process_name"; then
      echo "Đã kết thúc tiến trình $process_name"
    else
      echo "Không tìm thấy tiến trình $process_name"
      return 0 #keep the loop is not broke
    fi
}

if [ "$EUID" -ne 0 ]; then
  echo "Vui lòng chạy script với quyền root."
  exec sudo "$0" "$@"
  exit 1
fi

kill_processes "node"
kill_processes "sleep"
kill_processes "start.sh"
kill_processes "java -jar $HOME/sfcc_authen/ByPass2FA.jar"
kill_processes "$HOME/Android/Sdk/emulator/qemu/linux-x86_64/qemu-system-x86_64"

