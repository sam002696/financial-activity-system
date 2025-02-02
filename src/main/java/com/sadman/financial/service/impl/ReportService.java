package com.sadman.financial.service.impl;

import com.sadman.financial.entity.User;
import com.sadman.financial.exceptions.CustomMessageException;
import com.sadman.financial.repository.UserRepository;
import com.sadman.financial.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataSource dataSource;  // Inject the dataSource

    @Autowired
    private ResourceLoader resourceLoader;  // To load resources from the classpath

    // This method will obtain and return the connection from the DataSource
    private Connection getConnection() throws SQLException {
        logger.info("Establishing connection to the database...");
        return DataSourceUtils.getConnection(dataSource);  // Fetch the connection from the DataSource
    }

    // Generate the Income vs Expenses Report
    public JasperPrint generateIncomeExpenseReport() throws JRException, IOException {
        logger.info("Generating Income vs Expense Report...");


        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();

        logger.info("Retrieved userId from JWT: {}", userId);

        // Retrieve the User entity from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", userId);
                    return new CustomMessageException("User not found with id: " + userId);
                });

        logger.info("User found with id: {}", userId);


        // Load the IncomeExpenseReport from the resources folder
        Resource reportResource = resourceLoader.getResource("classpath:reports/IncomeExpenseReport.jrxml");

        // Check if the report file is found
        if (!reportResource.exists()) {
            logger.error("IncomeExpenseReport.jrxml not found in the classpath.");
            throw new JRException("IncomeExpenseReport.jrxml not found in the classpath.");
        }
        logger.info("Report file loaded: {}", reportResource.getURI());

        // Compile the JRXML to Jasper (compiled report)
        JasperReport jasperReport = compileReport(reportResource);

        // Set parameters for the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);

        // Fill the report with data
        try (Connection connection = getConnection()) {
            logger.info("Filling the report with data...");
            parameters.put("REPORT_CONNECTION", connection);  // Pass connection to report
            return JasperFillManager.fillReport(jasperReport, parameters, connection);
        } catch (SQLException | JRException e) {
            logger.error("Error filling Income Expense report", e);
            throw new JRException("Error filling Income Expense report", e);
        }
    }

    // Generate the Loan Status Report
    public JasperPrint generateLoanStatusReport() throws JRException, IOException {
        logger.info("Generating Loan Status Report...");

        // Retrieve the userId from the SecurityContext (JWT)
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();

        logger.info("Retrieved userId from JWT: {}", userId);

        // Retrieve the User entity from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", userId);
                    return new CustomMessageException("User not found with id: " + userId);
                });

        logger.info("User found with id: {}", userId);

        // Load the LoanStatusReport from the resources folder
        Resource reportResource = resourceLoader.getResource("classpath:reports/LoanStatus.jrxml");

        // Check if the report file is found
        if (!reportResource.exists()) {
            logger.error("LoanStatus.jrxml not found in the classpath.");
            throw new JRException("LoanStatus.jrxml not found in the classpath.");
        }
        logger.info("Report file loaded: {}", reportResource.getURI());

        // Compile the JRXML to Jasper (compiled report)
        JasperReport jasperReport = compileReport(reportResource);

        // Set parameters for the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);  // Pass the userId parameter dynamically

        // Fill the report with data
        try (Connection connection = getConnection()) {
            logger.info("Filling Loan Status Report with data for userId: {}", userId);
            return JasperFillManager.fillReport(jasperReport, parameters, connection);
        } catch (Exception e) {
            logger.error("Error filling Loan Status report", e);
            throw new JRException("Error filling Loan Status report", e);
        }
    }

    // Method to compile JRXML file to Jasper report
    private JasperReport compileReport(Resource reportResource) throws JRException {
        logger.info("Compiling the JRXML file to Jasper...");
        try (InputStream reportStream = reportResource.getInputStream()) {
            return JasperCompileManager.compileReport(reportStream);
        } catch (IOException e) {
            logger.error("Error loading JRXML file for compilation", e);
            throw new JRException("Error loading JRXML file for compilation", e);
        }
    }
}
