package com.sadman.financial.controller.Report;

import com.sadman.financial.service.impl.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@Tag(name = "Report Generation")
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/income-expense")
    @Operation(summary = "Generate Monthly Income vs Expense Report",
            description = "Generates a report showing monthly income vs expenses along with the balance.",
            responses = {
                    @ApiResponse(description = "Successfully generated the report", responseCode = "200", content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary"))),
                    @ApiResponse(description = "Error generating report", responseCode = "500")
            })
    public ResponseEntity<byte[]> generateIncomeExpenseReport() throws JRException, IOException {
        // Generating the report using the ReportService
        try {
            JasperPrint jasperPrint = reportService.generateIncomeExpenseReport();

            // Exporting the report to PDF
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);


            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            System.out.println("Generated PDF byte size: " + pdfBytes.length);

            // Preparing the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=IncomeExpenseReport.pdf");

            // Returning the PDF as a byte array
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(byteArrayOutputStream.toByteArray());
        }
        catch (JRException | IOException e) {
            // Logging the error and return a 500 error
            System.err.println("Error generating loan status report: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating report".getBytes());
        }
    }

    @GetMapping("/loan-status")
    @Operation(summary = "Generate Loan Status Report",
            description = "Generates a report showing the status of each loan (ACTIVE, PAID, OVERDUE) and repayment details.",
            responses = {
                    @ApiResponse(description = "Successfully generated the report", responseCode = "200", content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary"))),
                    @ApiResponse(description = "Error generating report", responseCode = "500")
            })
    public ResponseEntity<byte[]> generateLoanStatusReport() {
        try {
            // Generating the Loan Status Report
            JasperPrint jasperPrint = reportService.generateLoanStatusReport();

            // Exporting the report to PDF
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);

            // Debugging: Logging the byte length of the PDF content
            // here debugging because of jasper pdf print error
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            System.out.println("Generated PDF byte size: " + pdfBytes.length);

            // Setting the response headers to tell the client it's a PDF
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=Loan_Status_Report.pdf");

            // Returning the PDF as a byte array with the correct content type
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (JRException | IOException e) {
            // Logging the error and return a 500 error
            // debugging of a previous error not being
            // handled properly
            System.err.println("Error generating loan status report: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating report".getBytes());
        }
    }


}
