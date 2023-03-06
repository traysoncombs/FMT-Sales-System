package com.fmt.models.invoiceitems;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class LeasedEquipmentInvoiceItem extends InvoiceItem {
    private final Double fee;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public LeasedEquipmentInvoiceItem(Double fee, LocalDate startDate, LocalDate endDate) {
        this.fee = fee;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Double getTax() {
        double gross = this.getGrossCost();
        if (gross < 10000) {
            return 0.0;
        } else if (gross >= 10000 && gross < 100000) {
            return 500.0;
        } else {
            return 1500.0;
        }
    }

    @Override
    public Double getGrossCost() {
        return fee * (ChronoUnit.DAYS.between(startDate, endDate) / 30);
    }
}
