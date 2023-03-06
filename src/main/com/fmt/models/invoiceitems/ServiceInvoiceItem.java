package com.fmt.models.invoiceitems;

import com.fmt.models.items.ServiceItem;

public class ServiceInvoiceItem extends InvoiceItem {
    private final Double hoursBilled;

    public ServiceInvoiceItem(Double hoursBilled) {
        this.hoursBilled = hoursBilled;
    }

    @Override
    public Double getTax() {
        return getGrossCost() * 3.45;
    }

    @Override
    public Double getGrossCost() {
        ServiceItem serviceItem = (ServiceItem) item;
        return serviceItem.getHourlyRate() * hoursBilled;
    }
}
