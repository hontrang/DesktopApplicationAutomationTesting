#!/bin/bash
kill -9 $(pgrep -f "java -jar $HOME/sfcc_authen/ByPass2FA.jar")
kill -9 $(pgrep -f "$HOME/Android/Sdk/emulator/qemu/linux-x86_64/qemu-system-x86_64")
$HOME/Android/Sdk/emulator/emulator -avd Pixel_XL_API_30 &\
java -jar ~/sfcc_authen/ByPass2FA.jar -c "Hon Trang" -n code.txt -c icauto_asics.json &\
java -jar ~/sfcc_authen/ByPass2FA.jar -c "Hieu Nguyen" -n code_hieu.txt -c icauto_shiseido.json

