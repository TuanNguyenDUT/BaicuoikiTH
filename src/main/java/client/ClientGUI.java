package client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import model.*;
import util.ExcelWriter;

public class ClientGUI extends JFrame {
    private ExamClient client;
    private JTextField hostField;
    private JTextField portField;
    private JTable phancongTable;
    private JTable giamsatTable;
    private JLabel statusLabel;
    private JButton connectBtn;
    private JButton saveBtn;
    private JTextField canBoFileField;
    private JTextField phongThiFileField;
    private JButton sendBtn;

    public ClientGUI(ExamClient client) {
        this.client = client;
        initUI();
    }

    private void initUI() {
        setTitle("Client - Gửi file & Xem danh sách phân công");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel - Connection
        JPanel topPanel = new JPanel(new GridLayout(4, 1, 5, 5));

        // Connection info
        JPanel connPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        connPanel.add(new JLabel("Server Host:"));
        hostField = new JTextField("localhost", 15);
        connPanel.add(hostField);
        
        connPanel.add(new JLabel("Port:"));
        portField = new JTextField("5000", 8);
        connPanel.add(portField);
        
        connectBtn = new JButton("Kết nối");
        connectBtn.addActionListener(e -> connect());
        connPanel.add(connectBtn);

        topPanel.add(connPanel);

        // File selection panel - File cán bộ
        JPanel canBoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        canBoPanel.add(new JLabel("File Cán bộ:"));
        canBoFileField = new JTextField(30);
        canBoPanel.add(canBoFileField);
        JButton browseCanBoBtn = new JButton("Chọn...");
        browseCanBoBtn.addActionListener(e -> chooseCanBoFile());
        canBoPanel.add(browseCanBoBtn);
        topPanel.add(canBoPanel);

        // File selection panel - File phòng thi
        JPanel phongThiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        phongThiPanel.add(new JLabel("File Phòng thi:"));
        phongThiFileField = new JTextField(30);
        phongThiPanel.add(phongThiFileField);
        JButton browsePhongThiBtn = new JButton("Chọn...");
        browsePhongThiBtn.addActionListener(e -> choosePhongThiFile());
        phongThiPanel.add(browsePhongThiBtn);
        topPanel.add(phongThiPanel);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        sendBtn = new JButton("🚀 Gửi & Xử lý");
        sendBtn.setEnabled(false);
        sendBtn.addActionListener(e -> sendFilesAndProcess());
        actionPanel.add(sendBtn);

        saveBtn = new JButton("💾 Lưu kết quả Excel");
        saveBtn.setEnabled(false);
        saveBtn.addActionListener(e -> saveResults());
        actionPanel.add(saveBtn);

        topPanel.add(actionPanel);

        // Status
        statusLabel = new JLabel("Chưa kết nối...");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        topPanel.add(statusLabel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Middle Panel - Phân công
        JPanel phancongPanel = new JPanel(new BorderLayout());
        phancongPanel.setBorder(BorderFactory.createTitledBorder("Danh sách phân công giám thị"));
        phancongTable = new JTable();
        phancongPanel.add(new JScrollPane(phancongTable), BorderLayout.CENTER);

        // Bottom Panel - Giám sát
        JPanel giamsatPanel = new JPanel(new BorderLayout());
        giamsatPanel.setBorder(BorderFactory.createTitledBorder("Danh sách giám sát hành lang"));
        giamsatTable = new JTable();
        giamsatPanel.add(new JScrollPane(giamsatTable), BorderLayout.CENTER);

        // Split pane for tables
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, phancongPanel, giamsatPanel);
        splitPane.setDividerLocation(400);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void connect() {
        String host = hostField.getText().trim();
        int port;

        try {
            port = Integer.parseInt(portField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Port không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        client = new ExamClient(host, port);
        if (client.connect()) {
            statusLabel.setText("✅ Đã kết nối: " + host + ":" + port);
            statusLabel.setForeground(new Color(0, 150, 0));
            connectBtn.setEnabled(false);
            hostField.setEnabled(false);
            portField.setEnabled(false);
            sendBtn.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Kết nối thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("❌ Kết nối thất bại!");
            statusLabel.setForeground(new Color(200, 0, 0));
        }
    }

    private void chooseCanBoFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            canBoFileField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void choosePhongThiFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            phongThiFileField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void sendFilesAndProcess() {
        String canBoPath = canBoFileField.getText().trim();
        String phongThiPath = phongThiFileField.getText().trim();

        if (canBoPath.isEmpty() || phongThiPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn cả hai file!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        sendBtn.setEnabled(false);

        new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> statusLabel.setText("⏳ Đang gửi file cán bộ..."));
                if (!client.sendFile(canBoPath, "CANBO")) {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("❌ Lỗi gửi file cán bộ!");
                        statusLabel.setForeground(new Color(200, 0, 0));
                        sendBtn.setEnabled(true);
                    });
                    return;
                }

                SwingUtilities.invokeLater(() -> statusLabel.setText("⏳ Đang gửi file phòng thi..."));
                if (!client.sendFile(phongThiPath, "PHONGTHI")) {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("❌ Lỗi gửi file phòng thi!");
                        statusLabel.setForeground(new Color(200, 0, 0));
                        sendBtn.setEnabled(true);
                    });
                    return;
                }

