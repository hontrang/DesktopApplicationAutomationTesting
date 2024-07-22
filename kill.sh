#!/bin/bash
kill -9 $(pgrep -f "java -jar $HOME/sfcc_authen/ByPass2FA.jar")
kill -9 $(pgrep -f "$HOME/Android/Sdk/emulator/qemu/linux-x86_64/qemu-system-x86_64")

