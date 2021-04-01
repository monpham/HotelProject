package com.nhat.demo.config;

import com.nhat.demo.entity.Booking;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReportBookingExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Booking> bookings;

    public ReportBookingExcelExporter(List<Booking> bookings) {
        this.bookings = bookings;
        workbook = new XSSFWorkbook();
    }


    private void writeHeaderLine() {
        CreationHelper createHelper = workbook.getCreationHelper();
        sheet = workbook.createSheet("Booking");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Booking Id", style);
        createCell(row, 1, "Mã Booking", style);
        createCell(row, 2, "Ngày Booking", style);
        createCell(row, 3, "Ngày Check In", style);
        createCell(row, 4, "Ngày Check Out", style);
        createCell(row, 5, "Tổng tiền", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value) ;
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Booking booking : bookings) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, booking.getBookingId(), style);
            createCell(row, columnCount++, booking.getBookingCode(), style);
            createCell(row, columnCount++, booking.getLocalDateTimeFormatter(booking.getBookingDate()), style);
            createCell(row, columnCount++, booking.getLocalDateFormatter(booking.getCheckInDate()), style);
            createCell(row, columnCount++, booking.getLocalDateFormatter(booking.getCheckOutDate()), style);
            createCell(row, columnCount++, booking.getTotal(), style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