                SwingUtilities.invokeLater(() -> statusLabel.setText("⏳ Server đang xử lý phân công..."));
                if (!client.processAndGetResults()) {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("❌ Lỗi xử lý! Kiểm tra kết nối.");
                        statusLabel.setForeground(new Color(200, 0, 0));
                        sendBtn.setEnabled(true);
                    });
                    return;
                }

                int pc = client.getPhancongList().size();
                int gs = client.getGiamsatList().size();

                SwingUtilities.invokeLater(() -> {
                    displayPhancong();
                    displayGiamsat();
                    statusLabel.setText("✅ Xong! Phân công: " + pc + " | Giám sát: " + gs);
                    statusLabel.setForeground(new Color(0, 140, 0));
                    sendBtn.setEnabled(true);
                    saveBtn.setEnabled(true);
                    JOptionPane.showMessageDialog(ClientGUI.this,
                        "✅ Nhận kết quả thành công!\n\nPhân công giám thị: " + pc + " bản ghi\nGiám sát hành lang: " + gs + " bản ghi",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("❌ Lỗi: " + e.getMessage());
                    statusLabel.setForeground(new Color(200, 0, 0));
                    sendBtn.setEnabled(true);
                    JOptionPane.showMessageDialog(ClientGUI.this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }


    private void saveResults() {
        if (client.getPhancongList().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có dữ liệu để lưu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String base = "output/ketqua_" + System.currentTimeMillis();
            new java.io.File("output").mkdirs();
            ExcelWriter.writePhancong(base + "_PHANCONG.xlsx", client.getPhancongList());
            ExcelWriter.writeGiamsat(base + "_GIAMSAT.xlsx", client.getGiamsatList());
            statusLabel.setText("✅ Đã lưu: " + base + "_PHANCONG.xlsx");
            JOptionPane.showMessageDialog(this,
                "✅ Lưu thành công!\n" + base + "_PHANCONG.xlsx\n" + base + "_GIAMSAT.xlsx",
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayPhancong() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("STT");
        model.addColumn("Mã GV");
        model.addColumn("Họ và tên");
        model.addColumn("Giám thi 1");
        model.addColumn("Giám thi 2");
        model.addColumn("Phòng thi");

        for (PhancongGiamthi item : client.getPhancongList()) {
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

        for (GiamsatHanhlang item : client.getGiamsatList()) {
            model.addRow(new Object[]{
                item.getStt(),
                item.getMaCanBo(),
                item.getHoVaTen(),
                item.getPhongDuocGiamSat()
            });
        }

        giamsatTable.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExamClient client = new ExamClient("localhost", 5000);
            new ClientGUI(client);
        });
    }
}
