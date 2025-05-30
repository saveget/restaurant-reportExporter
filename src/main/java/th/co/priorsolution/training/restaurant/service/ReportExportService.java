package th.co.priorsolution.training.restaurant.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.model.DTO.OrderReportDTO;
import th.co.priorsolution.training.restaurant.report.ReportExporter;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportExportService {

    private final ReportExporter reportExporter;

    public void exportToCSV(List<OrderReportDTO> reportData, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=orders.csv");
        reportExporter.exportToCSV(reportData, response.getWriter());
    }

    public void exportToExcel(List<OrderReportDTO> reportData, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=orders.xlsx");
        reportExporter.exportToExcel(reportData, response.getOutputStream());
    }

    public void exportToPdf(List<OrderReportDTO> reportData, HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=orders.pdf");
        reportExporter.exportToPdf(reportData, response.getOutputStream());
    }
}