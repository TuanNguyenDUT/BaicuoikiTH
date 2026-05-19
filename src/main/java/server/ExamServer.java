package server;

import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.AssignmentRepository;
import model.*;
import util.*;

public class ExamServer {
    private static final int PORT = 5000;
    private List<CanBo> allCanBo;
    private List<PhongThi> allPhongThi;
    private List<PhancongGiamthi> phancongList;
    private List<GiamsatHanhlang> giamsatList;
    private String tempCanBoPath;
    private String tempPhongThiPath;
    private volatile ServerGUI gui;

    public ExamServer() {
        this.allCanBo = new ArrayList<>();
        this.allPhongThi = new ArrayList<>();
        this.phancongList = new ArrayList<>();
        this.giamsatList = new ArrayList<>();
    }

    /**
     * Bắt đầu server
     */
    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server bắt đầu lắng nghe trên cổng " + PORT);
            if (gui != null) gui.addMessage("✅ Server lắng nghe trên port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientIP = clientSocket.getInetAddress().getHostAddress();
                System.out.println("Client kết nối: " + clientIP);
                if (gui != null) gui.addMessage("🔌 Client kết nối từ: " + clientIP);

                new ClientHandler(clientSocket, this).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Đọc dữ liệu từ file Excel
     */
    public boolean loadExcelData(String canBoPath, String phongThiPath) {
        try {
            allCanBo = ExcelReader.readCanBo(canBoPath);
            allPhongThi = ExcelReader.readPhongThi(phongThiPath);
            
            System.out.println("Đã đọc " + allCanBo.size() + " cán bộ");
            System.out.println("Đã đọc " + allPhongThi.size() + " phòng thi");
            
            return true;
        } catch (IOException e) {
            System.out.println("Lỗi đọc file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sinh danh sách phòng thi tự động theo n
     */
    private List<PhongThi> generatePhongThi(int n) {
        List<PhongThi> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(new PhongThi(i, String.format("P%03d", i), "Toà A"));
        }
        return list;
    }

    /**
     * Sinh danh sách cán bộ tự động theo m
     */
    private List<CanBo> generateCanBo(int m) {
        List<CanBo> list = new ArrayList<>();
        for (int i = 1; i <= m; i++) {
            list.add(new CanBo(i, String.format("CB%03d", i), "Cán bộ " + i, "", ""));
        }
        return list;
    }

    /**
     * Phân công theo n phòng thi và m giám thị (dữ liệu sinh tự động)
     */
    public boolean performAssignment(int n, int m) {
        try {
            this.allPhongThi = generatePhongThi(n);
            this.allCanBo    = generateCanBo(m);

            AssignmentLogic logic = new AssignmentLogic(allCanBo, allPhongThi);
            AssignmentRepository.initTable();
            int Lmax = logic.tinhLmax();
            int line = AssignmentRepository.getAndIncrementLine(Lmax);

            System.out.println("n=" + n + ", m=" + m + ", Lmax=" + Lmax + ", line=" + line);
            if (gui != null) gui.addMessage("📐 n=" + n + " phòng | m=" + m + " GV | Lmax=" + Lmax + " | line=" + line);

            logic.doAssignment(line);
            this.phancongList = logic.getPhancongList();
            this.giamsatList  = logic.getGiamsatList();

            System.out.println("Phân công hoàn thành!");
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi phân công: " + e.getMessage());
            return false;
        }
    }

    /**
     * Phân công dùng n phòng đầu + m cán bộ đầu từ file đã load
     */
    public boolean performAssignmentFileWithNM(int n, int m) {
        try {
            if (allPhongThi == null || allCanBo == null
                    || allPhongThi.isEmpty() || allCanBo.isEmpty()) {
                throw new RuntimeException("Chưa có dữ liệu file. Hãy gửi file trước!");
            }
            int useN = Math.min(n, allPhongThi.size());
            int useM = Math.min(m, allCanBo.size());

            List<PhongThi> subPhong = new ArrayList<>(allPhongThi.subList(0, useN));
            List<CanBo>    subCanBo = new ArrayList<>(allCanBo.subList(0, useM));

            AssignmentLogic logic = new AssignmentLogic(subCanBo, subPhong);
            AssignmentRepository.initTable();
            int Lmax = logic.tinhLmax();
            int line  = AssignmentRepository.getAndIncrementLine(Lmax);

            System.out.println("n=" + useN + ", m=" + useM + ", Lmax=" + Lmax + ", line=" + line);
            if (gui != null) gui.addMessage("📐 n=" + useN + " phòng | m=" + useM + " GV | Lmax=" + Lmax + " | line=" + line);

            logic.doAssignment(line);
            this.phancongList = logic.getPhancongList();
            this.giamsatList  = logic.getGiamsatList();

            System.out.println("Phân công hoàn thành!");
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi phân công: " + e.getMessage());
            return false;
        }
    }

    /**
     * Thực hiện phân công
     */
    public boolean performAssignment() {
        try {
            AssignmentLogic logic = new AssignmentLogic(allCanBo, allPhongThi);

            // Khởi tạo bảng DB (nếu chưa có) và lấy line cho ca này
            AssignmentRepository.initTable();
            int Lmax = logic.tinhLmax();
            int line = AssignmentRepository.getAndIncrementLine(Lmax);

            System.out.println("Lmax = " + Lmax + ", sử dụng line = " + line);
            if (gui != null) gui.addMessage("📐 Lmax=" + Lmax + " | Ca này dùng line=" + line);

            logic.doAssignment(line);

            this.phancongList = logic.getPhancongList();
            this.giamsatList = logic.getGiamsatList();

            System.out.println("Phân công hoàn thành!");
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi phân công: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ghi kết quả vào file Excel
     */
    public boolean saveResults(String phancongPath, String giamsatPath) {
        try {
            ExcelWriter.writePhancong(phancongPath, phancongList);
            ExcelWriter.writeGiamsat(giamsatPath, giamsatList);
            
            System.out.println("Đã lưu file phân công");
            return true;
        } catch (IOException e) {
            System.out.println("Lỗi lưu file: " + e.getMessage());
            return false;
        }
    }

    // Getters
    public List<CanBo> getAllCanBo() {
        return allCanBo;
    }

    public List<PhongThi> getAllPhongThi() {
        return allPhongThi;
    }

    public List<PhancongGiamthi> getPhancongList() {
        return phancongList;
    }

    public List<GiamsatHanhlang> getGiamsatList() {
        return giamsatList;
    }

    public String getTempCanBoPath() {
        return tempCanBoPath;
    }

    public void setTempCanBoPath(String path) {
        this.tempCanBoPath = path;
    }

    public String getTempPhongThiPath() {
        return tempPhongThiPath;
    }

    public void setTempPhongThiPath(String path) {
        this.tempPhongThiPath = path;
    }

    public void setGui(ServerGUI gui) {
        this.gui = gui;
    }

    public ServerGUI getGui() {
        return gui;
    }

    public static void main(String[] args) {
        ExamServer server = new ExamServer();
        // Mở GUI server
        new ServerGUI(server);
        server.startServer();
    }
}

/**
 * Handler cho mỗi client kết nối
 */
class ClientHandler extends Thread {
    private Socket socket;
    private ExamServer server;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private Gson gson = new Gson();
    private static final String TEMP_DIR = "server_temp";

    public ClientHandler(Socket socket, ExamServer server) {
        this.socket = socket;
        this.server = server;
    }

    private void writeString(String s) throws IOException {
        byte[] b = s.getBytes("UTF-8");
        dataOut.writeInt(b.length);
        dataOut.write(b);
    }

    private String readString() throws IOException {
        int len = dataIn.readInt();
        byte[] b = new byte[len];
        dataIn.readFully(b);
        return new String(b, "UTF-8");
    }

    @Override
    public void run() {
        try {
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());

            // Tạo thư mục tạm
            File tempDir = new File(TEMP_DIR);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            while (true) {
                String request;
                try {
                    request = readString();
                } catch (EOFException e) {
                    break;
                }
                System.out.println("Nhận yêu cầu: " + request);

                if (request.startsWith("PROCESS_FILE_WITH_NM:")) {
                    String[] parts = request.split(":");
                    int n = Integer.parseInt(parts[1]);
                    int m = Integer.parseInt(parts[2]);
                    processFileWithNM(n, m);
                } else if (request.startsWith("PROCESS_WITH_PARAMS:")) {
                    String[] parts = request.split(":");
                    int n = Integer.parseInt(parts[1]);
                    int m = Integer.parseInt(parts[2]);
                    processWithParams(n, m);
                } else if (request.equals("GET_PHANCONG")) {
                    sendPhancong();
                } else if (request.equals("GET_GIAMSAT")) {
                    sendGiamsat();
                } else if (request.equals("GET_CANBO")) {
                    sendCanBo();
                } else if (request.equals("GET_PHONGTHI")) {
                    sendPhongThi();
                } else if (request.startsWith("UPLOAD_FILE:")) {
                    String[] parts = request.split(":", 2);
                    if (parts.length >= 2) {
                        String fileType = parts[1]; // CANBO hoặc PHONGTHI
                        receiveFile(fileType);
                    }
                } else if (request.equals("PROCESS_AND_SEND_RESULTS")) {
                    processAndSendResults();
                }
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Client ngắt kết nối: " + e.getMessage());
        }
    }

    /**
     * Nhận file từ client
     */
    private void receiveFile(String fileType) {
        try {
            // Đọc tên file
            String fileName = readString();
            // Đọc kích thước file
            long fileSize = dataIn.readLong();
            
            System.out.println("Nhận file: " + fileName + " (" + fileSize + " bytes)");
            
            // Tạo đường dẫn lưu
            String savePath = TEMP_DIR + File.separator + fileName;
            File outputFile = new File(savePath);
            
            // Nhận file từ client
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[4096];
                long totalRead = 0;
                int bytesRead;
                
                while (totalRead < fileSize) {
                    int toRead = (int) Math.min(buffer.length, fileSize - totalRead);
                    bytesRead = dataIn.read(buffer, 0, toRead);
                    if (bytesRead == -1) break;
                    fos.write(buffer, 0, bytesRead);
                    totalRead += bytesRead;
                }
            }
            
            // Lưu đường dẫn tạm
            if (fileType.equals("CANBO")) {
                server.setTempCanBoPath(savePath);
            } else if (fileType.equals("PHONGTHI")) {
                server.setTempPhongThiPath(savePath);
            }
            
            System.out.println("Đã nhận file: " + savePath);
            if (server.getGui() != null)
                server.getGui().addMessage("📥 Nhận file " + fileType + ": " + fileName + " (" + fileSize + " bytes)");

            // Gửi xác nhận
            writeString("OK");
            dataOut.flush();
        } catch (IOException e) {
            System.out.println("Lỗi nhận file: " + e.getMessage());
            try {
                writeString("ERROR:" + e.getMessage());
                dataOut.flush();
            } catch (IOException ex) { /* ignore */ }
        }
    }

    /**
     * Lấy n phòng đầu + m cán bộ đầu từ file đã load, phân công, gửi kết quả
     */
    private void processFileWithNM(int n, int m) {
        try {
            if (server.getGui() != null)
                server.getGui().addMessage("⏳ Phân công từ file: n=" + n + " phòng, m=" + m + " cán bộ");

            String canBoPath    = server.getTempCanBoPath();
            String phongThiPath = server.getTempPhongThiPath();
            if (canBoPath == null || phongThiPath == null
                    || !server.loadExcelData(canBoPath, phongThiPath)) {
                writeString("ERROR:Chưa nhận đủ file Excel từ client");
                dataOut.flush();
                return;
            }

            if (!server.performAssignmentFileWithNM(n, m)) {
                writeString("ERROR:Lỗi phân công");
                dataOut.flush();
                return;
            }

            writeString("PHANCONG:" + gson.toJson(server.getPhancongList()));
            writeString("GIAMSAT:"  + gson.toJson(server.getGiamsatList()));
            writeString("DONE");
            dataOut.flush();

            if (server.getGui() != null) server.getGui().refreshDisplay();
        } catch (Exception e) {
            System.out.println("Lỗi processFileWithNM: " + e.getMessage());
            try { writeString("ERROR:" + e.getMessage()); dataOut.flush(); } catch (IOException ex) { /* ignore */ }
        }
    }

    /**
     * Xử lý với tham số n, m và gửi kết quả
     */
    private void processWithParams(int n, int m) {
        try {
            System.out.println("Đang phân công với n=" + n + ", m=" + m);
            if (server.getGui() != null)
                server.getGui().addMessage("⏳ Client gửi n=" + n + " phòng, m=" + m + " giám thị");

            if (!server.performAssignment(n, m)) {
                writeString("ERROR:Lỗi phân công");
                dataOut.flush();
                return;
            }

            writeString("PHANCONG:" + gson.toJson(server.getPhancongList()));
            writeString("GIAMSAT:"  + gson.toJson(server.getGiamsatList()));
            writeString("DONE");
            dataOut.flush();

            if (server.getGui() != null) server.getGui().refreshDisplay();
        } catch (Exception e) {
            System.out.println("Lỗi processWithParams: " + e.getMessage());
            try { writeString("ERROR:" + e.getMessage()); dataOut.flush(); } catch (IOException ex) { /* ignore */ }
        }
    }

    /**
     * Xử lý file và gửi kết quả trở lại cho client
     */
    private void processAndSendResults() {
        try {
            String canBoPath = server.getTempCanBoPath();
            String phongThiPath = server.getTempPhongThiPath();
            
            if (canBoPath == null || phongThiPath == null) {
                writeString("ERROR:Chưa nhận đủ file");
                dataOut.flush();
                return;
            }
            
            System.out.println("Đang xử lý...");
            if (server.getGui() != null) server.getGui().addMessage("⏳ Đang đọc file và phân công...");
            
            // Load dữ liệu từ file tạm
            if (!server.loadExcelData(canBoPath, phongThiPath)) {
                writeString("ERROR:Lỗi đọc file Excel");
                dataOut.flush();
                return;
            }
            
            // Thực hiện phân công
            if (!server.performAssignment()) {
                writeString("ERROR:Lỗi phân công");
                dataOut.flush();
                return;
            }
            
            System.out.println("Xử lý xong, gửi kết quả...");
            if (server.getGui() != null) server.getGui().addMessage("⚙️ Phân công xong, đang gửi kết quả về Client...");
            
            // Gửi kết quả phân công
            String phancongJson = gson.toJson(server.getPhancongList());
            writeString("PHANCONG:" + phancongJson);
            
            // Gửi kết quả giám sát
            String giamsatJson = gson.toJson(server.getGiamsatList());
            writeString("GIAMSAT:" + giamsatJson);
            
            writeString("DONE");
            dataOut.flush();
            if (server.getGui() != null) server.getGui().refreshDisplay();
            
        } catch (Exception e) {
            System.out.println("Lỗi xử lý: " + e.getMessage());
            try {
                writeString("ERROR:" + e.getMessage());
                dataOut.flush();
            } catch (IOException ex) { /* ignore */ }
        }
    }

    /**
     * Gửi file Excel kết quả cho client
     */
    private void sendResultFile(String type) {
        try {
            // Tạo thư mục output nếu chưa có
            File outputDir = new File("output");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            
            String filePath = "output/" + type + ".xlsx";
            
            // Ghi file kết quả
            if (type.equals("phancong")) {
                ExcelWriter.writePhancong(filePath, server.getPhancongList());
            } else {
                ExcelWriter.writeGiamsat(filePath, server.getGiamsatList());
            }
            
            File file = new File(filePath);
            
            // Gửi thông tin file
            writeString("FILE:" + file.getName());
            dataOut.writeLong(file.length());
            dataOut.flush();
            
            // Gửi nội dung file
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dataOut.write(buffer, 0, bytesRead);
                }
                dataOut.flush();
            }
            
            System.out.println("Đã gửi file: " + filePath);
        } catch (Exception e) {
            System.out.println("Lỗi gửi file: " + e.getMessage());
        }
    }

    private void sendPhancong() {
        try {
            String json = gson.toJson(server.getPhancongList());
            writeString(json);
            dataOut.flush();
            System.out.println("Đã gửi danh sách phân công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendGiamsat() {
        try {
            String json = gson.toJson(server.getGiamsatList());
            writeString(json);
            dataOut.flush();
            System.out.println("Đã gửi danh sách giám sát");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCanBo() {
        try {
            String json = gson.toJson(server.getAllCanBo());
            writeString(json);
            dataOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPhongThi() {
        try {
            String json = gson.toJson(server.getAllPhongThi());
            writeString(json);
            dataOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
