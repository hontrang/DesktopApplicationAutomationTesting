#!/bin/bash

# Kiểm tra quyền root
if [ "$EUID" -ne 0 ]
  then echo "Please sudo it"
  exit
fi

# Xác định hệ điều hành
os=$(lsb_release -si 2>/dev/null || cat /etc/*release | grep ^ID= | cut -d= -f2)
version=$(lsb_release -sr 2>/dev/null || cat /etc/*release | grep VERSION_ID | cut -d= -f2 | tr -d '"')

# Hàm cài đặt Node.js
install_nodejs() {
  curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
  nvm install 20
}

# Cài đặt cho các hệ điều hành khác nhau
case "$os" in
  "ubuntu" | "debian")
    apt-get update
    apt-get install -y curl software-properties-common
    install_nodejs
    ;;
  "centos" | "rhel")
    yum install -y curl
    curl -sL https://rpm.nodesource.com/setup_18.x | bash -
    yum install -y nodejs
    ;;
  "fedora")
    dnf install -y curl
    curl -sL https://rpm.nodesource.com/setup_18.x | bash -
    dnf install -y nodejs
    ;;
  *)
    echo "Hệ điều hành không được hỗ trợ bởi script này."
    exit 1
    ;;
esac

# Kiểm tra cài đặt
if command -v node &>/dev/null && command -v npm &>/dev/null; then
  echo "Node.js và npm đã được cài đặt thành công."
  node -v
  npm -v
else
  echo "Có lỗi xảy ra trong quá trình cài đặt Node.js và npm."
fi