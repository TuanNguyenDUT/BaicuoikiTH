# 📱 FileShare Client - Java Swing GUI

## 🎯 Mục Đích

Ứng dụng Java Swing cung cấp giao diện **GUI** thân thiện để:
- ✅ Gửi file Excel lên server
- ✅ Tải file từ folder uploads
- ✅ Tải kết quả phân tích từ folder results
- ✅ Theo dõi tiến độ upload/download

---

## 📋 Yêu Cầu

### 1. Java JDK (không phải JRE)
```
Tối thiểu: Java 8 trở lên
```

**Download:**
- [Oracle JDK 21](https://www.oracle.com/java/technologies/downloads/)
- Hoặc [OpenJDK](https://openjdk.org/)

**Kiểm tra Java đã cài chưa:**
```bash
java -version
javac -version
```

---

## 🚀 Cách Chạy

### Cách 1: Windows (Dễ nhất)
```bash
# Vào folder BaicuoikiTH
cd BaicuoikiTH

# Double-click file
run_client.bat
```

**Hoặc chạy từ PowerShell:**
```powershell
cd BaicuoikiTH
.\run_client.bat
```

### Cách 2: Manual Compile & Run

```bash
# Vào folder
cd BaicuoikiTH

# Compile
javac FileShareClient.java

# Chạy
java FileShareClient
```

### Cách 3: Linux/Mac

```bash
cd BaicuoikiTH
javac FileShareClient.java
java FileShareClient
```

---

## 📖 Hướng Dẫn Sử Dụng

### 1. **Khởi động ứng dụng**
```
java FileShareClient
```

**Cửa sổ ứng dụng sẽ hiện:**
```
┌─────────────────────────────────────────┐
│  📁 File Share Client                   │
├─────────────────────────────────────────┤
│ 🌐 Cài đặt Server                       │
│  Server URL: http://192.168.235.44:8000 │
├─────────────────────────────────────────┤
│ 📤 Gửi File Lên (Upload)                │
│  [📁 Chọn File]  [⬆️ Gửi File Lên]     │
│  [📥 Tải File Từ Upload]                │
├─────────────────────────────────────────┤
│ 📊 Tải Kết Quả Phân Tích                │
│  [📥 Tải Kết Quả]                       │
├─────────────────────────────────────────┤
│ 📝 Trạng Thái                           │
│  [Status log...]                        │
├─────────────────────────────────────────┤
│ ⏳ Tiến độ                              │
│  [████████░░░░░░░░░░░░░░░]  40%        │
└─────────────────────────────────────────┘
```

### 2. **Gửi File Excel**

1. **Nhập Server URL** (nếu khác mặc định)
   - Ví dụ: `http://192.168.235.44:8000`

2. **Chọn file**
   - Click button: `📁 Chọn File`
   - Chọn file Excel (.xlsx, .xls, .csv)

3. **Gửi lên**
   - Click: `⬆️ Gửi File Lên`
   - Xem tiến độ ở thanh progress bar
   - Trạng thái sẽ hiển thị ở mục "📝 Trạng Thái"

**Kết quả:**
```
[14:58:30] ✅ Đã chọn file: danhsachcanbocoithi.xlsx
[14:58:35] 📤 Bắt đầu gửi file: danhsachcanbocoithi.xlsx
[14:58:42] ✅ Gửi file thành công!
[14:58:42] 📝 Phản hồi: File 'danhsachcanbocoithi.xlsx' uploaded successfully
```

### 3. **Tải File Từ Uploads**

1. Click button: `📥 Tải File Từ Upload`
2. Ứng dụng sẽ:
   - Kết nối đến server
   - Liệt kê danh sách file trong folder uploads
   - Hiển thị dialog chọn file

3. **Chọn file** và click OK

4. **Chọn vị trí lưu** và click Save

---

## 4. **Tải Kết Quả Phân Tích**

1. **Chạy script phân tích** (trên server)
   ```bash
   python analyze_excel.py
   ```

2. **Trên client, click**: `📥 Tải Kết Quả`

3. **Chọn file kết quả** từ list

4. **Lưu về máy**

---

## 📝 Các Nút Chức Năng

| Nút | Chức Năng |
|-----|----------|
| 📁 Chọn File | Mở dialog chọn file Excel |
| ⬆️ Gửi File Lên | Upload file đã chọn lên server |
| 📥 Tải File Từ Upload | Tải file từ folder uploads |
| 📥 Tải Kết Quả | Tải file kết quả từ folder results |

---

## 🔧 Cài Đặt Server URL

### Nếu server chạy trên máy khác:
```
Nhập: http://<IP_SERVER>:8000
```

**Ví dụ:**
```
http://192.168.235.44:8000
http://10.0.0.5:8000
```

### Nếu server chạy trên máy local:
```
http://localhost:8000
```

---

## ⚠️ Lỗi Thường Gặp

### 1. "Command 'javac' not found"
**Giải pháp:**
```
Cài Java JDK (không phải JRE)
Thêm JAVA_HOME vào environment variables
```

**Windows - Cài đặt JAVA_HOME:**
1. Download JDK
2. Cài đặt (ghi nhớ đường dẫn)
3. Mở Control Panel → System → Environment Variables
4. Tạo biến: `JAVA_HOME = C:\Program Files\Java\jdk-21`
5. Thêm `%JAVA_HOME%\bin` vào PATH

### 2. "Connection refused"
**Giải pháp:**
```
- Kiểm tra server chạy chưa
- Kiểm tra IP address đúng không
- Kiểm tra port 8000 có mở không
- Tắt firewall tạm thời để test
```

### 3. "Cannot connect to server"
**Kiểm tra:**
```bash
# Trên máy server, mở browser
http://localhost:8000

# Nếu OK, kiểm tra IP address
ipconfig

# Copy đúng IP vào client
http://192.168.x.x:8000
```

### 4. File upload lỗi
**Kiểm tra:**
```
- Folder uploads/ có tồn tại không
- File size < 100MB
- File format: xlsx, xls, hoặc csv
```

---

## 📊 Quy Trình Đầy Đủ

```
1. Server (Máy A):
   ├─ Chạy HTTP Server: python file_share_with_upload.py
   └─ Chạy Auto Analyzer: python auto_analyzer.py (tùy chọn)

2. Client (Máy B hoặc cùng máy A):
   ├─ Mở Java Client: java FileShareClient
   ├─ Gửi file Excel
   └─ Tải kết quả

3. Server (Phân tích):
   └─ Chạy thủ công: python analyze_excel.py

4. Client (Tải kết quả):
   └─ Click "📥 Tải Kết Quả" để download
```

---

## 🎨 Giao Diện

### Upload Panel
- Chọn file Excel
- Hiển thị tên file đã chọn
- Gửi lên server
- Progress bar theo dõi tiến độ

### Download Panel
- Tải file từ uploads
- Tải file kết quả từ results
- Chọn file từ list
- Dialog lưu file

### Status Area
- Hiển thị log real-time
- Timestamp mỗi action
- Thông báo lỗi/thành công

---

## 💡 Tip Hữu Ích

### 1. **Compile một lần, chạy nhiều lần**
```bash
# Compile (tạo .class file)
javac FileShareClient.java

# Chạy lần 1
java FileShareClient

# Chạy lần 2 (không cần compile lại)
java FileShareClient
```

### 2. **Tùy chỉnh server URL**
- Nó sẽ nhớ lần trước nếu bạn thay đổi

### 3. **Chạy song song nhiều client**
```bash
# Terminal 1
java FileShareClient

# Terminal 2
java FileShareClient

# Cùng lúc upload 2 file khác nhau
```

---

## 📚 File Liên Quan

- `file_share_with_upload.py` - Server HTTP
- `analyze_excel.py` - Script phân tích
- `auto_analyzer.py` - Auto analyzer (tùy chọn)

---

## ✅ Checklist Trước Khi Chạy

- [ ] Java JDK đã cài đặt
- [ ] Python server đang chạy
- [ ] IP address đúng
- [ ] Port 8000 mở/không bị firewall chặn
- [ ] Folder uploads, results tồn tại trên server
- [ ] File Excel để gửi đã sẵn sàng

---

## 🆘 Gặp Vấn Đề?

1. **Kiểm tra log** ở mục "📝 Trạng Thái"
2. **Kiểm tra server** đang chạy
3. **Kiểm tra kết nối** mạng
4. **Thử localhost** (http://localhost:8000) nếu cùng máy
5. **Tắt firewall** để test

---

Done! 🎉 Giờ bạn có GUI Java để quản lý file share! 📁
