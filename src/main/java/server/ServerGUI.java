package server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.*;
import util.ExcelWriter;

public class ServerGUI extends JFrame {
    private ExamServer server;
    private JTable phancongTable;
    private JTable giamsatTable;
    private JLabel statusLabel;
    private JTextArea messageLog;

    public ServerGUI(ExamServer server) {
        this.server = server;
        initUI();
    }

    private void initUI() {
        setTitle("SERVER - Phân công giám thị coi thi (Chờ Client kết nối)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel - Status + Save button
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));

        statusLabel = new JLabel("⏳ Server đang lắng nghe trên port 5000 — Chờ Client kết nối...");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 13));
        statusLabel.setForeground(new Color(0, 0, 180));
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        statusPanel.setBackground(new Color(220, 235, 255));
        statusPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237)));
        statusPanel.add(statusLabel);
        topPanel.add(statusPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        JButton saveBtn = new JButton("Lưu kết quả ra file");
        saveBtn.addActionListener(e -> saveResults());
        buttonPanel.add(saveBtn);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Message Log Panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createTitledBorder("📋 Nhật ký hoạt động"));
        messageLog = new JTextArea(5, 50);
        messageLog.setEditable(false);
        messageLog.setFont(new Font("Courier New", Font.PLAIN, 11));
        messageLog.setBackground(new Color(245, 248, 245));
        JScrollPane messageScroll = new JScrollPane(messageLog);
        messagePanel.add(messageScroll, BorderLayout.CENTER);

        // Phân công table
        JPanel phancongPanel = new JPanel(new BorderLayout());
        phancongPanel.setBorder(BorderFactory.createTitledBorder("Kết quả phân công giám thị (do Client gửi lên)"));
        phancongTable = new JTable();
        phancongPanel.add(new JScrollPane(phancongTable), BorderLayout.CENTER);

        // Giám sát table
        JPanel giamsatPanel = new JPanel(new BorderLayout());
        giamsatPanel.setBorder(BorderFactory.createTitledBorder("Kết quả giám sát hành lang (do Client gửi lên)"));
        giamsatTable = new JTable();
        giamsatPanel.add(new JScrollPane(giamsatTable), BorderLayout.CENTER);

        JSplitPane tableSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, phancongPanel, giamsatPanel);
        tableSplit.setDividerLocation(280);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, messagePanel, tableSplit);
        mainSplit.setDividerLocation(140);

        mainPanel.add(mainSplit, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
        addMessage("Server khởi động — Chờ Client kết nối và gửi file Excel...");
    }

    /**
     * Thêm dòng log — thread-safe, gọi được từ bất kỳ thread nào
     */
    public void addMessage(String message) {
        String timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        String line = "[" + timestamp + "] " + message + "\n";
        if (SwingUtilities.isEventDispatchThread()) {
            messageLog.append(line);
            messageLog.setCaretPosition(messageLog.getDocument().getLength());
        } else {
            SwingUtilities.invokeLater(() -> {
                messageLog.append(line);
                messageLog.setCaretPosition(messageLog.getDocument().getLength());
            });
        }
    }

    /**
     * Cập nhật bảng kết quả sau khi server xử lý xong — gọi từ ClientHandler
     */
    public void refreshDisplay() {
        SwingUtilities.invokeLater(() -> {
            displayPhancong();
            displayGiamsat();
            int pc = server.getPhancongList().size();
            int gs = server.getGiamsatList().size();
            statusLabel.setText("✅ Đã xử lý xong: " + pc + " phân công giám thị | " + gs + " giám sát hành lang");
            statusLabel.setForeground(new Color(0, 140, 0));
            setTitle("SERVER - Đã phân công xong (" + pc + " GV)");
        });
    }

    private void displayPhancong() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("STT");
        model.addColumn("Mã GV");
        model.addColumn("Họ và tên");
        model.addColumn("Giám thi 1");
        model.addColumn("Giám thi 2");
        model.addColumn("Phòng thi");

        for (PhancongGiamthi item : server.getPhancongList()) {
            model.addRow(new Object[]{
                item.getStt(),
                item.getMaCanBo(),
                item.getHoVaTen(),
                item.getGiamthi1() != null ? item.getGiamthi1() : "",
                item.getGiamthi2() != null ? item.getGiamthi2() : "",
                item.getPhongThi()
            });
        }
        phancongTable.setModel(model);
    }

    private void displayGiamsat() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("STT");
        model.addColumn("Mã GV");
        model.addColumn("Họ và tên");
        model.addColumn("Phòng được giám sát");

        for (GiamsatHanhlang item : server.getGiamsatList()) {
            model.addRow(new Object[]{
                item.getStt(),
                item.getMaCanBo(),
                item.getHoVaTen(),
                item.getPhongDuocGiamSat()
            });
        }
        giamsatTable.setModel(model);
    }

    private void saveResults() {
        if (server.getPhancongList().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Chưa có dữ liệu!\nChờ Client gửi file Excel lên để xử lý.",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            long ts = System.currentTimeMillis();
            String base = "output/kqlan" + (ts / 1000 % 100);
            addMessage("⏳ Đang lưu file...");
            if (server.saveResults(base + "_PHANCONG.xlsx", base + "_GIAMSAT.xlsx")) {
                addMessage("✅ Đã lưu: " + base + "_PHANCONG.xlsx");
                JOptionPane.showMessageDialog(this,
                    "✅ Lưu thành công!\n" + base + "_PHANCONG.xlsx\n" + base + "_GIAMSAT.xlsx",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            addMessage("❌ Lỗi lưu: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ExamServer server = new ExamServer();
        SwingUtilities.invokeLater(() -> {
            ServerGUI gui = new ServerGUI(server);
            server.setGui(gui);
        });
        new Thread(server::startServer, "ServerThread").start();
    }
}
