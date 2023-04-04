package com.fmt.models.invoiceitems;

import com.fmt.models.items.EquipmentItem;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Models an equipment item that was leased.
 */
public class LeasedEquipmentInvoiceItem extends InvoiceItem<EquipmentItem> {
    private final Double fee;
    private final Integer leaseLength;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public LeasedEquipmentInvoiceItem(String itemCode, EquipmentItem item, Double fee, LocalDate startDate, LocalDate endDate) {
        super(itemCode, item);
        this.fee = fee;
        this.startDate = startDate;
        this.endDate = endDate;
        // End date is exclusive so we need to add 1 day.
        this.leaseLength = Math.toIntExact(ChronoUnit.DAYS.between(startDate, endDate)) + 1;
    }

    @Override
    public Double getTax() {
        double net = this.getNetCost();
        if (net < 10000) {
            return 0.0;
        } else if (net >= 10000 && net < 100000) {
            return 500.0;
        } else {
            return 1500.0;
        }
    }

    /**
     * Calculates the cost excluding tax
     * and rounded to the nearest hundredth
     * of this item.
     */
    @Override
    public Double getNetCost() {
        return Math.round(fee * (leaseLength / 30.0) * 100) / 100.0;
    }

    @Override
    public String generateReport() {
        return String.format("%s      (Lease)  %s %s\n", item.getItemCode(), item.getName(), item.getModel()) +
                String.format("        %d days (%s -> %s @ $ %.2f/30 days)\n", leaseLength, startDate.toString(), endDate.toString(), fee);
    }
}
