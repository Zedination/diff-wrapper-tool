# Hướng dẫn cài đặt và sử dụng Diff Wrapper Tool

Diff Wrapper Tool là một Tool đơn giản, wrapper của diff2html và Beyond Compare dùng để compare git src và export kết quả compare tiện lợi hơn<br>
Viết tool vì nhìn ae export diff src khổ quá 😄
- Tác giả: Zedination
- Email: [leanhduc9999@gmail.com](mailto:leanhduc9999@gmail.com)
- Discord cá nhân: https://discordapp.com/users/958007722471719003

## Mục lục
- [Hướng dẫn cài đặt và sử dụng Diff Wrapper Tool](#hướng-dẫn-cài-đặt-và-sử-dụng-diff-wrapper-tool)
  - [Mục lục](#mục-lục)
  - [Tính năng](#tính-năng)
  - [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
  - [Cài đặt](#cài-đặt)
  - [Cách sử dụng](#cách-sử-dụng)
  - [License](#license)

## Tính năng
- Diff src bằng Diff2html
- Diff src bằng Beyond Compare (nên sử dụng version 4)
- Branch compare & commit compare
- Dùng được bằng Windows Exprorer Context Menu

## Yêu cầu hệ thống
- NodeJS version >= 14
- Windows 10 trở lên

## Cài đặt
1. Download bản cài [tại đây!](https://zedination.dev/diff-wrapper-tool/Diff%20Wrapper%20Tool-1.0.exe)
2. Cài đặt bản cài, sau khi cài đặt thành công, màn hình desktop sẽ xuất hiện app có tên Diff Wrapper Tool, lần mở đầu tiên sẽ tiến hành đăng ký windows context menu

## Cách sử dụng
Tool sẽ có giao diện như sau <br> <br>
![image](static/1.png)

1. Chọn folder git cần diff src, có thể chọn bằng cách sử dụng Explorer Context Menu <br>
![image](static/4.png)
2. Chọn file Bcomp.exe của Beyond compare được cài trên máy, sử dụng toggle để bật tắt sử dụng Beyond Compare
3. Chọn commit muốn compare tại đây, có thể diff trực tiếp tại giao diện chọn commit. Phải nhập tối thiểu một trong 2 input commit, mặc định sẽ là HEAD<br>
![image](static/3.png)
4. Nhập branch cần compare tại đây. Phải nhập tối thiểu 1 input branch, mặc định sẽ là nhánh đang chọn <br>
![image](static/2.png)

## License
This project is licensed under the MIT License

<blockquote>
Copyright (c) 2023 Zedination


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
</blockquote>