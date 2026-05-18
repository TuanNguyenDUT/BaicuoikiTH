import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileShareClient extends JFrame {
    private JTextField serverUrlField;
    private JTextArea statusArea;
    private JProgressBar progressBar;
    private JButton uploadButton, downloadButton, browseButton, downloadResultButton;
    private File selectedFile;
    private static final String DEFAULT_SERVER = "http://192.168.235.44:8000";

    public FileShareClient() {
        setTitle("File Share Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setResizable(true);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Server Settings"));
        
        topPanel.add(new JLabel("Server URL:"));
        serverUrlField = new JTextField(DEFAULT_SERVER, 30);
        topPanel.add(serverUrlField);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        JPanel uploadPanel = new JPanel(new BorderLayout(10, 10));
        uploadPanel.setBorder(BorderFactory.createTitledBorder("Upload File"));
        
        JPanel fileSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        browseButton = new JButton("Browse File");
        browseButton.addActionListener(e -> browseFile());
        fileSelectionPanel.add(browseButton);
        
        JLabel fileNameLabel = new JLabel("No file selected");
        fileSelectionPanel.add(fileNameLabel);
        
        uploadPanel.add(fileSelectionPanel, BorderLayout.NORTH);

        JPanel uploadButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        uploadButton = new JButton("Upload File");
        uploadButton.setFont(new Font("Arial", Font.BOLD, 12));
        uploadButton.setBackground(new Color(102, 126, 234));
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setEnabled(false);
        uploadButton.addActionListener(e -> uploadFile());
        uploadButtonPanel.add(uploadButton);

        downloadButton = new JButton("Download Uploads");
        downloadButton.addActionListener(e -> downloadUploads());
        uploadButtonPanel.add(downloadButton);

        uploadPanel.add(uploadButtonPanel, BorderLayout.CENTER);

        centerPanel.add(uploadPanel, BorderLayout.NORTH);

        JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
        resultPanel.setBorder(BorderFactory.createTitledBorder("Download Results"));
        
        JPanel resultButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        downloadResultButton = new JButton("Download Results");
        downloadResultButton.setFont(new Font("Arial", Font.BOLD, 12));
        downloadResultButton.setBackground(new Color(118, 75, 162));
        downloadResultButton.setForeground(Color.WHITE);
        downloadResultButton.addActionListener(e -> downloadResults());
        resultButtonPanel.add(downloadResultButton);
        
        resultPanel.add(resultButtonPanel, BorderLayout.NORTH);

        centerPanel.add(resultPanel, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout(10, 10));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));
        
        statusArea = new JTextArea(10, 50);
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(statusArea);
        statusPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(statusPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Progress"));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        bottomPanel.add(progressBar, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        browseButton.addActionListener(e -> {
            if (selectedFile != null) {
                fileNameLabel.setText("Selected: " + selectedFile.getName());
                uploadButton.setEnabled(true);
            }
        });
    }

    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Excel Files (*.xlsx, *.xls, *.csv)", "xlsx", "xls", "csv"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            ((JLabel) SwingUtilities.getAncestorOfClass(JPanel.class, browseButton)
                .getComponent(1)).setText("Selected: " + selectedFile.getName());
            uploadButton.setEnabled(true);
            addLog("[OK] File selected: " + selectedFile.getAbsolutePath());
        }
    }

    private void uploadFile() {
        if (selectedFile == null) {
            addLog("[ERROR] Please select a file first");
            return;
        }

        new Thread(() -> {
            try {
                addLog("[UPLOAD] Starting upload: " + selectedFile.getName());
                uploadButton.setEnabled(false);
                progressBar.setValue(0);

                String serverUrl = serverUrlField.getText().trim() + "/upload";
                URL url = new URL(serverUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                OutputStream os = conn.getOutputStream();
                
                String header = "--" + boundary + "\r\n" +
                    "Content-Disposition: form-data; name=\"file\"; filename=\"" + 
                    selectedFile.getName() + "\"\r\n" +
                    "Content-Type: application/octet-stream\r\n\r\n";
                os.write(header.getBytes());

                byte[] buffer = new byte[1024];
                try (FileInputStream fis = new FileInputStream(selectedFile)) {
                    int len;
                    long totalBytes = selectedFile.length();
                    long uploadedBytes = 0;
                    
                    while ((len = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                        uploadedBytes += len;
                        int progress = (int) ((uploadedBytes * 100) / totalBytes);
                        progressBar.setValue(progress);
                    }
                }

                String footer = "\r\n--" + boundary + "--\r\n";
                os.write(footer.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                    String line = br.readLine();
                    br.close();
                    
                    addLog("[OK] Upload successful!");
                    addLog("[INFO] Response: " + line);
                    progressBar.setValue(100);
                } else {
                    addLog("[ERROR] Upload failed: HTTP " + responseCode);
                    progressBar.setValue(0);
                }
                
                conn.disconnect();
            } catch (Exception e) {
                addLog("[ERROR] Error: " + e.getMessage());
                progressBar.setValue(0);
            } finally {
                uploadButton.setEnabled(true);
            }
        }).start();
    }

    private void downloadUploads() {
        new Thread(() -> {
            try {
                addLog("[DOWNLOAD] Loading uploads list...");
                String serverUrl = serverUrlField.getText().trim() + "/uploads/";
                downloadFromServer(serverUrl, "uploads");
            } catch (Exception e) {
                addLog("[ERROR] Error: " + e.getMessage());
            }
        }).start();
    }

    private void downloadResults() {
        new Thread(() -> {
            try {
                addLog("[DOWNLOAD] Loading results list...");
                String serverUrl = serverUrlField.getText().trim() + "/results/";
                downloadFromServer(serverUrl, "results");
            } catch (Exception e) {
                addLog("[ERROR] Error: " + e.getMessage());
            }
        }).start();
    }

    private void downloadFromServer(String folderUrl, String folderName) throws Exception {
        URL url = new URL(folderUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            addLog("[ERROR] Cannot connect to server");
            return;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        java.util.List<String> files = new java.util.ArrayList<>();

        while ((line = br.readLine()) != null) {
            if (line.contains("href=") && !line.contains("../")) {
                int start = line.indexOf("href=\"") + 6;
                int end = line.indexOf("\"", start);
                String filename = line.substring(start, end);
                if (!filename.endsWith("/")) {
                    files.add(filename);
                }
            }
        }
        br.close();
        conn.disconnect();

        if (files.isEmpty()) {
            addLog("[INFO] No files available");
            return;
        }

        addLog("[INFO] Found " + files.size() + " files:");
        for (String file : files) {
            addLog("      - " + file);
        }

        String[] fileArray = files.toArray(new String[0]);
        String selectedFile = (String) JOptionPane.showInputDialog(
            this,
            "Select file to download:",
            "Download File",
            JOptionPane.QUESTION_MESSAGE,
            null,
            fileArray,
            fileArray[0]
        );

        if (selectedFile != null) {
            downloadFile(folderUrl + selectedFile, selectedFile);
        }
    }

    private void downloadFile(String fileUrl, String fileName) throws Exception {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(fileName));
        
        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File saveFile = fileChooser.getSelectedFile();
        addLog("[DOWNLOAD] Downloading: " + fileName + " ...");

        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (InputStream is = conn.getInputStream();
             FileOutputStream fos = new FileOutputStream(saveFile)) {
            
            byte[] buffer = new byte[1024];
            int len;
            long downloadedBytes = 0;
            long totalBytes = conn.getContentLength();

            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                downloadedBytes += len;
                if (totalBytes > 0) {
                    int progress = (int) ((downloadedBytes * 100) / totalBytes);
                    progressBar.setValue(progress);
                }
            }

            addLog("[OK] Download successful: " + saveFile.getAbsolutePath());
            progressBar.setValue(100);
        }
        
        conn.disconnect();
    }

    private void addLog(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            statusArea.append("[" + timestamp + "] " + message + "\n");
            statusArea.setCaretPosition(statusArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileShareClient frame = new FileShareClient();
            frame.setVisible(true);
        });
    }
}
