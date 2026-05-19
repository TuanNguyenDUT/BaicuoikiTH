package client;

import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;

public class ExamClient {
    private String serverHost;
    private int serverPort;
    private Socket socket;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private Gson gson = new Gson();

    private List<CanBo> canBoList;
    private List<PhongThi> phongThiList;
    private List<PhancongGiamthi> phancongList;
    private List<GiamsatHanhlang> giamsatList;

    public ExamClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.canBoList = new ArrayList<>();
        this.phongThiList = new ArrayList<>();
        this.phancongList = new ArrayList<>();
        this.giamsatList = new ArrayList<>();
    }

    /**
     * Kết nối đến server
     */
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

    public boolean connect() {
        try {
            socket = new Socket(serverHost, serverPort);
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
            
            System.out.println("Kết nối đến server tại " + serverHost + ":" + serverPort);
            return true;
        } catch (IOException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gửi file Excel cho server
     */
    public boolean sendFile(String filePath, String fileType) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Lỗi: File không tồn tại: " + filePath);
                return false;
            }

            // Gửi lệnh upload
            writeString("UPLOAD_FILE:" + fileType);
            // Gửi tên file
            writeString(file.getName());
            // Gửi kích thước file
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

            // Đợi xác nhận từ server
            String ack = readString();
            if ("OK".equals(ack)) {
                System.out.println("✓ Đã gửi file: " + file.getName());
                return true;
            } else {
                System.out.println("Lỗi: " + ack);
                return false;
            }

        } catch (IOException e) {
            System.out.println("Lỗi gửi file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Yêu cầu server lấy n phòng đầu + m cán bộ đầu từ file đã upload, rồi phân công
     */
    public boolean processFileWithNM(int n, int m) {
        try {
            writeString("PROCESS_FILE_WITH_NM:" + n + ":" + m);
            dataOut.flush();
            return receiveResults();
        } catch (IOException e) {
            System.out.println("Lỗi processFileWithNM: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gửi n (phòng thi) và m (giám thị) để server tạo lịch phân công
     */
    public boolean processWithParams(int n, int m) {
        try {
            writeString("PROCESS_WITH_PARAMS:" + n + ":" + m);
            dataOut.flush();
            return receiveResults();
        } catch (IOException e) {
            System.out.println("Lỗi gửi tham số: " + e.getMessage());
            return false;
        }
    }

    /**
     * Đọc kết quả trả về từ server (dùng chung cho cả hai flow)
     */
    private boolean receiveResults() throws IOException {
        phancongList = new ArrayList<>();
        giamsatList = new ArrayList<>();
        boolean success = false;
        String line;
        while (true) {
            try {
                line = readString();
            } catch (EOFException e) {
                break;
            }
            System.out.println("Nhận: " + line.substring(0, Math.min(50, line.length())));
            if (line.startsWith("PHANCONG:")) {
                String json = line.substring(9);
                phancongList = gson.fromJson(json, new com.google.gson.reflect.TypeToken<List<PhancongGiamthi>>(){}.getType());
                System.out.println("✓ Nhận " + phancongList.size() + " bản ghi phân công");
            } else if (line.startsWith("GIAMSAT:")) {
                String json = line.substring(8);
                giamsatList = gson.fromJson(json, new com.google.gson.reflect.TypeToken<List<GiamsatHanhlang>>(){}.getType());
                System.out.println("✓ Nhận " + giamsatList.size() + " bản ghi giám sát");
            } else if (line.equals("DONE")) {
                success = true;
                System.out.println("✓ Xử lý hoàn thành!");
                break;
            } else if (line.startsWith("ERROR:")) {
                System.out.println("✗ Lỗi từ server: " + line.substring(6));
                break;
            }
        }
        return success;
    }

    /**
     * Yêu cầu server xử lý và gửi kết quả
     */
    public boolean processAndGetResults() {
        try {
            writeString("PROCESS_AND_SEND_RESULTS");
            dataOut.flush();

            String line;
            boolean success = false;

            // Đọc kết quả từ server
            while (true) {
                try {
                    line = readString();
                } catch (EOFException e) {
                    break;
                }
                System.out.println("Nhận: " + line.substring(0, Math.min(50, line.length())));

                if (line.startsWith("PHANCONG:")) {
                    String json = line.substring(9);
                    phancongList = gson.fromJson(json, new TypeToken<List<PhancongGiamthi>>(){}.getType());
                    System.out.println("✓ Nhận " + phancongList.size() + " bản ghi phân công");

                } else if (line.startsWith("GIAMSAT:")) {
                    String json = line.substring(8);
                    giamsatList = gson.fromJson(json, new TypeToken<List<GiamsatHanhlang>>(){}.getType());
                    System.out.println("✓ Nhận " + giamsatList.size() + " bản ghi giám sát");

                } else if (line.equals("DONE")) {
                    success = true;
                    System.out.println("✓ Xử lý hoàn thành!");
                    break;

                } else if (line.startsWith("ERROR:")) {
                    String error = line.substring(6);
                    System.out.println("✗ Lỗi từ server: " + error);
                    break;
                }
            }

            return success;

        } catch (IOException e) {
            System.out.println("Lỗi xử lý: " + e.getMessage());
            return false;
        }
    }

    /**
     * Nhận file kết quả từ server
     */
    private void receiveResultFile(String fileName, long fileSize) {
        try {
            String outputPath = "output" + File.separator + fileName;
            File outputDir = new File("output");
            if (!outputDir.exists()) {
                outputDir.mkdir();
            }

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                byte[] buffer = new byte[4096];
                long totalRead = 0;
                int bytesRead;

                while (totalRead < fileSize && (bytesRead = dataIn.read(buffer, 0,
                    (int) Math.min(buffer.length, fileSize - totalRead))) != -1) {
                    fos.write(buffer, 0, bytesRead);
                    totalRead += bytesRead;
                }
            }

            System.out.println("✓ Đã lưu file: " + outputPath);

        } catch (IOException e) {
            System.out.println("Lỗi nhận file: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách phân công từ server
     */
    public boolean getPhancong() {
        try {
            writeString("GET_PHANCONG");
            dataOut.flush();
            String response = readString();
            
            if (response != null && !response.isEmpty()) {
                phancongList = gson.fromJson(response, new TypeToken<List<PhancongGiamthi>>(){}.getType());
                System.out.println("Đã nhận " + phancongList.size() + " bản ghi phân công");
                return true;
            }
        } catch (IOException e) {
            System.out.println("Lỗi lấy dữ liệu: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy danh sách giám sát từ server
     */
    public boolean getGiamsat() {
        try {
            writeString("GET_GIAMSAT");
            dataOut.flush();
            String response = readString();
            
            if (response != null && !response.isEmpty()) {
                giamsatList = gson.fromJson(response, new TypeToken<List<GiamsatHanhlang>>(){}.getType());
                System.out.println("Đã nhận " + giamsatList.size() + " bản ghi giám sát");
                return true;
            }
        } catch (IOException e) {
            System.out.println("Lỗi lấy dữ liệu: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy danh sách cán bộ từ server
     */
    public boolean getCanBo() {
        try {
            writeString("GET_CANBO");
            dataOut.flush();
            String response = readString();
            
            if (response != null && !response.isEmpty()) {
                canBoList = gson.fromJson(response, new TypeToken<List<CanBo>>(){}.getType());
                return true;
            }
        } catch (IOException e) {
            System.out.println("Lỗi lấy dữ liệu: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy danh sách phòng thi từ server
     */
    public boolean getPhongThi() {
        try {
            writeString("GET_PHONGTHI");
            dataOut.flush();
            String response = readString();
            
            if (response != null && !response.isEmpty()) {
                phongThiList = gson.fromJson(response, new TypeToken<List<PhongThi>>(){}.getType());
                return true;
            }
        } catch (IOException e) {
            System.out.println("Lỗi lấy dữ liệu: " + e.getMessage());
        }
        return false;
    }

    /**
     * Đóng kết nối
     */
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters
    public List<CanBo> getCanBoList() {
        return canBoList;
    }

    public List<PhongThi> getPhongThiList() {
        return phongThiList;
    }

    public List<PhancongGiamthi> getPhancongList() {
        return phancongList;
    }

    public List<GiamsatHanhlang> getGiamsatList() {
        return giamsatList;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String host) {
        this.serverHost = host;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int port) {
        this.serverPort = port;
    }

    public static void main(String[] args) {
        ExamClient client = new ExamClient("localhost", 5000);
        new ClientGUI(client);
    }
}
