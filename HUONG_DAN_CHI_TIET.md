# 🚀 Hướng Dẫn Chi Tiết: Gửi File Excel Qua Mạng (Client-Server)

## 📌 Tóm Tắt Chức Năng

Bây giờ bạn có thể:
1. ✅ Client gửi file Excel cho Server qua mạng
2. ✅ Server nhận file và xử lý tự động
3. ✅ Server gửi kết quả trở lại cho Client
4. ✅ Client nhận kết quả và lưu vào thư mục `output/`

---

## 🔧 Chuẩn Bị

### Yêu cầu
- ✅ Java JDK 11+
- ✅ Maven
- ✅ 2 máy tính kết nối cùng mạng LAN hoặc WiFi

---

## 💻 BƯỚC 1: Biên Dịch Code

```powershell
cd e:\python\LapTrinhMang\BaicuoikiTH
mvn clean compile
```

✅ **Kết quả**: Không có lỗi compile

---

## 🖥️ BƯỚC 2: Chạy Server (Máy 2)

**Trên máy chạy Server:**

```powershell
cd e:\python\LapTrinhMang\BaicuoikiTH

# Chạy Server GUI
mvn exec:java -Dexec.mainClass="server.ServerGUI"
```

**Màn hình Server sẽ hiển thị:**
```
Server - Phân công giám thị coi thi
⏳ Chờ lệnh...
[Server bắt đầu lắng nghe...]
```

**Ghi nhớ IP địa chỉ của Server:**
- Mở PowerShell và chạy: `ipconfig`
- Tìm **IPv4 Address** (ví dụ: `192.168.1.100`)

---

## 🖱️ BƯỚC 3: Chạy Client (Máy 1)

**Trên máy chạy Client:**

```powershell
cd e:\python\LapTrinhMang\BaicuoikiTH

# Chạy Client GUI
mvn exec:java -Dexec.mainClass="client.ClientGUI"
```

**Giao diện Client sẽ mở ra:**

