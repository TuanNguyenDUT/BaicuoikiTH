# 📦 Hướng Dẫn Tải & Chạy Chương Trình (Không Cần JDK)

## ✅ Yêu Cầu Tối Thiểu

**Chỉ cần cài đặt Java Runtime (JRE):**
- **Tải từ:** https://www.java.com/
- **Phiên bản:** Java 11 trở lên
- **Kích thước:** ~100MB (nhẹ lắm!)

**KHÔNG cần cài đặt:**
- ❌ JDK
- ❌ Maven
- ❌ IDE (VS Code, IntelliJ, etc.)

---

## 🚀 Cách Chạy

### **Bước 1: Tải bộ cài**

Tải file `exam-assignment-distribution.zip` từ thư mục `target/`:

```
exam-assignment-distribution.zip
```

### **Bước 2: Giải nén**

Giải nén vào thư mục bất kỳ, ví dụ: `C:\MyApp\ExamAssignment\`

### **Bước 3: Chạy Server**

**Trên máy của người phân công (bạn):**

Double click vào: **`RUN-SERVER-STANDALONE.bat`**

Cửa sổ server sẽ mở lên tự động ✅

### **Bước 4: Chạy Client**

**Trên máy của người gửi file (bạn tôi):**

Double click vào: **`RUN-CLIENT-STANDALONE.bat`**

Cửa sổ client sẽ mở lên tự động ✅

### **Bước 5: Kết nối**

Trên Client GUI:
- **Server Host:** Nhập IP của máy chạy Server
- **Port:** `5000`
- Click **"Kết nối"**

Chọn file Excel → Click **"Gửi & Xử lý"** ✅

---

## 🔧 Chi Tiết File Được Cấp

```
📦 exam-assignment-distribution/
├── 📄 exam-server-all.jar          ← File JAR chứa tất cả (nhất thiết!)
├── 📂 input/                        ← Thư mục lưu file Excel input
├── 📂 output/                       ← Thư mục lưu kết quả
├── 🎯 RUN-SERVER-STANDALONE.bat    ← Chạy Server (double click)
├── 🎯 RUN-CLIENT-STANDALONE.bat    ← Chạy Client (double click)
├── 📖 START_HERE.md                ← Hướng dẫn bắt đầu
├── 📖 HUONG_DAN_GUI_FILE_QUA_MANG.md   ← Hướng dẫn gửi file
└── 📖 README.md                    ← Thông tin chung
```

---

## ✅ Lợi Ích

| Cách Cũ | Cách Mới |
|---------|---------|
| Cần cài JDK (400MB) | Chỉ cần JRE (100MB) |
| Cần cài Maven (200MB) | Không cần Maven |
| Cần cài IDE | Không cần IDE |
| Phức tạp, dễ lỗi | Chỉ double click |
| **Tổng: 600MB+** | **Tổng: 100MB** |

---

## 🎯 Quy Trình Test

### **Test trên 2 máy cùng mạng:**

```
Máy 1 (Server):
  1. Chạy RUN-SERVER-STANDALONE.bat
  2. Ghi lại IP: ví dụ 192.168.1.100
  3. Chờ Client kết nối

Máy 2 (Client):
  1. Chạy RUN-CLIENT-STANDALONE.bat
  2. Nhập Server Host: 192.168.1.100
  3. Click Kết nối
  4. Chọn file Excel & gửi
  5. Nhận kết quả ✅
```

### **Test trên cùng máy:**

```
Terminal 1:
  1. Chạy RUN-SERVER-STANDALONE.bat

Terminal 2:
  1. Chạy RUN-CLIENT-STANDALONE.bat
  2. Nhập Server Host: localhost
  3. Click Kết nối & gửi file ✅
```

---

## ❌ Nếu Gặp Lỗi

### **"Java chưa được cài đặt"**
- Tải Java Runtime từ: https://www.java.com/
- Cài đặt → Khởi động lại máy
- Chạy lại `.bat` file

### **"exam-server-all.jar không tìm thấy"**
- Kiểm tra đã giải nén đúng chưa
- Đảm bảo file `.jar` cùng thư mục với `.bat`

### **"Không kết nối được Server"**
- Kiểm tra IP address của Server: `ipconfig` (tìm IPv4)
- Kiểm tra firewall cho phép port 5000
- Ping test: `ping <IP_SERVER>`

### **"File quá lớn"**
- Chia nhỏ file Excel
- Hoặc tăng timeout trong code

---

## 📞 Hỗ Trợ Nhanh

| Vấn đề | Giải Pháp |
|-------|----------|
| Java không chạy | Tải Java từ java.com |
| Không tìm thấy JAR | Kiểm tra giải nén đầy đủ |
| Không kết nối được | Kiểm tra IP & firewall |
| GUI không mở | Chạy `.bat` từ Command Prompt |

---

## 📊 Thông Tin Kỹ Thuật

- **Ngôn ngữ:** Java 11+
- **Framework:** Swing (GUI)
- **Protocol:** TCP Socket
- **Port:** 5000 (có thể thay đổi)
- **Định dạng file:** Excel (.xlsx)
- **Thư viện:** Apache POI, GSON (đã bundle)

---

## 🎁 Lợi Ích Của Cách Này

✅ **Bạn tôi không cần:**
- Cài đặt gì cả (chỉ cần Java)
- Hiểu về code hay Maven
- IDE hay terminal commands
- Tải 600MB+ tool

✅ **Chỉ cần:**
- Double click `.bat` file
- Nhập IP Server
- Chọn file Excel
- Nhận kết quả

✅ **Dễ share:**
- Copy folder & gửi bạn
- Hoặc nén ZIP gửi qua email/USB
- Chạy là xong!

---

**Bạn tôi có thể chạy chương trình mà không biết về code! 🎉**
