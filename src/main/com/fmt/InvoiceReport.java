package com.fmt;

import com.fmt.datastore.NewDatastore;

import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Author: Trayson Combs
 * Date: 2023/02/22
 * Collection of classes to model and store data associated with
 * the FMT sales system.
 */

public class InvoiceReport {
    public static void main(String[] args) {
        NewDatastore ds = null;
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory("tcombs", "5BaxRSal");
            ds = new NewDatastore(connectionFactory);
        } catch (SQLException e) {
            Logger.getLogger("fmt").severe("Error creating datastore");
            e.printStackTrace();
            System.exit(-1);
        }

        ReportGenerator reportGenerator = new ReportGenerator(ds);
        System.out.println(reportGenerator.generateSummaryReport());
        System.out.println(reportGenerator.generateStoreSummary());
        System.out.println(reportGenerator.generateInvoiceReports());
    }
}
