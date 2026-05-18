package util;

import model.CanBo;
import model.PhongThi;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    /**
     * Lấy giá trị cell, xử lý tất cả loại dữ liệu
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long)cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private static double getCellValueAsDouble(Cell cell) {
        if (cell == null) return 0;
        
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return cell.getNumericCellValue();
                case STRING:
                    return Double.parseDouble(cell.getStringCellValue().trim());
                default:
                    return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Đọc danh sách cán bộ từ file Excel
     * @param filePath đường dẫn file
     * @return danh sách cán bộ
     */
    public static List<CanBo> readCanBo(String filePath) throws IOException {
        List<CanBo> list = new ArrayList<>();
        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int successCount = 0;
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            try {
                // Cột: TT, Mã GV, Họ Tên, Ngày sinh, Đơn vị công tác
                int stt = (int) getCellValueAsDouble(row.getCell(0));
                String maCanBo = getCellValueAsString(row.getCell(1));
                String hoVaTen = getCellValueAsString(row.getCell(2));
                String ngaySinh = getCellValueAsString(row.getCell(3));
                String donViCongTac = getCellValueAsString(row.getCell(4));

                if (maCanBo.isEmpty() || hoVaTen.isEmpty()) {
                    continue;
                }

                CanBo canBo = new CanBo(stt, maCanBo, hoVaTen, ngaySinh, donViCongTac);
                list.add(canBo);
                successCount++;
            } catch (Exception e) {
                System.err.println("Lỗi đọc dòng " + (i+1) + ": " + e.getMessage());
            }
        }

        workbook.close();
        fis.close();
        System.out.println("Đã đọc " + successCount + " cán bộ từ " + filePath);
        return list;
    }

    /**
     * Đọc danh sách phòng thi từ file Excel
     * @param filePath đường dẫn file
     * @return danh sách phòng thi
     */
    public static List<PhongThi> readPhongThi(String filePath) throws IOException {
        List<PhongThi> list = new ArrayList<>();
        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int successCount = 0;
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            try {
                // Cột: STT, Phòng thi, Ghi chú
                int stt = (int) getCellValueAsDouble(row.getCell(0));
                String maPhong = getCellValueAsString(row.getCell(1));
                String diaDiem = getCellValueAsString(row.getCell(2));

                if (maPhong.isEmpty()) {
                    continue;
                }

                PhongThi phongThi = new PhongThi(stt, maPhong, diaDiem);
                list.add(phongThi);
                successCount++;
            } catch (Exception e) {
                System.err.println("Lỗi đọc dòng " + (i+1) + ": " + e.getMessage());
            }
        }

        workbook.close();
        fis.close();
        System.out.println("Đã đọc " + successCount + " phòng thi từ " + filePath);
        return list;
    }
}
