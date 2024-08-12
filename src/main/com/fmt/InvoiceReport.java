package com.fmt;

import com.fmt.datastore.NewDatastore;
import com.fmt.models.Invoice;

import java.sql.SQLException;
import java.util.Comparator;
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
            ds = new NewDatastore(lexicoCustomerName());
        } catch (SQLException e) {
            Logger.getLogger("fmt").severe("Error creating datastore");
            e.printStackTrace();
            System.exit(-1);
        }

        ReportGenerator reportGenerator = new ReportGenerator(ds);
        System.out.println(reportGenerator.generateReportByOrder("Cstmr", lexicoCustomerName()));
        System.out.println(reportGenerator.generateReportByOrder("Total", descValue()));
        System.out.println(reportGenerator.generateReportByOrder("Store", storeThenSalesName()));
    }

    private static Comparator<Invoice> descValue() {
        return (o1, o2) -> o2.getNetCost().compareTo(o1.getNetCost());
    }

    private static Comparator<Invoice> lexicoCustomerName() {
        return (o1, o2) -> {
            int lastName = o1
                    .getCustomer()
                    .getLastName()
                    .compareTo(o2.getCustomer().getLastName());

            if (lastName == 0) {
                return o1
                        .getCustomer()
                        .getFirstName()
                        .compareTo(o2.getCustomer().getFirstName());
            }

            return lastName;
        };
    }

    private static Comparator<Invoice> storeThenSalesName() {
        return new Comparator<Invoice>() {

            @Override
            public int compare(Invoice o1, Invoice o2) {
                int storeCode = o1.getStoreCode().compareTo(o2.getStoreCode());
                int lastName = o1
                        .getSalesPerson()
                        .getLastName()
                        .compareTo(o2.getSalesPerson().getLastName());
                if (storeCode == 0) {
                    if (lastName == 0) {
                        return o1
                                .getSalesPerson()
                                .getFirstName()
                                .compareTo(o2.getSalesPerson().getFirstName());
                    }
                    return lastName;
                }
                return storeCode;

            }
        };
    }
}
