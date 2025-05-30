package th.co.priorsolution.training.restaurant.controller.rest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.priorsolution.training.restaurant.model.DTO.OrderReportDTO;
import th.co.priorsolution.training.restaurant.service.ReportExportService;
import th.co.priorsolution.training.restaurant.service.OrderService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final OrderService orderService;
    private final ReportExportService reportExportService;

    @GetMapping("/csv")
    public void exportCSV(HttpServletResponse response) throws IOException {
        List<OrderReportDTO> reportData = orderService.getAllOrderReports();
        reportExportService.exportToCSV(reportData, response);
    }

    @GetMapping("/excel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<OrderReportDTO> reportData = orderService.getAllOrderReports();
        reportExportService.exportToExcel(reportData, response);
    }

    @GetMapping("/pdf")
    public void exportPdf(HttpServletResponse response) throws Exception {
        List<OrderReportDTO> reportData = orderService.getAllOrderReports();
        reportExportService.exportToPdf(reportData, response);
    }
}
