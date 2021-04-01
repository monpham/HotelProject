package com.nhat.demo.config;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.nhat.demo.entity.Booking;
import com.nhat.demo.entity.Charge;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ExportBill {
    private Booking booking;
    private List<Charge> charges;

    public ExportBill(Booking booking, List<Charge> charges) {
        this.booking = booking;
        this.charges = charges;
    }

//    private void writeInforBooking() {
//        PdfPCell cell = new PdfPCell();
//        cell.setPadding(5);
//        Font font = FontFactory.getFont(FontFactory.HELVETICA);
//        font.setColor(Color.WHITE);
//        font.setSize(14);
//        Paragraph p = new Paragraph(booking.toString(), font);
//
////        cell.setPhrase(new Phrase("Thành tiền", font));
////        table.addCell(cell);
//    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);



        cell.setPhrase(new Phrase("Ten dich vu", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Ngay dung", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("So luong", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Don Gia", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Thanh tien", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (Charge charge : charges) {
            table.addCell(charge.getService().getServiceName());
            table.addCell(charge.getChargeDate().toString());
            table.addCell(charge.convertQuantityToString(charge.getQuantity()));
            table.addCell(charge.getService().convertUnitPriceToString(charge.getService().getUnitPrice()));
            table.addCell(charge.convertTotalToString(charge.getTotal()));

        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Font font1 = FontFactory.getFont(FontFactory.COURIER);
        font.setSize(16);


        Paragraph p2 = new Paragraph("THONG TIN NGUOI DAT PHONG", font);
        p2.setAlignment(Paragraph.ALIGN_CENTER);

        Paragraph p1 = new Paragraph(booking.toString(), font1);
        p1.setAlignment(Paragraph.ALIGN_LEFT);

        Paragraph p = new Paragraph("Bang dich vu da su dung", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p2);
        document.add(p1);
        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.5f, 3.5f, 1.5f, 2.0f,2.0f});
        table.setSpacingBefore(10);
//        writeInforBooking();
        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}
