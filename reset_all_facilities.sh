#!/bin/bash
kill -9 $(pgrep -f "java -jar $HOME/ocr-dev/ocr.jar")
kill -9 $(pgrep -f "$HOME/Android/Sdk/emulator/qemu/linux-x86_64/qemu-system-x86_64")
$HOME/Android/Sdk/emulator/emulator -avd Pixel_XL_API_30 &\
java -jar ~/ocr-dev/ocr.jar -r 600 200 100 30 -n code.txt -c icauto_asics.json -s screenshot.png &\
java -jar ~/ocr-dev/ocr.jar -r 600 280 100 30 -n code_hieu.txt -c icauto_shiseido.json -s screenshot_hieu.png 

