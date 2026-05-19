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
    private JTextField nField;
    private JTextField mField;
    private JLabel totalNLabel;
    private JLabel totalMLabel;
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

        // Top Panel
        JPanel topPanel = new JPanel(new GridLayout(5, 1, 4, 4));

        // 1. Connection info
        JPanel connPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
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

        // 2. File cán bộ + nhập m
        JPanel canBoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        canBoPanel.setBorder(BorderFactory.createTitledBorder("File danh sách cán bộ côi thi"));
        canBoFileField = new JTextField(28);
        canBoFileField.setToolTipText("danhsachcanbocoithi.xlsx");
        canBoPanel.add(canBoFileField);
        JButton browseCanBoBtn = new JButton("Chọn...");
        browseCanBoBtn.addActionListener(e -> chooseFile(canBoFileField, true));
        canBoPanel.add(browseCanBoBtn);
        totalMLabel = new JLabel("(chưa chọn)");
        totalMLabel.setForeground(Color.GRAY);
        canBoPanel.add(totalMLabel);
        canBoPanel.add(new JLabel("  Số cán bộ m:"));
        mField = new JTextField("0", 5);
        mField.setFont(new Font("Arial", Font.BOLD, 13));
        canBoPanel.add(mField);
        topPanel.add(canBoPanel);

        // 3. File phòng thi + nhập n
        JPanel phongThiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        phongThiPanel.setBorder(BorderFactory.createTitledBorder("File danh sách phòng thi"));
        phongThiFileField = new JTextField(28);
        phongThiFileField.setToolTipText("danhsachphongthi.xlsx");
        phongThiPanel.add(phongThiFileField);
        JButton browsePhongThiBtn = new JButton("Chọn...");
        browsePhongThiBtn.addActionListener(e -> chooseFile(phongThiFileField, false));
        phongThiPanel.add(browsePhongThiBtn);
        totalNLabel = new JLabel("(chưa chọn)");
        totalNLabel.setForeground(Color.GRAY);
        phongThiPanel.add(totalNLabel);
        phongThiPanel.add(new JLabel("  Số phòng n:"));
        nField = new JTextField("0", 5);
        nField.setFont(new Font("Arial", Font.BOLD, 13));
        phongThiPanel.add(nField);
        topPanel.add(phongThiPanel);

        // 4. Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 4));
        sendBtn = new JButton("🚀 Tạo lịch phân công");
        sendBtn.setEnabled(false);
        sendBtn.addActionListener(e -> sendParamsAndProcess());
        actionPanel.add(sendBtn);
        saveBtn = new JButton("💾 Lưu kết quả Excel");
        saveBtn.setEnabled(false);
        saveBtn.addActionListener(e -> saveResults());
        actionPanel.add(saveBtn);
        topPanel.add(actionPanel);

        // 5. Status
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

    private void chooseFile(JTextField targetField, boolean isCanBo) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel files", "xlsx", "xls"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        String path = chooser.getSelectedFile().getAbsolutePath();
        targetField.setText(path);
        if (isCanBo) totalMLabel.setText("⏳ đang đọc...");
        else         totalNLabel.setText("⏳ đang đọc...");

        new Thread(() -> {
            try {
                int count = isCanBo
                    ? util.ExcelReader.readCanBo(path).size()
                    : util.ExcelReader.readPhongThi(path).size();
                SwingUtilities.invokeLater(() -> {
                    if (isCanBo) {
                        totalMLabel.setText("Tổng: " + count + " cán bộ");
                        totalMLabel.setForeground(new Color(0, 120, 0));
                        mField.setText(String.valueOf(count));
                    } else {
                        totalNLabel.setText("Tổng: " + count + " phòng");
                        totalNLabel.setForeground(new Color(0, 120, 0));
                        nField.setText(String.valueOf(count));
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    if (isCanBo) totalMLabel.setText("❌ Lỗi");
                    else         totalNLabel.setText("❌ Lỗi");
                });
            }
        }).start();
    }

    private void sendParamsAndProcess() {
        String canBoPath    = canBoFileField.getText().trim();
        String phongThiPath = phongThiFileField.getText().trim();

        if (canBoPath.isEmpty() || phongThiPath.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn cả hai file Excel!",
                "Thiếu file", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int n, m;
        try {
            n = Integer.parseInt(nField.getText().trim());
            m = Integer.parseInt(mField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "n và m phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (n <= 0 || m <= 0) {
            JOptionPane.showMessageDialog(this, "n và m phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (m < 2 * n) {
            JOptionPane.showMessageDialog(this,
                "Số cán bộ m=" + m + " không đủ!\nCần ít nhất 2×n = " + (2 * n) + " cán bộ.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final int finalN = n, finalM = m;
        sendBtn.setEnabled(false);

        new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> statusLabel.setText("⏳ Đang gửi file cán bộ..."));
                if (!client.sendFile(canBoPath, "CANBO")) {
                    showError("❌ Lỗi gửi file cán bộ!"); return;
                }

                SwingUtilities.invokeLater(() -> statusLabel.setText("⏳ Đang gửi file phòng thi..."));
                if (!client.sendFile(phongThiPath, "PHONGTHI")) {
                    showError("❌ Lỗi gửi file phòng thi!"); return;
                }

                SwingUtilities.invokeLater(() -> statusLabel.setText("⏳ Server đang phân công n=" + finalN + ", m=" + finalM + "..."));
                if (!client.processFileWithNM(finalN, finalM)) {
                    showError("❌ Lỗi xử lý!"); return;
                }

                int pc = client.getPhancongList().size();
                int gs = client.getGiamsatList().size();
                SwingUtilities.invokeLater(() -> {
                    displayPhancong();
                    displayGiamsat();
                    statusLabel.setText("✅ Xong! n=" + finalN + " phòng | m=" + finalM + " cán bộ | Phân công: " + pc + " | Giám sát: " + gs);
                    statusLabel.setForeground(new Color(0, 140, 0));
                    sendBtn.setEnabled(true);
                    saveBtn.setEnabled(true);
                    JOptionPane.showMessageDialog(ClientGUI.this,
                        "✅ Phân công thành công!\n"
                        + "n = " + finalN + " phòng thi\n"
                        + "m = " + finalM + " cán bộ\n\n"
                        + "Phân công giám thị: " + pc + " bản ghi\n"
                        + "Giám sát hành lang: " + gs + " bản ghi",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception e) {
                showError("❌ Lỗi: " + e.getMessage());
                JOptionPane.showMessageDialog(ClientGUI.this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    private void showError(String msg) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(msg);
            statusLabel.setForeground(new Color(200, 0, 0));
            sendBtn.setEnabled(true);
        });
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
