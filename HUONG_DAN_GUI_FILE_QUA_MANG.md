# 📤 Hướng Dẫn Gửi File Excel Qua Mạng (2 Máy Tính Khác Nhau)

## 🎯 Mục tiêu
- **Máy 1 (Client)**: Gửi file Excel cho Server
- **Máy 2 (Server)**: Nhận file, xử lý, gửi kết quả về Client

---

## 📋 Các Bước Chuẩn Bị

### 1. Xác định IP địa chỉ Server
Trên **máy chạy Server** (Windows):
```powershell
ipconfig
```
Tìm **IPv4 Address** (ví dụ: `192.168.1.100`)

### 2. Tìm IP địa chỉ của mạng
Hai máy phải kết nối cùng một mạng:
- Cùng WiFi hoặc LAN kabel
- Kiểm tra ping: `ping <IP_SERVER>`

---

## 🚀 Hướng Dẫn Chạy

### Máy 2 - Server:

```powershell
cd e:\python\LapTrinhMang\BaicuoikiTH

# Biên dịch lại (nếu có thay đổi)
mvn clean compile

# Chạy Server
mvn exec:java -Dexec.mainClass="server.ServerGUI"
```

**Khi Server khởi động:**
1. Cửa sổ Server GUI mở ra
2. Server lắng nghe trên port **5000**
3. Ghi nhớ IP của Server (ví dụ: `192.168.1.100`)

### Máy 1 - Client:

```powershell
cd e:\python\LapTrinhMang\BaicuoikiTH

# Chạy Client
mvn exec:java -Dexec.mainClass="client.ClientGUI"
```

**Trên Client GUI:**
1. **Server Host**: Nhập IP Server (ví dụ: `192.168.1.100`)
   - ⚠️ **KHÔNG** dùng `localhost` nếu chạy trên 2 máy khác nhau!
2. **Port**: `5000`
3. Click **"Kết nối"**

---

## 📤 Quy Trình Gửi File

### Trên Client (Máy 1):
1. Click **"Chọn file Cán bộ"** → Chọn file Excel
2. Click **"Chọn file Phòng thi"** → Chọn file Excel khác
3. Click **"Gửi & Xử lý"** (nút mới)
4. Chờ kết quả từ Server

### Trên Server (Máy 2):
1. Khi Client gửi file, Server tự động nhận
2. Server tự động xử lý
3. Gửi kết quả trở lại cho Client
4. Client nhận kết quả và hiển thị

---

## 🔧 Tường Trình Chi Tiết (Kỹ Thuật)

### Bước 1: Gửi File từ Client
- Client mở file Excel
- Gửi **tên file** + **kích thước** + **nội dung binary** qua socket

### Bước 2: Server Nhận File
- Server nhận thông tin file
- Tạo file tạm trên Server
- Lưu dữ liệu nhận được

### Bước 3: Server Xử lý
- Đọc file Excel đã nhận
- Thực hiện phân công
- Tạo file kết quả

### Bước 4: Gửi Kết Quả Về Client
- Server gửi **2 file kết quả** về Client
- Client nhận và lưu trong thư mục `output/`

---

## ⚠️ Khắc Phục Sự Cố

### Lỗi: "Connection refused"
```
❌ Lỗi: Không thể kết nối đến Server
```
**Giải pháp:**
- Kiểm tra Server có đang chạy không
- Kiểm tra IP Server đúng không: `ping <IP_SERVER>`
- Kiểm tra Firewall cho phép port 5000

### Lỗi: "File not found"
```
❌ Lỗi: File không tìm thấy
```
**Giải pháp:**
- Chọn file trước khi gửi
- Kiểm tra file tồn tại

### Lỗi: "Port already in use"
```
❌ Lỗi: Port 5000 đã được sử dụng
```
**Giải pháp:**
```powershell
# Tìm process dùng port 5000
netstat -ano | findstr :5000

# Kết thúc process (thay thế PID)
taskkill /PID <PID> /F
```

---

## 🎓 Ví Dụ Chạy Thực Tế

### Máy Tính 1 (Server - IP: 192.168.1.100)
```
1. mvn exec:java -Dexec.mainClass="server.ServerGUI"
2. Server GUI mở → "Server bắt đầu lắng nghe..."
3. Chợ client kết nối...
```

### Máy Tính 2 (Client - IP: 192.168.1.50)
```
1. mvn exec:java -Dexec.mainClass="client.ClientGUI"
2. Client GUI mở
3. Nhập Server Host: 192.168.1.100
4. Port: 5000
5. Click "Kết nối"
6. Chọn 2 file Excel
7. Click "Gửi & Xử lý"
8. Chờ kết quả...
9. Kết quả tự động lưu vào output/
```

---

## 📝 Lưu Ý Quan Trọng

✅ **Làm được:**
- Gửi file qua mạng LAN
- Xử lý file trên Server
- Nhận kết quả trên Client
- Chạy trên Windows, Linux, Mac

❌ **Không thể:**
- Gửi qua Internet công cộng (chưa có mã hóa)
- Xử lý file cùng lúc từ nhiều client (cần thêm xử lý đa luồng)

---

## 🆘 Cần Giúp?
Nếu gặp lỗi, kiểm tra:
1. Server có đang chạy không?
2. IP Server đúng không?
3. 2 máy có cùng mạng không?
4. Port 5000 có bị chặn không?
