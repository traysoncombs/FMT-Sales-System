package com.fmt.models.invoiceitems;

import com.fmt.models.items.Item;
import com.fmt.models.items.ServiceItem;

public class ServiceInvoiceItem extends InvoiceItem<ServiceItem> {
    private final Double hoursBilled;

    public ServiceInvoiceItem(String itemCode, ServiceItem item, Double hoursBilled) {
        super(itemCode, item);
        this.hoursBilled = hoursBilled;
    }

    @Override
    public Double getTax() {
        return Math.round(getNetCost() * 3.45) / 100.0;
    }

    @Override
    public Double getNetCost() {
        ServiceItem serviceItem = item;
        return Math.round(serviceItem.getHourlyRate() * hoursBilled * 100.0) / 100.0;
    }

    public String toString() {
        return String.format("%s      (Service)  %s\n", item.getItemCode(), item.getName()) +
                String.format("        %.2f hours @ $ %.2f/hour\n", hoursBilled, item.getHourlyRate());
    }
}