```
┌─────────────────────────────────────────────────────────────┐
│ Client - Gửi file & Xem danh sách phân công                 │
├─────────────────────────────────────────────────────────────┤
│ Server Host: [localhost          ]  Port: [5000   ]  [Kết nối] │
│ File Cán bộ: [                             ]  [Chọn...]        │
│ File Phòng thi: [                          ]  [Chọn...]        │
│ [Gửi & Xử lý] [Lấy danh sách phân công] [Lấy danh sách giám sát] │
│ ❌ Chưa kết nối...                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔗 BƯỚC 4: Kết Nối Client-Server

### Trên giao diện Client:

1. **Thay đổi Server Host:**
   - Xóa `localhost`
   - Nhập IP Server (ví dụ: `192.168.1.100`)
   - **⚠️ KHÔNG dùng `localhost` nếu 2 máy khác nhau!**

2. **Port:** Giữ `5000`

3. **Click: "Kết nối"**
   - Nếu thành công: `✅ Đã kết nối: 192.168.1.100:5000`
   - Nếu thất bại: `❌ Kết nối thất bại!`

---

## 📤 BƯỚC 5: Gửi File & Xử Lý

### Trên giao diện Client:

1. **Chọn file Cán bộ:**
   - Click `[Chọn...]` bên File Cán bộ
   - Chọn file: `input/danhsachcanbocoithi.xlsx`
   - Hoặc file Excel của bạn

2. **Chọn file Phòng thi:**
   - Click `[Chọn...]` bên File Phòng thi
   - Chọn file: `input/danhsachphongthi.xlsx`
   - Hoặc file Excel của bạn

3. **Click: "Gửi & Xử lý"**

**Quá trình thực hiện:**
```
⏳ Đang gửi file cán bộ...
⏳ Đang gửi file phòng thi...
⏳ Đang xử lý...
✅ Xử lý hoàn thành!
```

---

## 📊 BƯỚC 6: Xem Kết Quả

**Tự động hiển thị trên Client:**
- Danh sách phân công giám thị
- Danh sách giám sát hành lang

**File kết quả được lưu trong thư mục `output/`:**
- `phancong.xlsx` - Danh sách phân công
- `giamsat.xlsx` - Danh sách giám sát

---

## 📋 Quy Trình Chi Tiết (Kỹ Thuật)

```
┌─────────────┐                          ┌─────────────┐
│ Máy Client  │                          │ Máy Server  │
└─────────────┘                          └─────────────┘
      │                                        │
      │ 1. Gửi: UPLOAD_FILE:CANBO              │
      ├──────────────────────────────────────>│
      │                                        │ Nhận file
      │ 2. Gửi dữ liệu file cán bộ (binary)   │ Lưu vào temp
      ├──────────────────────────────────────>│
      │                                        │
      │ 3. Nhận: OK (xác nhận)                 │
      │<──────────────────────────────────────┤
      │                                        │
      │ 4. Gửi: UPLOAD_FILE:PHONGTHI           │
      ├──────────────────────────────────────>│
      │                                        │
      │ 5. Gửi dữ liệu file phòng thi          │
      ├──────────────────────────────────────>│
      │                                        │
      │ 6. Nhận: OK                            │
      │<──────────────────────────────────────┤
      │                                        │
      │ 7. Gửi: PROCESS_AND_SEND_RESULTS       │
      ├──────────────────────────────────────>│
      │                                        │ Xử lý:
      │                                        │ - Đọc file
      │                                        │ - Phân công
      │                                        │ - Tạo kết quả
      │ 8. Nhận: PHANCONG:JSON                 │
      │<──────────────────────────────────────┤
      │                                        │
      │ 9. Nhận: GIAMSAT:JSON                  │
      │<──────────────────────────────────────┤
      │                                        │
      │ 10. Nhận file kết quả (binary)         │
      │<──────────────────────────────────────┤
      │ Lưu: output/phancong.xlsx              │
      │ Lưu: output/giamsat.xlsx               │
      │                                        │
      │ 11. DONE                               │
      │<──────────────────────────────────────┤
      │                                        │
      ✅ Hoàn thành!                           ✅ Đã xử lý
```

---

## 🆘 Khắc Phục Sự Cố

### ❌ Lỗi: "Connection refused"
```
Lỗi: Không thể kết nối đến server
```
**Giải pháp:**
1. Kiểm tra Server có đang chạy: `mvn exec:java -Dexec.mainClass="server.ServerGUI"`
2. Kiểm tra IP Server đúng: `ipconfig` → tìm IPv4
3. Ping test: `ping 192.168.1.100`
4. Firewall: Cho phép port 5000

### ❌ Lỗi: "File not found"
```
Lỗi: File không tồn tại
```
**Giải pháp:**
1. Chọn file trước khi gửi (click Chọn... sau đó chọn file)
2. Kiểm tra file Excel tồn tại
3. Kiểm tra đường dẫn đúng

### ❌ Lỗi: "Port already in use"
```
Lỗi: Port 5000 đã được sử dụng
```
**Giải pháp:**
```powershell
# Tìm process dùng port 5000
netstat -ano | findstr :5000

# Ví dụ output:
# TCP    0.0.0.0:5000           0.0.0.0:0              LISTENING       12345

# Kết thúc process
taskkill /PID 12345 /F
```

### ❌ Lỗi: "Localhost không kết nối"
```
Khi chạy 2 máy khác nhau
```
**Giải pháp:**
- ⚠️ **KHÔNG** dùng `localhost`
- **PHẢI** dùng IP thực của server (ví dụ: `192.168.1.100`)
- Localhost chỉ hoạt động khi 2 máy ở trên cùng 1 PC

---

## ✅ Kiểm Tra Kết Nối

### Trước khi chạy Client, kiểm tra:

```powershell
# 1. Kiểm tra IP Server
ipconfig /all

