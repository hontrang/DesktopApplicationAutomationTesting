#!/bin/bash

# Vòng lặp vô hạn
while true; do
  # Kiểm tra xem com.salesforce.authenticator có đang hoạt động hay không
  is_active=$(adb shell dumpsys activity activities | grep mResumedActivity | grep -c "com.salesforce.authenticator")

  # Nếu không có chuỗi com.salesforce.authenticator trong kết quả, khởi động ứng dụng
  if [ "$is_active" -eq 0 ]; then
    echo "Ứng dụng com.salesforce.authenticator không hoạt động. Đang khởi động..."
    adb shell monkey -p com.salesforce.authenticator -c android.intent.category.LAUNCHER 1
  else
    echo "Ứng dụng com.salesforce.authenticator đang hoạt động."
  fi

  # Chờ 5 giây trước khi kiểm tra lại
  sleep 5
done
