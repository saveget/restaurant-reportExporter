package th.co.priorsolution.training.restaurant.report;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import jakarta.servlet.ServletOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import th.co.priorsolution.training.restaurant.model.DTO.OrderItemReportDTO;
import th.co.priorsolution.training.restaurant.model.DTO.OrderReportDTO;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class ReportExporter {

    public void exportToCSV(List<OrderReportDTO> data, PrintWriter writer) throws IOException {
        writer.println("Order ID, Table, Status, Order Time, Serve Time, Menu, Qty, Price, Subtotal, Station");

        for (OrderReportDTO order : data) {
            for (OrderItemReportDTO item : order.getItems()) {
                writer.printf("%d, %s, %s, %s, %s, %s, %d, %.2f, %.2f, %s\n",
                        order.getOrderId(),
                        order.getTableName(),
                        order.getStatus(),
                        order.getOrderTime(),
                        order.getServeTime() != null ? order.getServeTime() : "",
                        item.getMenuName(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getSubtotal(),
                        item.getStation());
            }
        }
    }


    public void exportToExcel(List<OrderReportDTO> data, ServletOutputStream outputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Orders");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Order ID", "Table", "Status", "Order Time", "Serve Time", "Menu", "Qty", "Price", "Subtotal", "Station"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Data rows
            int rowIdx = 1;
            for (OrderReportDTO order : data) {
                for (OrderItemReportDTO item : order.getItems()) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(order.getOrderId());
                    row.createCell(1).setCellValue(order.getTableName());
                    row.createCell(2).setCellValue(order.getStatus());
                    row.createCell(3).setCellValue(order.getOrderTime().toString());
                    row.createCell(4).setCellValue(order.getServeTime() != null ? order.getServeTime().toString() : "");
                    row.createCell(5).setCellValue(item.getMenuName());
                    row.createCell(6).setCellValue(item.getQuantity());
                    row.createCell(7).setCellValue(item.getPrice());
                    row.createCell(8).setCellValue(item.getSubtotal());
                    row.createCell(9).setCellValue(item.getStation());
                }
            }

            workbook.write(outputStream);
        }
    }


    public void exportToPdf(List<OrderReportDTO> data, ServletOutputStream outputStream) throws Exception {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Order Report").setBold().setFontSize(14));

        for (OrderReportDTO order : data) {

            document.add(new Paragraph("Order #" + order.getOrderId()).setBold());

            // ตาราง Order รายการ
            Table orderTable = new Table(5);
            orderTable.addHeaderCell("Order ID");
            orderTable.addHeaderCell("Table");
            orderTable.addHeaderCell("Status");
            orderTable.addHeaderCell("Order Time");
            orderTable.addHeaderCell("Serve Time");

            orderTable.addCell(String.valueOf(order.getOrderId()));
            orderTable.addCell(order.getTableName());
            orderTable.addCell(order.getStatus());
            orderTable.addCell(order.getOrderTime().toString());
            orderTable.addCell(order.getServeTime() != null ? order.getServeTime().toString() : "-");

            document.add(orderTable);
            document.add(new Paragraph("\n"));

            // ตารางรายการอาหาร
            Table itemTable = new Table(5);
            itemTable.addHeaderCell("Menu");
            itemTable.addHeaderCell("Qty");
            itemTable.addHeaderCell("Price");
            itemTable.addHeaderCell("Subtotal");
            itemTable.addHeaderCell("Station");

            for (OrderItemReportDTO item : order.getItems()) {
                itemTable.addCell(item.getMenuName());
                itemTable.addCell(String.valueOf(item.getQuantity()));
                itemTable.addCell(String.valueOf(item.getPrice()));
                itemTable.addCell(String.valueOf(item.getSubtotal()));
                itemTable.addCell(item.getStation());
            }

            document.add(itemTable);

            document.add(new Paragraph("\n")); // ช่องว่างระหว่างออเดอร์
        }

        document.close();
    }
}