# 🚀 START HERE - HƯỚNG DẪN NHANH

## Xin chào! 👋

Ứng dụng **TCP Client-Server** cho phân công giám thị coi thi đã được tạo hoàn chỉnh.

Đây là cách để bắt đầu ngay trong 5 phút:

---

## 📖 BƯỚC 1: Chọn hướng dẫn phù hợp

### Nếu bạn chạy trên **cùng 1 máy tính**:
👉 Mở file: [`QUICK_TEST.md`](QUICK_TEST.md)

### Nếu bạn chạy trên **2 máy tính khác nhau**:
👉 Mở file: [`GUIDE_2_MACHINES.md`](GUIDE_2_MACHINES.md)

### Nếu bạn muốn **hiểu rõ cấu trúc project**:
👉 Mở file: [`PROJECT_STRUCTURE.md`](PROJECT_STRUCTURE.md)

### Nếu bạn muốn **setup chi tiết từng bước**:
👉 Mở file: [`INSTRUCTIONS.md`](INSTRUCTIONS.md)

---

## ⚡ QUICK START (30 giây)

### Terminal 1 - Server:
```bash
cd e:\python\LapTrinhMang\BaicuoikiTH

# Tạo file Excel mẫu
mvn exec:java -Dexec.mainClass="util.ExcelSampleGenerator"

# Chạy Server
mvn exec:java -Dexec.mainClass="server.ServerGUI"
```

### Terminal 2 - Client:
```bash
cd e:\python\LapTrinhMang\BaicuoikiTH

# Chạy Client
mvn exec:java -Dexec.mainClass="client.ClientGUI"
```

### Trên Server GUI:
1. File Cán bộ → `input/CANBOCOITHI.xlsx`
2. File Phòng thi → `input/PHONGTHI.xlsx`
3. Click **"Thực hiện phân công"**

### Trên Client GUI:
1. Server Host: `localhost`
2. Port: `5000`
3. Click **"Kết nối"**
4. Click **"Lấy danh sách phân công"**
5. Click **"Lấy danh sách giám sát"**

✅ **Xong!** Bạn đã chạy thành công ứng dụng.

---

## 📋 CÁC FILE QUAN TRỌNG

| File | Mô tả |
|------|-------|
| [`README.md`](README.md) | 📖 Tài liệu chi tiết |
| [`QUICK_TEST.md`](QUICK_TEST.md) | ⚡ Test nhanh (Cùng máy) |
| [`GUIDE_2_MACHINES.md`](GUIDE_2_MACHINES.md) | 💻 Hướng dẫn 2 máy tính |
| [`INSTRUCTIONS.md`](INSTRUCTIONS.md) | 📚 Hướng dẫn setup chi tiết |
| [`PROJECT_STRUCTURE.md`](PROJECT_STRUCTURE.md) | 🏗️ Cấu trúc project |
| [`COMPLETION_SUMMARY.md`](COMPLETION_SUMMARY.md) | ✅ Tóm tắt hoàn thành |
| [`setup_database.sql`](setup_database.sql) | 💾 SQL scripts PostgreSQL |
| [`pom.xml`](pom.xml) | ⚙️ Maven configuration |

---

## 🎯 ĐIỀU KIỆN TIÊN QUYẾT

Kiểm tra bạn có sẵn:
- ✅ Java JDK 11+ (`java -version`)
- ✅ Maven (`mvn -version`)
- ✅ Kết nối internet/LAN

---

## 🏆 BẠNE BƯỚC NGOÀI?

### 1️⃣ **Chạy lần đầu tiên?**
```bash
mvn clean package -DskipTests
```

### 2️⃣ **Lỗi port 5000?**
- Đảm bảo Server đang chạy
- Hoặc: Đóng ứng dụng cũ dùng port 5000

### 3️⃣ **Lỗi "File not found"?**
- Chạy `ExcelSampleGenerator` trước
- Hoặc: Tạo file Excel thủ công

### 4️⃣ **Connection refused?**
- Kiểm tra Server đang chạy
- Kiểm tra firewall cho phép port 5000

---

## 📊 GIẢI THÍCH NGẮN GỌN

### Server (Máy 1):
- Đọc file Excel
- Thực hiện logic phân công
- Lắng nghe port 5000
- Gửi dữ liệu cho client

### Client (Máy 2):
- Kết nối tới server
- Lấy dữ liệu phân công
- Hiển thị trong bảng
- Có thể lưu file Excel

### Logic Phân công:
- Mỗi phòng thi có 2 giám thị
- Giám thị lần trước không được xem phòng đó
- Cán bộ dư làm giám sát hành lang

---

## 🔧 STRUCTURE NHANH

```
Project/
├── Server Components
│   └── ExamServer.java + ServerGUI.java
├── Client Components  
│   └── ExamClient.java + ClientGUI.java
├── Model Classes
│   └── CanBo, PhongThi, etc.
├── Utilities
│   ├── ExcelReader/Writer
│   ├── AssignmentLogic
│   └── ExcelSampleGenerator
└── Documentation
    └── README, INSTRUCTIONS, etc.
```

---

## 💡 TIP & TRICKS

### Để xem log chi tiết:
- Mở Console/Terminal khi chạy
- Xem output khi nhấn button

### Để debug:
- Sửa file Java
- Chạy lại `mvn clean package`
- Chạy lại ứng dụng

### Để thay đổi port:
- Sửa `5000` trong `ExamServer.java` (dòng 11)
- Sửa port input trên `ClientGUI`

### Để dùng database:
- Chạy `setup_database.sql` trên PostgreSQL
- Sửa code Java để insert data vào DB

---

## ❓ FAQ

**Q: Có thể chạy trên Windows/Mac/Linux?**
A: Có! Java chạy trên mọi OS.

**Q: Cần bao nhiêu cán bộ?**
A: Tối thiểu 2×số phòng. Ví dụ: 5 phòng → 10 cán bộ.

**Q: Có thể có nhiều client?**
A: Có, server hỗ trợ multiple clients đồng thời.

**Q: Có lưu vào database?**
A: SQL scripts đã sẵn. Cần thêm code Java để lưu.

**Q: File Excel format nào?**
A: `.xlsx` (Excel 2007+). Không phải `.xls`.

---

## 🎓 LEARN MORE

- **TCP Sockets**: Google "Java TCP socket client server"
- **Swing GUI**: Oracle Swing Tutorial
- **Apache POI**: poi.apache.org
- **Gson**: Google JSON documentation

---

## 📞 NEXT STEPS

1. **Chọn file hướng dẫn:**
   - Cùng máy → [`QUICK_TEST.md`](QUICK_TEST.md)
   - 2 máy → [`GUIDE_2_MACHINES.md`](GUIDE_2_MACHINES.md)

2. **Đọc hướng dẫn:**
   - Làm theo từng bước

3. **Chạy ứng dụng:**
   - Mở 2 terminal/command prompt

4. **Test:**
   - Kiểm tra kết quả

5. **Mở rộng:**
   - Thêm database
   - Thêm features khác

---

## 🎉 Bạn đã sẵn sàng!

Hãy bắt đầu với file hướng dẫn phù hợp:

📱 **Cùng máy?** → [`QUICK_TEST.md`](QUICK_TEST.md)
🖥️ **2 máy?** → [`GUIDE_2_MACHINES.md`](GUIDE_2_MACHINES.md)

**Chúc bạn thành công!** 🚀

---

*Tạo: May 15, 2026*
*Version: 1.0 - Complete & Ready*
