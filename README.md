# DesktopApplicationAutomationTesting
![Build Jar](https://github.com/hontrang/DesktopApplicationAutomationTesting/actions/workflows/ci.yml/badge.svg)

### Prerequisite
Appium must be installed UiAutomator2
```bash
appium
```

Install sfcc authentication apk to emulator

### LIST AVD
```bash
emulator -list-avds
```

### START AVD
```bash
emulator -avd Pixel_3_XL_API_30
```

### AVD HEADLESS MODE
```bash
emulator -avd Pixel_3_XL_API_30  -no-window
```
### Sửa lỗi sdkmanager rỗng
1. Chạy sdkmanager -verbose để kiểm tra log
2. Dùng chmod -R username:group ~/.android/cache để cấp quyền

### Cài đặt remote desktop trên linux
Hướng dẫn [Cài đặt remote desktop trên linux](https://medium.com/@riley.kao/wsl2-ubuntu20-04-gui-remote-desktop-connection-rdp-2bbd21d2fa71)


### Sửa lỗi CPU acceleration status: This user doesn't have permissions to use KVM (/dev/kvm).
![CPU acceleration status: This user doesn't have permissions to use KVM (/dev/kvm)](/assets/kvm.png)