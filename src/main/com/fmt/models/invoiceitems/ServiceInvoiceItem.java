package com.fmt.models.invoiceitems;

import com.fmt.models.items.ServiceItem;

/**
 * Models a service invoice item.
 */
public class ServiceInvoiceItem extends InvoiceItem<ServiceItem> {
    private final Double hoursBilled;

    public ServiceInvoiceItem(String invoiceCode, ServiceItem item, Double hoursBilled) {
        super(invoiceCode, item);
        this.hoursBilled = hoursBilled;
    }

    /**
     * Calculates the 3.45% service tax
     * rounded to the nearest hundredth.
     */
    @Override
    public Double getTax() {
        return Math.round(getNetCost() * 3.45) / 100.0;
    }

    /**
     * Calculates the cost excluding tax
     * and rounded to the nearest hundredth
     * of this service.
     */
    @Override
    public Double getNetCost() {
        ServiceItem serviceItem = item;
        return Math.round(serviceItem.getHourlyRate() * hoursBilled * 100.0) / 100.0;
    }

    @Override
    public String generateReport() {
        return String.format("%s      (Service)  %s\n", item.getItemCode(), item.getName()) +
                String.format("        %.2f hours @ $ %.2f/hour\n", hoursBilled, item.getHourlyRate());
    }

    @Override
    public boolean saveToDB() {
        return false;
    }
}