# 2. Ping từ Client sang Server
ping 192.168.1.100

# Output mong đợi:
# Pinging 192.168.1.100 with 32 bytes of data:
# Reply from 192.168.1.100: bytes=32 time=2ms TTL=64
# ✅ Kết nối thành công!
```

---

## 🎯 Ví Dụ Hoàn Chỉnh

### **Máy 1 (Server - IP: 192.168.1.100)**

**Terminal:**
```powershell
E:\python\LapTrinhMang\BaicuoikiTH> mvn exec:java -Dexec.mainClass="server.ServerGUI"
```

**GUI:**
```
Server - Phân công giám thị coi thi
⏳ Chờ lệnh...

📋 Nhật ký:
[14:30:15] Server khởi động
[14:30:20] Client kết nối: 192.168.1.50
[14:30:25] Nhận yêu cầu: UPLOAD_FILE:CANBO
[14:30:26] ✅ Tải: 50 cán bộ, 10 phòng
[14:30:30] Phân công xong: 45 phân công, 5 giám sát
```

### **Máy 2 (Client - IP: 192.168.1.50)**

**Terminal:**
```powershell
E:\python\LapTrinhMang\BaicuoikiTH> mvn exec:java -Dexec.mainClass="client.ClientGUI"
```

**GUI:**
```
Server Host: [192.168.1.100  ]  Port: [5000  ]  [Kết nối]
File Cán bộ: [C:/input/canbo.xlsx   ]  [Chọn...]
File Phòng thi: [C:/input/phong.xlsx ]  [Chọn...]
[Gửi & Xử lý] [Lấy danh sách phân công] [Lấy danh sách giám sát]

✅ Đã kết nối: 192.168.1.100:5000

Danh sách phân công giám thị:
[Bảng hiển thị kết quả...]

Danh sách giám sát hành lang:
[Bảng hiển thị kết quả...]
```

---

## 📁 Cấu Trúc Thư Mục

```
BaicuoikiTH/
├── input/
│   ├── danhsachcanbocoithi.xlsx
│   └── danhsachphongthi.xlsx
├── output/
│   ├── phancong.xlsx        ← Kết quả
│   └── giamsat.xlsx          ← Kết quả
├── server_temp/             ← File tạm trên server
│   ├── danhsachcanbocoithi.xlsx
│   └── danhsachphongthi.xlsx
└── src/
    └── main/java/
        ├── client/
        │   ├── ExamClient.java    ← ✅ Cập nhật: thêm sendFile()
        │   └── ClientGUI.java      ← ✅ Cập nhật: thêm UI gửi file
        └── server/
            └── ExamServer.java     ← ✅ Cập nhật: nhận file & xử lý
```

---

## 🔐 Lưu Ý Bảo Mật

⚠️ **Chỉ dùng trong mạng nội bộ (LAN) vì:**
- Dữ liệu gửi qua socket không mã hóa
- Chưa có xác thực người dùng
- Chưa có kiểm tra file

**Để sử dụng trên Internet công cộng:**
- Thêm SSL/TLS encryption
- Thêm xác thực username/password
- Thêm kiểm tra file type

---

## ✨ Các Tính Năng Sẽ Thêm Trong Tương Lai

- [ ] Gửi nhiều file cùng lúc
- [ ] Tiến trình upload (progress bar)
- [ ] Nén file trước gửi
- [ ] Mã hóa SSL/TLS
- [ ] Xác thực người dùng
- [ ] Lịch sử gửi file

---

## 📞 Cần Giúp?

Nếu gặp vấn đề, kiểm tra:

1. ✅ Server có đang chạy không?
2. ✅ IP Server có đúng không?
3. ✅ 2 máy có cùng mạng không?
4. ✅ Firewall có cho phép port 5000 không?
5. ✅ File Excel có tồn tại không?
6. ✅ Có lỗi trong console output không?
