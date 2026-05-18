package util;

import model.PhancongGiamthi;
import model.GiamsatHanhlang;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {

    /**
     * Ghi danh sách phân công giám thị vào file Excel
     * @param filePath đường dẫn file
     * @param data danh sách phân công
     */
    public static void writePhancong(String filePath, List<PhancongGiamthi> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Phân công");

        // Tạo header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"STT", "Mã GV", "Họ và tên", "Giám thi 1", "Giám thi 2", "Phòng thi"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Ghi dữ liệu
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1);
            PhancongGiamthi item = data.get(i);

            row.createCell(0).setCellValue(item.getStt());
            row.createCell(1).setCellValue(item.getMaCanBo());
            row.createCell(2).setCellValue(item.getHoVaTen());
            row.createCell(3).setCellValue(item.getGiamthi1() != null ? item.getGiamthi1() : "");
            row.createCell(4).setCellValue(item.getGiamthi2() != null ? item.getGiamthi2() : "");
            row.createCell(5).setCellValue(item.getPhongThi());
        }

        // Điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream fos = new FileOutputStream(new File(filePath));
        workbook.write(fos);
        fos.close();
        workbook.close();
    }

    /**
     * Ghi danh sách giám sát hành lang vào file Excel
     * @param filePath đường dẫn file
     * @param data danh sách giám sát
     */
    public static void writeGiamsat(String filePath, List<GiamsatHanhlang> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Giám sát");

        // Tạo header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"STT", "Mã GV", "Họ và tên", "Phòng thi được giám sát"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Ghi dữ liệu
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1);
            GiamsatHanhlang item = data.get(i);

            row.createCell(0).setCellValue(item.getStt());
            row.createCell(1).setCellValue(item.getMaCanBo());
            row.createCell(2).setCellValue(item.getHoVaTen());
            row.createCell(3).setCellValue(item.getPhongDuocGiamSat());
        }

        // Điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream fos = new FileOutputStream(new File(filePath));
        workbook.write(fos);
        fos.close();
        workbook.close();
    }
}
