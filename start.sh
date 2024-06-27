#!/bin/bash

# Tên của script bạn muốn chạy
SCRIPT="reset_all_facilities.sh"

# Vòng lặp vô hạn để restart script khi nó bị lỗi
while true; do
    # Chạy script
    ./$SCRIPT
    # Kiểm tra trạng thái thoát
    STATUS=$?
    # Nếu trạng thái thoát là 0, thoát vòng lặp (script chạy thành công)
    if [ $STATUS -eq 0 ]; then
        break
    fi
    # Nếu trạng thái thoát khác 0, in thông báo lỗi và restart lại sau 5 giây
    echo "Script bị lỗi với mã thoát $STATUS. Đang khởi động lại..."
    sleep 5
done
