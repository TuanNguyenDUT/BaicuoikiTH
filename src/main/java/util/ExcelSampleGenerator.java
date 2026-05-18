package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelSampleGenerator {

    /**
     * Tạo file Excel mẫu cho danh sách cán bộ
     */
    public static void generateCanBoSample(String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách");

        // Header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"STT", "Họ và tên", "Ngày sinh", "Mã cán bộ", "Đơn vị công tác"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Sample data
        String[][] data = {
            {"1", "Nguyễn Anh", "09/04/1961", "GV001", "ĐHBK"},
            {"2", "Nguyễn Bê", "07/09/1979", "GV002", "ĐHKT"},
            {"3", "Trần Hưng", "15/06/1985", "GV003", "ĐHSP"},
            {"4", "Lê Văn A", "22/03/1990", "GV004", "ĐHXD"},
            {"5", "Phạm Thị B", "10/12/1988", "GV005", "ĐHCN"},
            {"6", "Vũ Minh C", "05/05/1992", "GV006", "ĐHQG"},
            {"7", "Đỗ Thị D", "20/01/1987", "GV007", "ĐHNN"},
            {"8", "Bùi Văn E", "14/08/1991", "GV008", "ĐHNL"},
            {"9", "Hoàng Thị F", "30/11/1989", "GV009", "ĐHCS"},
            {"10", "Võ Văn G", "17/02/1986", "GV010", "ĐHKH"}
        };

        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < data[i].length; j++) {
                if (j == 0) {
                    row.createCell(j).setCellValue(Integer.parseInt(data[i][j]));
                } else {
                    row.createCell(j).setCellValue(data[i][j]);
                }
            }
        }

        // Auto size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream fos = new FileOutputStream(new File(filePath));
        workbook.write(fos);
        fos.close();
        workbook.close();

        System.out.println("Tạo file cán bộ mẫu: " + filePath);
    }

    /**
     * Tạo file Excel mẫu cho danh sách phòng thi
     */
    public static void generatePhongThiSample(String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách");

        // Header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"STT", "Phòng thi", "Địa điểm"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Sample data - 5 phòng thi
        String[][] data = {
            {"1", "128", "Đà Nẵng"},
            {"2", "129", "Huế"},
            {"3", "130", "Quảng Nam"},
            {"4", "131", "Quảng Bình"},
            {"5", "132", "Hà Tĩnh"}
        };

        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < data[i].length; j++) {
                if (j == 0) {
                    row.createCell(j).setCellValue(Integer.parseInt(data[i][j]));
                } else {
                    row.createCell(j).setCellValue(data[i][j]);
                }
            }
        }

        // Auto size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream fos = new FileOutputStream(new File(filePath));
        workbook.write(fos);
        fos.close();
        workbook.close();

        System.out.println("Tạo file phòng thi mẫu: " + filePath);
    }

    public static void main(String[] args) {
        try {
            String basePath = "input/";
            new File(basePath).mkdirs();
            
            generateCanBoSample(basePath + "CANBOCOITHI.xlsx");
            generatePhongThiSample(basePath + "PHONGTHI.xlsx");
            
            System.out.println("Tạo file mẫu thành công!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
